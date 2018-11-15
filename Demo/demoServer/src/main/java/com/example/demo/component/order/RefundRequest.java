package com.example.demo.component.order;

import com.example.demo.component.location.Location;
import com.example.demo.component.staff.Staff;
import com.example.demo.component.user.User;
import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.model.HourHasPrice;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_refund_request")
public class RefundRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "amount")
    private Double amount;

    @JoinColumn(name = "tbl_staft_username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Staff staff;

    @JoinColumn(name = "tbl_manager_username", referencedColumnName = "username")
    @ManyToOne
    private Staff manager;


    @JoinColumn(name = "tbl_refund_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RefundStatus refundStatus;

    @Column(name = "tbl_order_id")
    private Integer orderId;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "close_date")
    private Long closeDate;

    @Transient
    private Order order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Staff getManager() {
        return manager;
    }

    public void setManager(Staff manager) {
        this.manager = manager;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Long closeDate) {
        this.closeDate = closeDate;
    }
}
