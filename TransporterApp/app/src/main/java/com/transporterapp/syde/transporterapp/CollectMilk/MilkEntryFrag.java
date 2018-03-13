package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transporterapp.syde.transporterapp.DataStructures.Jug;
import com.transporterapp.syde.transporterapp.Main;
import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.commonUtil;
import com.transporterapp.syde.transporterapp.databases.DatabaseConstants;
import com.transporterapp.syde.transporterapp.databases.dbUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MilkEntryFrag extends Fragment {

    private EditText milkVolume;
    private Button SaveData;
    private RadioGroup smellTest;
    private RadioGroup densityTest;
    private RadioGroup alcoholTest;
    private EditText txtComments;
    private String mFarmerName;
    private LinearLayout mCarouselContainer;
    private HorizontalScrollView mScrollView;
    private LinearLayout jugPagination;
    private RelativeLayout jugHolderView;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private int maxScrollX;
    private List<Jug> jug_list = new ArrayList<Jug>();

    private static final String FARMER_NAME = "farmername";

    //Number of Jugs - may need to change this number later or add function to add jugs
    private final static int INITIAL_JUG_COUNT=5;
    private String jugIdClicked;
    private boolean jugAlreadyClicked = false;
    private String prevJugSelected = "";
    private String currentJugSelection = "";
    private boolean dataSaved = false;

    private int jugSize = 20; //Placeholder

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

        smellTest = (RadioGroup) view.findViewById(R.id.smell_test);
        densityTest = (RadioGroup) view.findViewById(R.id.density_test);
        alcoholTest = (RadioGroup) view.findViewById(R.id.alcohol_test);
        milkVolume = (EditText) view.findViewById(R.id.milk_volume);
        SaveData = (Button)view.findViewById(R.id.milk_record_save_data);
        txtComments = (EditText) view.findViewById(R.id.comments);
        mCarouselContainer = (LinearLayout) view.findViewById(R.id.carousel);
        mScrollView = (HorizontalScrollView) view.findViewById(R.id.scrollView);
        leftArrow = (ImageView) view.findViewById(R.id.previous);
        rightArrow = (ImageView) view.findViewById(R.id.next);
        jugPagination =(LinearLayout) view.findViewById(R.id.viewPagerCountDots);


        Cursor dbResponse = dbUtil.selectStatement("jug","transporter_id", "=", getArguments().getString("transporterId"), context);
        jug_list = commonUtil.convertCursorToJugList(dbResponse);

        leftArrow.setVisibility(View.INVISIBLE);

        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                maxScrollX = mScrollView.getChildAt(0)
                        .getMeasuredWidth()-mScrollView.getMeasuredWidth();
            }
        });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                double scrollPosition = mScrollView.getScrollX();
                setArrowVisibility(scrollPosition);
                setJugPagination(scrollPosition, true);

                return false;
            }
        });

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
            jugProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            jugText = new TextView(getContext());
            jugAmount = new TextView(getContext());
            jugHolderView = new RelativeLayout(getContext());
            Drawable jugDrawable = getResources().getDrawable(R.drawable.enabled_progressbar_states);

            //Setup jug holder view
            RelativeLayout.LayoutParams holderParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            jugHolderView.setLayoutParams(holderParams);
            jugHolderView.setPadding(10, 0, 10, 4);

            // Create new layout parameters for progress bar
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, imageWidth);
            // Apply the layout parameters for progress bar
            jugProgressBar.setLayoutParams(lp);
            jugProgressBar.setProgress(Math.round(Float.parseFloat(jug_list.get(i).getCurrentVolume())));
            jugProgressBar.setTag(i);
            jugProgressBar.setProgressDrawable(jugDrawable);

            jugProgressBar.setMax(Integer.valueOf(jug_list.get(i).getSize()));

            //Layout of jug text
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.BELOW, jugProgressBar.getId());
            jugText.setLayoutParams(textParams);
            jugText.setText("Jug " + jug_list.get(i).getId());

            //Layout of jug amount text
            RelativeLayout.LayoutParams jugAmountParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            jugAmountParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            jugAmount.setLayoutParams(jugAmountParams);
            jugAmount.setText(jug_list.get(i).getCurrentVolume() + "L");

            jugProgressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (milkVolume.getText().toString().isEmpty()) {
                        milkVolume.setError("Milk volume is required");
                        Toast.makeText(getContext(),"Please enter a valid milk volume", Toast.LENGTH_LONG).show();
                    } else if (commonUtil.isNumeric(milkVolume.getText().toString()) == false) {
                        milkVolume.setError("Milk volume is required");
                        Toast.makeText(getContext(),"Please enter a valid milk volume", Toast.LENGTH_LONG).show();
                    } else {
                        int position = (Integer) v.getTag();
                        float milkVol;

                        jugIdClicked = jug_list.get(position).getId();
                        currentJugSelection = jugIdClicked;
                        if(TextUtils.isEmpty(milkVolume.getText().toString())){
                            milkVol = 0;
                        } else {

                            milkVol = Float.parseFloat(milkVolume.getText().toString());
                        }

                        if (jugAlreadyClicked) {
                            String otherJugId = jug_list.get(Integer.valueOf(prevJugSelected)).getId();
                            List<String> temp = dbUtil.selectStatement("jug", "currentVolume", "id", "=", otherJugId, v.getContext());
                            String originalMilkVol = temp.get(0);

                            RelativeLayout otherJug = (RelativeLayout) mCarouselContainer.getChildAt(Integer.valueOf(prevJugSelected));
                            TextView prevJugAmount = (TextView) otherJug.getChildAt(2);
                            ProgressBar prevProgressBar = (ProgressBar) otherJug.getChildAt(0);

                            int progress = (int) Math.round(Float.parseFloat(originalMilkVol));
                            prevProgressBar.setProgress(progress);

                            prevJugAmount.setText(originalMilkVol + "L");
                        }

                        if((milkVol < jugSize - jugProgressBar.getProgress()) && milkVol < jugSize){
                            String originalJugAmount = jugAmount.getText().toString().substring(0, jugAmount.getText().toString().length() - 1);
                            Double temp = Double.valueOf(originalJugAmount);
                            milkVol += temp;
                            int progress = Math.round(milkVol);
                            jugProgressBar.setProgress(progress);
                            jugAmount.setText(String.valueOf(milkVol) + "L");
                        } else {
                            Toast.makeText(getContext(),"Please select a different jug", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(),"Jug " + jugIdClicked, Toast.LENGTH_SHORT).show();

                        if (jugAlreadyClicked == false) {
                            jugAlreadyClicked = true;
                        }
                        prevJugSelected = String.valueOf(position);
                    }

                }
            });

            jugHolderView.addView(jugProgressBar);
            jugHolderView.addView(jugText);
            jugHolderView.addView(jugAmount);

            mCarouselContainer.addView(jugHolderView);
        }

        drawPageSelectionIndicators(0, (int) Math.ceil(jug_list.size() / 3.0), true);

        //adding listener for quality fields
        densityTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.density_bad) {
                    milkVolume.setEnabled(false);
                    mCarouselContainer.setEnabled(false);
                    for (int i = 0; i < mCarouselContainer.getChildCount(); i++) {
                        RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(i);
                        ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
                        jugProgressBar.setEnabled(false);
                        Drawable jugDrawable = getResources().getDrawable(R.drawable.disabled_progressbar_states);
                        jugProgressBar.setProgressDrawable(jugDrawable);
                    }
                    double scrollPosition = mScrollView.getScrollX();
                    setJugPagination(scrollPosition, false);
                    mScrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_LONG).show();
                } else {
                    milkVolume.setEnabled(true);
                    mCarouselContainer.setEnabled(true);
                    for (int i = 0; i < mCarouselContainer.getChildCount(); i++) {
                        RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(i);
                        ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
                        jugProgressBar.setEnabled(true);
                        Drawable jugDrawable = getResources().getDrawable(R.drawable.enabled_progressbar_states);
                        jugProgressBar.setProgressDrawable(jugDrawable);
                    }
                    double scrollPosition = mScrollView.getScrollX();
                    setJugPagination(scrollPosition, true);

                    mScrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            double scrollPosition = mScrollView.getScrollX();
                            setArrowVisibility(scrollPosition);
                            setJugPagination(scrollPosition, true);

                            return false;
                        }
                    });
                }
            }
        });

        smellTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.smell_bad) {
                    milkVolume.setEnabled(false);
                    mCarouselContainer.setEnabled(false);
                    for (int i = 0; i < mCarouselContainer.getChildCount(); i++) {
                        RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(i);
                        ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
                        jugProgressBar.setEnabled(false);
                        Drawable jugDrawable = getResources().getDrawable(R.drawable.disabled_progressbar_states);
                        jugProgressBar.setProgressDrawable(jugDrawable);
                    }
                    double scrollPosition = mScrollView.getScrollX();
                    setJugPagination(scrollPosition, false);
                    mScrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_LONG).show();
                } else {
                    milkVolume.setEnabled(true);
                    mCarouselContainer.setEnabled(true);
                    for (int i = 0; i < mCarouselContainer.getChildCount(); i++) {
                        RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(i);
                        ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
                        jugProgressBar.setEnabled(true);
                        Drawable jugDrawable = getResources().getDrawable(R.drawable.enabled_progressbar_states);
                        jugProgressBar.setProgressDrawable(jugDrawable);
                    }
                    double scrollPosition = mScrollView.getScrollX();
                    setJugPagination(scrollPosition, true);

                    mScrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            double scrollPosition = mScrollView.getScrollX();
                            setArrowVisibility(scrollPosition);
                            setJugPagination(scrollPosition, true);

                            return false;
                        }
                    });
                }
            }
        });

        alcoholTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.alcohol_bad) {
                    milkVolume.setEnabled(false);
                    mCarouselContainer.setEnabled(false);
                    for (int i = 0; i < mCarouselContainer.getChildCount(); i++) {
                        RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(i);
                        ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
                        jugProgressBar.setEnabled(false);
                        Drawable jugDrawable = getResources().getDrawable(R.drawable.disabled_progressbar_states);
                        jugProgressBar.setProgressDrawable(jugDrawable);
                    }
                    double scrollPosition = mScrollView.getScrollX();
                    setJugPagination(scrollPosition, false);
                    mScrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_LONG).show();
                } else {
                    milkVolume.setEnabled(true);
                    mCarouselContainer.setEnabled(true);
                    for (int i = 0; i < mCarouselContainer.getChildCount(); i++) {
                        RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(i);
                        ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
                        jugProgressBar.setEnabled(true);
                        Drawable jugDrawable = getResources().getDrawable(R.drawable.enabled_progressbar_states);
                        jugProgressBar.setProgressDrawable(jugDrawable);
                    }
                    double scrollPosition = mScrollView.getScrollX();
                    setJugPagination(scrollPosition, true);

                    mScrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            double scrollPosition = mScrollView.getScrollX();

                            setArrowVisibility(scrollPosition);

                            setJugPagination(scrollPosition, true);

                            return false;
                        }
                    });
                }
            }
        });
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
                            milkVolume.setError("Milk volume is required");
                            Toast.makeText(getContext(),"Please enter a valid milk volume", Toast.LENGTH_LONG).show();
                        } else {
                            if (jugAlreadyClicked == false) {
                                Toast.makeText(getContext(),"Please select a jug", Toast.LENGTH_LONG).show();
                            } else {
                                List<String> columns = new ArrayList<>();
                                columns.addAll(Arrays.asList(DatabaseConstants.coltrFarmerTransporter));
                                columns.remove(DatabaseConstants.coltrFarmerTransporter.length - 1);
                                columns.remove(0);

                                List<String> values = Arrays.asList(transporterId, farmerId, jugId, todayDate, todayTime, milkweight, alcoholIndex, smellIndex, comments, densityIndex);
                                saveData(columns, values, v.getContext());

                                Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            }
                        }

                    }
                }
        );

    }

    public boolean areDataFieldsEmpty(){
        if (dataSaved == true) {
            return true;
        }
        if ((smellTest.getCheckedRadioButtonId() == R.id.smell_unchecked) && (densityTest.getCheckedRadioButtonId() == R.id.density_unchecked) && (alcoholTest.getCheckedRadioButtonId() == R.id.alcohol_unchecked)
                && (txtComments.getText().toString().isEmpty()) && (milkVolume.getText().toString().isEmpty())) {
            return true;
        }
        return false;
    }

    public void saveData(List<String> columns, List<String> values, Context context) {
        dbUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, columns, values, context);
        Cursor cursor = dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.id, "=", currentJugSelection, context);
        Jug jug = commonUtil.convertCursorToJug(cursor);
        double finalVolume = Double.valueOf(jug.getCurrentVolume()) + Double.valueOf(values.get(5));
        dbUtil.updateStatement(DatabaseConstants.tblJug, DatabaseConstants.currentVolume, String.valueOf(finalVolume), DatabaseConstants.id, "=", currentJugSelection, context);
        dataSaved = true;
    }

    public void clearData() {
         smellTest.check(R.id.smell_unchecked);
         densityTest.check(R.id.density_unchecked);
         alcoholTest.check(R.id.alcohol_unchecked);
         milkVolume.setText("");
         txtComments.setText("");
         jugAlreadyClicked = false;
         prevJugSelected = "";
         dataSaved = false;
    }

    private void drawPageSelectionIndicators(int mPosition, int dotsCount, boolean enabled){
        if(jugPagination !=null) {
            jugPagination.removeAllViews();
        }
         ImageView[] dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getContext());
            if(i==mPosition)
                if (enabled) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.enabled_selected_item));
                } else {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.disabled_selected_item));
                }

            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 4, 4, 4);
            jugPagination.addView(dots[i], params);
        }
    }

    public void setArrowVisibility(double scrollPosition){
        if (scrollPosition == 0){
            leftArrow.setVisibility(View.INVISIBLE);
        } else {
            leftArrow.setVisibility(View.VISIBLE);
        }
        if (scrollPosition == maxScrollX){
            rightArrow.setVisibility(View.INVISIBLE);
        } else {
            rightArrow.setVisibility(View.VISIBLE);
        }
    }

    public void setJugPagination(double scrollPosition, boolean enabled){
        int numOfDots = (int) Math.ceil(jug_list.size() / 3.0);
        double scrollSeparator = maxScrollX / numOfDots;

        int position = 0;
        double temp = scrollSeparator;
        while (scrollPosition > temp) {
            temp += scrollSeparator;
            position++;
        }

        if (scrollPosition == maxScrollX) {
            position = numOfDots - 1;
        }


        drawPageSelectionIndicators(position, numOfDots, enabled);
    }
}
