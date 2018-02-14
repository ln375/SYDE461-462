package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MilkEntryFrag extends Fragment {

    private static List<String> milkEntryColumns = Arrays.asList("MILK_WEIGHT", "SMELL", "DENSITY");

    private EditText milkVolume;
    private Button SaveData;
    private RadioGroup smellTest;
    private RadioGroup densityTest;
    private RadioGroup alcoholTest;
    private EditText txtComments;
    private String mFarmerName;
    private LinearLayout mCarouselContainer;
    //private RelativeLayout mjugHolderView;
    private RelativeLayout jugHolderView;
    private GradientDrawable jugImageDrawable;

    private static final String FARMER_NAME = "farmername";

    //Number of Jugs - may need to change this number later or add function to add jugs
    private final static int INITIAL_JUG_COUNT=5;
    private String jugIdClicked;

    private int jugSize = 10; //Placeholder

    public MilkEntryFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mFarmerName = getArguments().getString(FARMER_NAME);

            // Set title bar
            ((Main) getActivity()).setActionBarTitle(mFarmerName);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_farmer_milk_data_entry, container, false);
        Context context = view.getContext();

        //Data fields
        smellTest = (RadioGroup) view.findViewById(R.id.smell_test);
        densityTest = (RadioGroup) view.findViewById(R.id.density_test);
        alcoholTest = (RadioGroup) view.findViewById(R.id.alcohol_test);
        milkVolume = (EditText) view.findViewById(R.id.milk_volume);
        SaveData = (Button)view.findViewById(R.id.save_data);
        txtComments = (EditText) view.findViewById(R.id.comments);
        mCarouselContainer = (LinearLayout) view.findViewById(R.id.carousel);

        final List<String> jug_list = dbUtil.selectStatement("jug","id", "transporter_id", "=", getArguments().getString("transporterId"), context);

        //Carousel
        // Compute the width of a carousel item based on the screen width and number of initial items.
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int imageWidth = (int) (displayMetrics.widthPixels / INITIAL_JUG_COUNT);

        for (int i = 0 ; i < jug_list.size() ; i++) {
            final ProgressBar jugProgressBar;
            final TextView jugText;
            final TextView jugAmount;

            // Create new progress bar
            jugProgressBar = new ProgressBar(this.getContext(), null, android.R.attr.progressBarStyleHorizontal);
            jugText = new TextView(this.getContext());
            jugAmount = new TextView(this.getContext());
            jugHolderView = new RelativeLayout(this.getContext());
            Drawable jugDrawable = getResources().getDrawable(R.drawable.progressbar_states);

            //Setup jug holder view
            RelativeLayout.LayoutParams holderParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            jugHolderView.setLayoutParams(holderParams);
            jugHolderView.setPadding(10, 0, 10, 0);

            // Create new layout parameters for progress bar
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 300);
            // Apply the layout parameters for progress bar
            jugProgressBar.setLayoutParams(lp);
            jugProgressBar.setProgress(0);
            jugProgressBar.setTag(i);
            jugProgressBar.setId(i);
            jugProgressBar.setProgressDrawable(jugDrawable);
            jugProgressBar.setMax(jugSize);

            //Layout of jug text
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.ABOVE, jugProgressBar.getId());
            jugText.setLayoutParams(textParams);
            jugText.setId(i);
            jugText.setText("Jug" + jug_list.get(i));

            //Layout of jug amount text
            RelativeLayout.LayoutParams jugAmountParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            jugAmountParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            jugAmount.setLayoutParams(jugAmountParams);
            jugAmount.setText("O L");

            jugProgressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    int milkVol;

                    jugIdClicked = jug_list.get(position);

                    if(TextUtils.isEmpty(milkVolume.getText().toString())){
                        milkVol = 0;
                    } else {
                        milkVol = Integer.valueOf(milkVolume.getText().toString());
                    }

                    if((milkVol < jugSize - jugProgressBar.getProgress()) && milkVol < jugSize){
                        jugProgressBar.setProgress(milkVol);
                        jugAmount.setText(Integer.toString(milkVol) + "L");
                    } else {
                        Toast.makeText(getContext(),"Please select a different jug", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(),"Jug " + jugIdClicked, Toast.LENGTH_SHORT).show();

                }
            });

            jugHolderView.addView(jugProgressBar);
            jugHolderView.addView(jugText);
            jugHolderView.addView(jugAmount);

            mCarouselContainer.addView(jugHolderView);
        }

        AddData();

        return view;
    }

    public void AddData() {
        SaveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String farmerId = getArguments().getString("farmerid");
                        String transporterId = getArguments().getString("transporterId");
                        String smellIndex = String.valueOf(smellTest.indexOfChild(getView().findViewById(smellTest.getCheckedRadioButtonId())));
                        String densityIndex = String.valueOf(densityTest.indexOfChild(getView().findViewById(densityTest.getCheckedRadioButtonId())));
                        String alcoholIndex = String.valueOf(alcoholTest.indexOfChild(getView().findViewById(alcoholTest.getCheckedRadioButtonId())));
                        String jugId = jugIdClicked;
                        String comments = txtComments.getText().toString();
                        String milkweight = milkVolume.getText().toString();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String todayDate= dateFormat.format(date);

                        dateFormat = new SimpleDateFormat("HH:mm:ss");
                        String todayTime = dateFormat.format(date);

                        // Make Milk volume required field
                        if(TextUtils.isEmpty(milkweight)){
                            Toast.makeText(getContext(),"Milk Volume required", Toast.LENGTH_LONG).show();
                        } else if (Integer.valueOf(milkweight) > 10){
                            //Placeholder for now just for testing
                            Toast.makeText(getContext(),"Milk Volume must be smaller than 10 L", Toast.LENGTH_LONG).show();
                        } else {
                            List<String> columns = new ArrayList<>();
                            columns.addAll(Arrays.asList(DatabaseConstants.coltrFarmerTransporter));
                            columns.remove(DatabaseConstants.coltrFarmerTransporter.length - 1);
                            columns.remove(0);

                            List<String> values = Arrays.asList(transporterId, farmerId, jugId, todayDate, todayTime, milkweight, alcoholIndex, smellIndex, comments, densityIndex);

                            dbUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, columns, values, v.getContext());
                        }

                        Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_LONG).show();

                    }
                }
        );

    }

    public boolean areDataFieldsEmpty(){
        if ((smellTest.getCheckedRadioButtonId() == R.id.rb_unchecked) && (densityTest.getCheckedRadioButtonId() == R.id.density_rb_unchecked) && (alcoholTest.getCheckedRadioButtonId() == R.id.alcohol_rb_unchecked)
                && (txtComments.getText().toString().isEmpty()) && (milkVolume.getText().toString().isEmpty())) {
            return true;
        }
        return false;
    }

    public void clearData() {
         smellTest.check(R.id.rb_unchecked);
         densityTest.check(R.id.density_rb_unchecked);
         alcoholTest.check(R.id.alcohol_rb_unchecked);
         milkVolume.setText("");
    }


}
