package com.example.predictcgpa;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CGPA_Calculation_Fragment extends Fragment {
    private LinearLayout parentLinearLayout;
    List<AppCompatEditText> list = new ArrayList<AppCompatEditText>();
    List<AppCompatEditText> list2 = new ArrayList<>();
    AppCompatEditText editText,creditEditText;
    EditText totalCGPA,totalCredit;
    TextView currentCGPA;
    Button addField,calculationButton;

    LinearLayout resultLayout;


    public CGPA_Calculation_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);


        parentLinearLayout = view.findViewById(R.id.parent_linear_layout);

        totalCGPA = view.findViewById(R.id.totalCGPAId);
        totalCredit = view.findViewById(R.id.totalCreditId);
        currentCGPA = view.findViewById(R.id.currentCGPAId);
        addField = view.findViewById(R.id.add_field_button);
        calculationButton = view.findViewById(R.id.calculateButtonId);

        resultLayout = view.findViewById(R.id.resultLayoutId);

        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field, null);

                editText = rowView.findViewById(R.id.cgpa_editTextId);
                creditEditText = rowView.findViewById(R.id.credit__editTextId);
                list.add(editText);
                list2.add(creditEditText);

                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }
        });

        calculationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultLayout.setVisibility(View.VISIBLE);
                if (list.isEmpty() && list2.isEmpty()) {
                    Toast.makeText(getContext(), "Add your desire fields.", Toast.LENGTH_SHORT).show();
                } else {

                    double values = 0;
                    double total_credit = 0;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getEditableText().toString().equals("")) {
                            Toast.makeText(getContext(), "Give your GPA and Credit", Toast.LENGTH_SHORT).show();
                        } else {
                            double value = Double.parseDouble(list.get(i).getEditableText().toString());
                            double credit = Double.parseDouble(list2.get(i).getEditableText().toString());
                            total_credit += credit;

                            values += value * credit;

                            totalCGPA.setText(values + "");
                            totalCredit.setText(total_credit + "");
                            currentCGPA.setText(String.format("%.2f",values / total_credit));
                            Toast.makeText(getContext(), "Final = " + values / total_credit, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });


        return view;
    }

//    public void onAddField(View v) {
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View rowView = inflater.inflate(R.layout.field, null);
//
//        editText = rowView.findViewById(R.id.cgpa_editTextId);
//        creditEditText = rowView.findViewById(R.id.credit__editTextId);
//        list.add(editText);
//        list2.add(creditEditText);
//
//        // Add the new row before the add field button.
//        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
//    }

    public void onDelete(View v) {
//        parentLinearLayout.removeView((View) v.getParent());
//        for (int i=0;i<list.size();i++){
//            Toast.makeText(this, "Position "+list.get(i), Toast.LENGTH_SHORT).show();
//            list.get(i);
//        }



    }

//    public void onCalculate(View view) {
//
//        if (list.isEmpty() && list2.isEmpty()) {
//            Toast.makeText(getContext(), "Add your desire fields.", Toast.LENGTH_SHORT).show();
//        } else {
//
//            double values = 0;
//            double total_credit = 0;
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getEditableText().toString().equals("")) {
//                    Toast.makeText(getContext(), "Give your GPA and Credit", Toast.LENGTH_SHORT).show();
//                } else {
//                    double value = Double.parseDouble(list.get(i).getEditableText().toString());
//                    double credit = Double.parseDouble(list2.get(i).getEditableText().toString());
//                    total_credit += credit;
//
//                    values += value * credit;
//
//                    totalCGPA.setText(values + "");
//                    totalCredit.setText(total_credit + "");
//                    currentCGPA.setText(values / total_credit + "");
//                    Toast.makeText(getContext(), "Final = " + values / total_credit, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
//    }
}
