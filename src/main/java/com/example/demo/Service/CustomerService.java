package com.example.demo.Service;

import com.example.demo.DTO.CustomerDTO;
import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Utility;
import com.example.demo.Repo.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<CustomerDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.get("status");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> data = customerRepository.search(search, status, pageable);
        return data.map(Customer::toDTO);
    }

    public List<CustomerDTO> getAll() {
        List<Customer> customers = customerRepository.findAllByOrderByIdDesc();
        return customers.stream().map(Customer::toDTO).collect(Collectors.toList());
    }

    public CustomerDTO create(CustomerDTO customerDTO) {
        customerDTO.validate(customerDTO);
        Optional<Customer> maxIdSP = customerRepository.findMaxId();
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Customer customer = Customer.toEntity(customerDTO);
        customer.setCustomerCode("C"+maxId);
        customer.setStatus(1);
        Customer newCustomer = customerRepository.save(customer);
        return Customer.toDTO(newCustomer);
    }

    public CustomerDTO update(BigInteger id, CustomerDTO customerDTO) {
        customerDTO.validate(customerDTO);
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Customer customer = optionalCustomer.get();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setIdentityNumber(customerDTO.getIdentityNumber());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setBirthdate(customerDTO.getBirthdate());
        customer = customerRepository.save(customer);
        return Customer.toDTO(customer);
    }

    public CustomerDTO delete(BigInteger id){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }
        Customer customer = optionalCustomer.get();
        customer.setStatus(0);
        customer= customerRepository.save(customer);
        return Customer.toDTO(customer);
    }

    public CustomerDTO restore(BigInteger id){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }
        Customer customer = optionalCustomer.get();
        customer.setStatus(1);
        customer= customerRepository.save(customer);
        return Customer.toDTO(customer);
    }

    public CustomerDTO findById(BigInteger id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }
        return Customer.toDTO(optionalCustomer.get());
    }
}
