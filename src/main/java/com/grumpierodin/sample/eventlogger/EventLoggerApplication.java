package com.grumpierodin.sample.eventlogger;

import com.grumpierodin.sample.eventlogger.repository.EventStore;
import com.grumpierodin.sample.eventlogger.repository.MatchStore;
import com.grumpierodin.sample.eventlogger.repository.data.Event;
import com.grumpierodin.sample.eventlogger.repository.data.Match;
import com.grumpierodin.sample.eventlogger.service.EventLogReader;
import com.grumpierodin.sample.eventlogger.service.processor.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class EventLoggerApplication implements CommandLineRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(EventLoggerApplication.class);
    @Autowired
    EventLogReader reader;
    @Autowired
    EventProcessor eventProcessor;
    @Autowired
    EventStore eventStore;
    @Autowired
    MatchStore matchStore;
    public static void main(String[] args) {
        SpringApplication.run(EventLoggerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for(String name : args) {
            LOGGER.debug(name);
            if(new File(name).exists()) {
                reader.execute(name, eventProcessor);
            }
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Events");
            for(Event event : eventStore.findAll()) {
                LOGGER.info(event.toString());
            }
            LOGGER.info("Matched");
            for(Match match : matchStore.findAll()) {
                LOGGER.info(match.toString());
            }
        }
    }
}
