package com.example.timewisefrontend.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.timewisefrontend.R


class GraphFragment : Fragment() {

    //TODO: To be implemented in part 3
//    val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
//    toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
//    toolbar.title=getString(R.string.TimeSheet)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }


}