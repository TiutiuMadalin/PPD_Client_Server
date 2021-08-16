package com.ppd.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Currency implements Serializable {
    @Column(name = "value")
    private double value;

    @Column(name = "currency")
    private String currency;

    public Currency(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public Currency() {
    }

    public void add(Currency currency) {
        if (!this.currency.equals(currency.getCurrency()))
            throw new RuntimeException("Different currency type.");
        this.value += currency.value;
    }

    public Currency multiply(int count) {
        return new Currency(value * count, this.currency);
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String toString() {return "Currency(value=" + this.getValue() + ", currency=" + this.getCurrency() + ")";}
}
