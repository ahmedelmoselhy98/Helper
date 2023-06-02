package com.chefshub.app.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chefshub.app.R
import com.chefshub.app.databinding.ActivityPrivacyBinding
import com.chefshub.app.databinding.ActivitySignupBinding
import com.chefshub.app.presentation.main.MainActivity
import com.chefshub.base.BaseActivity

class privacyActivity : BaseActivity() {
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.skip.setOnClickListener {
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}