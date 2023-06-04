package com.example.timewisefrontend.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText


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
        var name:String
        val catName:TextInputEditText= TextInputEditText(requireContext())
        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabCat)
        extendedFab.setOnClickListener{
            //TODO:PRompt for a name check list if not existing then create category
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.newCat))
                .setMessage(getString(R.string.catPro))
                .setNeutralButton(resources.getString(R.string.close)) { dialog, which ->
                    // Respond to neutral button press



                }
                .setPositiveButton(resources.getString(R.string.create)) { dialog, which ->
                    // Respond to positive button press
                    if (catName.text.isNullOrEmpty()) {

                    }
                    else
                    {
                        name=catName.text.toString()
                        if (UserDetails.categories.any { it.Name.equals(name) } )
                        {
                            catName.error="Already exists"
                        }
                        else
                        {
                            val category= Category(UserDetails.userId,null,name,null)
                            //reload native list
                        }
                    }


                }
                .setView(catName)
                .show()

        }




    }


}