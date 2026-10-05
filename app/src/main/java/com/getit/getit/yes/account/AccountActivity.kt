package com.getit.getit.yes.account

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.getit.getit.yes.R
import com.getit.getit.yes.auth.LoginActivity
import com.getit.getit.yes.data.UserRepository
import com.getit.getit.yes.databinding.ActivityAccountBinding
import com.getit.getit.yes.databinding.DialogDeleteAccountBinding
import com.getit.getit.yes.util.ImageUtils
import com.getit.getit.yes.util.ThemePrefs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@registerForActivityResult
        runTask(R.string.photo_updated) {
            val jpeg = ImageUtils.avatarJpeg(this, uri)
            UserRepository.updatePhoto(jpeg)
            showPhoto(jpeg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.scroll) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime()
            )
            view.updatePadding(left = bars.left, right = bars.right, bottom = bars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val photoRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        binding.photo.setOnClickListener { pickPhoto.launch(photoRequest) }
        binding.changePhoto.setOnClickListener { pickPhoto.launch(photoRequest) }
        binding.removePhoto.setOnClickListener {
            runTask(R.string.photo_removed) {
                UserRepository.updatePhoto(null)
                showPhoto(null)
            }
        }
        binding.saveProfile.setOnClickListener { saveProfile() }
        binding.resetPassword.setOnClickListener {
            runTask(R.string.reset_email_sent_short) { UserRepository.sendPasswordReset() }
        }
        binding.logout.setOnClickListener { confirmLogout() }
        binding.deleteAccount.setOnClickListener { confirmDeleteAccount() }
        setUpThemeToggle()

        binding.email.setText(FirebaseAuth.getInstance().currentUser?.email)
        runTask(null) {
            UserRepository.profile()?.let { profile ->
                binding.name.setText(profile.name)
                binding.place.setText(profile.place)
                showPhoto(profile.photo)
            }
        }
    }

    private fun setUpThemeToggle() {
        val buttons = listOf(binding.themeSystem.id, binding.themeLight.id, binding.themeDark.id)
        binding.themeToggle.check(buttons[ThemePrefs.modes.indexOf(ThemePrefs.saved(this)).coerceAtLeast(0)])
        binding.themeToggle.addOnButtonCheckedListener { _, id, checked ->
            if (checked) ThemePrefs.set(this, ThemePrefs.modes[buttons.indexOf(id)])
        }
    }

    private fun showPhoto(jpeg: ByteArray?) {
        val bitmap = jpeg?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        if (bitmap != null) binding.photo.setImageBitmap(bitmap) else binding.photo.setImageResource(R.drawable.user)
        binding.removePhoto.visibility = if (bitmap != null) View.VISIBLE else View.GONE
    }

    private fun saveProfile() {
        val name = binding.name.text?.toString()?.trim().orEmpty()
        binding.nameLayout.error = if (name.isEmpty()) getString(R.string.enter_name) else null
        if (name.isEmpty()) return
        val place = binding.place.text?.toString()?.trim().orEmpty()
        runTask(R.string.profile_saved) { UserRepository.updateProfile(name, place) }
    }

    private fun runTask(successMessage: Int?, block: suspend () -> Unit) {
        setLoading(true)
        lifecycleScope.launch {
            try {
                block()
                successMessage?.let { Toast.makeText(this@AccountActivity, it, Toast.LENGTH_SHORT).show() }
            } catch (e: Exception) {
                Toast.makeText(
                    this@AccountActivity,
                    e.localizedMessage ?: getString(R.string.generic_error),
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        listOf(binding.saveProfile, binding.changePhoto, binding.removePhoto, binding.resetPassword)
            .forEach { it.isEnabled = !loading }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.logout_confirm)
            .setPositiveButton(R.string.logout) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                goToLogin()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun confirmDeleteAccount() {
        val dialogBinding = DialogDeleteAccountBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_account)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.delete, null)
            .setNegativeButton(R.string.cancel, null)
            .show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { button ->
            val password = dialogBinding.password.text?.toString().orEmpty()
            if (password.isEmpty()) {
                dialogBinding.passwordLayout.error = getString(R.string.enter_password)
                return@setOnClickListener
            }
            button.isEnabled = false
            lifecycleScope.launch {
                try {
                    UserRepository.deleteAccount(password)
                    dialog.dismiss()
                    Toast.makeText(this@AccountActivity, R.string.account_deleted, Toast.LENGTH_LONG).show()
                    goToLogin()
                } catch (e: Exception) {
                    dialogBinding.passwordLayout.error = e.localizedMessage ?: getString(R.string.generic_error)
                    button.isEnabled = true
                }
            }
        }
    }

    private fun goToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }
}
