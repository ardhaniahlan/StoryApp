package org.apps.storyapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import org.apps.storyapp.databinding.ActivitySplashScreenBinding
import org.apps.storyapp.ui.login.LoginActivity
import org.apps.storyapp.model.User
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.ui.story.StoryActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userPreference: UserPreference
    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        userPreference = UserPreference(this)
        user = userPreference.getUser()
        sessionUser()
        playAnimation()
    }

    private fun navigate(intent: Intent){
        val delayMillis = 3000L
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, delayMillis)
    }

    private fun sessionUser() {
        if (user.userId != null && user.name != null && user.token != null) {
            navigate(Intent(this, StoryActivity::class.java))
        } else {
            navigate(Intent(this, LoginActivity::class.java))
        }
    }

    private fun playAnimation(){
        val imgSelfie = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply{
            playTogether(imgSelfie, tvName)
            start()
        }
    }

}