package com.chefshub.app.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chefshub.app.databinding.ActivityPrivacyBinding
import com.chefshub.app.databinding.ActivitySignupBinding
import com.chefshub.base.BaseActivity

class privacyActivity : BaseActivity() {
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}