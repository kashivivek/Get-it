package com.getit.getit.yes.providers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import coil.load
import com.getit.getit.yes.R
import com.getit.getit.yes.data.Categories
import com.getit.getit.yes.data.Provider
import com.getit.getit.yes.data.ProviderRepository
import com.getit.getit.yes.databinding.ActivityProviderDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ProviderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProviderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        val provider = IntentCompat.getParcelableExtra(intent, EXTRA_PROVIDER, Provider::class.java)
        if (provider == null) {
            finish()
            return
        }

        binding = ActivityProviderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.scroll) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(left = bars.left, right = bars.right, bottom = bars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        bind(provider)
    }

    private fun bind(provider: Provider) {
        val serviceType = Categories.type(provider.category, provider.type)
        binding.collapsingToolbar.title = provider.name
        val fallback = serviceType?.image ?: R.drawable.logo_image
        if (provider.imageUrl.startsWith("https://")) {
            binding.image.load(provider.imageUrl) {
                placeholder(fallback)
                error(fallback)
                crossfade(true)
            }
        } else {
            binding.image.setImageResource(fallback)
        }

        binding.type.text = listOfNotNull(
            Categories.category(provider.category)?.label,
            serviceType?.label ?: provider.type,
        ).joinToString(" › ")
        binding.phone.text = provider.phone.ifBlank { getString(R.string.no_phone) }
        showOptional(binding.addressLabel, binding.address, provider.address)
        showOptional(binding.descriptionLabel, binding.description, provider.description)

        binding.callButton.setOnClickListener { ProviderActions.call(this, provider) }
        binding.directionsButton.setOnClickListener { ProviderActions.directions(this, provider) }
        binding.reportButton.setOnClickListener { report(provider) }
    }

    private fun report(provider: Provider) {
        val reasons = resources.getStringArray(R.array.report_reasons)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.report_listing)
            .setItems(reasons) { _, which ->
                lifecycleScope.launch {
                    val ok = runCatching { ProviderRepository.report(provider.id, reasons[which]) }.isSuccess
                    Toast.makeText(
                        this@ProviderDetailActivity,
                        if (ok) R.string.report_sent else R.string.generic_error,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (ok) binding.reportButton.isEnabled = false
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showOptional(label: View, value: android.widget.TextView, text: String) {
        val visibility = if (text.isBlank()) View.GONE else View.VISIBLE
        label.visibility = visibility
        value.visibility = visibility
        value.text = text
    }

    companion object {
        private const val EXTRA_PROVIDER = "provider"

        fun intent(context: Context, provider: Provider): Intent =
            Intent(context, ProviderDetailActivity::class.java).putExtra(EXTRA_PROVIDER, provider)
    }
}
