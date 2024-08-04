package com.example.demo.Controller;

import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Service.UtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/utility")
public class UtilityController {
    private final UtilityService utilityService;

    public UtilityController(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(utilityService.search(payload,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for utilities: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid")BigInteger cid) {
        try {
            return ResponseEntity.ok(utilityService.getAll(cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for utilities: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody UtilityDTO utilityDTO) {
        try {
            return ResponseEntity.ok(utilityService.create(utilityDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Utility: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid")BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(utilityService.findById(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Utility: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@RequestHeader("cid")BigInteger cid,
                                    @PathVariable("id") BigInteger id,
                                    @RequestBody UtilityDTO utilityDTO) {
        try {
            return ResponseEntity.ok(utilityService.update(id, utilityDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Utility: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid")BigInteger cid,
                                    @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(utilityService.delete(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete Utility: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid")BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(utilityService.restore(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore Utility: " + e.getMessage());
        }
    }

}
