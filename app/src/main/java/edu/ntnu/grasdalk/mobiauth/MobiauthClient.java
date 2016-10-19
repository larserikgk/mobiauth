package edu.ntnu.grasdalk.mobiauth;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.models.Application;
import edu.ntnu.grasdalk.mobiauth.models.Organization;
import edu.ntnu.grasdalk.mobiauth.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MobiauthClient {

    @GET("users/{username}/?format=json")
    Call<User> getUser(@Path("username") String username);

    @GET("getOrganizations/?format=json")
    Call<List<Organization>> getOrganizations();

    @GET("getApplications/?format=json")
    Call<List<Application>> getApplications();
}
