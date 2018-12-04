package com.example.demo.component.order;

import com.example.demo.component.staff.Staff;
import com.example.demo.component.staff.StaffRepository;
import com.example.demo.component.user.User;
import com.example.demo.component.user.UserRepository;
import com.example.demo.config.OrderStatusEnum;
import com.example.demo.config.RefundStatusEnum;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RefundRequestService {

    @Autowired
    private EntityManager entityManager;

    private final RefundRequestRepository refundRequestRepository;
    private final RefundStatusRepository refundStatusRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final StaffRepository staffRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public RefundRequestService(RefundRequestRepository refundRequestRepository, RefundStatusRepository refundStatusRepository, OrderStatusRepository orderStatusRepository, StaffRepository staffRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.refundRequestRepository = refundRequestRepository;
        this.refundStatusRepository = refundStatusRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.staffRepository = staffRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefundRequest> getRequestById(Integer id) {
        return refundRequestRepository.findById(id);
    }

    public ResponseObject searchRequest(List<SearchCriteria> params, int pagNumber, int pageSize) {
        ResponseObject responseObject = new ResponseObject();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RefundRequest> query = builder.createQuery(RefundRequest.class);
        Root r = query.from(RefundRequest.class);

        Predicate predicate = builder.conjunction();

        for (SearchCriteria param : params) {
            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                Object type;
                if (param.getKey().equalsIgnoreCase("manager")) {
                    type = Staff.class;
                } else if (param.getKey().equalsIgnoreCase("orderId")){
                    type = Order.class;
                } else {
                    type = r.get(param.getKey()).getJavaType();
                }
                if (type == Order.class) {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()),
                                     param.getValue()));
                } else if (r.get(param.getKey()).getJavaType() == Staff.class) {
                    if (param.getKey().equalsIgnoreCase("manager")) {
                        Join<RefundRequest, Staff> join = r.join("manager");
                        Predicate staffPredicate = builder.like(join.get("username"), "%" + param.getValue() + "%");
                        predicate = builder.and(predicate, staffPredicate);
                    } else {
                        Join<RefundRequest, Staff> join = r.join("staff");
//                        Predicate rolePredicate = builder.equal(join.get("isManager"), )
                        Predicate staffPredicate = builder.like(join.get("username"), "%" + param.getValue() + "%");
                        predicate = builder.and(predicate, staffPredicate);
                    }
                } else if (r.get(param.getKey()).getJavaType() == RefundStatus.class) {
                    Join<RefundRequest, Staff> join = r.join("refundStatus");
                    Predicate staffPredicate = builder.like(join.get("name"), "%" + param.getValue() + "%");
                    predicate = builder.and(predicate, staffPredicate);
                }
            } else {
                predicate = builder.and(predicate,
                        builder.equal(r.get(param.getKey()), param.getValue()));
            }
        }

        query.where(predicate);
        TypedQuery<RefundRequest> typedQuery = entityManager.createQuery(query);
        List<RefundRequest> result = typedQuery.getResultList();

        int totalPages = (int) Math.ceil((double) result.size() / pageSize);
        typedQuery.setFirstResult(pagNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<RefundRequest> refundRequestList = typedQuery.getResultList();
        //TODO ????????????
        for (RefundRequest refundRequest : refundRequestList){
            refundRequest.setOrder(orderRepository.findById(refundRequest.getOrderId()).get());
        }
        responseObject.setData(refundRequestList);
        responseObject.setTotalPages(totalPages);
        responseObject.setPageNumber(pagNumber);
        responseObject.setPageSize(pageSize);
        return responseObject;
    }

    @Transactional
    public boolean requestRefundOrder(Integer orderId, String username, double amount, String description) {
        Optional<Staff> staff = staffRepository.findByUsername(username);
        if (staff.isPresent()) {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                order.get().setOrderStatusId(orderStatusRepository.findByName(OrderStatusEnum.RefundRequested.getName()).get());
                orderRepository.save(order.get());
                RefundRequest refundRequest = new RefundRequest();
                refundRequest.setStaff(staff.get());
                refundRequest.setAmount(amount);
                refundRequest.setCreateDate(new Date().getTime());
                refundRequest.setOrderId(orderId);
                refundRequest.setDescription(description);
                refundRequest.setRefundStatus(refundStatusRepository.findByName(RefundStatusEnum.Open.getName()).get());
                refundRequestRepository.save(refundRequest);
                return true;
            }
        }
        return false;
    }

    public Optional<RefundRequest> approveRefundOrder(Integer requestId, String username, boolean isApprove) {
        Optional<Staff> manager = staffRepository.findByUsername(username);
        if (manager.isPresent()) {
            Optional<RefundRequest> refundRequest = refundRequestRepository.findById(requestId);
            if (refundRequest.isPresent()) {
                Optional<Order> order = orderRepository.findById(refundRequest.get().getOrderId());
                if (order.isPresent()) {
                    if (isApprove) {
                        order.get().setOrderStatusId(orderStatusRepository.findByName(OrderStatusEnum.Refunded.getName()).get());
                        orderRepository.save(order.get());
                        User user = order.get().getUserId();
                        user.setMoney(user.getMoney() + refundRequest.get().getAmount());
                        userRepository.save(user);
                    } else {
                        order.get().setOrderStatusId(orderStatusRepository.findByName(OrderStatusEnum.Close.getName()).get());
                        orderRepository.save(order.get());
                    }
                    refundRequest.get().setManager(manager.get());
                    refundRequest.get().setCloseDate(new Date().getTime());
                    refundRequest.get().setRefundStatus(refundStatusRepository.findByName(RefundStatusEnum.approveEnum(isApprove).getName()).get());
                    refundRequestRepository.save(refundRequest.get());
                    return refundRequest;
                }
            }
        }
        return null;
    }
}
