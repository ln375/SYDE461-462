package com.transporterapp.syde.transporterapp.CollectMilk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.transporterapp.syde.transporterapp.R;
import com.transporterapp.syde.transporterapp.databases.DataBaseUtil;

import java.util.List;

public class MilkEntryFrag extends Fragment {

    EditText milkVolume;
    Button SaveData;
    RadioGroup smellTest;
    RadioGroup densityTest;

    public MilkEntryFrag() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_milk_data_entry, container, false);

        Context context = view.getContext();

        TextView farmerName = (TextView) view.findViewById(R.id.milk_entry_farmer_name);
        farmerName.setText(getArguments().getString("farmername"));

        String id = getArguments().getString("farmerid");

        List<String> jug_list = DataBaseUtil.selectStatement("jug","id", "transporter_id", "=", "3", context);

        Spinner jugDropdown = (Spinner) view.findViewById(R.id.jug_spinner);

        ArrayAdapter<String> jugListAdapter = new ArrayAdapter<>(
                context, R.layout.jug_row_layout, R.id.jug_id, jug_list);

        jugDropdown.setAdapter(jugListAdapter);

        return view;


    }
}
