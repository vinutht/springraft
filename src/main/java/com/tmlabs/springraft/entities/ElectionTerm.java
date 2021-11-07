package com.tmlabs.springraft.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "election_term")
public class ElectionTerm {
    @Id
    private Long id;

    @Column(name = "current_term")
    private int currentTerm;

    @Column(name = "voted_form")
    private int votedFor;

}
