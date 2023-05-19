package org.apps.storyapp.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import org.apps.storyapp.R
import org.apps.storyapp.databinding.ActivityProfileBinding
import org.apps.storyapp.ui.login.LoginActivity
import org.apps.storyapp.model.User
import org.apps.storyapp.model.UserPreference

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userPref: UserPreference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.profil)

        userPref = UserPreference(this)
        user = userPref.getUser()

        userLogout()
        setProfile()
        setLanguage()
    }

    private fun setProfile(){
        binding.nameTextView.text = user.name
        binding.messageTextView.text = user.userId
    }

    private fun setLanguage(){
        binding.btnBahasa.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun userLogout() {
        binding.logoutButton.setOnClickListener {
            userPref.setLogout()
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.anda_keluar))
                setMessage(getString(R.string.terimakasih))
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return true
    }

}