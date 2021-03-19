package com.example.predictcgpa;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }


    TextView changePic,curCGPATV,crCompleteTV,crLeftTV;
    CircleImageView circleImageView;
    EditText pNameET,pDemartmentET,pemailET,pPhoneET,pUniversityET,pFathernameET,pMothernameET,pAddressET;
    Button updateProfileButton,goUms;
    Uri uri;
    String imageUrl;
    static final int GALLERY_REQUEST_CODE = 1;
    ProgressDialog mDialog;

    String currentUid;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    DatabaseReference firebaseDatabase;
    StorageReference userProfileImageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

       changePic = view.findViewById(R.id.changeProfilePicId);
        circleImageView = view.findViewById(R.id.fProfileImageId);
        goUms = view.findViewById(R.id.goUmsButtonId);
        curCGPATV = view.findViewById(R.id.pCurrentCGPAId);
        crCompleteTV = view.findViewById(R.id.pCreditCompleteId);
        crLeftTV = view.findViewById(R.id.pCreditLeftId);
         pNameET= view.findViewById(R.id.pFullnameId);
         pDemartmentET= view.findViewById(R.id.pDepartmentId);
         pemailET= view.findViewById(R.id.profileemailETId);
         pPhoneET= view.findViewById(R.id.profilePhoneETId);
         pUniversityET= view.findViewById(R.id.profileUniETID);
         pFathernameET= view.findViewById(R.id.profileFatherETId);
         pMothernameET= view.findViewById(R.id.profileMotherETId);
         pAddressET= view.findViewById(R.id.profileAddressETId);

        updateProfileButton = view.findViewById(R.id.profileUpdateButtonId);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        currentUid = firebaseAuth.getCurrentUser().getUid();



        loadSettings();
        loadProfileStatus();


        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePic.setTextColor(Color.parseColor("#FFCD3030"));
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, 1);
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pNameET.getText().toString();
                String deparment = pDemartmentET.getText().toString();
                String email = pemailET.getText().toString();
                String phone = pPhoneET.getText().toString();
                String university = pUniversityET.getText().toString();
                String fname = pFathernameET.getText().toString();
                String mname = pMothernameET.getText().toString();
                String address = pAddressET.getText().toString();
                if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(deparment)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(phone)&& !TextUtils.isEmpty(university)&& !TextUtils.isEmpty(fname)&& !TextUtils.isEmpty(mname)&& !TextUtils.isEmpty(address)){

                uploadProfile();

                }else {
                    Toast.makeText(getContext(), "Enter Profile Information !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        goUms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ForUMS.class);
                startActivity(intent);
            }
        });



        return view;
    }

    private void uploadProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Profile Updating.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String name = pNameET.getText().toString();
        String department = pDemartmentET.getText().toString();
        String email = pemailET.getText().toString();
        String phone = pPhoneET.getText().toString();
        String university = pUniversityET.getText().toString();
        String fatherName = pFathernameET.getText().toString();
        String motherName = pMothernameET.getText().toString();
        String address = pAddressET.getText().toString();

        ProfilePosoClass pPosoclass  = new ProfilePosoClass(name, department, email, phone, university, fatherName, motherName, address);

//        HashMap<String, Object> profileMap = new HashMap<>();
//        profileMap.put("fullName", name);
//        profileMap.put("department", department);
//        profileMap.put("email", email);
//        profileMap.put("phone", phone);
//        profileMap.put("university", university);
//        profileMap.put("fatherName", fatherName);
//        profileMap.put("motherName", motherName);
//        profileMap.put("address", address);
//        profileMap.put("imgUrl", imageUrl);

        FirebaseDatabase.getInstance().getReference("User")
                .child(currentUid).setValue(pPosoclass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Upload Profile done !", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed !", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImages() {
        if (uri != null)  {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image").child(uri.getLastPathSegment());

            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Uploading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            UploadTask image_path = storageReference.putFile(uri);
            image_path.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
//                    uploadProfile();
                    imageUpload();


                    progressDialog.dismiss();

                    Intent intent = new Intent(getContext(), DrawerActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Cancelled ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Select a pic", Toast.LENGTH_SHORT).show();
        }
    }

    private  void imageUpload(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("imgUrl", imageUrl);
        FirebaseDatabase.getInstance().getReference("User")
                .child(currentUid).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Upload Profile done !", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed !", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            circleImageView.setImageURI(uri);
            uploadImages();
        } else {
            Toast.makeText(getContext(), "Didn't select a pic", Toast.LENGTH_SHORT).show();
        }
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK){
//            uri = data.getData();
//            circleImageView.setImageURI(uri);
//        }else {
//            Toast.makeText(getContext(), "You haven't picked images", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void loadSettings() {
        firebaseDatabase.child("User").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    changePic.setVisibility(View.VISIBLE);

                    ProfilePosoClassImg data = dataSnapshot.getValue(ProfilePosoClassImg.class);

                    String proPic = data.getImgUrl();
                    String name = data.getFullName();
                    String deparment = data.getDepartment();
                    String email = data.getEmail();
                    String phone = data.getPhone();
                    String university = data.getUniversity();
                    String fname = data.getFatherName();
                    String mname = data.getMotherName();
                    String address = data.getAddress();

                    Glide.with(getContext()).load(proPic).error(R.drawable.default_pro_pic).into(circleImageView);
                    pNameET.setText(name);
                    pDemartmentET.setText(deparment);
                    pemailET.setText(email);
                    pPhoneET.setText(phone);
                    pUniversityET.setText(university);
                    pFathernameET.setText(fname);
                    pMothernameET.setText(mname);
                    pAddressET.setText(address);

//                    HomeFragment homeFragment = new HomeFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Name",name);
//
//                    homeFragment.setArguments(bundle);




                }else{
                    Toast.makeText(getContext(), "Profile not updated ! !", Toast.LENGTH_SHORT).show();
                    pNameET.setText("User Name");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database Error"+databaseError, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void loadProfileStatus() {
        firebaseDatabase.child("User Status").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    ProfileStatusPosoClass data = dataSnapshot.getValue(ProfileStatusPosoClass.class);

                    String pCGPA = data.getCurrentCGPA();
                    String pCrdone = data.getCreditComplete();
                    String pcrLft = data.getCreditLeft();

                    curCGPATV.setText(pCGPA);
                    crCompleteTV.setText(pCrdone);
                    crLeftTV.setText(pcrLft);

//                    HomeFragment homeFragment = new HomeFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Name",name);
//
//                    homeFragment.setArguments(bundle);


                }else{
                    Toast.makeText(getContext(), "No data found !", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database Error"+databaseError, Toast.LENGTH_SHORT).show();

            }
        });


    }

}
