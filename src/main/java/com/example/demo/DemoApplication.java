package com.example.demo;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Configuration
@EnableScheduling
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@Slf4j
@RestController
@Timed
class HelloWorldController {

    private final MeterRegistry meterRegistry;

    public HelloWorldController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @Scheduled(fixedRate = 1000)
    @Timed(description = "Time spent serving orders")
    public void generateRandom() throws InterruptedException {
        Random random = new Random();
        int rvalue = random.nextInt(1000);
        Gauge.builder("wavefrontbootiful.randomValue", rvalue, Integer::intValue)
                .description("Latest Random Value Generated")
                .register(meterRegistry);
        log.error(String.valueOf(rvalue));
    }
}