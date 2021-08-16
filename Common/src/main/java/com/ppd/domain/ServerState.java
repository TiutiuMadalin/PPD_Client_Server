package com.ppd.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@javax.persistence.Entity
@Table(name = "server_state")
public class ServerState implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "server_state_id")
    private List<LogShow> shows;

    @Column(name = "log_time")
    private LocalDateTime logTime;

    public ServerState() {}

    public ServerState(List<LogShow> shows, LocalDateTime logTime) {
        this.shows = shows;
        this.logTime = logTime;
    }

    @Override
    public Long getId() {
        return id;
    }

    public List<LogShow> getShows() {return this.shows;}

    public LocalDateTime getLogTime() {return this.logTime;}

    @Override
    public String toString() {
        return "ServerState{" +
                "id=" + id +
                ", shows=" + shows +
                ", logTime=" + logTime +
                '}';
    }
}
