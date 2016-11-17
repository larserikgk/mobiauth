package edu.ntnu.grasdalk.mobiauth.models;


public class User {
    int id;
    String username;
    String first_name;
    String last_name;
    String email;

    @Override
    public String toString() {
        return first_name + " " +last_name;
    }
}
