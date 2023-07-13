package com.chefshub.app.presentation.main_video.my_profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentEditProfileBinding
import com.chefshub.app.presentation.login.LoginViewModel
import com.chefshub.app.presentation.main.MainViewModel
import com.chefshub.base.BaseActivity
import com.chefshub.base.BaseFragment
import com.chefshub.utils.ext.loadImage
import createDynamicLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shareDeepLink

private const val TAG = "EditProfileFragment"
@AndroidEntryPoint
class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val viewModel: LoginViewModel by viewModels()
    val PICK_IMAGE_REQUEST_CODE = 122
    var selectedImage: Bitmap? =null


    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        setupActions()
        observe ()
        observeValidations()
    }

    private fun setupActions() {

        Log.e(TAG, "setupActions: ffffffff   ${mainViewModel.getUser()?.regionalCuisines}")
        binding.toolbar.ivShare.isVisible = false
        binding.toolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.toolbar.tvTitle.setText(R.string.editProfile)

        binding.userImg.loadImage(mainViewModel.getUserProfile())
        binding.userName.text = mainViewModel.getUser()?.name
        binding.userNickName.text = "@" + mainViewModel.getUser()?.name

        binding.save.setOnClickListener {
            setUpdate()
        }

        binding.userImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

    }


    // In your onActivityResult method, handle the selected image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap?
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.e("tester", "onActivityResult: "+data.data)
            binding.userImg.setImageURI(data.data)


                    val picUri: Uri? = data.data
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(context?.contentResolver, picUri)

                    } catch (e: Exception) {
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(
                                context?.contentResolver,
                                data.data
                            )
                        } catch (ee: Exception) {
                        }
                        e.printStackTrace()
                    }
                } else {
                    selectedImage = data?.extras?.get("data") as Bitmap?
                }
            }


    fun observe() {
        handleSharedFlow(viewModel.updateProfileFlow, onSuccess = {
            Log.e("profile", "observeFlow: ${it.toString()}")
            binding.userName.text = mainViewModel.getUser()?.name
            binding.userNickName.text = "@" + mainViewModel.getUser()?.name
            binding.userImg.loadImage(mainViewModel.getUserProfile())
        })
    }

    private fun observeValidations() {
        observeValidation(viewModel.isValidEmail, binding.inputEmail)
        observeValidation(viewModel.isValidPassword, binding.textInputLayoutPassword)
        observeValidation(viewModel.isValidUserName, binding.textInputLayoutName)
        observeValidation(viewModel.isValidConfirmPassword, binding.textInputLayoutRePassword)
    }


    private fun setUpdate() {
        with(viewModel) {
            updateProfile(
                binding.edtEmail.editableText.toString(),
                binding.edtName.editableText.toString(),
                binding.edtPassword.editableText.toString(),
                binding.edtRePassword.editableText.toString(),
                if (selectedImage!= null) selectedImage else null,

//                arrayOf("low","keto")

            ).also {
                isValidEmail(binding.edtEmail.editableText.toString())
            }.also {
                isValidPassword(binding.edtPassword.editableText.toString())
            }.also {
                isValidUserName(binding.edtName.editableText.toString())
            }.also {
                isValidConfirmPassword(binding.edtPassword.editableText.toString() , binding.edtRePassword.editableText.toString())
            }
        }
    }

    private fun shareProfile(id: Int?) {

        createDynamicLink(requireActivity() as BaseActivity, id.toString()) { dynamicLink ->
            this.shareDeepLink(dynamicLink)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}


