package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DummyUserService implements UserService {


    @Autowired
    private GreetingService greetingService;

    public void greetAll() {
        ArrayList<String> people = new ArrayList();
        people.add("Elodie");
        people.add("Charles");

        for (String person : people) {
            greetingService.greet(person);
        }
    }


}
