package com.example.demo.component.order;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefundStatusService {
    private final RefundStatusRepository refundStatusRepository;

    public RefundStatusService(RefundStatusRepository refundStatusRepository) {
        this.refundStatusRepository = refundStatusRepository;
    }

    public Optional<RefundStatus> getRefundStatusById(Integer id){
        return refundStatusRepository.findById(id);
    }
}
