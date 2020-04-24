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

                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please Waiting .....");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check user is exist
                        if (dataSnapshot.child(edPhoneNumber.getText().toString()).exists()) {


                            //Get user info
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edPhoneNumber.getText().toString()).getValue(User.class);
                                if (user.getPassword()  != null && user.getPassword().equals(edtPawwordSignIn.getText().toString())) {
                                    Toast.makeText(SignInActivity.this, "Sign In Successfully !", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(SignInActivity.this,HomeActivity.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                                }
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User not exist in database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
