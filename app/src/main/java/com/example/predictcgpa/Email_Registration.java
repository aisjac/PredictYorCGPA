package com.example.predictcgpa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Email_Registration extends AppCompatActivity {
    EditText user_email,user_pass,confPassET;
    Button singup_button;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_email__registration );
        user_email = findViewById(R.id.singup_email_id);
        user_pass = findViewById(R.id.signup_pass_id);
        confPassET = findViewById( R.id.conf_pass_id );
        singup_button  =findViewById(R.id.signup_button_id);
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "দয়া করে অপেক্ষা করুন" );

        mAuth = FirebaseAuth.getInstance();

        singup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = user_email.getText().toString().trim();
                String password = user_pass.getText().toString().trim();
                String confPassword = confPassET.getText().toString().trim();
//                final String mbnumber = "+88"+ mobileNumber.getText().toString();
//                Toast.makeText( Email_Registration.this, mbnumber, Toast.LENGTH_SHORT ).show();
//                mbnumber.length()>11
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    if (password.equals(confPassword)){

                        progressDialog.show();
                        mAuth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    user_email.setText( "" );
                                                    user_pass.setText( "" );
                                                    Intent intent = new Intent( getApplicationContext(),Email_Login.class );
//                                                intent.putExtra( "MobileNumber",mbnumber );
                                                    startActivity( intent );
                                                    finish();
                                                    Toast.makeText(getApplicationContext(), "Check your mail to varify you. ", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Sorry, your request is denied", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                       }else{
                        Toast.makeText(Email_Registration.this, "Password didn't match", Toast.LENGTH_SHORT).show();

                    }

                }else {
                    confPassET.setError( "Empty" );
                    user_email.setError( "Empty" );
                    user_pass.setError( "Empty" );
                    Toast.makeText(getApplicationContext(), "Give your correct mail and 6 digit password ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    public void buildActionCodeSettings() {
//        // [START auth_build_action_code_settings]
//        ActionCodeSettings actionCodeSettings =
//                ActionCodeSettings.newBuilder()
//                        // URL you want to redirect back to. The domain (www.example.com) for this
//                        // URL must be whitelisted in the Firebase Console.
//                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
//                        // This must be true
//                        .setHandleCodeInApp(true)
//                        .setIOSBundleId("com.example.ios")
//                        .setAndroidPackageName(
//                                "com.example.android",
//                                true, /* installIfNotAvailable */
//                                "12"    /* minimumVersion */)
//                        .build();
//        // [END auth_build_action_code_settings]
//    }

}