package net.aymanx.ai.akalatyelsaheya.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.aymanx.ai.akalatyelsaheya.R;
import net.aymanx.ai.akalatyelsaheya.pojo.User;

public class SignUpActivity extends AppCompatActivity {

    EditText edPhoneNumber, edtPawwordSignUp, edtName;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edtPawwordSignUp = findViewById(R.id.edtPawwordSignUp);
        edtName = findViewById(R.id.edName);
        signUpButton = findViewById(R.id.signup);

        //Firebase init
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Please Waiting .....");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check user is exist
                        if (dataSnapshot.child(edPhoneNumber.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Phone Number Already Register !", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            User user = new User(edtName.getText().toString(), edtPawwordSignUp.getText().toString());
                            table_user.child(edPhoneNumber.getText().toString()).setValue(user);
                            if (user.getPassword() != null && user.getPassword().equals(edtPawwordSignUp.getText().toString())) {
                                Toast.makeText(SignUpActivity.this, "Sign Up Successfully !", Toast.LENGTH_SHORT).show();
                                finish();
                            }
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