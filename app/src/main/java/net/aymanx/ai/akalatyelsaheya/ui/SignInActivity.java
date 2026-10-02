package net.aymanx.ai.akalatyelsaheya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.aymanx.ai.akalatyelsaheya.R;
import net.aymanx.ai.akalatyelsaheya.common.Common;
import net.aymanx.ai.akalatyelsaheya.pojo.User;

public class SignInActivity extends AppCompatActivity {

    EditText edPhoneNumber ,edtPawwordSignIn ;
    Button signInButton ;
    private ProgressDialog activeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edtPawwordSignIn = findViewById(R.id.edtPawwordSignIn);
        signInButton = findViewById(R.id.signin);

        //Firebase init
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone = edPhoneNumber.getText().toString().trim();
                final String password = edtPawwordSignIn.getText().toString();
                if (!phone.matches("\\+?[0-9]{6,20}") || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Enter a valid phone number and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                signInButton.setEnabled(false);

                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                activeDialog = mDialog;
                mDialog.setMessage("Please Waiting .....");
                mDialog.show();
                table_user.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check user is exist
                        if (isFinishing() || isDestroyed()) return;
                        signInButton.setEnabled(true);
                        mDialog.dismiss();
                        activeDialog = null;
                        if (dataSnapshot.exists()) {


                            //Get user info
                            User user = dataSnapshot.getValue(User.class);
                                if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
                                    Toast.makeText(SignInActivity.this, "Sign In Successfully !", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(SignInActivity.this,HomeActivity.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                                }
                        }else {
                            Toast.makeText(SignInActivity.this, "User not exist in database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (isFinishing() || isDestroyed()) return;
                        mDialog.dismiss();
                        activeDialog = null;
                        signInButton.setEnabled(true);
                        Toast.makeText(SignInActivity.this, "Unable to sign in. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    @Override
    protected void onDestroy() {
        if (activeDialog != null) activeDialog.dismiss();
        activeDialog = null;
        super.onDestroy();
    }
}
