package edu.ntnu.grasdalk.mobiauth.models;


public class User {
    int id;
    String username;
    public String first_name;
    public String last_name;
    public String email;

    @Override
    public String toString() {
        return first_name + " " +last_name;
    }
}
