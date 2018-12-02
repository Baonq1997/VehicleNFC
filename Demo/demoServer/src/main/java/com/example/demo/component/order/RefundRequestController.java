package com.example.demo.component.order;

import com.example.demo.config.PaginationEnum;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/refund")
public class RefundRequestController {
    @Autowired
    private ServletContext servletContext;

    private final RefundRequestService refundRequestService;
    private final OrderService orderService;

    public RefundRequestController(RefundRequestService refundRequestService, OrderService orderService) {
        this.refundRequestService = refundRequestService;
        this.orderService = orderService;
    }

    @GetMapping("/index")
    public ModelAndView verifyForm(ModelAndView mav) {
        mav.setViewName("refund-request");
        return mav;
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getTransactionStatusById(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(refundRequestService.getRequestById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Refund Status");
        }
    }

    @PostMapping("/search-request")
    public ResponseEntity<ResponseObject> searchVehicle(@RequestBody List<SearchCriteria> params
            , @RequestParam(defaultValue = "0") Integer page) {
        ResponseObject response = new ResponseObject();
        response.setData(refundRequestService.searchRequest(params, page, PaginationEnum.userPageSize.getNumberOfRows()));

        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping("/request")
    public ResponseEntity<Boolean> requestRefund(@Param("orderId") Integer orderId
            , @Param("amount") String amount, @Param("description") String description
            , @Param("username") String username) {
        Map<String, String> managerList = (Map<String, String>) servletContext.getAttribute("managerTokenList");
        try {
            if (refundRequestService.requestRefundOrder(orderId, username, Double.parseDouble(amount),description)) {
                if (managerList != null) {
                    for (Map.Entry<String, String> token : managerList.entrySet()) {
                        PushNotificationService.sendRefundNotification(token.getValue(), orderId, true, null);
                    }
                }
                return ResponseEntity.status(OK).body(true);
            }
        } catch (NumberFormatException e) {
        }
        return ResponseEntity.status(OK).body(false);
    }

    @PostMapping("/approve")
    public ResponseEntity<Boolean> approveRefund(@Param("requestId") Integer requestId
            , @Param("isApprove") boolean isApprove
            , @Param("username") String username) {
        Map<String, String> staffList = (Map<String, String>) servletContext.getAttribute("staffTokenList");
        Optional<RefundRequest> refundRequest = refundRequestService.approveRefundOrder(requestId, username, isApprove);
        if (refundRequest.isPresent()) {
            if (staffList != null) {
                String token = staffList.get(refundRequest.get().getStaff().getUsername());
                if (token != null) {
                    PushNotificationService.sendRefundNotification(token, refundRequest.get().getOrderId(), false, refundRequest.get().getRefundStatus().getName());
                }
            }
            return ResponseEntity.status(OK).body(true);
        }
        return ResponseEntity.status(OK).body(false);
    }
}
