package com.example.mukundwn.plog;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.mukundwn.plog.SignUpActivity.email_id;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Bitmap b = null;
    ListView listView;
    Button search;
    EditText search_item;
    public String helpline;
    FirebaseFirestore firebaseFirestore;
    ImageView i;
    public String city_name;
    String user_name;
    TextView e_mail,u_name;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        search=(Button)findViewById(R.id.button6);
        search_item=(EditText)findViewById(R.id.editText12);
        String search_Text=search_item.getText().toString();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String email=firebaseUser.getEmail();
        listView=(ListView)findViewById(R.id.hosp_list_view);
        registerForContextMenu(listView);
        final ArrayList<String> hosp_name=new ArrayList<>();
        final ArrayList<String> hosp_area=new ArrayList<>();
        final ArrayList<String> hosp_city=new ArrayList<>();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               city_name=search_item.getText().toString();
                //Toast.makeText(getApplicationContext(), city_name, Toast.LENGTH_SHORT).show();
                firebaseFirestore.collection("Hospitals").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                            String name= doc.getDocument().getString("Name");
                            String area= doc.getDocument().getString("Area");
                            String city=doc.getDocument().getString("City");
                            if(city_name.equals(city))
                            {
                                hosp_name.add(name);
                            }
                        }
                    }
                });
                ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,hosp_name);
                listView.setAdapter(adapter);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter.clear();

            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sessionData obj = new sessionData();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String a = sharedPref.getString(email_id,"na");


        if (!a.equals("na")){

            byte[] decodedBytes = Base64.decode(a, 0);
            b= BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        e_mail = (TextView)header.findViewById(R.id.textView);
        e_mail.setText(email);
        View hView =  navigationView.getHeaderView(0);
        ImageView img = (ImageView)hView.findViewById(R.id.imageView);
        if (b != null){

            img.setImageBitmap(b);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu,v,menuInfo);
        menu.setHeaderTitle("Select the action");
        menu.add(0,v.getId(),0,"Contact number");
        menu.add(0,v.getId(),1,"Email id");
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Contact number")
        {
            helpline="1066";
            Toast.makeText(getApplicationContext(),"Retrieving Helpline number: "+helpline,Toast.LENGTH_LONG).show();

        }
        else if(item.getTitle()=="Email id")
        {
            Toast.makeText(getApplicationContext(),"Getting email id:  hospitals_"+city_name+"@gmail.com",Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_personal) {
            Intent i=new Intent(getApplicationContext(),ViewPersonalInfo.class);
            startActivity(i);
        } else if (id == R.id.edit_personal) {
            Intent i=new Intent(getApplicationContext(),EditPersonalInfo.class);
            startActivity(i);

        } else if (id == R.id.change_pw) {
            Intent i=new Intent(getApplicationContext(),ChangePassword.class);
            startActivity(i);

        } else if (id == R.id.feedback) {
            Intent i=new Intent(getApplicationContext(),Feedback.class);
            startActivity(i);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
