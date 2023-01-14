package com.chefshub.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chefshub.data.entity.NetworkState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow

@AndroidEntryPoint
abstract class BaseBottomSheetFragment(private val layout: Int) : BottomSheetDialogFragment() {
    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

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

}