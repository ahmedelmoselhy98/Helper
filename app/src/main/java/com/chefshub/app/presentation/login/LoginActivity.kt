package com.chefshub.app.presentation.login

import FacebookLoginHelper
import GoogleSingingHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import com.chefshub.app.R
import com.chefshub.app.databinding.ActivityLoginBinding
import com.chefshub.app.presentation.main.MainActivity
import com.chefshub.app.presentation.select_pref.PrefActivity
import com.chefshub.base.BaseActivity
import com.chefshub.data.entity.user.UserModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    private val facebookLoginHelper by lazy { FacebookLoginHelper(this) }
    private val googleLoginHelper by lazy { GoogleSingingHelper(this) }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        facebookLoginHelper.userAccountLivedata

        binding.btnLogin.setOnClickListener { setLogin() }
        binding.signup.setOnClickListener {
            startActivity(SignUpActivity::class.java)
        }
        binding.skip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("desc", R.id.videoFragment)
            }
            startActivity(intent)
            finishAffinity()
        }

        binding.ivLoginFacebook.setOnClickListener { facebookLoginHelper.singIn() }
        binding.ivLoginGoogle.setOnClickListener { googleLoginHelper.singIn() }
        observeFlow()
        observeValidations()
    }

    private fun setLogin() {
        with(viewModel) {
            loginWithEmail(
                binding.edtEmail.editableText.toString(),
                binding.edtPassword.editableText.toString(),
                getDeviceID()
            ).also {
                isValidEmail(binding.edtEmail.editableText.toString())
            }.also {
                isValidPassword(binding.edtPassword.editableText.toString())
            }
        }
    }

    private fun getDeviceID() =
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)


    private fun observeValidations() {
        observeValidation(viewModel.isValidEmail, binding.inputEmail)
        observeValidation(viewModel.isValidPassword, binding.textInputLayoutPassword)
    }

    private fun observeFlow() {
        googleLoginHelper.userAccountLivedata.observe(this) {
            if (it == null) return@observe
            it.let {
                viewModel.loginWithSocial(
                    provider_id = it.id!!,
                    provider_name = "google",
                    name = it.displayName!!,
                    avatar_path = it.photoUrl.toString(),
                    device_id = getDeviceID(),
                )
            }
        }

        facebookLoginHelper.userAccountLivedata.observe(this) {
            if (it == null) return@observe
            it.let {json ->
                viewModel.loginWithSocial(
                    provider_id = json.optString("id"),
                    provider_name = "facebook",
                    name = json.optString("name"),
                    avatar_path = ("https://graph.facebook.com/${json.optString("id")}/picture?type=large")
                        ?: "",
                    device_id = getDeviceID(),
                )
            }
        }



        handleSharedFlow(viewModel.userFlow, onSuccess = {
            if (it is UserModel) {
                if (it.foodSystems.isNullOrEmpty().not() && it.regionalCuisines.isNullOrEmpty()
                        .not()
                )
                    startActivity(PrefActivity::class.java)
                else
                    startActivity(PrefActivity::class.java)
                finish()
            }
        })
    }

}