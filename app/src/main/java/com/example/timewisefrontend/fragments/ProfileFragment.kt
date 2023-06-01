package com.example.timewisefrontend.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.timewisefrontend.R
import com.example.timewisefrontend.fragments.User


class ProfileFragment : Fragment() {

    private lateinit var user: User

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var jobEditText: EditText
    private lateinit var minEditText: EditText
    private lateinit var maxEditText: EditText

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

        saveButton = view.findViewById(R.id.saveButton)
        profilePictureImageView = view.findViewById(R.id.profilePictureImageView)

        user = User("1234", "Username", "email@example.com", "Developer", "Password123", 100.0, 0.0)

        // Update EditText fields with the user's current information
        updateFields()

        saveButton.setOnClickListener {
            user.username = usernameEditText.text.toString()
            user.email = emailEditText.text.toString()
            user.job = jobEditText.text.toString()
            user.min = minEditText.text.toString().toDoubleOrNull() ?: user.min
            user.max = maxEditText.text.toString().toDoubleOrNull() ?: user.max

            // Save the User object the data source
            saveUser(user)
        }

        return view
    }

    private fun updateFields() {
        usernameEditText.setText(user.username)
        emailEditText.setText(user.email)
        jobEditText.setText(user.job)
        minEditText.setText(user.min.toString())
        maxEditText.setText(user.max.toString())
    }

    private fun saveUser(user: User) {
        // Implement save logic here
    }
}