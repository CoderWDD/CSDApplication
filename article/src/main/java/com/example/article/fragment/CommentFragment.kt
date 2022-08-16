package com.example.article.fragment

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
import com.example.article.adapter.CommentAdapter
import com.example.article.databinding.FragmentCommentBinding
import com.example.article.utils.WidgetController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 *     author : swk
 *     time   : 2022/08/14
 *     desc   : 评论Fragment
 *     version: 1.0
 */

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private lateinit var binding: FragmentCommentBinding

    private lateinit var sendCommentDialogFragment: SendCommentDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        //设置高度为屏幕的2/3
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            WidgetController.getScreenWidthAndHeight(requireActivity()).second * 2 / 3
        )

        sendCommentDialogFragment = SendCommentDialogFragment()
        //弹出输入框
        binding.includeSendComment.editTextComment.setOnClickListener{
            sendCommentDialogFragment.show(parentFragmentManager, "comment")
        }


        //TODO:实现RecyclerView的上划加载
        //评论的recyclerview
        binding.recyclerViewComment.apply {
            //添加Android自带的分割线
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = CommentAdapter(binding.recyclerViewComment)
            binding.recyclerViewComment.layoutManager =
                LinearLayoutManager(requireContext()).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
        }
        return binding.root

    }
}