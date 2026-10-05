package com.getit.getit.yes.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.getit.getit.yes.R
import com.getit.getit.yes.data.UserRepository
import com.getit.getit.yes.databinding.ActivitySignupBinding
import com.getit.getit.yes.home.MainHomeActivity
import com.getit.getit.yes.util.padForSystemBars
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.padForSystemBars()

        binding.signUp.setOnClickListener { signUp() }
        binding.goToLogin.setOnClickListener { finish() }
    }

    private fun signUp() {
        val name = binding.name.text?.toString()?.trim().orEmpty()
        val place = binding.place.text?.toString()?.trim().orEmpty()
        val email = binding.email.text?.toString()?.trim().orEmpty()
        val password = binding.password.text?.toString().orEmpty()

        binding.nameLayout.error = if (name.isEmpty()) getString(R.string.enter_name) else null
        val emailOk = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        binding.emailLayout.error = if (emailOk) null else getString(R.string.enter_email)
        binding.passwordLayout.error = if (password.length < 6) getString(R.string.password_too_short) else null
        if (name.isEmpty() || !emailOk || password.length < 6) return

        setLoading(true)
        lifecycleScope.launch {
            try {
                UserRepository.signUp(email, password, name, place)
                startActivity(
                    Intent(this@SignupActivity, MainHomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } catch (e: Exception) {
                val reason = e.localizedMessage ?: getString(R.string.generic_error)
                Toast.makeText(this@SignupActivity, getString(R.string.signup_failed, reason), Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.signUp.isEnabled = !loading
    }
}
