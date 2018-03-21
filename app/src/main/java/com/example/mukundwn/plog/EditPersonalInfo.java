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

public class EditPersonalInfo extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    static String b_group,p_age,p_ht,p_wt,doc,months,marital;
    EditText bloodgroup,age,height,weight,doc_name,month,mar;
    Button submit;
    Firebase mRef;
    String s;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        s=firebaseUser.getUid();
        bloodgroup=(EditText)findViewById(R.id.editText11);
        age=(EditText)findViewById(R.id.editText13);
        height=(EditText)findViewById(R.id.editText14);
        weight=(EditText)findViewById(R.id.editText15);
        mar=(EditText)findViewById(R.id.editText16);
        doc_name=(EditText)findViewById(R.id.editText17);
        month=(EditText)findViewById(R.id.editText18);
        firebaseAuth=FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://plog-26c24.firebaseio.com/Users");
        submit=(Button)findViewById(R.id.button5);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_group=bloodgroup.getText().toString();
                p_age=age.getText().toString();
                p_ht=height.getText().toString();
                p_wt=weight.getText().toString();
                doc=doc_name.getText().toString();
                months=month.getText().toString();
                marital=mar.getText().toString();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("BloodGroup",b_group);
                hashMap.put("Age",p_age);
                hashMap.put("Height",p_ht);
                hashMap.put("Weight",p_wt);
                hashMap.put("MonthsOfPreg",months);
                hashMap.put("DoctorName",doc);
                hashMap.put("MaritalStatus",marital);
                firebaseFirestore.collection("Personal").document(s).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(EditPersonalInfo.this, "Personal Details have been updated on DB", Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(getApplicationContext(),Dashboard.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(EditPersonalInfo.this, "Detail update Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



    }
}
