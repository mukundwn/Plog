package com.example.mukundwn.plog;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPersonalInfo extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String e_mail,u_name,pw,ph_num;
    EditText email_id,username,password,phnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal_info);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        e_mail=firebaseUser.getEmail();
        email_id=(EditText)findViewById(R.id.editText11);
        username=(EditText)findViewById(R.id.editText13);
        password=(EditText)findViewById(R.id.editText14);
        phnum=(EditText)findViewById(R.id.editText15);
        String u_id=firebaseUser.getUid();
        final DocumentReference docRef = firebaseFirestore.collection("users").document(u_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        email_id.setText(e_mail);
                        u_name=document.get("name").toString();
                        pw=document.get("password").toString();
                        ph_num=document.get("phonenum").toString();
                        username.setText(u_name);
                        password.setText(pw);
                        phnum.setText(ph_num);

                    }
                }
            }
        });








    }
}
