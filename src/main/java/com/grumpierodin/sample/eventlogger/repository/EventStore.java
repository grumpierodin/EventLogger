package com.grumpierodin.sample.eventlogger.repository;

import com.grumpierodin.sample.eventlogger.repository.data.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventStore extends JpaRepository<Event, String>{
    List<Event> findAllByIdOrderByTimestamp(String id);

    Optional<Event> findFirstByIdOrderByTimestampDesc(String id);
}
