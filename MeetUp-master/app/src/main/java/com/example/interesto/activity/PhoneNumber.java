package com.example.interesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.interesto.R;
import com.example.interesto.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PhoneNumber extends AppCompatActivity {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(PhoneNumber.this,Information.class);
            startActivity(intent);
            finish();
        }

        keyPad(true);
        binding.phoneNumber.requestFocus();
        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.phoneNumber.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(PhoneNumber.this,"Invalid Phone Number !",Toast.LENGTH_SHORT).show();
                }
                else if(binding.phoneNumber.getText().toString().trim().length()!=10)
                {
                    Toast.makeText(PhoneNumber.this,"Enter Valid Phone Number !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.next.setEnabled(false);
                    keyPad(false);
                    sendOTP();
                }
            }
        });
    }

    private void sendOTP()
    {
        try {
            String phNumber="+91 "+binding.phoneNumber.getText().toString();
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(PhoneNumber.this)
                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                            binding.next.setEnabled(true);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            keyPad(true);
                            Toast.makeText(PhoneNumber.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);

                            Intent intent=new Intent(PhoneNumber.this,OTPVerification.class);
                            intent.putExtra("phonenumber",phNumber);
                            intent.putExtra("verifyid",s);
                            startActivity(intent);

                            binding.next.setEnabled(true);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        }
        catch (Exception e)
        {
            Toast.makeText(PhoneNumber.this,e.toString(),Toast.LENGTH_SHORT).show();
            binding.next.setEnabled(true);
            binding.progressBar.setVisibility(View.INVISIBLE);
            keyPad(true);
            Log.d("krishna",e.toString());
        }
    }

    private void keyPad(boolean set)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE);
        if(set)
        {
            imm.showSoftInput(binding.phoneNumber, InputMethodManager.SHOW_IMPLICIT);
        }else{
            imm.hideSoftInputFromWindow(binding.phoneNumber.getApplicationWindowToken(), 0);
        }
    }
}