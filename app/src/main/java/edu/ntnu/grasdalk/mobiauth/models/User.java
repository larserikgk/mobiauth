package edu.ntnu.grasdalk.mobiauth.models;


public class User {
    int id;
    String username;
    String firstName;
    String lastName;
    String email;

    @Override
    public String toString() {
        return firstName + lastName;
    }
}
