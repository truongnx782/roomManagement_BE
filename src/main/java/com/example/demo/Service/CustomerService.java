package com.example.demo.Service;

import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Entity.Customer;
import com.example.demo.Repo.CustomerRepository;
import com.example.demo.Util.Excel;
import com.example.demo.Util.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<CustomerDTO> search(Map<String, Object> payload, BigInteger cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status", null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> data = customerRepository.search(search, status, cid, pageable);
        return data.map(Customer::toDTO);
    }

    public List<CustomerDTO> getAll(BigInteger cid) {
        List<Customer> customers = customerRepository.findAllByCompanyIdOrderByIdDesc(cid);
        return customers.stream().map(Customer::toDTO).collect(Collectors.toList());
    }

    public CustomerDTO create(CustomerDTO customerDTO, BigInteger cid) {
        customerDTO.validate(customerDTO);
        Optional<Customer> maxIdSP = customerRepository.findMaxIdByCompanyId(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Customer customer = Customer.toEntity(customerDTO);
        customer.setCustomerCode("C" + maxId);
        customer.setStatus(Utils.ACTIVE);
        customer.setCompanyId(cid);
        Customer newCustomer = customerRepository.save(customer);
        return Customer.toDTO(newCustomer);
    }

    public CustomerDTO update(BigInteger id, CustomerDTO customerDTO, BigInteger cid) {
        customerDTO.validate(customerDTO);
        Optional<Customer> optionalCustomer = customerRepository.findByIdAndCompanyId(id, cid);
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

    public CustomerDTO delete(BigInteger id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }
        Customer customer = optionalCustomer.get();
        customer.setStatus(0);
        customer = customerRepository.save(customer);
        return Customer.toDTO(customer);
    }

    public CustomerDTO restore(BigInteger id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }
        Customer customer = optionalCustomer.get();
        customer.setStatus(1);
        customer = customerRepository.save(customer);
        return Customer.toDTO(customer);
    }

    public CustomerDTO findById(BigInteger id, BigInteger cid) {
        Optional<Customer> optionalCustomer = customerRepository.findByIdAndCompanyId(id, cid);
        if (!optionalCustomer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }
        return Customer.toDTO(optionalCustomer.get());
    }

    public Object importExcel(MultipartFile file, BigInteger cid) throws IOException {
        Optional<Customer> maxIdSP = customerRepository.findMaxIdByCompanyId(cid);
        List<Customer> existingCustomers = customerRepository.findAllByCompanyIdOrderByIdDesc(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        List<Customer> customerList = new ArrayList<>();
        Set<String> uniqueRows = new HashSet<>();
        List<String> duplicateRows = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua hàng tiêu đề
                Row row = sheet.getRow(i);
                if (row == null) continue; // Bỏ qua các hàng trống

                // Lấy giá trị từ các cột
                String column1 = Excel.getCellValue(row.getCell(0));
                String column2 = Excel.getCellValue(row.getCell(1));
                String column3 = Excel.getCellValue(row.getCell(2));
                String column4 = Excel.getCellValue(row.getCell(3));

                // Tạo chuỗi đại diện cho hàng dữ liệu
                String rowData = column1 + "_" + column3;

                // Kiểm tra xem hàng dữ liệu đã tồn tại chưa
                boolean isDuplicate = !uniqueRows.add(rowData) || existingCustomers.stream()
                        .anyMatch(r -> r.getCustomerName().equals(column1) &&
                                r.getIdentityNumber().equals(column2) &&
                                r.getPhoneNumber().equals(column3));

                if (isDuplicate) {
                    duplicateRows.add(rowData);
                    continue; // Chuyển sang hàng tiếp theo
                }

                Customer customer = new Customer();
                customer.setCustomerCode("C" + maxId);
                customer.setCustomerName(column1);
                customer.setIdentityNumber(column2);
                customer.setPhoneNumber(column3);

                LocalDate ngaySinh = null;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    ngaySinh = LocalDate.parse(column4, formatter);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                customer.setBirthdate(ngaySinh);
                customer.setCompanyId(cid);
                customer.setStatus(Utils.ACTIVE);
                customerList.add(customer);
                maxId = maxId.add(BigInteger.ONE);
            }

            List<Customer> result = customerRepository.saveAll(customerList);


            if (!duplicateRows.isEmpty()) {
                throw new IllegalArgumentException("Dữ liệu bị trùng: " + duplicateRows);
            }

            return result.stream()
                    .map(Customer::toDTO)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Dữ liệu không đúng định dạng!", e);
        }
    }


    public byte[] exportTemplate() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("customerName");
            headerRow.createCell(1).setCellValue("IdentityNumber");
            headerRow.createCell(2).setCellValue("PhoneNumber");
            headerRow.createCell(3).setCellValue("Birthdate");

            // Tạo CellStyle với định dạng text
            DataFormat format = workbook.createDataFormat();
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(format.getFormat("@")); // "@" là định dạng cho text

            // Áp dụng định dạng text cho tất cả các cột
            for (int i = 0; i < 4; i++) {
                sheet.setDefaultColumnStyle(i, textStyle);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate template", e);
        }
    }


    public byte[] exportData(Map<String, Object> payload, BigInteger cid) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            // Create header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("CustomerCode");
            header.createCell(1).setCellValue("CustomerName");
            header.createCell(2).setCellValue("IdentityNumber");
            header.createCell(3).setCellValue("PhoneNumber");
            header.createCell(4).setCellValue("Birthdate");
            header.createCell(5).setCellValue("Status");

            // Fetch data
            Page<CustomerDTO> customerDTOS = search(payload, cid);
            List<Customer> customers = customerDTOS.stream()
                    .map(Customer::toEntity)
                    .collect(Collectors.toList());

            // Write data to sheet
            int rowIndex = 1;
            for (Customer r : customers) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(r.getCustomerCode());
                row.createCell(1).setCellValue(r.getCustomerName());
                row.createCell(2).setCellValue(r.getIdentityNumber());
                row.createCell(3).setCellValue(r.getPhoneNumber());
                row.createCell(4).setCellValue(r.getBirthdate());
                row.createCell(5).setCellValue(r.getStatus());
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data", e);
        }
    }
}
