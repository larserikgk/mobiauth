package edu.ntnu.grasdalk.mobiauth.api;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.models.Application;
import edu.ntnu.grasdalk.mobiauth.models.AuthenticationSession;
import edu.ntnu.grasdalk.mobiauth.models.Organization;
import edu.ntnu.grasdalk.mobiauth.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MobiauthClient {

    @GET("users/{username}/?format=json")
    Call<User> getUser(@Path("username") String username);

    @GET("organizations/?format=json")
    Call<List<Organization>> getOrganizations();

    @GET("applications/?format=json")
    Call<List<Application>> getApplications();

    @POST("authentication/create")
    Call<AuthenticationSession> creatAuthenticationSession(@Body AuthenticationSession session);
}
