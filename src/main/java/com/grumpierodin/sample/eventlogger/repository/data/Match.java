package com.grumpierodin.sample.eventlogger.repository.data;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long matchid;
    private final String id;
    private final String type;
    private final String host;
    private final long start;
    private final long finish;
    private final long duration;
    private final boolean alert;

    public Match() {
        this.id = "";
        this.type = "";
        this.host = "";
        this.start = 0L;
        this.finish = 0L;
        this.duration =0L;
        this.alert= false;
    }
    public Match(String id, String type, String host, long start, long finish) {
        this.id = id;
        this.type = type;
        this.host = host;
        this.start = start;
        this.finish = finish;
        LocalDateTime started = Instant.ofEpochMilli(start).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime finished = Instant.ofEpochMilli(finish).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.duration = ChronoUnit.MILLIS.between(started, finished);
        this.alert = this.duration > 4;
    }
    public long getMatchid() {
        return matchid;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public long getStart() {
        return start;
    }

    public long getFinish() {
        return finish;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isAlert() {
        return alert;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchid=" + matchid +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", start=" + start +
                ", finish=" + finish +
                ", duration=" + duration +
                ", alert=" + alert +
                '}';
    }
}
