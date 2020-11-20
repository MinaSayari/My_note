package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.jar.Attributes;

public class LoginActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Email;
    private EditText Password;
    private Button Login;
    private Button CreAcc;

    String txt_Username;
    String txt_Email;
    String txt_Password;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;

    ProgressDialog ProDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.btnLogin);
        CreAcc = findViewById(R.id.btnCreat);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        ProDia = new ProgressDialog(this);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_Username = Username.getText().toString();
                txt_Email = Email.getText().toString();
                txt_Password = Password.getText().toString();

                if (TextUtils.isEmpty(txt_Password) || TextUtils.isEmpty(txt_Email)) {
                    Toast.makeText(LoginActivity.this ,"Username or Email or Password is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_Email,txt_Password);
                }
            }

        });

        CreAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_Username = Username.getText().toString();
                txt_Email = Email.getText().toString();
                txt_Password = Password.getText().toString();

                if (TextUtils.isEmpty(txt_Username) || TextUtils.isEmpty(txt_Password) || TextUtils.isEmpty(txt_Email)) {
                    Toast.makeText(LoginActivity.this ,"Username or Email or Password is empty!", Toast.LENGTH_SHORT).show();
                } else if (txt_Password.length() < 5 ) {
                    Toast.makeText(LoginActivity.this, "Password is too short.\nPassword must be 5 character or more than that.", Toast.LENGTH_SHORT).show();
                } else {
                    CreateAccount(txt_Username,txt_Email,txt_Password);
                }
            }
        });

    }

    private void loginUser(String Email, String Password) {
        mAuth.signInWithEmailAndPassword(Email , Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login is Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void CreateAccount(final String Username,final String Email, final String Password){
        ProDia.setMessage("Please Wait ...");
        ProDia.show();

        mAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Username", Username);
                map.put("Email", Email);
                map.put("Password", Password);
                map.put("id", mAuth.getCurrentUser().getUid());

                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ProDia.dismiss();
                            Toast.makeText(LoginActivity.this,"Create Account is Successful!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProDia.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}