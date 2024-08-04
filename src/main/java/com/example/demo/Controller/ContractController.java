package com.example.demo.Controller;

import com.example.demo.DTO.ContractDTO;
import com.example.demo.Service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") String cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(contractService.search(payload, cid));
    }


    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") BigInteger cid,
                                    @RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.create(contractDTO, cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") BigInteger cid,
                                     @PathVariable("id") BigInteger id) throws Exception {
        return ResponseEntity.ok(contractService.findById(id, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("cid") BigInteger cid,
                                    @PathVariable("id") BigInteger id,
                                    @RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.update(id, contractDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid") BigInteger cid,
                                    @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(contractService.delete(id, cid));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid") BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(contractService.restore(id, cid));
    }
}
