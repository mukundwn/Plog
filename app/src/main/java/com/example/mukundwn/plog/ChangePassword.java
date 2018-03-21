package com.example.mukundwn.plog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static com.example.mukundwn.plog.otp_screen.s;

public class ChangePassword extends AppCompatActivity {
    private Firebase mRef;
    Button b1;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser firebaseUser;
    EditText password,confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        firebaseAuth=FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://plog-26c24.firebaseio.com/Users");
        password=(EditText)findViewById(R.id.editText19);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        s=firebaseUser.getUid();
        b1=(Button)findViewById(R.id.button7);
        firebaseFirestore=FirebaseFirestore.getInstance();
        confirmpassword=(EditText)findViewById(R.id.editText20);
        String pw=password.getText().toString();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(confirmpassword.getText().toString())) {
                    Toast.makeText(ChangePassword.this, "Password details matched, pushing to DB", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ChangePassword.this, "Passwords don't match re-enter", Toast.LENGTH_SHORT).show();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("password", password.getText().toString());
                firebaseFirestore.collection("users").document(s).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Password has been saved", Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(getApplicationContext(),Dashboard.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Password update Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });
     }

}

