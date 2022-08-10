package com.example.article.ui.android

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.article.R

class AndroidFragment : Fragment() {

    companion object {
        fun newInstance() = AndroidFragment()
    }

    private lateinit var viewModel: AndroidViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_android, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AndroidViewModel::class.java)
        // TODO: Use the ViewModel
    }

}