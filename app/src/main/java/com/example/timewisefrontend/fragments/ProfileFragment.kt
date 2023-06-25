package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.User
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class ProfileFragment : Fragment() {


    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var jobEditText: TextInputEditText
    private lateinit var minEditText: TextInputEditText
    private lateinit var maxEditText: TextInputEditText
    private lateinit var progress: CircularProgressIndicator
    private lateinit var saveButton: Button
    private lateinit var profilePictureImageView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        usernameEditText = view.findViewById(R.id.usernameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        jobEditText = view.findViewById(R.id.jobEditText)
        minEditText = view.findViewById(R.id.minEditText)
        maxEditText = view.findViewById(R.id.maxEditText)
        progress =view.findViewById(R.id.progressProf)
        progress.visibility=View.GONE
        usernameEditText.setText(UserDetails.name)
        emailEditText.setText(UserDetails.email)
        jobEditText.setText(UserDetails.job)
        minEditText.setText(UserDetails.min.toString())
        maxEditText.setText(UserDetails.max.toString())
        saveButton = view.findViewById(R.id.saveButton)


        //get vaiables and set them following then updates the database after validation

        saveButton.setOnClickListener {
            progress.visibility=View.VISIBLE
            if (valid())
            {
                val user=User(
                    UserDetails.userId,
                    usernameEditText.text.toString(),
                    emailEditText.text.toString(),
                    jobEditText.text.toString(),
                    maxEditText.text.toString().toInt(),
                    minEditText.text.toString().toInt())

                updateUser(user)
                UserDetails.max=maxEditText.text.toString().toInt()
                UserDetails.min= minEditText.text.toString().toInt()
                Timer().schedule(2000) {

                    activity?.runOnUiThread(Runnable {
                        progress.visibility=View.GONE
                        Snackbar.make(view,resources.getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
                    })

                }
            }
            else
            {
                progress.visibility=View.GONE
                Snackbar.make(view,getString(R.string.error_fix), Snackbar.LENGTH_LONG).show()
            }


        }


    }

    private fun valid():Boolean
    {
        var valid:Boolean=true
        if (maxEditText.text.toString().toInt()>minEditText.text.toString().toInt())
        {
            if (maxEditText.text.isNullOrEmpty())
            {
                maxEditText.error=getString(R.string.error_input)+" Max Daily Hours"
                valid=false
            }
            else if (maxEditText.text.toString().toInt()>20)
            {
                maxEditText.error="Max Daily Hours Cannot be Greater Than 20"
                valid=false
            }
            else
            {
                maxEditText.error=null
            }
            if (minEditText.text.isNullOrEmpty())
            {
                minEditText.error=getString(R.string.error_input)+" Max Daily Hours"
                valid=false
            }
            else if (minEditText.text.toString().toInt()<0)
            {
                minEditText.error="Min Daily Hours Cannot be Less Than 0"
                valid=false
            }
            else
            {
                minEditText.error=null
            }
        }
        else
        {
            maxEditText.error="Max hours cannot be less than Min Hours"
            minEditText.error="Min hours cannot be greater tha Max hours"
        }


        return valid
    }





    private fun updateUser(user:User)
    {
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ user.toString())
        GlobalScope.launch{
            timewiseapi.editUser(UserDetails.userId,user).enqueue(
                object : Callback<User> {

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("testing", "Failure")


                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail")
                    }

                })
        }
    }


}