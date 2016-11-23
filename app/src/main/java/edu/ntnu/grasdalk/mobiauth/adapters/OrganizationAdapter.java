package edu.ntnu.grasdalk.mobiauth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.R;
import edu.ntnu.grasdalk.mobiauth.models.Organization;

public class OrganizationAdapter
        extends RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder> {
    private List<Organization> mDataset;


    public static class OrganizationViewHolder extends RecyclerView.ViewHolder {

        public TextView organizationName;
        public OrganizationViewHolder(View view) {
            super(view);
            organizationName = (TextView) view.findViewById(R.id.organizationlist_organization_name);
        }
    }

    public OrganizationAdapter(List<Organization> dataset) {
        mDataset = dataset;
    }

    @Override
    public OrganizationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organization_list_entry, parent, false);

        return new OrganizationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrganizationViewHolder holder, int position) {
        holder.organizationName.setText(mDataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
