package com.grumpierodin.sample.eventlogger.service.processor;

import org.springframework.stereotype.Component;

@Component
public interface DataProcessor {
    void onData(Object data);
}
