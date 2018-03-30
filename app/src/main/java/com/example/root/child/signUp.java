package com.example.root.child;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class signUp extends Fragment {

    Button submit;
    RadioGroup gender,userType;
    View view;
    ArrayList <String> userData = new ArrayList<>();
    String Susename,Semail,Spassword,SconfirmPassword,Sgender,SuserType;
    EditText signUpUsername,signUpUserEmail,signUpPassword,signUpConfirmPassword;
    public signUp() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_sign_up, container, false);
         submit=view.findViewById(R.id.submit);
         signUpConfirmPassword = view.findViewById(R.id.SignUpConfirmPassword);
         signUpPassword = view.findViewById(R.id.SignUpPassword);
         signUpUserEmail = view.findViewById(R.id.signUpEmail);
         signUpUsername = view.findViewById(R.id.signUpname);
        submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(conditionSignUp()){
                     userData.add(0,Susename);
                     userData.add(1,Semail);
                     userData.add(2,Spassword);
                     userData.add(3,Sgender);
                     userData.add(4,SuserType);
                     connectActivity();
                 }

             }
         });


        gender = view.findViewById(R.id.Gender);
        userType = view.findViewById(R.id.userType);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMale:
                        Sgender = "male";
                        break;
                    case R.id.radioFemale:
                        Sgender = "female";
                        break;
                }
            }
        });
        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioParent:
                        SuserType = "Parent";
                        break;
                    case R.id.radioChild:
                        SuserType = "Child";
                        break;
                }

            }
        });
         return view;
    }
   public boolean conditionSignUp()
   {

       Susename = signUpUsername.getText().toString().trim();
       Semail = signUpUserEmail.getText().toString().trim();
       Spassword = signUpPassword.getText().toString().trim();
       SconfirmPassword= signUpConfirmPassword.getText().toString().trim();
       if(TextUtils.isEmpty(Susename))
       {
           Toast.makeText(getActivity(),"Enter the name",Toast.LENGTH_LONG).show();
           return  false;
       }
       if(TextUtils.isEmpty(SconfirmPassword) || TextUtils.isEmpty(Spassword))
       {
           Toast.makeText(getActivity(),"Password field is empty",Toast.LENGTH_LONG).show();
           return false;
       }
       if(!TextUtils.equals(SconfirmPassword,Spassword))
       {
           Toast.makeText(getActivity(),"Password not matched",Toast.LENGTH_LONG).show();
           return false;
       }
       if (TextUtils.isEmpty(Semail))
       {
           Toast.makeText(getActivity(),"Email field is empty",Toast.LENGTH_LONG).show();
           return  false;
       }
       return true;
   }
   public  void connectActivity()
   {
       Intent intent = new Intent(getActivity(),otpVerification.class);
       intent.putExtra("databalue",userData);
       startActivity(intent);

   }
}
