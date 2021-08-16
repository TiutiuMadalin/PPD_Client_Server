package com.ppd.domain;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "log_show")
public class LogShow implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "show_id")
    private long showId;

    @Column(name = "correct")
    private int correct;

    @Column(name = "incorrect")
    private int incorrect;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<LogSale> sales;

    @Embedded
    private Currency totalSoldValue;

    public LogShow() {}

    public LogShow(LogShow show) {
        this.id = show.getId();
        this.showId = show.getShowId();
        this.correct = show.getCorrect();
        this.incorrect = show.getIncorrect();
        this.sales = show.getSales();
        this.totalSoldValue = show.getTotalSoldValue();
    }

    public LogShow(long showId, int correct, int incorrect, List<LogSale> sales, Currency totalSoldValue) {
        this.showId = showId;
        this.correct = correct;
        this.incorrect = incorrect;
        this.sales = sales;
        this.totalSoldValue = totalSoldValue;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public long getShowId() {return this.showId;}

    public int getCorrect() {return this.correct;}

    public synchronized void incCorrect() {this.correct++;}

    public synchronized int getIncorrect() {return this.incorrect;}

    public void incIncorrect() {this.incorrect++;}

    public List<LogSale> getSales() {return this.sales;}

    public Currency getTotalSoldValue() {return this.totalSoldValue;}

    public synchronized void addToTotalSoldValue(Currency value) {this.totalSoldValue.add(value);}

    @Override
    public String toString() {
        return "LogShow{" +
                "id=" + id +
                ", showId=" + showId +
                ", correct=" + correct +
                ", incorrect=" + incorrect +
                ", sales=" + sales +
                ", totalSoldValue=" + totalSoldValue +
                '}';
    }
}
