package com.example.interesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.interesto.R;
import com.example.interesto.databinding.ActivityOtpverificationBinding;
import com.example.interesto.supportive.GenericTextWatcher;
import com.example.interesto.supportive.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class OTPVerification extends AppCompatActivity {
    EditText in1,in2,in3,in4,in5,in6;

    ActivityOtpverificationBinding binding;
    String verificationId,myCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpverificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        setOTPView();

        String phoneNumber=getIntent().getStringExtra("phonenumber");
        binding.number.setText("Verify "+phoneNumber);
        verificationId=getIntent().getStringExtra("verifyid");

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(in1.getText().toString().trim().isEmpty() || in2.getText().toString().trim().isEmpty() || in3.getText().toString().trim().isEmpty() || in4.getText().toString().trim().isEmpty() || in5.getText().toString().trim().isEmpty() || in6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(OTPVerification.this,"OTP is not valid !",Toast.LENGTH_LONG).show();
                }
                else{
                    if(verificationId!=null) {
                        try
                        {
                            myCode = in1.getText().toString() + in2.getText().toString() + in3.getText().toString() + in4.getText().toString() + in5.getText().toString() + in6.getText().toString();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, myCode);

                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseAuth auth1=FirebaseAuth.getInstance();
                                        String imageUri="No Image";
                                        String uid=auth1.getUid();
                                        String phone=auth1.getCurrentUser().getPhoneNumber();
                                        String userName=auth1.getCurrentUser().getPhoneNumber().substring(3,13);
                                        String aboutUser="About YourSelf";

                                        User user=new User(uid,userName,phone,imageUri,aboutUser,"General");

                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //nothing to do here
                                                    }
                                                });

                                        Intent intent=new Intent(OTPVerification.this,Profile.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(OTPVerification.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        catch (Exception e) {
                            Toast.makeText(OTPVerification.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("krishna", e.toString());
                        }
                    }
                }

            }
        });
    }

    private void setOTPView()
    {
        in1=binding.inputotp1;
        in2=binding.inputotp2;
        in3=binding.inputotp3;
        in4=binding.inputotp4;
        in5=binding.inputotp5;
        in6=binding.inputotp6;

        EditText[] edit={in1,in2,in3,in4,in5,in6};
        in1.addTextChangedListener(new GenericTextWatcher(in1,edit));
        in2.addTextChangedListener(new GenericTextWatcher(in2,edit));
        in3.addTextChangedListener(new GenericTextWatcher(in3,edit));
        in4.addTextChangedListener(new GenericTextWatcher(in4,edit));
        in5.addTextChangedListener(new GenericTextWatcher(in5,edit));
        in6.addTextChangedListener(new GenericTextWatcher(in6,edit));

        in1.requestFocus();
    }
}