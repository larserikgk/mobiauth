package edu.ntnu.grasdalk.mobiauth;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.models.Organization;
import edu.ntnu.grasdalk.mobiauth.models.StackOverflowQuestions;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MobiauthClient {

    @GET("organizations/?format=json")
    Call<List<Organization>> organizations();

    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Call<StackOverflowQuestions> loadQuestions(@Query("tagged") String tags);
}
