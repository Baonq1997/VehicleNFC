package trung.topupdemo;

import java.io.Serializable;
import java.math.BigDecimal;

class ProductObj implements Serializable {

    private String Name;
    private BigDecimal Price;
    private String Currency;

    public ProductObj(String name, int price, String currency) {
        Name = name;
        Price = new BigDecimal(price);
        Currency = currency;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

}
