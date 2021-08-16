package com.ppd.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "show_room")
public class ShowRoom implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "seat_count")
    private long seatCount;

    @Column(name = "shows")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "showRoom")
    private List<Show> shows;

    public ShowRoom(long id, long seatCount, List<Show> shows) {
        this.id = id;
        this.seatCount = seatCount;
        this.shows = shows;
    }

    public ShowRoom() {}

    @Override
    public Long getId() {
        return id;
    }

    public List<Show> getShows() {
        return shows;
    }

    public long getSeatCount() {
        return seatCount;
    }

    public String toString() {
        return "ShowRoom(id=" + this.getId() + ", seatCount=" + this.seatCount + ", shows=" + this.getShows() + ")";
    }
}
