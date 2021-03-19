package com.example.predictcgpa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    List<AppCompatEditText> list = new ArrayList<AppCompatEditText>();
    List<AppCompatEditText> list2 = new ArrayList<>();
    AppCompatEditText editText,creditEditText;
    EditText totalCGPA,totalCredit;
    TextView currentCGPA;
    FirebaseAuth mAuth;
    LinearLayout resultViewLayout;


    double cgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        resultViewLayout = findViewById(R.id.resultLayoutId);

        totalCGPA = findViewById(R.id.totalCGPAId);
        totalCredit = findViewById(R.id.totalCreditId);
        currentCGPA = findViewById(R.id.currentCGPAId);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =  mAuth.getCurrentUser();
        if (user != null){
            sendMainMenu();
        }
    }
    private void sendMainMenu() {
        Intent intent = new Intent(MainActivity.this,DrawerActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginId:
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
                return true;

//            case R.id.registerId:
//                Intent intent2 = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent2);
//                return true;

            default:
                return false;
        }
    }


    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);

        editText = rowView.findViewById(R.id.cgpa_editTextId);
        creditEditText = rowView.findViewById(R.id.credit__editTextId);
        list.add(editText);
        list2.add(creditEditText);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
//        parentLinearLayout.removeView((View) v.getParent());
//        for (int i=0;i<list.size();i++){
//            Toast.makeText(this, "Position "+list.get(i), Toast.LENGTH_SHORT).show();
//            list.get(i);
//        }



    }

    public void onCalculate(View view) {

        resultViewLayout.setVisibility(View.VISIBLE);
        if (list.isEmpty() && list2.isEmpty()){
            Toast.makeText(this, "Add your desire fields.", Toast.LENGTH_SHORT).show();
        }else {

            double values = 0;
            double total_credit = 0;
            for (int i=0;i<list.size();i++){
                if (list.get(i).getEditableText().toString().equals("")){
                    Toast.makeText(this, "Give your GPA and Credit", Toast.LENGTH_SHORT).show();
                }else {
                    double value = Double.parseDouble(list.get(i).getEditableText().toString());
                    double credit = Double.parseDouble(list2.get(i).getEditableText().toString());
                    total_credit += credit;

                    values+=value*credit;

                    totalCGPA.setText(values+"");
                    totalCredit.setText(total_credit+"");
                    currentCGPA.setText(values/total_credit+"");
                    Toast.makeText(this, "Final = "+values/total_credit, Toast.LENGTH_SHORT).show();
                }
            }

//            double values2 = 0;
//            for (int j=0;j<list2.size();j++){
//                if (list2.get(j).getEditableText().toString().equals("")){
//                    Toast.makeText(this, "Give your input...", Toast.LENGTH_SHORT).show();
//                }else {
//                    double value2 = Double.parseDouble(list2.get(j).getEditableText().toString());
//                    values2+=value2;
//                    totalCredit.setText(values2+"");
//                }
//            }
////            cgpa = (values*values2)/values2;
//            currentCGPA.setText((values*values2)/values2 +"");
//
//            Toast.makeText(this, "Value: ", Toast.LENGTH_SHORT).show();
        }
    }




}