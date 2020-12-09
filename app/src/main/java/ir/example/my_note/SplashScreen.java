package ir.example.my_note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;

import ir.example.my_note.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onStart() {
        super.onStart();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                }
            }

        },2000);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animation.reset();
        binding.myNoteImg.clearAnimation();
        binding.myNoteImg.startAnimation(animation);

    }


}