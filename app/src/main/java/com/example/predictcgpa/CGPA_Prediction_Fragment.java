package com.example.predictcgpa;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CGPA_Prediction_Fragment extends Fragment {

    EditText currentCGPAET,creditCompleteET,targetCGPAET,creditLeftET,statusTV;
    TextView noteTV ;
    Button predictButton;
    String currentUid;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    DatabaseReference firebaseDatabase;
    StorageReference userProfileImageRef;

    public CGPA_Prediction_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cgpa_predict_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        currentUid = firebaseAuth.getCurrentUser().getUid();

        currentCGPAET = view.findViewById(R.id.currentCGPAETId);
        creditCompleteET = view.findViewById(R.id.creditCompleteETId);
        targetCGPAET = view.findViewById(R.id.targetCGPAETId);
        creditLeftET = view.findViewById(R.id.creditLeftETId);

        statusTV = view.findViewById(R.id.statusTVId);

        predictButton = view.findViewById(R.id.predictButtonId);
        noteTV = view.findViewById(R.id.noteTVId);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curGPA = currentCGPAET.getText().toString();
                String crComplete = creditCompleteET.getText().toString();
                String tarCGPA = targetCGPAET.getText().toString();
                String crLeft = creditLeftET.getText().toString();

                if (!TextUtils.isEmpty( curGPA ) && !TextUtils.isEmpty( crComplete ) && !TextUtils.isEmpty( tarCGPA ) && !TextUtils.isEmpty( crLeft )){

                    double currentSGPA = Double.parseDouble(curGPA)*Double.parseDouble(crComplete);
                    double targetSGPA = Double.parseDouble(tarCGPA)*Double.parseDouble(crComplete);
                    double difference = targetSGPA-currentSGPA;
                    double remainingSGPA = Double.parseDouble(tarCGPA)*Double.parseDouble(crLeft);
                    double predict = (remainingSGPA+difference)/Double.parseDouble(crLeft);

                    if (predict>4.0){
                        statusTV.setText("To reach your goal,"+"\n"+" your GPA for next "+crLeft+" credit must be "+predict+" which is not possible to gain in 4 scale");
                        noteTV.setVisibility(View.VISIBLE);
                        uploadProfileStatus();
                    }else
                        statusTV.setText("To reach your goal,"+"\n"+" your GPA for next "+crLeft+" credit must be "+predict);
                    noteTV.setVisibility(View.VISIBLE);
                    noteTV.setText("This "+predict+" credit is considered for single credit.For example :"+"\n"+"1. if you take "+crLeft+" credit which is 1 crdit course then total SGPA will divided by "+crLeft+"\n"+"2. if you take "+crLeft+" credit which is 3 crdit course then sumation of all courses SGPA will be devided by sumation of total credit which varify this "+predict+" is for 1 credit");
                    uploadProfileStatus();
                }else
                    Toast.makeText(getContext(), "Please give all information", Toast.LENGTH_SHORT).show();

             }
        });


        return view;
    }

    private void uploadProfileStatus() {

        String curGPA = currentCGPAET.getText().toString();
        String crComplete = creditCompleteET.getText().toString();
        String crLeft = creditLeftET.getText().toString();

        ProfileStatusPosoClass pStatusPosoclass = new ProfileStatusPosoClass(curGPA, crComplete, crLeft);

        firebaseDatabase.child("User Status")
                .child(currentUid).setValue(pStatusPosoclass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Done !", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed !", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
