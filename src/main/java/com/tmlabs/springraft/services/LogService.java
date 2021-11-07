package com.tmlabs.springraft.services;

import com.tmlabs.springraft.entities.LogEntry;
import com.tmlabs.springraft.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void appendLog(LogEntry logEntry) {
        logRepository.save(logEntry);
    }
}
