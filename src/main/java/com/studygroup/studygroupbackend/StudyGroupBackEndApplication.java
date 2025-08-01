package com.studygroup.studygroupbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyGroupBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyGroupBackEndApplication.class, args);
    }

}
