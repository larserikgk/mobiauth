package edu.ntnu.grasdalk.mobiauth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.R;
import edu.ntnu.grasdalk.mobiauth.models.Application;
import edu.ntnu.grasdalk.mobiauth.models.Organization;

public class ApplicationAdapter
        extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Application> mDataset;
    private List<Organization> organizationList;


    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        public TextView applicationName;
        public TextView organizationName;
        public TextView applicationTestdata;

        public ApplicationViewHolder(View view) {
            super(view);
            applicationName = (TextView) view.findViewById(R.id.applicationlist_application_name);
            organizationName = (TextView) view.findViewById(R.id.applicationlist_organization_name);
            applicationTestdata = (TextView) view.findViewById(R.id.application_testdata);
        }
    }

    public ApplicationAdapter(List<Application> dataset, List<Organization> organizationList) {
        mDataset = dataset;
        this.organizationList = organizationList;
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_list_entry, parent, false);

        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        holder.applicationName.setText(mDataset.get(position).getName());
        holder.organizationName.setText(String.valueOf(organizationList.get(position).getName()));
        holder.applicationTestdata.setText(String.valueOf(mDataset.get(position).requiresPhotoBiometrics()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
