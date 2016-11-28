package edu.ntnu.grasdalk.mobiauth.models;


import com.google.gson.annotations.SerializedName;

public class User {
    int id;
    String username;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    public String email;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
