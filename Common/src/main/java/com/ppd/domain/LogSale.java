package com.ppd.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class LogSale{
    @Column(name = "sale")
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Column(name = "successful")
    private boolean successful;

    public LogSale(Sale sale, boolean successful) {
        this.sale = sale;
        this.successful = successful;
    }

    public LogSale() {}

    public Sale getSale() {return this.sale;}

    public boolean isSuccessful() {return this.successful;}

    @Override
    public String toString() {
        return "LogSale{" +
                "sale=" + sale +
                ", successful=" + successful +
                '}';
    }
}
