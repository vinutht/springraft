package com.tmlabs.springraft.services;

import com.tmlabs.springraft.entities.ElectionTerm;
import com.tmlabs.springraft.entities.LogEntry;
import com.tmlabs.springraft.repository.ElectionTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ElectionTermService {

    @Autowired
    private ElectionTermRepository electionTermRepository;

    public void init() {

        Mono<ElectionTerm> etMono = electionTermRepository
                .findById(1)
                .switchIfEmpty(Mono.defer(() -> {
                    ElectionTerm electionTerm = new ElectionTerm();
                    electionTerm.setCurrentTerm(-1);
                    electionTerm.setVotedFor(-1);
                    electionTermRepository.save(electionTerm).subscribe();
                    return electionTermRepository.findById(1);
                }));

        etMono.subscribe();

    }
}
