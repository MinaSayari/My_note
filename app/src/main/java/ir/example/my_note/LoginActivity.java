package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import ir.example.my_note.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    String username;
    String email;
    String password;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private ActivityLoginBinding binding;

    ProgressDialog ProDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.loginActivityToolbar.toolbarSimpleBackImageButton.setVisibility(View.GONE);
        binding.loginActivityToolbar.toolbarSimpleTitleTextView.setText(getString(R.string.app_name));
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        ProDia = new ProgressDialog(this);


        binding.loginButton.setOnClickListener(v -> {
            username = String.valueOf(binding.usernameEditText.getText());
            email = String.valueOf(binding.emailEditText.getText());
            password = String.valueOf(binding.passwordEditText.getText());

            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Username or Email or Password is empty!", Toast.LENGTH_LONG).show();
            } else {
                loginUser(email, password);
            }
        });

        binding.createAccountButton.setOnClickListener(v -> {
            username = String.valueOf(binding.usernameEditText.getText());
            email = String.valueOf(binding.emailEditText.getText());
            password = String.valueOf(binding.passwordEditText.getText());

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Username or Email or Password is empty!", Toast.LENGTH_LONG).show();
            } else if (password.length() < 5) {
                Toast.makeText(LoginActivity.this, "Password is too short.\nPassword must be 5 character or more than that.", Toast.LENGTH_SHORT).show();
            } else {
                CreateAccount(username, email, password);
            }
        });

        binding.changeModeLoginButton.setOnClickListener(v -> {
            binding.usernameEditText.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.loginImageView.setVisibility(View.VISIBLE);
            binding.changeModeSignUpButton.setVisibility(View.VISIBLE);

            binding.signUpImageView.setVisibility(View.INVISIBLE);
            binding.createAccountButton.setVisibility(View.GONE);
            binding.changeModeLoginButton.setVisibility(View.INVISIBLE);
        });
        binding.changeModeSignUpButton.setOnClickListener(v -> {
            binding.usernameEditText.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.GONE);
            binding.loginImageView.setVisibility(View.INVISIBLE);
            binding.changeModeSignUpButton.setVisibility(View.INVISIBLE);

            binding.signUpImageView.setVisibility(View.VISIBLE);
            binding.createAccountButton.setVisibility(View.VISIBLE);
            binding.changeModeLoginButton.setVisibility(View.VISIBLE);
        });

        //chane Status Toolbar Background to gradient
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Drawable background = getDrawable(R.drawable.blue_gradient); //bg_gradient is your gradient.
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void loginUser(String Email, String Password) {
        mAuth.signInWithEmailAndPassword(Email , Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login is Successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
                            Toast.makeText(LoginActivity.this,"Create Account is Successful!",Toast.LENGTH_LONG).show();
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
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}