package com.transporterapp.syde.transporterapp.CollectMilk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FarmerItem.farmer} and makes a call to the
 * specified {@link FarmerListFrag.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFarmerRecyclerViewAdapter extends RecyclerView.Adapter<MyFarmerRecyclerViewAdapter.ViewHolder> {

    private final List<FarmerItem.farmer> mValues;
    private final FarmerListFrag.OnListFragmentInteractionListener mListener;

    public MyFarmerRecyclerViewAdapter(List<FarmerItem.farmer> items, FarmerListFrag.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_farmer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId());
        String farmerName = mValues.get(position).getFirstName() + " " + mValues.get(position).getLastName();
        holder.mContentView.setText(farmerName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    String temp = "wee";
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public FarmerItem.farmer mItem;

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
}
