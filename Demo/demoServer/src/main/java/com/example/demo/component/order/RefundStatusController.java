package com.example.demo.component.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/refund-status")
public class RefundStatusController {

    private final RefundStatusService refundStatusService;

    public RefundStatusController(RefundStatusService refundStatusService) {
        this.refundStatusService = refundStatusService;
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getTransactionStatusById(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(refundStatusService.getRefundStatusById(id));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Refund Request");
        }
    }
}
