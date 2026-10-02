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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import net.aymanx.ai.akalatyelsaheya.R;
import net.aymanx.ai.akalatyelsaheya.pojo.User;

public class SignUpActivity extends AppCompatActivity {

    EditText edPhoneNumber, edtPawwordSignUp, edtName;
    Button signUpButton;
    private ProgressDialog activeDialog;

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

                final String phone = edPhoneNumber.getText().toString().trim();
                final String name = edtName.getText().toString().trim();
                final String password = edtPawwordSignUp.getText().toString();
                if (!phone.matches("\\+?[0-9]{6,20}") || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter a name, valid phone number and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                signUpButton.setEnabled(false);

                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                activeDialog = mDialog;
                mDialog.setMessage("Please Waiting .....");
                mDialog.show();

                final User user = new User(name, password);
                table_user.child(phone).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        if (currentData.getValue() != null) {
                            return Transaction.abort();
                        }
                        currentData.setValue(user);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(DatabaseError error, boolean committed, DataSnapshot dataSnapshot) {
                        if (isFinishing() || isDestroyed()) return;
                        mDialog.dismiss();
                        activeDialog = null;
                        signUpButton.setEnabled(true);
                        if (error != null) {
                            Toast.makeText(SignUpActivity.this, "Unable to register. Please try again.", Toast.LENGTH_SHORT).show();
                        } else if (!committed) {
                            Toast.makeText(SignUpActivity.this, "Phone Number Already Register !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign Up Successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
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
