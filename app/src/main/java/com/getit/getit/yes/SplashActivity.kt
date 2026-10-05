package com.getit.getit.yes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.getit.getit.yes.auth.LoginActivity
import com.getit.getit.yes.home.MainHomeActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val target = if (FirebaseAuth.getInstance().currentUser != null) {
            MainHomeActivity::class.java
        } else {
            LoginActivity::class.java
        }
        startActivity(Intent(this, target))
        finish()
    }
}
