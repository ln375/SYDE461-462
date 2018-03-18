package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import android.widget.RadioButton;
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
import java.text.DecimalFormat;
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
    private String mRouteId;
    private String mWeight;
    private String mAlcohol;
    private String mSmell;
    private String mDensity;
    private String mComments;
    private String mTrFarmerTransporterId;
    private String mJugId;
    private String mDate;
    private String mTime;
    public boolean mPrevRecord = false;
    private LinearLayout mCarouselContainer;
    private HorizontalScrollView mScrollView;
    private LinearLayout jugPagination;
    private RelativeLayout jugHolderView;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private int maxScrollX;
    private List<Jug> jug_list = new ArrayList<Jug>();

    private static final String FARMER_NAME = "farmername";
    private static final String ROUTE_ID = "routeId";
    private static final String MILK_WEIGHT = "milkweight";
    private static final String PREV_RECORD = "prevRecord";
    private static final String ALCOHOL = "alcohol";
    private static final String SMELL = "smell";
    private static final String DENSITY = "density";
    private static final String COMMENTS = "comments";
    private static final String JUG_ID = "jugid";
    private static final String TBL_FARMER_TRANSPORTER = "trfarmertransporter";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String TRANSACTION_ID = "transactionid";

    //Number of Jugs - may need to change this number later or add function to add jugs
    private final static int INITIAL_JUG_COUNT=5;
    private String jugIdClicked = "";
    private boolean jugAlreadyClicked = false;
    private String prevJugSelected = "";
    private String currentJugSelection = "";
    private int currentJugPosition = 0;
    private boolean clearedSavedJug = false;
    private boolean dataSaved = false;
    private boolean poorQuality = false;




    private int jugSize = 20; //Placeholder

    public MilkEntryFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mFarmerName = getArguments().getString(FARMER_NAME);
            mRouteId = getArguments().getString(ROUTE_ID);
            mWeight = getArguments().getString(MILK_WEIGHT);
            mAlcohol = getArguments().getString(ALCOHOL);
            mSmell = getArguments().getString(SMELL);
            mDensity = getArguments().getString(DENSITY);
            mComments = getArguments().getString(COMMENTS);
            mPrevRecord = getArguments().getBoolean(PREV_RECORD);
            mTrFarmerTransporterId = getArguments().getString(TBL_FARMER_TRANSPORTER);
            mJugId = getArguments().getString(JUG_ID);
            mDate = getArguments().getString(DATE);
            mTime = getArguments().getString(TIME);

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
            textParams.addRule(RelativeLayout.ALIGN_PARENT_START);
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
                    // First check to make sure there is a milk volume
                    if (milkVolume.getText().toString().isEmpty()) {
                        milkVolume.setError("Milk volume is required");
                        Toast.makeText(getContext(),"Please enter a milk volume", Toast.LENGTH_LONG).show();
                    } else if (commonUtil.isNumeric(milkVolume.getText().toString()) == false) {
                        milkVolume.setError("Invalid milk volume error:\nMilk volume must be a valid number (e.g. \"3.5\", \"3\")");
                    } else {
                        int position = (Integer) v.getTag();
                        float milkVol;

                        String oldPos = "";
                        if (mPrevRecord) {
                            for (int i = 0; i < jug_list.size(); i++){
                                if (jug_list.get(i).getId().equalsIgnoreCase(mJugId)) {
                                    oldPos = String.valueOf(i);
                                    break;
                                }
                            }
                        }

                        jugIdClicked = jug_list.get(position).getId();
                        currentJugSelection = jugIdClicked;
                        currentJugPosition = position;

                        milkVol = Float.parseFloat(milkVolume.getText().toString());

                        // change progress for saved jug
                        if (mPrevRecord) {
                            if (!clearedSavedJug) {
                                if (!currentJugSelection.equalsIgnoreCase(mJugId)) {
                                    // need to change progress bar of previous saved jug
                                    if (!oldPos.isEmpty()) {
                                        //Remove milk from originally selected jug
                                        RelativeLayout otherJug = (RelativeLayout) mCarouselContainer.getChildAt(Integer.valueOf(oldPos));
                                        TextView prevJugAmount = (TextView) otherJug.getChildAt(2);
                                        ProgressBar prevProgressBar = (ProgressBar) otherJug.getChildAt(0);

                                        Jug originalJug = jug_list.get(Integer.valueOf(oldPos));
                                        Float temp = Float.parseFloat(originalJug.getCurrentVolume()) - Float.parseFloat(mWeight);
                                        int progress = (int) Math.round(temp);
                                        prevProgressBar.setProgress(progress);
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        prevJugAmount.setText(String.valueOf(df.format(temp)) + " L");
                                    }
                                    clearedSavedJug = true;
                                }
                            }
                        }

                        if (jugAlreadyClicked) {
                            String otherJugId = jug_list.get(Integer.valueOf(prevJugSelected)).getId();
                            List<String> temp = dbUtil.selectStatement("jug", "currentVolume", "id", "=", otherJugId, v.getContext());
                            String originalMilkVol = temp.get(0);

                            RelativeLayout otherJug = (RelativeLayout) mCarouselContainer.getChildAt(Integer.valueOf(prevJugSelected));
                            TextView prevJugAmount = (TextView) otherJug.getChildAt(2);
                            ProgressBar prevProgressBar = (ProgressBar) otherJug.getChildAt(0);

                            if (!otherJugId.equalsIgnoreCase(mJugId)) {
                                int progress = (int) Math.round(Float.parseFloat(originalMilkVol));
                                prevProgressBar.setProgress(progress);

                                prevJugAmount.setText(originalMilkVol + "L");
                            } else {
                                Float newVol = Float.parseFloat(originalMilkVol) - Float.parseFloat(mWeight);
                                int progress = (int) Math.round(newVol);
                                prevProgressBar.setProgress(progress);
                                DecimalFormat df = new DecimalFormat("#.##");
                                prevJugAmount.setText(String.valueOf(df.format(newVol)) + "L");
                            }
                        }

                        if((milkVol < jugSize - jugProgressBar.getProgress()) && milkVol < jugSize){
                            if (clearedSavedJug == true) {
                                String originalJugAmount = jugAmount.getText().toString().substring(0, jugAmount.getText().toString().length() - 1);
                                Double temp = Double.valueOf(originalJugAmount);
                                milkVol += temp;
                                int progress = Math.round(milkVol);
                                jugProgressBar.setProgress(progress);
                                DecimalFormat df = new DecimalFormat("#.#");
                                jugAmount.setText(String.valueOf(df.format(milkVol)) + " L");
                            } else {
                                if (!currentJugSelection.equalsIgnoreCase(mJugId)) {
                                    String originalJugAmount = jugAmount.getText().toString().substring(0, jugAmount.getText().toString().length() - 1);
                                    Double temp = Double.valueOf(originalJugAmount);
                                    milkVol += temp;
                                    int progress = Math.round(milkVol);
                                    jugProgressBar.setProgress(progress);
                                    DecimalFormat df = new DecimalFormat("#.#");
                                    jugAmount.setText(String.valueOf(df.format(milkVol)) + " L");
                                } else {
                                    // if milk vol is different
                                    if (!oldPos.isEmpty()) {
                                        Jug originalJug = jug_list.get(Integer.valueOf(oldPos));
                                        Float trueOriginalAmount = Float.parseFloat(originalJug.getCurrentVolume()) - Float.parseFloat(mWeight);

                                        Double temp = Double.valueOf(trueOriginalAmount);
                                        milkVol += temp;
                                        int progress = Math.round(milkVol);
                                        jugProgressBar.setProgress(progress);
                                        DecimalFormat df = new DecimalFormat("#.#");
                                        jugAmount.setText(String.valueOf(df.format(milkVol)) + " L");
                                    }
                                }
                            }
                            //Toast.makeText(getContext(),"Jug " + jugIdClicked, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"Milk quantity does not fit inside jug.", Toast.LENGTH_SHORT).show();
                        }

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
                    disableFields();
                    if (!mPrevRecord) {
                        Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!dataSaved) {
                            Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //check to see if the other quality ratings are bad
                    if (smellTest.getCheckedRadioButtonId() != R.id.smell_bad && alcoholTest.getCheckedRadioButtonId()!= R.id.alcohol_bad) {
                        enableFields();
                    }
                }
            }
        });

        smellTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.smell_bad) {
                   disableFields();
                    if (!mPrevRecord) {
                        Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!dataSaved) {
                            Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //check to see if the other quality ratings are bad
                    if (densityTest.getCheckedRadioButtonId() != R.id.density_bad && alcoholTest.getCheckedRadioButtonId()!= R.id.alcohol_bad) {
                        enableFields();
                    }
                }
            }
        });

        alcoholTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.alcohol_bad) {
                    disableFields();
                    if (!mPrevRecord) {
                        Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!dataSaved) {
                            Toast.makeText(getContext(),"Milk quantity and jug selection disabled due to poor milk quality", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //check to see if the other quality ratings are bad
                    if (smellTest.getCheckedRadioButtonId() != R.id.smell_bad && densityTest.getCheckedRadioButtonId()!= R.id.density_bad) {
                        enableFields();
                    }
                }
            }
        });

        initFields(mPrevRecord);

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
                        String smellRating = convertQualityRating(smellTest);
                        int radioButtonID = densityTest.getCheckedRadioButtonId();
                        View radioButton = densityTest.findViewById(radioButtonID);
                        int idx = densityTest.indexOfChild(radioButton);
                        RadioButton r = (RadioButton)  densityTest.getChildAt(idx);
                        String densityRating = r.getText().toString();
                        String status = DatabaseConstants.status_pending;

                        String alcoholRating = convertQualityRating(alcoholTest);
                        String jugId = jugIdClicked;
                        String comments = txtComments.getText().toString();
                        String milkweight = milkVolume.getText().toString();
                        String todayDate = "";
                        String todayTime = "";

                        if (mPrevRecord) {
                            todayDate = mDate;
                            todayTime = mTime;
                        } else {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            todayDate = dateFormat.format(date);

                            dateFormat = new SimpleDateFormat("HH:mm:ss");
                            todayTime = dateFormat.format(date);
                        }

                        // Make Milk volume required field
                        if(TextUtils.isEmpty(milkweight) && poorQuality == false){
                            milkVolume.setError("Milk volume is required");
                            Toast.makeText(getContext(),"Please enter a valid milk volume", Toast.LENGTH_LONG).show();
                        } else {

                            if (jugAlreadyClicked == false && poorQuality == false && mPrevRecord == false) {
                                Toast.makeText(getContext(),"Please select a jug", Toast.LENGTH_LONG).show();
                            } else {
                                List<String> columns = new ArrayList<>();
                                columns.addAll(Arrays.asList(DatabaseConstants.coltrFarmerTransporter));
                                columns.remove(Arrays.asList(DatabaseConstants.coltrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
                                columns.remove(0);

                                List<String> values = Arrays.asList(transporterId, farmerId, jugId, todayDate, todayTime, milkweight, alcoholRating, smellRating, comments, densityRating, mRouteId, status);
                                if (mPrevRecord) {
                                    updateData(columns, values, v.getContext());
                                } else {
                                    saveData(columns, values, v.getContext());
                                }


                                Toast.makeText(getContext(),"Data Inserted", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();

                            }
                        }

                    }
                }
        );

    }

    public boolean areDataFieldsEmpty(){
        if (!dataSaved) {
            if (mPrevRecord) {
                // Check to make sure values are still the same
                String smellRating = convertQualityRating(smellTest);
                int radioButtonID = densityTest.getCheckedRadioButtonId();
                View radioButton = densityTest.findViewById(radioButtonID);
                int idx = densityTest.indexOfChild(radioButton);
                RadioButton r = (RadioButton) densityTest.getChildAt(idx);
                String densityRating = r.getText().toString();

                String alcoholRating = convertQualityRating(alcoholTest);
                String jugId = jugIdClicked;
                String comments = txtComments.getText().toString();
                String milkweight = milkVolume.getText().toString();

                if (smellRating.equalsIgnoreCase(mSmell)) {
                    if (densityRating.equalsIgnoreCase(mDensity)) {
                        if (alcoholRating.equalsIgnoreCase(mAlcohol)) {
                            if (jugId.isEmpty() || jugId.equalsIgnoreCase(mJugId)) {
                                if (comments.equalsIgnoreCase(mComments)) {
                                    if (milkweight.equalsIgnoreCase(mWeight)) {
                                        return true;
                                    }
                                }
                            }

                        }
                    }
                }

                if (smellRating.equalsIgnoreCase(mSmell) && densityRating.equalsIgnoreCase(mDensity) && alcoholRating.equalsIgnoreCase(mAlcohol) && jugId.equalsIgnoreCase(mJugId)
                        && comments.equalsIgnoreCase(mComments) && milkweight.equalsIgnoreCase(mWeight)) {
                    return true;
                } else {
                    return false;
                }
            }

            if ((smellTest.getCheckedRadioButtonId() == R.id.smell_unchecked) && (densityTest.getCheckedRadioButtonId() == R.id.density_unchecked) && (alcoholTest.getCheckedRadioButtonId() == R.id.alcohol_unchecked)
                    && (txtComments.getText().toString().isEmpty()) && (milkVolume.getText().toString().isEmpty())) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    public void saveData(List<String> columns, List<String> values, Context context) {
        dbUtil.insertStatement(DatabaseConstants.tbltrFarmerTransporter, columns, values, context);
        Cursor cursor = dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.id, "=", currentJugSelection, context);
        Jug jug = commonUtil.convertCursorToJug(cursor);
        if (jug != null) {
            double finalVolume = Double.valueOf(jug.getCurrentVolume()) + Double.valueOf(values.get(5));
            dbUtil.updateStatement(DatabaseConstants.tblJug, DatabaseConstants.currentVolume, String.valueOf(finalVolume), DatabaseConstants.id, "=", currentJugSelection, context);
        }

        // update history table
        List<String> col = new ArrayList<>();
        col.addAll(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter));

        Cursor mostRecentTransaction = dbUtil.getMostRecent(DatabaseConstants.tbltrFarmerTransporter, context);
        mTrFarmerTransporterId = commonUtil.getMostRecentId(mostRecentTransaction);

        List<String> vals = new ArrayList<>();
        vals.addAll(values);
        vals.add(mTrFarmerTransporterId);

        col.remove(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
        col.remove(0);

        dbUtil.insertStatement(DatabaseConstants.tblHisttrFarmerTransporter, col, vals, context);


        dataSaved = true;
    }

    public void updateData(List<String> columns, List<String> values, Context context) {
        dbUtil.updateStatement(DatabaseConstants.tbltrFarmerTransporter, columns, values, DatabaseConstants.id, "=", mTrFarmerTransporterId, context);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todayDate= dateFormat.format(date);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        String todayTime = dateFormat.format(date);

        List<String> col = new ArrayList<>();
        col.addAll(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter));

        List<String> vals = new ArrayList<>();
        vals.addAll(values);
        vals.set(3, todayDate);
        vals.set(4, todayTime);
        vals.add(mTrFarmerTransporterId);

        col.remove(Arrays.asList(DatabaseConstants.colHistTrFarmerTransporter).indexOf(DatabaseConstants.tr_transporter_cooling_id));
        col.remove(0);

        dbUtil.insertStatement(DatabaseConstants.tblHisttrFarmerTransporter, col, vals, context);


        // update jugs table
        if (values.get(2) != mJugId) {
            // remove quantity from old jug
            Cursor cursor = dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.id, "=", mJugId, context);
            Jug jug = commonUtil.convertCursorToJug(cursor);
            if (jug != null) {
                double finalVolume = Double.valueOf(jug.getCurrentVolume()) - Double.valueOf(mWeight);
                dbUtil.updateStatement(DatabaseConstants.tblJug, DatabaseConstants.currentVolume, String.valueOf(finalVolume), DatabaseConstants.id, "=", mJugId, context);
            }

            // add quantity to new jug
            cursor = dbUtil.selectStatement(DatabaseConstants.tblJug, DatabaseConstants.id, "=", currentJugSelection, context);
            jug = commonUtil.convertCursorToJug(cursor);
            if (jug != null) {
                double finalVolume = Double.valueOf(jug.getCurrentVolume()) + Double.valueOf(values.get(5));
                dbUtil.updateStatement(DatabaseConstants.tblJug, DatabaseConstants.currentVolume, String.valueOf(finalVolume), DatabaseConstants.id, "=", currentJugSelection, context);
            }
        }
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
         milkVolume.setError(null);
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

            params.setMargins(4, 13, 4, 4);
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

    public void disableFields(){
        // do not have jug or milk info
        if (!currentJugSelection.isEmpty()) {
            RelativeLayout jug = (RelativeLayout) mCarouselContainer.getChildAt(Integer.valueOf(currentJugPosition));
            TextView jugAmount = (TextView) jug.getChildAt(2);
            ProgressBar jugProgressBar = (ProgressBar) jug.getChildAt(0);
            String originalJugAmount = jugAmount.getText().toString().substring(0, jugAmount.getText().toString().length() - 1);
            Float temp = Float.valueOf(originalJugAmount);
            temp -= Float.valueOf(milkVolume.getText().toString());
            int progress = Math.round(temp);
            jugProgressBar.setProgress(progress);
            DecimalFormat df = new DecimalFormat("#.#");
            jugAmount.setText(String.valueOf(df.format(temp)) + " L");
        }
        milkVolume.setText("");

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
        poorQuality = true;
    }

    public void enableFields(){
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
        poorQuality = false;
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

    public String convertQualityRating(RadioGroup radioGroup){
        String rating = "1";
        List<String> qualityRating = new ArrayList<>();
        if (radioGroup.getCheckedRadioButtonId() == R.id.density_bad || radioGroup.getCheckedRadioButtonId() == R.id.alcohol_bad || radioGroup.getCheckedRadioButtonId() == R.id.smell_bad) {
            qualityRating = dbUtil.selectStatement("qualityratings", "id", "rating", "=", "Bad", getContext());
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.density_good || radioGroup.getCheckedRadioButtonId() == R.id.alcohol_good || radioGroup.getCheckedRadioButtonId() == R.id.smell_good) {
            qualityRating = dbUtil.selectStatement("qualityratings", "id", "rating", "=", "Good", getContext());
        } /*else if (radioGroup.getCheckedRadioButtonId() == R.id.density_28  || radioGroup.getCheckedRadioButtonId() == R.id.density_29 || radioGroup.getCheckedRadioButtonId() == R.id.density_30 || radioGroup.getCheckedRadioButtonId() == R.id.alcohol_okay) {
            qualityRating = dbUtil.selectStatement("qualityratings", "id", "rating", "=", "Okay", getContext());
        }*/
        if (qualityRating.size() > 0) {
            rating = qualityRating.get(0);
        }
        return rating;
    }

    public void initFields(boolean prevRecord) {
        if (prevRecord == true) {
            milkVolume.setText(mWeight);
            txtComments.setText(mComments);

            switch (mSmell) {
                case "1":

                    smellTest.check(R.id.smell_unchecked);
                    break;
                case "2":
                    smellTest.check(R.id.smell_bad);
                    break;
                case "4":
                    smellTest.check(R.id.smell_good);
                    break;
                default:
                    smellTest.check(R.id.smell_unchecked);
                    break;
            }

            switch (mAlcohol) {
                case "1":
                    alcoholTest.check(R.id.alcohol_unchecked);
                    break;
                case "2":
                    alcoholTest.check(R.id.alcohol_bad);
                    break;
                case "3":
                    alcoholTest.check(R.id.alcohol_okay);
                    break;
                case "4":
                    alcoholTest.check(R.id.alcohol_good);
                    break;
                default:
                    alcoholTest.check(R.id.smell_unchecked);
                    break;
            }

            switch (mDensity) {
                case "N/A":
                    densityTest.check(R.id.density_unchecked);
                    break;
                case "27":
                    densityTest.check(R.id.density_bad);
                    break;
                case "28":
                    densityTest.check(R.id.density_28);
                    break;
                case "29":
                    densityTest.check(R.id.density_29);
                    break;
                case "30":
                    densityTest.check(R.id.density_30);
                    break;
                case "31+":
                    densityTest.check(R.id.density_good);
                    break;
                default:
                    densityTest.check(R.id.density_unchecked);
                    break;
            }

            if (mSmell.equalsIgnoreCase("2") || mAlcohol.equalsIgnoreCase("2") || mDensity.equalsIgnoreCase("27")) {
                disableFields();
            }

        } else {
            clearData();
        }
    }
}
