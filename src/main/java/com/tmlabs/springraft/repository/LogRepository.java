package com.tmlabs.springraft.repository;

import com.tmlabs.springraft.entities.LogEntry;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LogRepository extends ReactiveCrudRepository<LogEntry, Long> {

    Mono<LogEntry> findByCommand(String command);
}
