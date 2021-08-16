package com.ppd.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@javax.persistence.Entity
@Table(name = "show")
public class Show implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "show_date")
    private LocalDateTime showDate;

    @Column(name = "title")
    private String title;

    @Embedded
    private Currency ticketPrice;

    @Column(name = "sales")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "show")
    private List<Sale> sales;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "show_room_id")
    private ShowRoom showRoom;

    public Show(long id, LocalDateTime showDate, String title, Currency ticketPrice, List<Sale> sales, ShowRoom showRoom) {
        this.id = id;
        this.showDate = showDate;
        this.title = title;
        this.ticketPrice = ticketPrice;
        this.sales = sales;
        this.showRoom = showRoom;
    }

    public Show() {}

    public List<Sale> getSales() {
        return sales;
    }

    public Currency getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String toString() {
        return "Show(id=" + this.getId() + ", showDate=" + this.showDate + ", title=" + this.title + ", ticketPrice=" + this.ticketPrice + ", sales=" + this
                .getSales() + ", showRoom=" + this.showRoom.getId() + ")";
    }
}
