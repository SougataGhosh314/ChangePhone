package com.brainstorm314.sougataghosh.changephone;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference usersRef, usersRefToUse;
    private EditText editTextNewPhone;
    private EditText editTextUserId;
    private TextView textViewCurrentPhone;
    private Button buttonGetPhone, buttonChangePhone;

    private String currentUser;
    private String newPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setOnClickListeners();
    }

    private void findViews(){
        editTextNewPhone = findViewById(R.id.editTextNewPhone);
        editTextUserId = findViewById(R.id.editTextUserId);
        textViewCurrentPhone = findViewById(R.id.textViewCurrentPhone);
        buttonChangePhone = findViewById(R.id.buttonChangePhone);
        buttonGetPhone = findViewById(R.id.buttonGetPhone);
        usersRef = FirebaseDatabase.getInstance().getReference("UserSignIn");
        usersRefToUse = FirebaseDatabase.getInstance().getReference("UserSignIn");
    }

    private void setOnClickListeners(){
        buttonGetPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = editTextUserId.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(currentUser)){
                    editTextUserId.setError("Enter valid User");
                    editTextUserId.requestFocus();
                    return;
                }
                setCurrentPhone();
            }
        });

        buttonChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = editTextUserId.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(currentUser)){
                    editTextUserId.setError("Enter valid User");
                    editTextUserId.requestFocus();
                    return;
                }

                final String u = currentUser;
                newPhone = editTextNewPhone.getText().toString().trim();

                if(newPhone.length()!=10){
                    editTextNewPhone.setError("Enter Valid Phone");
                    editTextNewPhone.requestFocus();
                    return;
                }

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(u)){

                            usersRefToUse.child(u)
                                    .child("mobno")
                                    .setValue(newPhone);

                            usersRefToUse.child(u)
                                    .child("pass")
                                    .setValue("1234");

                            editTextNewPhone.setText("");
                            setCurrentPhone();
                            Toast.makeText(getApplicationContext(),
                                    "New Phone number set sucessfully",
                                    Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(getApplicationContext(),
                                    "User ID doesn't exist!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setCurrentPhone(){
        currentUser = editTextUserId.getText().toString().trim().toUpperCase();
        if (TextUtils.isEmpty(currentUser)){
            editTextUserId.setError("Enter valid User");
            editTextUserId.requestFocus();
            return;
        }

        final String u = currentUser;

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(u)){
                    Log.e("DEBUG", snapshot.child(u).toString());
                    Log.e("DEBUG", snapshot.child(u).child("mobno").getValue().toString() );
                    textViewCurrentPhone.setText(
                            snapshot.child(u)
                                    .child("mobno")
                                    .getValue()
                                    .toString()
                    );
                } else{
                    Toast.makeText(getApplicationContext(),
                            "User ID doesn't exist or Error in fetching current phone number!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
