package com.chefshub.app.presentation.main_video.comments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chefshub.app.R
import com.chefshub.app.databinding.FragmentCommentsBinding
import com.chefshub.base.BaseBottomSheetFragment
import com.chefshub.data.entity.comments.CommentsModel

class CommentsFragment : BaseBottomSheetFragment(R.layout.fragment_comments) {

    companion object {
        val POST_ID = "post-id"
        val NUM_COMMENTS = "num_coments"

    }

    private var postId = -1
    private var numComments = 0

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommentsViewModel by viewModels()

    private val commentsAdapter by lazy { CommentsAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCommentsBinding.bind(view)



        setupRecyclerView()

        postId = arguments?.getInt(POST_ID) ?: -1
        numComments = arguments?.getInt(NUM_COMMENTS) ?: -1

        setAction()
        getPostComments()
        setupObserver()
    }

    private fun setupObserver() {
        handleSharedFlow(viewModel.addCommentFlow, onSuccess = {
            getPostComments()
        })
        handleSharedFlow(viewModel.commentsListFlow, onSuccess = {

            Log.e("mmmmmm"," jjjj "+it)
            if (it is ArrayList<*>) {
                commentsAdapter.setAll(it as ArrayList<CommentsModel>)
//                binding.tvCommentsNumber.text =
//                    commentsAdapter.itemCount.toString() + getString(R.string.comments)
            }
        })
    }

    private fun getPostComments() {
        viewModel.getComment(postId)
    }

    private fun setAction() {

        binding.tvCommentsNumber.text= numComments.toString() +" comments"
        binding.sendMassage.setOnClickListener{
            Toast.makeText(requireContext() , "Unauthorized ", Toast.LENGTH_SHORT).show()
        }
        binding.edtComment.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                viewModel.addComment(postId, binding.edtComment.text.toString())
                binding.edtComment.setText("")
            }
            return@OnEditorActionListener true
        })

        binding.ivMention.setOnClickListener{
            binding.edtComment.setText("@")
        }
        binding.ivClose.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = commentsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}