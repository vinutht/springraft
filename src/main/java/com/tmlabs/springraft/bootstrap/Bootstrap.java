package com.tmlabs.springraft.bootstrap;

import org.springframework.boot.CommandLineRunner;

public interface Bootstrap extends CommandLineRunner {

    public void setup() throws Exception;
}
