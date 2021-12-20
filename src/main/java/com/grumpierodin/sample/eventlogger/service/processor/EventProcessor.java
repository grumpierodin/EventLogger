package com.grumpierodin.sample.eventlogger.service.processor;

import com.grumpierodin.sample.eventlogger.repository.EventStore;
import com.grumpierodin.sample.eventlogger.repository.MatchStore;
import com.grumpierodin.sample.eventlogger.repository.data.Event;
import com.grumpierodin.sample.eventlogger.repository.data.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventProcessor implements DataProcessor{
    private final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);
    @Autowired
    EventStore eventStore;
    @Autowired
    MatchStore matchStore;
    @Override
    public void onData(Object data) {
        Event event = (Event) data;
        LOGGER.debug(event.toString());
        Optional<Event> foundEvent = eventStore.findFirstByIdOrderByTimestampDesc(event.getId());
        LOGGER.debug(" is found: " + foundEvent.isPresent());
        if (foundEvent.isPresent()) {
            Event previousEvent = foundEvent.get();
            if ("STARTED".equalsIgnoreCase(previousEvent.getState())) {
                Match matched = new Match(previousEvent.getId(), previousEvent.getType(), previousEvent.getHost(), previousEvent.getTimestamp(), event.getTimestamp());
                matchStore.save(matched);
                if (matched.isAlert()) {
                    LOGGER.info(matched.toString());
                }
            } else {
                Match matched = new Match(event.getId(), event.getType(), event.getHost(), event.getTimestamp(), previousEvent.getTimestamp());
                matchStore.save(matched);
                if (matched.isAlert()) {
                    LOGGER.info(matched.toString());
                }
            }
        }
        eventStore.save(event);
    }
}
