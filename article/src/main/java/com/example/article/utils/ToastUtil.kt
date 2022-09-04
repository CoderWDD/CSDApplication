package com.example.article.utils
import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object {
        private var toast: Toast? = null

        /**默认为short*/
        fun makeText(context: Context, text: String){
            toast?.cancel()
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            //记得调用show(), 否则不显示
            toast?.show()
        }
        /**可以设置为长*/
        fun makeText(context: Context, text: String, duration: Int){
            toast?.cancel()
            toast = Toast.makeText(context, text, duration)
            //记得调用show(), 否则不显示
            toast?.show()
        }
    }
}