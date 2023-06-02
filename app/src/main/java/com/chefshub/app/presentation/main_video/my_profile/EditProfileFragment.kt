package com.chefshub.app.presentation.main_video.my_profile

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import shareDeepLink
import java.io.IOException

@AndroidEntryPoint
class EditProfileFragment : BaseFragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val viewModel: LoginViewModel by viewModels()
    val PICK_IMAGE_REQUEST_CODE = 122
    var selectedImage: Uri? =null


    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        setupActions()
        observe ()
        observeValidations()
    }

    private fun setupActions() {
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
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
             selectedImage = data.data
            Log.e("tester", "onActivityResult: "+selectedImage )
            binding.userImg.setImageURI(selectedImage)

//            val contentResolver = requireActivity().contentResolver
//            try {
//                val inputStream = contentResolver.openInputStream(selectedImage!!)
//
//                Log.e("tester", "onActivityResult: /// "+inputStream )
//                // Read the image data from the input stream
//                // Perform the upload operation with the image data
//                // ...
//            } catch (e: IOException) {
//                e.printStackTrace()
//                // Handle any errors that occur during reading the image data
//                // ...
//            }
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
//                selectedImage!!
                if (selectedImage!= null) selectedImage.toString() else null

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


