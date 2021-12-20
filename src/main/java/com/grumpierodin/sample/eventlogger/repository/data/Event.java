package com.grumpierodin.sample.eventlogger.repository.data;

import javax.persistence.*;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventid;
    private final String id;
    private final String state;
    private final String type;
    private final String host;
    private final long timestamp;

    public Event(String id, String state, String type, String host, long timestamp) {
        this.id = id;
        this.state = state;
        this.type = type;
        this.host = host;
        this.timestamp = timestamp;
    }

    public Event() {
        this.id = "";
        this.state = "";
        this.type = "";
        this.host = "";
        this.timestamp = 0L;
    }

    public String getId() {
        return id;
    }
    public String getState() {
        return state;
    }
    public String getType() {
        return type;
    }
    public String getHost() {
        return host;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public long getEntryId() {
        return eventid;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventid=" + eventid +
                ", id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
