package org.apps.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import org.apps.storyapp.R
import org.apps.storyapp.databinding.ActivityRegisterBinding
import org.apps.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        playAnimation()
        setupButtonAction()
        handlerEditText()
        setMyButtonEnable()

        registerViewModel.isSuccess.observe(this) {
            registerSuccess(it)
        }

        registerViewModel.isFailed.observe(this) {
            registerFailed(it)
        }

        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setupButtonAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerViewModel.postRegister(name, email, password)
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }

    }

    private fun registerSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.sukses))
                setMessage(getString(R.string.berhasil))
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun registerFailed(isError: Boolean) {
        if (isError) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.gagal))
                setMessage(getString(R.string.bergagal))
                setPositiveButton(getString(R.string.coba_lagi)) { _, _ ->
                    startActivity(Intent(this@RegisterActivity, RegisterActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun setMyButtonEnable() {
        val name = binding.edRegisterName.text
        val email = binding.edRegisterEmail.text
        val pass = binding.edRegisterPassword.text
        binding.btnRegister.isEnabled =
            name.toString().isNotEmpty() && email.toString().isNotEmpty() && pass.toString().isNotEmpty()
    }

    private fun handlerEditText(){
        val edName = binding.edRegisterName
        edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })

        val edEmail = binding.edRegisterEmail
        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })

        val edPass = binding.edRegisterPassword
        edPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imgRegis, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvName = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val edName = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val lyEdName = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val lyEdEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val tvPass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val edPass = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val lyEdPass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val btnRegis = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvName, edName, lyEdName, tvEmail, edEmail, lyEdEmail, tvPass, edPass, lyEdPass, btnRegis)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}