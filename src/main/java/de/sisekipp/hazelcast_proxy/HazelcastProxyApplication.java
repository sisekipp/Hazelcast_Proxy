package de.sisekipp.hazelcast_proxy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HazelcastProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastProxyApplication.class, args);
    }
}
