package com.chefshub.base

import CustomErrorThrow
import ERROR_API
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chefshub.data.entity.NetworkState
import com.chefshub.utils.ext.setLoading
import com.chefshub.utils.ext.stopLoading
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "BaseActivity"

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    private val baseViewModel: BaseViewModel by viewModels()


    protected fun setFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        observeConnection()
        observeUnAuth()
    }

    /****** 401 response code *****/
    private fun observeUnAuth() {
        lifecycleScope.launchWhenStarted {
            baseViewModel.unAuthorizedFlow.collectLatest {
                if (it) {
                    startLogin()
                }
            }
        }
    }

    private fun observeConnection() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                baseViewModel.connectionErrorFlow.collectLatest {
//                    if (it) NoInternetDialog.show(this@BaseActivity, {})
                }
            }
        }
    }


    fun handleSharedFlow(
        userFlow: SharedFlow<NetworkState>,
        onShowProgress: (() -> Unit)? = null,
        onHideProgress: (() -> Unit)? = null,
        onSuccess: (data: Any) -> Unit,
        onError: ((th: Throwable) -> Unit)? = null
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userFlow.collect { networkState ->
                    when (networkState) {
                        is NetworkState.Success<*> -> {
                            onSuccess(networkState.data!!)
                        }
                        is NetworkState.Loading -> {
                            if (onShowProgress == null) setLoading() else onShowProgress()
                        }
                        is NetworkState.StopLoading -> {
                            if (onHideProgress == null) stopLoading() else onHideProgress()

                        }
                        is NetworkState.Error -> {
                            if (onError == null) handleErrorGeneral(networkState.throwable) else
                                onError(networkState.throwable)
                        }

                        else -> {
                        }
                    }
                }
            }
        }
    }


    fun <T> startActivity(classz: Class<T>) {
        startActivity(Intent(this, classz))
    }

    fun handleErrorGeneral(th: Throwable, func: (() -> Unit)? = null): CustomErrorThrow? {
        //Log.e("error",th.message.toString())
        th.printStackTrace()

        when (th.message) {
            ERROR_API.BAD_REQUEST -> {
                Log.d(TAG, "handleErrorGeneral: bad request")
//                ErrorDialog.show(this, getString(R.string.some_error))
            }
            ERROR_API.NOT_FOUND -> {
                Log.d(TAG, "handleErrorGeneral: not found")
//                ErrorDialog.show(this, getString(R.string.some_error))
            }
            ERROR_API.UNAUTHRIZED -> {
                Log.d(TAG, "handleErrorGeneral: not auth")
                showMessage("no AUth")
                baseViewModel.logout()
            }
            ERROR_API.UPDATE_APP->{
                showMessage(getString(R.string.update_your_app))
            }
            ERROR_API.MAINTENANCE -> {
                Log.d(TAG, "handleErrorGeneral: maintenance")
//                handleMaintenance()
            }
            ERROR_API.CONNECTION_ERROR -> {
                Log.d(TAG, "handleErrorGeneral: connection error")
//                NoInternetDialog.show(this@BaseActivity)
            }

            else -> {
                //ErrorDialog.show(this, getString(R.string.some_error))
                if (th.cause is CustomErrorThrow) {
                    val cause = th.cause as CustomErrorThrow
                    //ErrorDialog.show(this, cause.value)
                    return cause

                } else if (th.message != null) {
                    Log.d(TAG, "handleErrorGeneral: ${th.message}")
//                    ErrorDialog.show(this, th.message ?: "")
                    showMessage(th.message ?: "")
                } else
                    th.printStackTrace()
            }
        }
        return null
    }

    private fun startLogin() {
        try {
            val intent =
                Intent(this, Class.forName("com.chefshub.app.presentation.login.LoginActivity"))
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        finishAffinity()
    }


    private fun showMessage(s: String) {
        Snackbar.make(
            window.decorView.findViewById(android.R.id.content),
            s,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun observeValidation(
        validEmail: SharedFlow<Validation>,
        inputEmail: TextInputLayout
    ) {
        lifecycleScope.launchWhenStarted {
            validEmail.collectLatest {
                when (it) {
                    Validation.IS_VALID -> inputEmail.error = null
                    Validation.NOT_VALID -> inputEmail.error = getString(R.string.email_not_valid)
                    Validation.EMPTY -> inputEmail.error = getString(R.string.email_is_required)
                    Validation.IDLE -> inputEmail.error = null
                }
            }
        }
    }
}