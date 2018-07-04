package com.mukhayy.firebasedatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registration extends AppCompatActivity {

    private EditText phone, name;
    FirebaseAuth auth;
    DatabaseReference ref;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        phone = findViewById(R.id.phone);
        name = findViewById(R.id.Name);

        Button register = findViewById(R.id.register);

        ref = FirebaseDatabase.getInstance().getReference("User");
        auth = FirebaseAuth.getInstance();
        user = new User();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
                ref.push().setValue(user);
                String phoneNumber = phone.getText().toString().trim();

                if (phoneNumber.isEmpty()) {
                    phone.setError("Valid number is required");
                    phone.requestFocus();
                    return;
                }

                Intent intent = new Intent(Registration.this, Confirmation.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });
    }

    private void getValues(){
        user.setName(name.getText().toString().trim());
        user.setPhone(phone.getText().toString().trim());
    }


}
