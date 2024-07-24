package com.example.demo.Service;

import com.example.demo.DTO.*;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.ContractDetail;
import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Room;
import com.example.demo.Repo.ContractDetailRepository;
import com.example.demo.Repo.ContractRepository;
import com.example.demo.Repo.CustomerRepository;
import com.example.demo.Request.ContractReq;
import com.example.demo.Util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final ContractDetailRepository contractDetailRepository;
    private final CustomerRepository customerRepository;

    public ContractService(ContractRepository contractRepository,ContractDetailRepository contractDetailRepository, CustomerRepository customerRepository) {
        this.contractRepository = contractRepository;
        this.contractDetailRepository = contractDetailRepository;
        this.customerRepository = customerRepository;
    }

    public Page<ContractDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.get("status");
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
        contract.setStatus(0);
        contract=contractRepository.save(contract);
        return Contract.toDTO(contract);
    }

    public ContractDTO restore(BigInteger id){
        Optional<Contract> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Contract contract = optionalContract.get();
        contract.setStatus(1);
        contract=contractRepository.save(contract);
        return Contract.toDTO(contract);
    }

    public ContractDTO create(ContractDTO contractDTO) {
        ContractDTO result = new ContractDTO();

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
        return result;
    }

    public ContractDTO update(BigInteger id, ContractDTO contractDTO) {
        ContractDTO result = new ContractDTO();

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

        result.setCustomerIds(contractDTO.getCustomerIds());

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
