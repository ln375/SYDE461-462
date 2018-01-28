package com.transporterapp.syde.transporterapp.History;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.DataStructures.FarmerItem;
import com.transporterapp.syde.transporterapp.DataStructures.MilkRecord;
import com.transporterapp.syde.transporterapp.History.HistListFrag.OnListFragmentInteractionListener;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.dbUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MilkRecord} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMilkRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyMilkRecordRecyclerViewAdapter.ViewHolder> {

    private final List<MilkRecord> mValues;
    private final OnListFragmentInteractionListener mListener;
    private View view;

    public MyMilkRecordRecyclerViewAdapter(List<MilkRecord> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_milkrecord, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        String farmerId = mValues.get(position).getFarmerId();

        List<FarmerItem> farmer = commonUtil.convertCursorToFarmerItemList(dbUtil.selectStatement(DatabaseConstants.tblFarmer, "id", "=", farmerId, view.getContext()));
        if (farmer != null) {
            String name = farmer.get(0).getFirstName() + " " + farmer.get(0).getLastName();
            holder.mContentView.setText(mValues.get(position).getDate() + " : " + name);
        }

        if (holder.mContentView.getText().toString().isEmpty()) {
            String temp = "What is going on?";
        }

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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public MilkRecord mItem;

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
