package com.example.timewisefrontend.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.User
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class ProfileFragment : Fragment() {


    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var jobEditText: TextInputEditText
    private lateinit var minEditText: TextInputEditText
    private lateinit var maxEditText: TextInputEditText

    private lateinit var saveButton: Button
    private lateinit var profilePictureImageView: ImageView

    /*inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        usernameEditText = view.findViewById(R.id.usernameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        jobEditText = view.findViewById(R.id.jobEditText)
        minEditText = view.findViewById(R.id.minEditText)
        maxEditText = view.findViewById(R.id.maxEditText)

        usernameEditText.setText(UserDetails.name)
        emailEditText.setText(UserDetails.email)
//        jobEditText.setText(user.job)
//        minEditText.setText(user.min)
//        maxEditText.setText(user.max)
        saveButton = view.findViewById(R.id.saveButton)

        //user = User("1234", "Username", "email@example.com", "Developer", "Password123", 100.0, 0.0)

        // Update EditText fields with the user's current information
//        updateFields()

        saveButton.setOnClickListener {
            if (valid())
            {
            //user.username = usernameEditText.text.toString()
//            user.email = emailEditText.text.toString()
//            user.job = jobEditText.text.toString()
//            user.min = minEditText.text.toString().toDoubleOrNull() ?: user.min
//            user.max = maxEditText.text.toString().toDoubleOrNull() ?: user.max
//
//            // Save the User object the data source
//            saveUser(user)
            }
            else
            {
                Snackbar.make(view,getString(R.string.error_fix), Snackbar.LENGTH_LONG)
            }
//
        }

        return view
    }

    private fun valid():Boolean
    {
        var valid:Boolean=true;
        if (maxEditText.text.isNullOrEmpty())
        {
            maxEditText.error=getString(R.string.error_input)+" Max Daily Hours"
            valid=false;
        }
        else if (maxEditText.text.toString().toDouble()>20)
        {
            maxEditText.error="Max Daily Hours Cannot be Greater Than 20"
            valid=false;
        }
        else
        {
            maxEditText.error=null
        }
        if (minEditText.text.isNullOrEmpty())
        {
            minEditText.error=getString(R.string.error_input)+" Max Daily Hours"
            valid=false;
        }
        else if (minEditText.text.toString().toDouble()>0)
        {
            minEditText.error="Min Daily Hours Cannot be Less Than 0"
            valid=false;
        }
        else
        {
            minEditText.error=null
        }

        return valid
    }

//    private fun updateFields() {
//        usernameEditText.setText(user.username)
//        emailEditText.setText(user.email)
//        jobEditText.setText(user.job)
//        minEditText.setText(user.min.toString())
//        maxEditText.setText(user.max.toString())
//    }

    private fun saveUser(user: User) {
        // Implement save logic here
    }



}