package com.example.article.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.article.databinding.FragmentSendCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * <pre>
 *     author : swk
 *     time   : 2022/08/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

class SendCommentFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSendCommentBinding

    private lateinit var dialogg: Dialog

    private var mLastDiff = 0 //键盘弹出过程中最后的高度值

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogg = super.onCreateDialog(savedInstanceState)
        //设置点击外部可消失
        dialogg.setCanceledOnTouchOutside(true)

        dialogg.window?.apply {
            //配置输入法，避免弹出遮挡布局
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//        win.setWindowAnimations(R.style.Anim_Dialog_Bottom) //这里设置dialog的进出动画
        }

        return dialogg
    }


    override fun onCreateView(inflater: LayoutInflater,  container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSendCommentBinding.inflate(inflater)
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.requestLayout()

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        //R.articleID.dialog_ly这里是根布局
//        val dialogLy = binding.root
//        dialogLy.addOnLayoutChangeListener { view, i, i1, i2, i3, i4, i5, i6, i7 ->
//            val r = Rect()
//            //获取当前界面可视部分
//            dialogg.window!!.decorView.getWindowVisibleDisplayFrame(r)
//            val screenHeight = dialogg.window!!.decorView.rootView.height //获取屏幕的高度
//            val heightDifference = screenHeight - r.bottom //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//            if (heightDifference <= 0 && mLastDiff > 0) {
//                onDismiss(dialogg) //手动关闭输入法时，对话框也跟着关闭
//            }
//            mLastDiff = heightDifference
//        }
//
//    }


    override fun onStart() {
        super.onStart()
        val view: View? = view
        view?.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val mBottomSheetBehavior: BottomSheetBehavior<View> = behavior as BottomSheetBehavior
            mBottomSheetBehavior.isHideable = false //禁止下拉取消弹框
            mBottomSheetBehavior.setPeekHeight(view.measuredHeight) //让内容显示完整
        }
    }
}
