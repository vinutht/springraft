package com.tmlabs.springraft.repository;

import com.tmlabs.springraft.entities.LogEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest
public class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    @Test
    public void whenDeleteAll_then0IsExpected() {


        logRepository.deleteAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    /*@Test
    public void whenInsert6_then6AreExpected() {

        insertPlayers();

        logRepository.findAll().subscribe(System.out::println);

        logRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(6)
                .verifyComplete();
    }*/

    @Test
    public void whenSearchForCommand_then1IsExpected() {

        insertPlayers();

        logRepository.findAll().subscribe(System.out::println);

        logRepository.findByCommand("SET height 6.0")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }



    private void insertPlayers() {

        logRepository.deleteAll();

        List<LogEntry> logEntries = Arrays.asList(
                new LogEntry(null, 1, "SET name vinuth", UUID.randomUUID().toString()),
                new LogEntry(null, 2, "SET age 40", UUID.randomUUID().toString()),
                new LogEntry(null, 3, "SET height 6.0", UUID.randomUUID().toString()),
                new LogEntry(null, 4, "SET weight 60", UUID.randomUUID().toString()),
                new LogEntry(null, 5, "SET home bangalore", UUID.randomUUID().toString()),
                new LogEntry(null, 6, "SET work hpe", UUID.randomUUID().toString())
        );

        logRepository.saveAll(logEntries).subscribe();
    }
}
