package com.example.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;

    private String customerCode;

    private String customerName;

    private String identityNumber;

    private String phoneNumber;

    private int status;

    private LocalDate birthdate;

    private Long companyId;

    public  void validate(CustomerDTO customerDTO){
        if(customerDTO.customerName == null || customerDTO.getCustomerName().isEmpty()){
            throw  new IllegalArgumentException("name cannot be empty");
        }
        if(customerDTO.identityNumber == null || customerDTO.getIdentityNumber().isEmpty()){
            throw  new IllegalArgumentException("identityNumber cannot be empty");
        }
        if(customerDTO.getPhoneNumber() == null || customerDTO.getPhoneNumber().isEmpty()){
            throw  new IllegalArgumentException("phone cannot be empty");
        }
        if(customerDTO.getBirthdate() == null ){
            throw  new IllegalArgumentException("Birthdate cannot be empty");
        }
        if (customerDTO.getBirthdate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be future");
        }
    }
}
