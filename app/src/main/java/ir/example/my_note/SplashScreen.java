package ir.example.my_note;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import ir.example.my_note.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animation.reset();
        binding.myNoteImg.clearAnimation();
        binding.myNoteImg.startAnimation(animation);
    }
}