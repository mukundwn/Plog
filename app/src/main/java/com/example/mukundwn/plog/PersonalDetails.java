package com.example.mukundwn.plog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;

import static com.example.mukundwn.plog.SignUpActivity.email_id;
import static com.example.mukundwn.plog.SignUpActivity.u_name;

public class PersonalDetails extends Activity {
    EditText bloodgroup,age,height,weight,doc_name;
    static String b_group,p_age,p_ht,p_wt,doc,months,marital;
    private static int RESULT_LOAD_IMAGE = 1;
    String s;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    StorageReference mStorage;
    Spinner s1,s2;
    Button b;
    FirebaseFirestore firebaseFirestore;
    String[] MonthsofPreg={"1","2","3","4","5","6","7","8","9","10"};
    String[] MaritalStatus={"Married","Single","In a Relationship","Live In Relationship","Widowed"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseFirestore firebaseFirestore;
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        s=firebaseUser.getUid();
        s1=(Spinner)findViewById(R.id.spinner);
        s2=(Spinner)findViewById(R.id.spinner2);
        bloodgroup=(EditText)findViewById(R.id.editText6);
        age=(EditText)findViewById(R.id.editText7);
        height=(EditText)findViewById(R.id.editText8);
        weight=(EditText)findViewById(R.id.editText9);
        doc_name=(EditText)findViewById(R.id.editText10);
        b=(Button)findViewById(R.id.button4);

        mStorage= FirebaseStorage.getInstance().getReference();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,MonthsofPreg);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,MaritalStatus);
        s1.setAdapter(adapter);
        s2.setAdapter(adapter1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                months=s1.getSelectedItem().toString();
                b_group=bloodgroup.getText().toString();
                p_age=age.getText().toString();
                p_ht=height.getText().toString();
                p_wt=weight.getText().toString();
                doc=doc_name.getText().toString();
                marital=s2.getSelectedItem().toString();
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
                            Toast.makeText(PersonalDetails.this, "Personal Details have been saved", Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(getApplicationContext(),Dashboard.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(PersonalDetails.this, "Detail entering Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        Button buttonLoadImage = (Button) findViewById(R.id.img_button);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                sessionData s = new sessionData();
                s.img = bmp;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(email_id,encoded);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageBitmap(bmp);
            Uri uri=data.getData();
            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PersonalDetails.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
           });
        }


    }



    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}