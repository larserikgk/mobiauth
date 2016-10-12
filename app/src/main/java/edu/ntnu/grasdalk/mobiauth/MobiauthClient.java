package edu.ntnu.grasdalk.mobiauth;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.models.Application;
import edu.ntnu.grasdalk.mobiauth.models.Organization;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MobiauthClient {

    @GET("organizations/?format=json")
    Call<List<Organization>> organizations();

    @GET("applications/?format=json")
    Call<List<Application>> applications();
}
