package com.getit.getit.yes.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.getit.getit.yes.R
import com.getit.getit.yes.account.AccountActivity
import com.getit.getit.yes.admin.AddProviderActivity
import com.getit.getit.yes.auth.LoginActivity
import com.getit.getit.yes.databinding.ActivityMainHomeBinding
import com.getit.getit.yes.providers.ProviderMapActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainHomeBinding

    private val pages = listOf(
        Page(R.string.title_home, R.drawable.ic_tab_home, null),
        Page(R.string.title_household, R.drawable.ic_tab_householdworks, R.layout.fragment_household),
        Page(R.string.title_lifestyle, R.drawable.ic_tab_lifestyle, R.layout.fragment_lifestyle),
        Page(R.string.title_groceries, R.drawable.ic_tab_groceries, R.layout.fragment_groceries_home),
        Page(R.string.title_food, R.drawable.ic_tab_food, R.layout.fragment_food_home),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            goToLogin()
            return
        }

        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(left = bars.left, right = bars.right, bottom = bars.bottom)
            insets
        }

        binding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = pages.size
            override fun createFragment(position: Int): Fragment =
                pages[position].layout?.let { SectionFragment.newInstance(it) } ?: HomeFragment()
        }
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.setIcon(pages[position].icon)
            tab.contentDescription = getString(pages[position].title)
        }.attach()
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                title = getString(pages[position].title)
            }
        })

        binding.fab.setOnClickListener { contactUs() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.pager.currentItem != 0) binding.pager.currentItem = 0 else finish()
            }
        })
    }

    /** Invoked via `android:onClick` on the category cards; the view's tag is `category:type`. */
    fun openMapsMarker(view: View) {
        val (category, type) = (view.tag as? String)?.split(":")?.takeIf { it.size == 2 } ?: return
        startActivity(
            ProviderMapActivity.intent(this, category, type, (view as? TextView)?.text?.toString().orEmpty())
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add_provider -> {
            startActivity(AddProviderActivity.intent(this)); true
        }
        R.id.action_privacy -> {
            showPrivacyPolicy(); true
        }
        R.id.action_logout -> {
            confirmLogout(); true
        }
        R.id.action_account -> {
            startActivity(Intent(this, AccountActivity::class.java)); true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun contactUs() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + getString(R.string.support_email)))
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPrivacyPolicy() {
        val webView = WebView(this).apply {
            settings.javaScriptEnabled = false
            settings.allowFileAccess = false
            loadUrl("file:///android_asset/privacypolicy.html")
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.privacy)
            .setView(webView)
            .setPositiveButton(android.R.string.ok, null)
            .show()
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

    private fun goToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    private data class Page(val title: Int, val icon: Int, val layout: Int?)
}
