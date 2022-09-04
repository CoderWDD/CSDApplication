package com.example.article.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.article.R
import com.example.article.ui.adapter.CommentAdapter
import com.example.article.ui.base.gsonInstance
import com.example.article.databinding.FragmentCommentBinding
import com.example.article.ui.inter.FragmentBackHandler
import com.example.article.data.entity.Article
import com.example.article.data.entity.Comment
import com.example.article.utils.BackHandlerHelper
import com.example.article.utils.WidgetController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken


/**
 *     author : swk
 *     time   : 2022/08/14
 *     desc   : 评论Fragment
 *     version: 1.0
 */


/**
 * A simple [Fragment] subclass.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentFragment: BottomSheetDialogFragment(), FragmentBackHandler {

    private var comments = ArrayList<Comment>()

    private lateinit var commentsAdapter: CommentAdapter

    private lateinit var binding: FragmentCommentBinding

//    private lateinit var sendCommentDialogFragment: SendCommentFragment

    //默认有评论
    private var hasComments = true

    override fun onBackPressed(): Boolean {
        return BackHandlerHelper.handleBackPress(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comments = gsonInstance.fromJson(it.getString(ARG_COMMENT), type)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        //设置点击外部可消失
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.apply {
            //设置使软键盘弹出的时候dialog不会被顶起
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            //设置dialog的进出动画
            this.setWindowAnimations(com.google.android.material.R.style.Animation_Design_BottomSheetDialog)
        }
        return dialog
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(inflater)

        //设置dialog高度为屏幕的2/3
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            WidgetController.getScreenWidthAndHeight(requireActivity()).second * 2 / 3
        )

        //TODO: 实现评论功能（受限于访客均被识别为统一账户API）
//        sendCommentDialogFragment = SendCommentFragment()
//        弹出输入框
//        binding.includeSendComment.editTextComment.setOnFocusChangeListener { _, _ ->
//            sendCommentDialogFragment.show(childFragmentManager, "comment")
//        }


        commentsAdapter = CommentAdapter(binding.recyclerViewComment).apply {
            updateData(comments)
        }

        //评论的recyclerview
        binding.recyclerViewComment.apply {
            //添加Android自带的分割线
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = commentsAdapter
            binding.recyclerViewComment.layoutManager =
                LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
        }

        //无评论则显示“暂无评论”
        hasComments = commentsAdapter.hasComments()

        binding.tvNoComment.visibility = if(hasComments) View.GONE else View.VISIBLE

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Snackbar
            .make(requireContext(), binding.root, "暂不支持发表评论, 请移步网页版 ~", Snackbar.LENGTH_INDEFINITE)
            .setAction("收到"){}
            .show()
    }



    companion object {
        private val type = object : TypeToken<List<Comment>>(){}.type

        private const val ARG_COMMENT = "comments"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param comments 文章的评论
         * @return A new instance of fragment CommentFragment.
         */
        @JvmStatic
        fun newInstance(comments: List<Comment>) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COMMENT, gsonInstance.toJson(comments, type))
                }
            }
    }
}