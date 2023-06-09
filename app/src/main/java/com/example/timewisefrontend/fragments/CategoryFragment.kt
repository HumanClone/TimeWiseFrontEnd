package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.CategoryAdapter
import com.example.timewisefrontend.adapters.TimeSheetAdapter
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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
        val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
        toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
        toolbar.title=getString(R.string.Category)
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            drawerLayout.open()
        }
        Log.d("testing", "onViewCreated: "+UserDetails.categories)

        var name:String
        val catName:TextInputEditText= TextInputEditText(requireContext())
        recycle=view.findViewById(R.id.category_recycler_category)
        progress =view.findViewById(R.id.progressCat)
        progress.visibility=View.GONE
        if (UserDetails.categories.isNotEmpty())
        {
            populateRecyclerViewCT(UserDetails.categories,recycle)
        }
        //button that prompts dialog and saves category in database
        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabCat)
        extendedFab.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.newCat))
                .setMessage(getString(R.string.catPro))
                .setNeutralButton(resources.getString(R.string.close)) { dialog, which ->
                    // Respond to neutral button press



                }
                .setPositiveButton(resources.getString(R.string.create)) { dialog, which ->
                    // Respond to positive button press
                    if (catName.text.isNullOrEmpty()) {
                        Snackbar.make(view,resources.getString(R.string.no_values), Snackbar.LENGTH_LONG).show()
                    }
                    else
                    {
                        Log.d("testing", "create")
                        progress.visibility=View.VISIBLE
                        name=catName.text.toString()
                        if (UserDetails.categories.any { it.Name.equals(name) } )
                        {
                            catName.error=resources.getString(R.string.exists)
                        }
                        else
                        {
                            val category= Category(UserDetails.userId,null,name,null)
                            addCat(category)
                            Timer().schedule(3000) {

                                activity?.runOnUiThread(Runnable {

                                    progress.visibility=View.GONE
                                    populateRecyclerViewCT(UserDetails.categories,recycle)
                                    Snackbar.make(view,resources.getString(R.string.saved), Snackbar.LENGTH_LONG).show()
                                })

                            }
                        }
                    }
                }
                .setView(catName)
                .show()
        }

        recycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Called when the scroll state changes (e.g., idle, dragging, settling)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    extendedFab.extend()  // Extend the FAB if scroll state is idle
                }
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    extendedFab.shrink()
                }
            }
        })

        getUserCategoriesNorm()
    }

    //adds category
    private fun addCat(category: Category)
    {
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing", "String of Object  $category")
        GlobalScope.launch{
            timewiseapi.addCategory(category).enqueue(
                object : Callback<Category> {

                    override fun onFailure(call: Call<Category>, t: Throwable) {
                        Log.d("testing", "Failure")
                        UserDetails.categories+category
                        Log.d("testing", UserDetails.categories.toString())
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

    //gets categories to update local list
    private fun getUserCategoriesNorm()
    {
        Log.d("testing","Getting all")
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {
                val call:List<Category> = timeWiseApi.getAllCatHours(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }
                UserDetails.categories=call

                Log.d("testing","Got all"+ call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewCT( UserDetails.categories, recycle)
    }

   private fun populateRecyclerViewCT(data: List<Category>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(requireContext())
       Log.d("testing", "populateRecyclerViewCT: "+data)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

       adapter.setOnClickListener(object : CategoryAdapter.OnClickListener {
           override fun onClick(position: Int, model: Category) {

              val  temp:List<TimeSheet> =UserDetails.ts.filter { it.categoryId==model.id }
               ModalView.ts=temp
               ModalView.date=model.Name
               ModalView.catName=model.Name
               ModalView.useDate=""
               val mBS = ModalBottomSheet()
               mBS.show(parentFragmentManager,ModalBottomSheet.TAG)

           }
       })

    }


}