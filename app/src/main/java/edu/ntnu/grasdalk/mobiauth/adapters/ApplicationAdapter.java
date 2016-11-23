package edu.ntnu.grasdalk.mobiauth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.R;
import edu.ntnu.grasdalk.mobiauth.models.Application;

public class ApplicationAdapter
        extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Application> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        public TextView applicationName;
        public TextView organizationName;
        public TextView applicationTestdata;

        public ApplicationViewHolder(View view) {
            super(view);
            applicationName = (TextView) view.findViewById(R.id.application_name);
            organizationName = (TextView) view.findViewById(R.id.organization_name);
            applicationTestdata = (TextView) view.findViewById(R.id.application_testdata);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ApplicationAdapter(List<Application> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_list_entry, parent, false);

        return new ApplicationViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.applicationName.setText(mDataset.get(position).getName());
        holder.organizationName.setText(String.valueOf(mDataset.get(position).getOrganizationId()));
        holder.applicationTestdata.setText(String.valueOf(mDataset.get(position).isRequirePhotoBiometrics()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
