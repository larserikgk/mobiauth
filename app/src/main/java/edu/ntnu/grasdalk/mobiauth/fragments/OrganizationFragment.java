package edu.ntnu.grasdalk.mobiauth.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.MainActivity;
import edu.ntnu.grasdalk.mobiauth.R;
import edu.ntnu.grasdalk.mobiauth.adapters.OrganizationAdapter;
import edu.ntnu.grasdalk.mobiauth.api.MobiauthClient;
import edu.ntnu.grasdalk.mobiauth.api.ServiceGenerator;
import edu.ntnu.grasdalk.mobiauth.models.Organization;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mRecyclerViewLabel;
    private MobiauthClient mobiauthClient;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE);

        mobiauthClient = ServiceGenerator.createService(
                MobiauthClient.class,
                getResources().getString(R.string.server_api_path),
                sharedPref.getString(getString(R.string.prompt_username), ""),
                sharedPref.getString(getString(R.string.prompt_password), ""));

        return inflater.inflate(R.layout.fragment_organization, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mRecyclerViewLabel = (TextView) getActivity().findViewById(R.id.recycler_view_label);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewLabel.setText("Organizations you have access to:");

        Call<List<Organization>> call = mobiauthClient.getOrganizations();
        call.enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(Call<List<Organization>> call, Response<List<Organization>> response) {
                if (response.isSuccessful()) {
                    mAdapter = new OrganizationAdapter(response.body());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.d("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Organization>> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast
                        .makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}
