package com.example.mukundwn.plog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicineList extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String id;
    DocumentReference documentReference;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_class);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        id=firebaseUser.getUid();
        lv=(ListView)findViewById(R.id.listview);
        final ArrayList<String> Meds=new ArrayList<>();
        registerForContextMenu(lv);
       documentReference=firebaseFirestore.collection("Medicines").document(id);
       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          DocumentSnapshot documentSnapshot=task.getResult();
          if (task.isSuccessful()){
              String s1=documentSnapshot.getString("Med1");
              String s2=documentSnapshot.getString("Med2");
              String s3=documentSnapshot.getString("Med3");
              Meds.add(s1);
              Meds.add(s2);
              Meds.add(s3);
              ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,Meds);
              lv.setAdapter(adapter);
          }

           }
       });

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select the action");
        menu.add(0, v.getId(), 0, "Click to set an alarm");

    }

    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Click to set an alarm")
        {
            Toast.makeText(this, "You have chosen to set an alarm", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
