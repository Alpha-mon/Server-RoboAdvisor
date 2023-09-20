package org.ai.roboadvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RoboAdvisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoboAdvisorApplication.class, args);
    }

}
