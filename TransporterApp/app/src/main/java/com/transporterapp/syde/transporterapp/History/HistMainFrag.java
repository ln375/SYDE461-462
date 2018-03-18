package com.transporterapp.syde.transporterapp.History;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistMainFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistMainFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistMainFrag extends Fragment {

    private OnFragmentInteractionListener mListener;
    private HistListFrag histListFrag = new HistListFrag();
    private HistOverviewFrag histOverviewFrag = new HistOverviewFrag();
    private Button btnOverview;
    private Button btnTransaction;

    public HistMainFrag() {
        // Required empty public constructor
    }

    public static HistMainFrag newInstance(String param1, String param2) {
        HistMainFrag fragment = new HistMainFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hist_main, container, false);

        // Set title bar
        ((Main) getActivity()).setActionBarTitle("History");

        btnOverview = (Button) v.findViewById(R.id.hist_overview_button);
        btnTransaction = (Button) v.findViewById(R.id.hist_transaction_button);

        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", getArguments().getString("userid"));

                if (histListFrag.getArguments() != null) {
                    histListFrag.getArguments().clear();
                    histListFrag.getArguments().putAll(bundle);
                } else {
                    histListFrag.setArguments(bundle);
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, histListFrag).addToBackStack(null).commit();
            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", getArguments().getString("userid"));

                if (histOverviewFrag.getArguments() != null) {
                    histOverviewFrag.getArguments().clear();
                    histOverviewFrag.getArguments().putAll(bundle);
                } else {
                    histOverviewFrag.setArguments(bundle);
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, histOverviewFrag).addToBackStack(null).commit();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
