package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.CategoryAdapter
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.User
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class CategoryFragment : Fragment() {


    private lateinit var progress: CircularProgressIndicator
    private lateinit var recycle:RecyclerView
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
        recycle=view.findViewById(R.id.category_recycler_category)
        progress =view.findViewById(R.id.progressCat)
        progress.visibility=View.GONE
        if (!UserDetails.categories.isEmpty())
        {
            populateRecyclerViewCT(UserDetails.categories,recycle)
        }
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
                        progress.visibility=View.VISIBLE
                        name=catName.text.toString()
                        if (UserDetails.categories.any { it.Name.equals(name) } )
                        {
                            catName.error="Already exists"
                        }
                        else
                        {
                            val category= Category(UserDetails.userId,null,name,null)
                            addCat(category)
                            Timer().schedule(4000) {

                                activity?.runOnUiThread(Runnable {
                                    populateRecyclerViewCT(UserDetails.categories,recycle)
                                    progress.visibility=View.GONE
                                })

                            }
                        }
                    }
                }
                .setView(catName)
                .show()
        }
    }

    private fun addCat(category: Category)
    {
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ category.toString())
        GlobalScope.launch{
            timewiseapi.addCategory(category).enqueue(
                object : Callback<Category> {

                    override fun onFailure(call: Call<Category>, t: Throwable) {
                        Log.d("testing", "Failure")
                        UserDetails.categories+category
                        getUserCategoriesNorm()
                    }

                    override fun onResponse(call: Call<Category>, response: Response<Category>) {
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

    private fun getUserCategoriesNorm()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {
                val call:List<Category> = timeWiseApi.getAllCategoriesNorm(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }
                UserDetails.categories=call

                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewCT( UserDetails.categories, recycle)
    }

    fun populateRecyclerViewCT(data: List<Category>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

    }
}