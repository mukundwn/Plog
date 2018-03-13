package com.example.mukundwn.plog;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.IOException;

public class SignUpActivity extends Activity {
    static String u_name,email_id,pw,cnfpw;
    EditText name,email,password,confirmpassword;
    private Firebase mRef;
    Button b1;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=(EditText)findViewById(R.id.editText3);
        email=(EditText)findViewById(R.id.editText4);
        password=(EditText)findViewById(R.id.editText5);
        confirmpassword=(EditText)findViewById(R.id.editText);
        //b1=(Button)findViewById(R.id.button4);
        firebaseAuth=FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://plog-26c24.firebaseio.com/Users");
        Button otp_button = (Button) findViewById(R.id.button2);
        otp_button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                u_name=name.getText().toString();
                email_id=email.getText().toString();
                pw=password.getText().toString();
                cnfpw=confirmpassword.getText().toString();
                Firebase mRefChild0=mRef.child("Name");
                Firebase mRefChild1=mRef.child("Email");
                Firebase mRefChild2=mRef.child("Password");
                Firebase mRefChild3=mRef.child("ConfirmPassword");
                //mRef.push().setValue(u_name);
                mRefChild0.setValue(u_name);
                mRefChild1.setValue(email_id);
                mRefChild2.setValue(pw);
                mRefChild3.setValue(cnfpw);
                Intent i = new Intent(getApplicationContext(), otp_screen.class);
                startActivity(i);

                String em=email.getText().toString().trim();
                String ps=password.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(em,ps).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(),otp_screen.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(getApplicationContext(), "error" + " " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }
                });

            }


        });




    }
}
