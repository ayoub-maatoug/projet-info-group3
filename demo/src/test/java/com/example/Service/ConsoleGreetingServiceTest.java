package com.example.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
public class ConsoleGreetingServiceTest {

    @Test
    public void testGreeting( CapturedOutput output) {
        ConsoleGreetingService greetingService = new ConsoleGreetingService(); // 1
        greetingService.greet("Spring");

        Assertions.assertThat(output.getAll()).contains("Hello, Elodie!", "Hello, Charles!");
    }
}