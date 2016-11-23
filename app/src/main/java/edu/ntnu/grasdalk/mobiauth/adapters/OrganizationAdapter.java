package edu.ntnu.grasdalk.mobiauth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.R;
import edu.ntnu.grasdalk.mobiauth.models.Organization;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {
    private List<Organization> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView organizationName;
        public ViewHolder(View view) {
            super(view);
            organizationName = (TextView) view.findViewById(R.id.organizationlist_organization_name);
        }
    }

    public OrganizationAdapter(List<Organization> dataset) {
        mDataset = dataset;
    }


    @Override
    public OrganizationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organization_list_entry, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.organizationName.setText(mDataset.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
