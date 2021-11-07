package com.tmlabs.springraft.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "log")
public class LogEntry {
    @Id
    private Long index;
    private int term;
    private String command;
    private String uuid;
}
