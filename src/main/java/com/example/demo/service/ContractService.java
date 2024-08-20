package com.example.demo.service;

import com.example.demo.DTO.ContractDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final ContractDetailRepository contractDetailRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentRepository paymentRepository;
    private final RoomRepository roomRepository;

    public ContractService(ContractRepository contractRepository,
                           ContractDetailRepository contractDetailRepository,
                           PaymentDetailRepository paymentDetailRepository,
                           PaymentRepository paymentRepository,
                           RoomRepository roomRepository) {
        this.contractRepository = contractRepository;
        this.contractDetailRepository = contractDetailRepository;
        this.paymentDetailRepository = paymentDetailRepository;
        this.paymentRepository = paymentRepository;
        this.roomRepository = roomRepository;
    }

    public Page<ContractDTO> search(Map<String, Object> payload, Long cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status", null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> data = contractRepository.search(search, status,cid, pageable);
        return data.map(Contract::toDTO);
    }

    public ContractDTO delete(Long id,Long cid) {
        Optional<Contract> optionalContract = contractRepository.findByIdAndCompanyId(id,cid);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(Utils.IN_ACTIVE);
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractIdAndCompanyId(id,cid);
        List<Payment> payments = paymentRepository.findAllByContractIdAndCompanyId(id,cid);
        List<Long> paymentIds = payments.stream().map(payment -> payment.getId()).collect(Collectors.toList());
        List<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentIdsAndCompanyId(paymentIds,cid);
        contractDetails.forEach(x -> x.setStatus(Utils.IN_ACTIVE));
        paymentDetails.forEach(x -> x.setStatus(Utils.IN_ACTIVE));
        payments.forEach(x -> x.setStatus(Utils.IN_ACTIVE));

        contract = contractRepository.save(contract);
        paymentDetailRepository.saveAll(paymentDetails);
        paymentRepository.saveAll(payments);
        contractDetailRepository.saveAll(contractDetails);
        return Contract.toDTO(contract);
    }

    public ContractDTO restore( Long id,Long cid) {
        Optional<Contract> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(Utils.ACTIVE);
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractIdAndCompanyId(id,cid);
        List<Payment> payments = paymentRepository.findAllByContractIdAndCompanyId(id,cid);
        List<Long> paymentIds = payments.stream().map(payment -> payment.getId()).collect(Collectors.toList());
        List<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentIdsAndCompanyId(paymentIds,cid);
        contractDetails.forEach(x -> x.setStatus(Utils.ACTIVE));
        paymentDetails.forEach(x -> x.setStatus(Utils.ACTIVE));
        payments.forEach(x -> x.setStatus(Utils.ACTIVE));

        contract = contractRepository.save(contract);
        paymentDetailRepository.saveAll(paymentDetails);
        paymentRepository.saveAll(payments);
        contractDetailRepository.saveAll(contractDetails);
        return Contract.toDTO(contract);
    }

    public ContractDTO create(ContractDTO contractDTO, Long cid) {
        contractDTO.validateContractTO(contractDTO);
        ContractDTO result = new ContractDTO();

        Optional<Room> room = roomRepository.findByIdAndCompanyId(contractDTO.getRoom().getId(),cid);
        if (room.isPresent()) {
            room.get().setRentStatus(Utils.ACTIVE);
            roomRepository.save(room.get());
        }

        Optional<Contract> maxIdSP = contractRepository.findMaxIdAndCompanyId(cid);
        Long maxId = maxIdSP.isPresent() ? maxIdSP.get().getId()+1 : 1;

        Contract contract = Contract.toEntity(contractDTO);
        contract.setContractCode("C" + maxId);
        Optional<Room> optionalRoom = roomRepository.findById(contract.getRoom().getId());
        contract.setRentPrice(optionalRoom.get().getRentPrice());
        contract.setStatus(Utils.ACTIVE);
        contract.setCompanyId(cid);
        Contract newContract = contractRepository.save(contract);

        List<ContractDetail> contractDetails = new ArrayList<>();

        for (Long customerId : contractDTO.getCustomerIds()) {
            Customer customer = new Customer();
            ContractDetail contractDetail = new ContractDetail();
            customer.setId(customerId);
            contractDetail.setContract(newContract);
            contractDetail.setCustomer(customer);
            contractDetail.setStatus(Utils.ACTIVE);
            contractDetail.setCompanyId(cid);
            contractDetails.add(contractDetail);
        }
        contractDetailRepository.saveAll(contractDetails);

        //tạo payment
        Payment payment = new Payment();
        payment.setContract(contract);
        LocalDate startDate = contractDTO.getStartDate();
        LocalDate nextMonthDate = startDate.plusMonths(1);
        payment.setPaymentCode("TT-" + startDate);
        payment.setPaymentDate(nextMonthDate);
        payment.setPaymentStatus(Utils.UNPAID);
        payment.setStatus(Utils.ACTIVE);
        payment.setCompanyId(cid);
        System.out.println(payment);
        paymentRepository.save(payment);

        result=Contract.toDTO(newContract);
        return result;
    }

    public Object update(Long id, ContractDTO contractDTO, Long cid) {
        contractDTO.validateContractTO(contractDTO);
        ContractDTO result = new ContractDTO();

        List<Payment> paymentList = paymentRepository.findAllByContractIdAndCompanyId(id,cid);
        System.out.println(paymentList.get(0).getPaymentDate()+""+contractDTO.getEndDate());
        if (paymentList.get(0).getPaymentDate().isAfter(contractDTO.getEndDate())) {
            throw new IllegalArgumentException("Ngày kết thúc không được nhỏ hơn: "+paymentList.get(0).getPaymentDate());
        }
        else {
            Payment payment = paymentRepository.findAllByContractIdAndCompanyId(id,cid).get(0);
            LocalDate startDate = contractDTO.getStartDate();
            LocalDate nextMonthDate = startDate.plusMonths(1);
            payment.setPaymentCode("TT-" + startDate);
            payment.setPaymentDate(nextMonthDate);
            paymentRepository.save(payment);
        }


        Optional<Room> optionalRoom = contractRepository.findRoomByContractIdAndCompanyId(id,cid);
        if (optionalRoom.get().getId() != contractDTO.getRoom().getId()) {
            optionalRoom.get().setRentStatus(Utils.IN_ACTIVE);
            roomRepository.save(optionalRoom.get());

            Optional<Room> room = roomRepository.findById(contractDTO.getRoom().getId());
            if (room.isPresent()) {
                room.get().setRentStatus(Utils.ACTIVE);
                roomRepository.save(room.get());
            }
        }

        Optional<Contract> optionalContract = contractRepository.findByIdAndCompanyId(id,cid);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        Optional<Room> room = roomRepository.findByIdAndCompanyId(contractDTO.getRoom().getId(),cid);
        contract.setRentPrice(room.get().getRentPrice());
        contract.setStartDate(contractDTO.getStartDate());
        contract.setEndDate(contractDTO.getEndDate());
        contract.setRoom(Room.toEntity(contractDTO.getRoom()));
        contract.setTerms(contractDTO.getTerms());
        contract.setStatus(Utils.ACTIVE);
        contract = contractRepository.save(contract);

        contractDetailRepository.deleteAllByContactIdAndCompanyId(id,cid);

        LocalDate today = LocalDate.now();

        List<ContractDetail> contractDetails = new ArrayList<>();
        for (Long customerId : contractDTO.getCustomerIds()) {
            Customer customer = new Customer();
            ContractDetail contractDetail = new ContractDetail();
            customer.setId(customerId);
            contractDetail.setContract(contract);
            contractDetail.setCustomer(customer);
            contractDetail.setStatus(contractDTO.getEndDate() == null ||contractDTO.getEndDate().isAfter(today) ? Utils.ACTIVE : Utils.EXPIRED);
            contractDetail.setCompanyId(cid);
            contractDetails.add(contractDetail);
        }
        contractDetailRepository.saveAll(contractDetails);

        result.setCustomerIds(contractDTO.getCustomerIds());
        return result;
    }

    public ContractDTO findById(Long id,Long cid) throws Exception {
        Optional<Contract> optionalContract = contractRepository.findByIdAndCompanyId(id,cid);
        if (!optionalContract.isPresent()) {
            throw new Exception("contract not found");
        }
        ContractDTO contractDTO = optionalContract.map(Contract::toDTO).get();
        List<Customer> customers = contractDetailRepository.findAllCustomerByContractIdAndCompanyId(id,cid);
        contractDTO.setCustomerIds(customers.stream().map(x -> x.getId()).collect(Collectors.toList()));
        return contractDTO;
    }
}
