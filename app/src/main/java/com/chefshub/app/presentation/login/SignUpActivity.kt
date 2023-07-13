package com.chefshub.app.presentation.login

import FacebookLoginHelper
import GoogleSingingHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.chefshub.app.R
import com.chefshub.app.databinding.ActivityLoginBinding
import com.chefshub.app.databinding.ActivitySignupBinding
import com.chefshub.app.presentation.main.MainActivity
import com.chefshub.app.presentation.select_pref.PrefActivity
import com.chefshub.base.BaseActivity
import com.chefshub.data.entity.user.UserModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    private val facebookLoginHelper by lazy { FacebookLoginHelper(this) }
    private val googleLoginHelper by lazy { GoogleSingingHelper(this) }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        facebookLoginHelper.userAccountLivedata

        binding.btnLogin.setOnClickListener { setLogin() }
        binding.skip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("desc", R.id.videoFragment)
            }
            startActivity(intent)
            finishAffinity()
        }

        privacyClicked()


        binding.ivLoginFacebook.setOnClickListener { facebookLoginHelper.singIn() }
        binding.ivLoginGoogle.setOnClickListener { googleLoginHelper.singIn() }
        observeFlow()
        observeValidations()

    }

    private fun privacyClicked() {
//        val textView = findViewById<TextView>(R.id.textView)
//        val text = "Creating an account means you are okay with our terms of services and our Privacy Policy"

// Create a SpannableString with the entire text
        val spannableString = SpannableString(binding.privacy.text)

// Define the clickable spans for "terms of services" and "our Privacy Policy"
        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val intent = Intent(applicationContext, privacyActivity::class.java)
                startActivity(intent)
                // Perform the action for terms of services click
                // For example, navigate to the terms of services activity or open a web page
            }

            override fun updateDrawState(ds: TextPaint) {
                // Customize the appearance of the clickable text
                ds.isUnderlineText = true  // Remove underline
                ds.color = ContextCompat.getColor(applicationContext, R.color.green)  // Set text color
            }
        }

        val privacyClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Perform the action for Privacy Policy click
                // For example, navigate to the Privacy Policy activity or open a web page

                val intent = Intent(applicationContext, privacyActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                // Customize the appearance of the clickable text
                ds.isUnderlineText = true  // Remove underline
                ds.color = ContextCompat.getColor(applicationContext, R.color.green)  // Set text color
            }
        }

// Set the clickable spans to the respective text positions
        spannableString.setSpan(termsClickableSpan, binding.privacy.text.indexOf("terms of services"), binding.privacy.text.indexOf("terms of services") + "terms of services".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(privacyClickableSpan, binding.privacy.text.indexOf("our Privacy Policy"), binding.privacy.text.indexOf("our Privacy Policy") + "our Privacy Policy".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

// Set the modified SpannableString to the TextView
        binding.privacy.text = spannableString
        binding.privacy.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun setLogin() {
        with(viewModel) {
            signup(
                binding.edtEmail.editableText.toString(),
                binding.edtName.editableText.toString(),
                binding.edtPassword.editableText.toString(),
                binding.edtConfirmPassword.editableText.toString(),
                getDeviceID()
            ).also {
                isValidEmail(binding.edtEmail.editableText.toString())
            }.also {
                isValidPassword(binding.edtPassword.editableText.toString())
            }.also {
                isValidUserName(binding.edtName.editableText.toString())
            }.also {
                isValidConfirmPassword(binding.edtPassword.editableText.toString() , binding.edtConfirmPassword.editableText.toString())
            }
        }
    }

    private fun getDeviceID() =
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)


    private fun observeValidations() {
        observeValidation(viewModel.isValidEmail, binding.inputEmail)
        observeValidation(viewModel.isValidPassword, binding.textInputLayoutPassword)
        observeValidation(viewModel.isValidUserName, binding.inputName)
        observeValidation(viewModel.isValidConfirmPassword, binding.textInputLayoutConfirmPassword)
    }

    private fun observeFlow() {
        googleLoginHelper.userAccountLivedata.observe(this) {
            Log.e("googleLoginHelper", " googleLoginHelper " + it?.displayName + " photo "+it?.photoUrl)
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
            it.let { json ->
                //   providerName = "facebook",
                //                        providerID = json.optString("id"),
                //                        email = json.optString("email"),
                //                        name = json.optString("name"),
                //                        profileImage = "https://graph.facebook.com/${json.optString("id")}/picture?type=large"
                //
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
            //UserModel(id=4, name=User 1667631034, email=mohamed@gmail.com, avatarPath=null, foodSystems=[], regionalCuisines=[])
            Log.e(TAG, "observeFlow: ${it.toString()}")
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