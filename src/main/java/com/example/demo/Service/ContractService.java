package com.example.demo.Service;

import com.example.demo.DTO.ContractDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repo.*;
import com.example.demo.Util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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

    public Page<ContractDTO> search(Map<String, Object> payload, String cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status", null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> data = contractRepository.search(search, status,cid, pageable);
        System.out.println(data);
        return data.map(Contract::toDTO);
    }

    public ContractDTO delete(BigInteger id,BigInteger cid) {
        Optional<Contract> optionalContract = contractRepository.findByIdAndCompanyId(id,cid);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(Utils.IN_ACTIVE);
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractIdAndCompanyId(id,cid);
        List<Payment> payments = paymentRepository.findAllByContractIdAndCompanyId(id,cid);
        List<BigInteger> paymentIds = payments.stream().map(payment -> payment.getId()).collect(Collectors.toList());
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

    public ContractDTO restore( BigInteger id,BigInteger cid) {
        Optional<Contract> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(Utils.ACTIVE);
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractIdAndCompanyId(id,cid);
        List<Payment> payments = paymentRepository.findAllByContractIdAndCompanyId(id,cid);
        List<BigInteger> paymentIds = payments.stream().map(payment -> payment.getId()).collect(Collectors.toList());
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

    public ContractDTO create(ContractDTO contractDTO, BigInteger cid) {
        contractDTO.validateContractTO(contractDTO);
        ContractDTO result = new ContractDTO();

        Optional<Room> room = roomRepository.findByIdAndCompanyId(contractDTO.getRoom().getId(),cid);
        if (room.isPresent()) {
            room.get().setRentStatus(Utils.ACTIVE);
            roomRepository.save(room.get());
        }

        Optional<Contract> maxIdSP = contractRepository.findMaxIdAndCompanyId(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Contract contract = Contract.toEntity(contractDTO);
        contract.setContractCode("C" + maxId);
        Optional<Room> optionalRoom = roomRepository.findById(contract.getRoom().getId());
        contract.setRentPrice(optionalRoom.get().getRentPrice());
        contract.setStatus(Utils.ACTIVE);
        contract.setCompanyId(cid);
        Contract newContract = contractRepository.save(contract);

        List<ContractDetail> contractDetails = new ArrayList<>();

        Customer customer = new Customer();
        ContractDetail contractDetail = new ContractDetail();
        for (BigInteger customerId : contractDTO.getCustomerIds()) {
            customer.setId(customerId);
            contractDetail.setContract(newContract);
            contractDetail.setCustomer(customer);
            contractDetail.setStatus(Utils.ACTIVE);
            contractDetail.setCompanyId(cid);
            contractDetails.add(contractDetail);
        }
        contractDetailRepository.saveAll(contractDetails);
//        result.setCustomerIds(contractDTO.getCustomerIds());

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

    public Object update(BigInteger id, ContractDTO contractDTO, BigInteger cid) {
        contractDTO.validateContractTO(contractDTO);
        ContractDTO result = new ContractDTO();

        List<Payment> paymentList = paymentRepository.findAllByContractIdAndCompanyId(id,cid);
        if (paymentList.isEmpty()) {
            throw new IllegalArgumentException("Payment not found");
        }
        if (paymentList.size() > 1) {
            return false;
        }

        Payment payment = paymentList.get(0);
        LocalDate startDate = contractDTO.getStartDate();
        LocalDate nextMonthDate = startDate.plusMonths(1);
        payment.setPaymentCode("TT-" + startDate);
        payment.setPaymentDate(nextMonthDate);
        paymentRepository.save(payment);
        result.setCustomerIds(contractDTO.getCustomerIds());

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
        contract = contractRepository.save(contract);

        contractDetailRepository.deleteAllByContactIdAndCompanyId(id,cid);

        List<ContractDetail> contractDetails = new ArrayList<>();

        Customer customer = new Customer();
        ContractDetail contractDetail = new ContractDetail();
        for (BigInteger customerId : contractDTO.getCustomerIds()) {
            customer.setId(customerId);
            contractDetail.setContract(contract);
            contractDetail.setCustomer(customer);
            contractDetail.setStatus(Utils.ACTIVE);
            contractDetail.setCompanyId(cid);
            contractDetails.add(contractDetail);
        }
        contractDetailRepository.saveAll(contractDetails);

        return result;
    }

    public ContractDTO findById(BigInteger id,BigInteger cid) throws Exception {
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
