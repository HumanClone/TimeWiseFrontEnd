package com.example.timewisefrontend.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.timewisefrontend.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class CategoryFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabCat)
        extendedFab.setOnClickListener{
            //TODO:PRompt for a name check list if not existing then create category
        }




    }


}