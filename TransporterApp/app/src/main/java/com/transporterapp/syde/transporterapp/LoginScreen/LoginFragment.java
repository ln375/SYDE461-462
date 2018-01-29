package com.transporterapp.syde.transporterapp.LoginScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.transporterapp.syde.transporterapp.CollectMilk.FarmerListFrag;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button loginButton;
    private AppCompatEditText txtUserName;
    private AppCompatEditText txtPassword;
    private FarmerListFrag farmerListFragment = new FarmerListFrag();
    public static final String PREFS_NAME = "MyPrefsFile";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button) view.findViewById(R.id.btn_login);
        txtUserName = (AppCompatEditText) view.findViewById(R.id.input_username);
        txtPassword = (AppCompatEditText) view.findViewById(R.id.input_password);

        loginButton.setOnClickListener(this);

        return view;
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

    @Override
    public void onClick(View v) {
        if (txtUserName.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()) {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Incorrect data")
                    .setMessage("Please enter a correct username and password.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, incorrectData).show();
        }

        String username = txtUserName.getText().toString();
        String password = txtPassword.getText().toString();

        Cursor user = dbUtil.selectStatement("settings", "username", "=", username, v.getContext());
        while (user.moveToNext()) {
            if (user.getString(1).equals(username)) {
                if (user.getString(2).equals(password)) {

                    SharedPreferences settings = v.getContext().getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putBoolean("hasLoggedIn", true);

                    editor.commit();

                    Intent intent = new Intent(getContext(), Main.class);
                            startActivity(intent);

                }
            }
        }

    }

    /**
     * DialogInterface listener used for showUnsavedDataMessage
     */
    private DialogInterface.OnClickListener incorrectData = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    break;
            }
        }
    };

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
