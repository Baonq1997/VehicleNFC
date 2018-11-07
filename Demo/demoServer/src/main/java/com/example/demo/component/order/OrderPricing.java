package com.example.demo.component.order;

import com.example.demo.component.policy.Pricing;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_order_pricing")
public class OrderPricing implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "from_hour")
    private int fromHour;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price_per_hour")
    private double pricePerHour;
    @Column(name = "late_fee_per_hour")
    private Double lateFeePerHour;

    @Basic
    @Column(name = "tbl_order_id")
    private Integer orderId;

    public OrderPricing() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getLateFeePerHour() {
        return lateFeePerHour;
    }

    public void setLateFeePerHour(Double lateFeePerHour) {
        this.lateFeePerHour = lateFeePerHour;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public static List<OrderPricing> convertListPricingToOrderPricing(List<Pricing> pricings) {
        List<OrderPricing> orderPricings = null;
        if (pricings != null) {
            orderPricings = new ArrayList<>();
            for (Pricing pricing : pricings
                    ) {
                orderPricings.add(convertListPricingToOrderPricing(pricing));
            }
        }
        return orderPricings;
    }

    public static OrderPricing convertListPricingToOrderPricing(Pricing pricing) {
        OrderPricing orderPricing = new OrderPricing();

        orderPricing.setFromHour(pricing.getFromHour());
        orderPricing.setLateFeePerHour(pricing.getLateFeePerHour());
        orderPricing.setPricePerHour(pricing.getPricePerHour());

        return orderPricing;
    }
}
