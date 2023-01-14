package com.chefshub.base

import CustomErrorThrow
import android.content.Context
import androidx.fragment.app.Fragment
import com.chefshub.data.entity.NetworkState
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseFragment(private val layout: Int) : Fragment(layout) {

    lateinit var _context: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }


    fun handleSharedFlow(
        userFlow: SharedFlow<NetworkState>,
        onShowProgress: (() -> Unit)? = null,
        onHideProgress: (() -> Unit)? = null,
        onSuccess: (data: Any) -> Unit,
        onError: ((th: Throwable) -> Unit)? = null
    ) {
        if (activity is BaseActivity?)
            (activity as BaseActivity?)?.handleSharedFlow(
                userFlow,
                onShowProgress,
                onHideProgress,
                onSuccess,
                onError
            )
    }

    protected fun handleErrorGeneral(th: Throwable, func: (() -> Unit)? = null): CustomErrorThrow? {
        return (activity as BaseActivity)?.handleErrorGeneral(th, func)
    }


}