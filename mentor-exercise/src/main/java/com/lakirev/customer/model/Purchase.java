package com.lakirev.customer.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.Objects;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer cost;

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    private Date date;

    public Purchase() {}

    public Purchase(Integer cost, Product product, Date date) {
        this.cost = cost;
        this.product = product;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
