package com.tmlabs.springraft.repository;

import com.tmlabs.springraft.entities.ElectionTerm;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionTermRepository extends ReactiveCrudRepository<ElectionTerm, Integer> {
}
