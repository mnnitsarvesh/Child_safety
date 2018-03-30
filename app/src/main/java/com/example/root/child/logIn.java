package com.example.root.child;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class logIn extends Fragment {

    public logIn() {
        // Required empty public constructor
    }

    private FirebaseAuth mAuth;
    private EditText mEmail,mPassword;
    private Button mLogin,mSignup;
    private TextView forgotPassword;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgressdialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        mAuth = FirebaseAuth.getInstance();
        mEmail=view.findViewById(R.id.email);
        mPassword=view.findViewById(R.id.password);
        mLogin=view.findViewById(R.id.login);
        mSignup=view.findViewById(R.id.signup);
        forgotPassword = view.findViewById(R.id.forgotpasswd);
        mProgressdialog= new ProgressDialog(getContext());
        mProgressdialog.setMessage("Please wait...");
        mProgressdialog.setCanceledOnTouchOutside(false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("demodatabase");
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            ConnectActivity();
        }
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ForgotActivity.class);
                startActivity(intent);
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email =mEmail.getText().toString().trim();
                String password =mPassword.getText().toString().trim();
                if(isVerified()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //verified
                                synchronized (this) {
                                    ConnectActivity();
                                }

                            } else {
                                //error
                                mProgressdialog.dismiss();
                                Toast.makeText(getActivity(),"email id or password is incorrect",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Please side to go signup",Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }

    public boolean isVerified()
    {
        if(TextUtils.isEmpty(mEmail.getText().toString().trim()))
        {
            return  false;
        }
        if(TextUtils.isEmpty(mPassword.getText().toString().trim()))
        {
            return false;
        }
        return true;
    }
    public void ConnectActivity(){
        mProgressdialog.show();

        databaseReference.child(mAuth.getCurrentUser().getUid()).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String userType = dataSnapshot.getValue(String.class);
                    Intent intent;
                    if(userType.equals("Child"))
                     intent =  new Intent(getActivity(),ParentActivity.class);
                    else
                     intent = new  Intent(getActivity(),ChildActivity.class);
                    mProgressdialog.hide();
                    mProgressdialog.dismiss();
                    startActivity(intent);
                    getActivity().finish();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressdialog.dismiss();
            }
        });
    }


}
