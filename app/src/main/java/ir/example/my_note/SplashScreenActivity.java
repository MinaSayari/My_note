package ir.example.my_note;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;

import ir.example.my_note.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onStart() {
        super.onStart();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                }
            }

        },2000);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animation.reset();
        binding.myNoteImg.clearAnimation();
        binding.myNoteImg.startAnimation(animation);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}