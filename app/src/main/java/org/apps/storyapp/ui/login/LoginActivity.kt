package org.apps.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import com.airbnb.lottie.LottieAnimationView
import org.apps.storyapp.R
import org.apps.storyapp.ui.register.RegisterActivity
import org.apps.storyapp.databinding.ActivityLoginBinding
import org.apps.storyapp.model.User
import org.apps.storyapp.model.UserPreference
import org.apps.storyapp.network.response.LoginResponse
import org.apps.storyapp.ui.story.StoryActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var userPref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        playAnimation()
        setupButtonAction()
        setMyButtonEnable()
        handlerEditText()

        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }

        loginViewModel.loginResponse.observe(this){
            loginSuccess(it)
        }

        loginViewModel.isFailed.observe(this){
            loginFailed(it)
        }
    }

    private fun setMyButtonEnable() {
        val email = binding.edLoginEmail.text
        val pass = binding.edLoginPassword.text
        val isPassValid = pass.toString().length >= 8

        binding.btnLogin.isEnabled = email.toString().isNotEmpty() && isPassValid
    }

    private fun handlerEditText(){
        val edEmail = binding.edLoginEmail
        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })

        val edPass = binding.edLoginPassword
        edPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }


    private fun loginSuccess(loginResponse: LoginResponse) {
        userPref = UserPreference(this)
        val loginResult = loginResponse.loginResult

        if (!loginResponse.error) {
            saveLogin(loginResponse)
            showLoading(true)
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.sukses))
                setMessage("Hello ${loginResult.name}")
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun loginFailed(isError: Boolean) {
        if (isError) {
            showLoading(true)
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.gagal))
                setMessage(getString(R.string.gagal_masuk))
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun saveLogin(login: LoginResponse) {
        userPref = UserPreference(this)
        val loginResult = login.loginResult
        val saveUser = User(
            loginResult.userId, loginResult.name, loginResult.token
        )
        userPref.setLogin(saveUser)
    }

    private fun playAnimation(){
        val imgSelfie: ImageView = findViewById(R.id.img_selfie)

        ObjectAnimator.ofFloat(imgSelfie, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val lyEdEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val tvPass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val edPass = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val lyEdPass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvEmail, edEmail, lyEdEmail, tvPass, edPass, lyEdPass, btnLogin)
            start()
        }
    }

    private fun setupButtonAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.postLogin(email, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }

    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}