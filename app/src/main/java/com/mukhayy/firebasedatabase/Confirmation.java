package com.mukhayy.firebasedatabase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class Confirmation extends AppCompatActivity {

    private Button next;
    private EditText code;
    private String verificationId;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        code = findViewById(R.id.code);
        next = findViewById(R.id.Next);

        auth = FirebaseAuth.getInstance();

        String phoneNumber = getIntent().getStringExtra("phoneNumber").trim();
        sendVerificationCode(phoneNumber);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codee = code.getText().toString().trim();
                if (codee.isEmpty() || codee.length() < 6) {
                    code.setError("Enter code...");
                    code.requestFocus();
                    return;
                }
                verifySignInCode(codee);
            }
        });
    }

    private void verifySignInCode(String codeEntered){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, codeEntered);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Confirmation.this, Retrieve.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {
                            Toast.makeText(Confirmation.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String codereceived = phoneAuthCredential.getSmsCode();
            if (codereceived != null) {
                code.setText(codereceived);
                verifySignInCode(codereceived);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Confirmation.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    };

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
