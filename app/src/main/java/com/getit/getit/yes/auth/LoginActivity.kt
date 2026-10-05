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
import com.getit.getit.yes.databinding.ActivityLoginBinding
import com.getit.getit.yes.home.MainHomeActivity
import com.getit.getit.yes.util.padForSystemBars
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.padForSystemBars()

        binding.signIn.setOnClickListener { signIn() }
        binding.forgotPassword.setOnClickListener { sendReset() }
        binding.goToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun validEmail(): String? {
        val email = binding.email.text?.toString()?.trim().orEmpty()
        val ok = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        binding.emailLayout.error = if (ok) null else getString(R.string.enter_email)
        return email.takeIf { ok }
    }

    private fun signIn() {
        val email = validEmail() ?: return
        val password = binding.password.text?.toString().orEmpty()
        binding.passwordLayout.error = if (password.isEmpty()) getString(R.string.enter_password) else null
        if (password.isEmpty()) return

        setLoading(true)
        lifecycleScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                startActivity(
                    Intent(this@LoginActivity, MainHomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, R.string.login_failed, Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun sendReset() {
        val email = validEmail() ?: return
        setLoading(true)
        lifecycleScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                Toast.makeText(this@LoginActivity, getString(R.string.reset_email_sent, email), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, R.string.generic_error, Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.signIn.isEnabled = !loading
        binding.forgotPassword.isEnabled = !loading
    }
}
