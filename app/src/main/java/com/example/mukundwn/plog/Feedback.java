package com.example.mukundwn.plog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Feedback extends AppCompatActivity {

    EditText feedback;
    Button b;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String s;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        b=(Button)findViewById(R.id.button8);
        feedback=(EditText)findViewById(R.id.editText21);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        s=firebaseUser.getUid();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String f_b=feedback.getText().toString();
                f_b.trim();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("feedback",f_b);
                firebaseFirestore.collection("Feedback").document(s).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Feedback.this, "Thank you for your valuable feedback, necessary steps will be taken", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),Dashboard.class);
                        startActivity(i);

                    }
                });
            }
        });

    }
}
