package com.transporterapp.syde.transporterapp.FarmerList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.R;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FarmerItem} and makes a call to the
 * specified {@link FarmerListFrag.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFarmerRecyclerViewAdapter extends RecyclerView.Adapter<MyFarmerRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final ArrayList<FarmerItem> mValues;
    private final FarmerListFrag.OnListFragmentInteractionListener mListener;
    private FarmerFilter farmerFilter;
    private ArrayList<FarmerItem> farmerItemList;
    private ArrayList<FarmerItem> filteredList;

    public MyFarmerRecyclerViewAdapter(ArrayList<FarmerItem> farmerItemList, FarmerListFrag.OnListFragmentInteractionListener listener) {
        mValues = farmerItemList;
        mListener = listener;
        filteredList = farmerItemList;

        getFilter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_farmer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = filteredList.get(position);
        //holder.mIdView.setText(filteredList.get(position).getId());
        String farmerName = filteredList.get(position).getFirstName() + " " + filteredList.get(position).getLastName();
        holder.mContentView.setText(farmerName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public FarmerItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    @Override
    public Filter getFilter(){
        if (farmerFilter == null) {
            farmerFilter = new FarmerFilter();
        }

        return farmerFilter;
    }


    /**
     * Custom filter for farmer list
     * Filter content in farmer list according to the search text
     */
    private class FarmerFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults filterResults = new FilterResults();

            if(constraint != null && constraint.length()>0){
                ArrayList<FarmerItem> tempList = new ArrayList<FarmerItem>();

                for (FarmerItem farmerItem : mValues) {
                    if(farmerItem.getFirstName().toLowerCase().contains(constraint.toString()) || farmerItem.getLastName().toLowerCase().contains(constraint.toString())){
                        tempList.add(farmerItem);
                    }
                }

                filterResults.values = tempList;
            } else {
                filterResults.values = mValues;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            filteredList = (ArrayList<FarmerItem>) results.values;
            notifyDataSetChanged();
        }

    }
}
