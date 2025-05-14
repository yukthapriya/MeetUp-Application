package com.example.interesto.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.interesto.R;
import com.example.interesto.databinding.ActivityProfileBinding;
import com.example.interesto.supportive.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.net.URI;

public class Profile extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri targetUri;
    String checkImageExist,interestCategory="General";
    ProgressDialog dialog;
    String drop_down_array[]={"General","Sports","Science","Health","Entertainment","Technology","Bussiness"};
    ArrayAdapter<String> drop_down_adapter;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Updating Profile...");

        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();

        drop_down_adapter=new ArrayAdapter<String>(Profile.this,R.layout.dropdownlist_item,drop_down_array);
        binding.autoCompleteTxt.setAdapter(drop_down_adapter);
        binding.autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                interestCategory=adapterView.getItemAtPosition(i).toString();
            }
        });

        binding.userName.requestFocus();

        database.getReference().child("users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    User user=task.getResult().getValue(User.class);
                    Log.d("krishna",user.getName()+" "+user.getUid());
                    checkImageExist=user.getProfileImage();
                    Glide.with(Profile.this).load(checkImageExist)    //using Glide library for image processing
                            .placeholder(R.drawable.user)
                            .into(binding.imageView2);
                    binding.userName.setText(user.getName());
                    binding.about.setText(user.getUabout());
                    binding.autoCompleteTxt.setText(user.getUinterest(),false);
                    interestCategory=user.getUinterest();
                }
            }
        });

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,45);
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.userName.getText().toString();
                String about=binding.about.getText().toString();

                if(name.trim().isEmpty())
                {
                    binding.userName.setError("Please Enter Name");
                    return;
                }

                if(about.trim().isEmpty())
                {
                    binding.about.setError("Required");
                    return;
                }

                dialog.show();
                if(targetUri!=null)
                {
                    StorageReference storageReference=storage.getReference().child("profile").child(auth.getUid());
                    storageReference.putFile(targetUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri=uri.toString();
                                        String uid=auth.getUid();
                                        String phone=auth.getCurrentUser().getPhoneNumber();
                                        String userName=binding.userName.getText().toString();
                                        String aboutUser=binding.about.getText().toString();

                                        User user=new User(uid,userName,phone,imageUri,aboutUser,interestCategory);

                                        database.getReference().child("users").child(uid).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Toast.makeText(Profile.this,"Profile Updated Successfully!!!",Toast.LENGTH_LONG).show();
                                                        Intent intent=new Intent(Profile.this,Information.class);
                                                        startActivity(intent);
                                                        finishAffinity();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
                else if(!checkImageExist.equalsIgnoreCase("no image"))
                {
                    String imageUri=checkImageExist;
                    String uid=auth.getUid();
                    String phone=auth.getCurrentUser().getPhoneNumber();
                    String userName=binding.userName.getText().toString();
                    String aboutUser=binding.about.getText().toString();

                    User user=new User(uid,userName,phone,imageUri,aboutUser,interestCategory);

                    database.getReference().child("users").child(uid).setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(Profile.this,"Profile Updated Successfully!!!",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(Profile.this,Information.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });
                }
                else{
                    String imageUri="No Image";
                    String uid=auth.getUid();
                    String phone=auth.getCurrentUser().getPhoneNumber();
                    String userName=binding.userName.getText().toString();
                    String aboutUser=binding.about.getText().toString();

                    User user=new User(uid,userName,phone,imageUri,aboutUser,interestCategory);

                    database.getReference().child("users").child(uid).setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Toast.makeText(Profile.this,"Profile Updated Successfully!!!"+imageUri,Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(Profile.this,Information.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });
                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null)
        {
            if(data.getData()!=null){
                targetUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    binding.imageView2.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}