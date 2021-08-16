package com.ppd.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "sale")
public class Sale implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "show_id")
    private Show show;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "sold_seats")
    private Set<Integer> soldSeats;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    public Sale(long id, Show show, Set<Integer> soldSeats, LocalDateTime saleDate) {
        this.id = id;
        this.show = show;
        this.soldSeats = soldSeats;
        this.saleDate = saleDate;
    }

    public Sale() {}

    public Set<Integer> getSoldSeats() {
        return soldSeats;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String toString() {
        return "Sale(id=" + this.getId() + ", show=" + this.show.getId() + ", soldSeats=" + this.getSoldSeats() + ", " +
                "saleDate=" + this.saleDate + ")";
    }
}
