package com.example.demo.Service;

import com.example.demo.DTO.*;
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
    private  final RoomRepository roomRepository;

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

    public Page<ContractDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status",null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> data = contractRepository.search(search, status, pageable);
        System.out.println(data);
        return data.map(Contract::toDTO);
    }

    public ContractDTO delete(BigInteger id){
        Optional<Contract> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(Utils.IN_ACTIVE);
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractId(id);
        List<Payment> payments = paymentRepository.findAllByContractId(id);
        List<BigInteger> paymentIds = payments.stream().map(payment -> payment.getId()).collect(Collectors.toList());
        List<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentIds(paymentIds);
        contractDetails.forEach(x->x.setStatus(Utils.IN_ACTIVE));
        paymentDetails.forEach(x->x.setStatus(Utils.IN_ACTIVE));
        payments.forEach(x->x.setStatus(Utils.IN_ACTIVE));

        contract=contractRepository.save(contract);
        paymentDetailRepository.saveAll(paymentDetails);
        paymentRepository.saveAll(payments);
        contractDetailRepository.saveAll(contractDetails);
        return Contract.toDTO(contract);
    }

    public ContractDTO restore(BigInteger id){
        Optional<Contract> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(Utils.ACTIVE);
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractId(id);
        List<Payment> payments = paymentRepository.findAllByContractId(id);
        List<BigInteger> paymentIds = payments.stream().map(payment -> payment.getId()).collect(Collectors.toList());
        List<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentIds(paymentIds);
        contractDetails.forEach(x->x.setStatus(Utils.ACTIVE));
        paymentDetails.forEach(x->x.setStatus(Utils.ACTIVE));
        payments.forEach(x->x.setStatus(Utils.ACTIVE));

        contract=contractRepository.save(contract);
        paymentDetailRepository.saveAll(paymentDetails);
        paymentRepository.saveAll(payments);
        contractDetailRepository.saveAll(contractDetails);
        return Contract.toDTO(contract);
    }

    public ContractDTO create(ContractDTO contractDTO) {
        contractDTO.validateContractTO(contractDTO);
        ContractDTO result = new ContractDTO();

        Optional<Room> room = roomRepository.findById(contractDTO.getRoom().getId());
        if(room.isPresent()){
            room.get().setRentStatus(Utils.ACTIVE);
            roomRepository.save(room.get());
        }

        Optional<Contract> maxIdSP = contractRepository.findMaxId();
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Contract contract = Contract.toEntity(contractDTO);
        contract.setContractCode("C"+maxId);
        contract.setStatus(1);
        Contract newContract = contractRepository.save(contract);

        List<ContractDetail> contractDetails = new ArrayList<>();

        for (BigInteger customerId:contractDTO.getCustomerIds()) {
            Customer customer = new Customer();
            customer.setId(customerId);
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setContract(newContract);
            contractDetail.setCustomer(customer);
            contractDetail.setStatus(Utils.ACTIVE);
            contractDetails.add(contractDetail);
        }
        contractDetailRepository.saveAll(contractDetails);
        result.setCustomerIds(contractDTO.getCustomerIds());

        //tạo payment
        Payment payment = new Payment();
        payment.setContract(contract);
        LocalDate startDate = contractDTO.getStartDate();
        LocalDate nextMonthDate = startDate.plusMonths(1);
        payment.setPaymentCode("TT - "+ startDate);
        payment.setPaymentDate(nextMonthDate);
        payment.setPaymentStatus(Utils.UNPAID);
        payment.setStatus(Utils.ACTIVE);
        System.out.println(payment);
        paymentRepository.save(payment);

        return result;
    }

    public Object update(BigInteger id, ContractDTO contractDTO) {
        contractDTO.validateContractTO(contractDTO);
        ContractDTO result = new ContractDTO();

        Optional<Room> optionalRoom = contractRepository.findRoomByContractId(id);
        if(optionalRoom.get().getId()!=contractDTO.getRoom().getId()){
            optionalRoom.get().setRentStatus(Utils.IN_ACTIVE);
            roomRepository.save(optionalRoom.get());

            Optional<Room> room = roomRepository.findById(contractDTO.getRoom().getId());
            if(room.isPresent()){
                room.get().setRentStatus(Utils.ACTIVE);
                roomRepository.save(room.get());
            }
        }


        Optional<Contract>optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStartDate(contractDTO.getStartDate());
        contract.setEndDate(contractDTO.getEndDate());
        contract.setRoom(Room.toEntity(contractDTO.getRoom()));
        contract.setTerms(contractDTO.getTerms());
        contract=contractRepository.save(contract);

        contractDetailRepository.deleteAllByContactId(id);

        List<ContractDetail> contractDetails = new ArrayList<>();

        for (BigInteger customerId:contractDTO.getCustomerIds()) {
            Customer customer = new Customer();
            customer.setId(customerId);
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setContract(contract);
            contractDetail.setCustomer(customer);
            contractDetail.setStatus(Utils.ACTIVE);
            contractDetails.add(contractDetail);
        }
        contractDetailRepository.saveAll(contractDetails);

        List<Payment> paymentList =paymentRepository.findAllByContractId(contract.getId());
        if (paymentList.isEmpty()) {
            throw new IllegalArgumentException("Payment not found");
        }
        if(paymentList.size()>1){
            return false;
        }
        else {
            Payment payment = paymentList.get(0);
            LocalDate startDate = contractDTO.getStartDate();
            LocalDate nextMonthDate = startDate.plusMonths(1);
            payment.setPaymentCode("TT - " + startDate);
            payment.setPaymentDate(nextMonthDate);
            paymentRepository.save(payment);
            result.setCustomerIds(contractDTO.getCustomerIds());
        }
        return result;
    }

    public ContractDTO findById(BigInteger id) throws Exception {
        Optional<Contract> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new Exception("contract not found");
        }
        ContractDTO contractDTO = optionalContract.map(Contract::toDTO).get();
        List<Customer> customers = contractDetailRepository.findAllCustomerById(id);
        System.out.println(customers);
        contractDTO.setCustomerIds(customers.stream().map(x->x.getId()).collect(Collectors.toList()));
        return contractDTO;
    }
}
