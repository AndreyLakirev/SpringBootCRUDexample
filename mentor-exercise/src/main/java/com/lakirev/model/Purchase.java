package com.lakirev.model;

import java.util.Date;
import java.util.Objects;

public class Purchase {
    private Integer cost;
    private Product product;
    private Date date;

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(cost, purchase.cost) && Objects.equals(product, purchase.product) && Objects.equals(date, purchase.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, product, date);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "cost=" + cost +
                ", product=" + product +
                ", date=" + date +
                '}';
    }
}
