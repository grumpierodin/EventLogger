package com.grumpierodin.sample.eventlogger.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.grumpierodin.sample.eventlogger.repository.data.Event;
import com.grumpierodin.sample.eventlogger.service.processor.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
@Service
public class EventLogReader implements DataProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(EventLogReader.class);
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    public EventLogReader() {
    }
    public void execute(String fileName, DataProcessor processor) {
        try {
            InputStream is = getLogDataStream(fileName);
            parseJson(is, processor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void execute(String fileName) {
        try {
            InputStream is = getLogDataStream(fileName);
            parseJson(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void parseJson(InputStream is) throws IOException {
        parseJson(is, this);
    }

    public void parseJson(InputStream is, DataProcessor processor) throws IOException {
        try (JsonParser jsonParser = mapper.getFactory().createParser(is)) {
            if (jsonParser.nextToken() != JsonToken.START_OBJECT) {
                throw new IllegalStateException("Expected content to be an object");
            }
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                if(jsonParser.hasCurrentToken()) {
                    Object event = mapper.readValue(jsonParser, Event.class);
                    processor.onData(event);
                }
                if(jsonParser.isClosed()) {
                    break;
                }
            }
        }
    }
    public InputStream getLogDataStream(String fileName) throws IOException {
        File input = new File(fileName);
        return new FileInputStream(input);
    }
    @Override
    public void onData(Object data) {

    }
}
