package com.example.Service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class  ConsoleGreetingService implements GreetingService {
    @Override
    public void greet(String name) {
        System.out.println(Arrays.asList("Hello, Elodie!", "Hello, Charles!"));
    }
}
