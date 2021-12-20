package com.grumpierodin.sample.eventlogger;

import com.grumpierodin.sample.eventlogger.repository.EventStore;
import com.grumpierodin.sample.eventlogger.repository.MatchStore;
import com.grumpierodin.sample.eventlogger.repository.data.Event;
import com.grumpierodin.sample.eventlogger.service.EventLogReader;
import com.grumpierodin.sample.eventlogger.service.processor.EventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
@DataJpaTest
public class EventProcessingTest {
    @TestConfiguration
    static class EventRepositoryTestContextConfiguration {

        @Bean
        public EventLogReader reader() {
            return new EventLogReader();
        }
        @Bean
        public EventProcessor processor() {
            return new EventProcessor();
        }
    };

    @Autowired
    private TestEntityManager entityManager;
    @BeforeEach
    public void clearEntityManger() {
        entityManager.clear();
    }
    @Autowired
    EventStore eventStore;
    @Autowired
    MatchStore matchStore;
    private final Event event1 = new Event("1234", "STARTED", "", "localhost", 1491377495212L);
    private final Event event2 = new Event("1234", "FINISHED", "", "localhost", 1491377495217L);
    @Test
    void eventDetails() {
        assert("1234".equalsIgnoreCase(event1.getId()));
        assert("STARTED".equalsIgnoreCase(event1.getState()));
    }
    @Test
    void eventDetailsInDb() {
        assert("1234".equalsIgnoreCase(event1.getId()));
        assert("STARTED".equalsIgnoreCase(event1.getState()));
        eventStore.save(event1);
        Optional<Event> event = eventStore.findFirstByIdOrderByTimestampDesc(event1.getId());
        assert(event.isPresent());
    }
    @Test
    void eventFindLastTimestampForId() {
        eventStore.save(event1);
        Optional<Event> event = eventStore.findFirstByIdOrderByTimestampDesc(event1.getId());
        assert(event.isPresent());
        eventStore.save(event2);
        List<Event> events = eventStore.findAll();
        assert(events.size()==2);
        event = eventStore.findFirstByIdOrderByTimestampDesc(event1.getId());
        assert(event.isPresent());
        assert(event2.getState().equalsIgnoreCase(event.get().getState()));
    }
    void eventFindLastTimestampForIdUnOrdered() {
        eventStore.save(event2);
        Optional<Event> event = eventStore.findFirstByIdOrderByTimestampDesc(event2.getId());
        assert(event.isPresent());
        eventStore.save(event1);
        List<Event> events = eventStore.findAll();
        assert(events.size()==2);
        event = eventStore.findFirstByIdOrderByTimestampDesc(event1.getId());
        assert(event.isPresent());
        assert(event2.getState().equalsIgnoreCase(event.get().getState()));
    }
}
