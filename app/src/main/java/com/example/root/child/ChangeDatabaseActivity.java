package com.example.root.child

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ChangeDatabaseActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var email: TextView? = null
    private var name: EditText? = null
    private var gender: EditText? = null
    private var mobile: EditText? = null
    private var Update: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_database)

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        email = findViewById(R.id.changeEmail)
        name = findViewById(R.id.changeName)
        gender = findViewById(R.id.chageGender)
        mobile = findViewById(R.id.ChangeMobile)
        Update = findViewById(R.id.update)
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("demodatabase")
        email!!.text = mAuth!!.currentUser!!.email

        databaseReference!!.child(mAuth!!.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    name!!.setText(dataSnapshot.child("name").getValue<String>(String::class.java))
                    mobile!!.setText(dataSnapshot.child("name").getValue<String>(String::class.java))
                    gender!!.setText(dataSnapshot.child("gender").getValue<String>(String::class.java))

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "error occurred", Toast.LENGTH_LONG).show()

            }
        })

        Update!!.setOnClickListener {
            //write code for update database
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
