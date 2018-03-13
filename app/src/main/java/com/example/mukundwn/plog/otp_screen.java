package com.example.mukundwn.plog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Scanner;

import static com.example.mukundwn.plog.SignUpActivity.pw;
import static com.example.mukundwn.plog.SignUpActivity.u_name;

public class otp_screen extends AppCompatActivity {
    EditText e,edit1;
    Button b1,b2;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
   static  String s,s1;
   Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        Button b=(Button)findViewById(R.id.button);
        e=(EditText)findViewById(R.id.phno);
        edit1= (EditText) findViewById(R.id.editText2);
        b1=(Button)findViewById(R.id.button3);
        //b2=(Button)findViewById(R.id.button4);
        //bundle = getIntent().getExtras();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();



        Toast.makeText(this, u_name, Toast.LENGTH_SHORT).show();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s.equals(edit1.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"OTP verification successful!!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), PersonalDetails.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Sorry ,Wrong OTP entered!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s1=firebaseUser.getUid();
                String phnum=e.getText().toString();
                String s2=firebaseUser.getEmail();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("email",s2);
                hashMap.put("name",u_name);
                hashMap.put("password",pw);
                hashMap.put("phonenum",phnum);

                firebaseFirestore.collection("users").document(s1).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(otp_screen.this, "Adding to Database Successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(otp_screen.this, "Not successful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                 s=generateOTP();
                Uri u=Uri.parse("https://www.smsgatewayhub.com/api/mt/SendSMS").buildUpon().appendQueryParameter("APIKey","ziWS4SVzTU2rLIJvxvICeA").appendQueryParameter("senderid","TESTIN").appendQueryParameter("channel","2").appendQueryParameter("DCS","0").appendQueryParameter("flashsms","0").appendQueryParameter("number",e.getText().toString()).appendQueryParameter("text","OTP is:"+s).appendQueryParameter("route","").build();
                try {
                    URL url = new URL(u.toString());
                    new Otp().execute(url);


                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }
    public static int range = 9;  // to generate a single number with this range, by default its 0..9
    public static int length = 4;
    public String generateOTP(){
        int randomNumber;
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }

        return s;
    }


    public class Otp extends AsyncTask<URL,Void,String>{
        @Override
        protected String doInBackground(URL... urls) {
            String sa="error";
            try {
                URL urlmy=urls[0];
                sa=getResponseFromHttpUrl(urlmy);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return sa;
        }
        public  String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String results) {
            if(results!=null||!results.equals("")){
                //e.setText(results.toString());
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    finishAffinity();

    }
}