package com.example.timewisefrontend.fragments

import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.timewisefrontend.R
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.Picture
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class CreateTs : Fragment() {

    lateinit var imageView: ShapeableImageView
    lateinit var date:TextInputEditText
    lateinit var category:AutoCompleteTextView
    lateinit var hours:TextInputEditText
    lateinit var des:TextInputEditText
    lateinit var datelay:TextInputLayout
    lateinit var catlay:TextInputLayout
    lateinit var hourlay:TextInputLayout
    lateinit var deslay:TextInputLayout
    lateinit var progress: CircularProgressIndicator
    lateinit var startDate:String
    var pos:Int=-1

    var link:String=""
    val storageRef= Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_create_ts, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assigning variables
        datelay=view.findViewById(R.id.DateLay)
        catlay=view.findViewById(R.id.CateLay)
        hourlay=view.findViewById(R.id.HourLay)
        deslay=view.findViewById(R.id.DesLay)
        progress=view.findViewById(R.id.progressTSC)
        imageView=view.findViewById(R.id.ImageField)
        date =view.findViewById(R.id.DateField)
        category=view.findViewById(R.id.CatField)
        hours=view.findViewById(R.id.HoursField)
        des=view.findViewById(R.id.DesField)
        date.inputType=(InputType.TYPE_NULL)
        date.setKeyListener(null)
        progress.visibility=View.GONE

        //validation sand setting onclick listeners

        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabCS)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click
            progress.visibility=View.VISIBLE
            var empty:Boolean=false
            var incorrect:Boolean=false


            if (category.text.isNullOrEmpty())
            {
                catlay.error=getString(R.string.error_select)+" Category"
                empty=true
            }
            else
            {
                catlay.error=null
            }

            if (hours.text.isNullOrEmpty())
            {
                hourlay.error=getString(R.string.error_input)+"Number of Hours "
                empty=true
                Log.d("testing","Got here")
            }
            else if(!hours.text.toString().isNumber())
            {
                hourlay.error=getString(R.string.error_numbers)
                incorrect=true

            }
            else if(hours.text.toString().toInt()>20)
            {
                hourlay.error=getString(R.string.error_impossible)
                incorrect=true
            }
            else
            {
                hourlay.error=null
            }

            if (des.text.isNullOrEmpty())
            {
                deslay.error=getString(R.string .error_input)+" A Description"
                empty=true
            }
            else
            {
                deslay.error=null
            }

            if (date.text.isNullOrEmpty())
            {
                datelay.error=getString(R.string.error_select)+" Date"
                empty=true
            }
            else
            {
                datelay.error=null
            }
            if (!empty)
            {
                if (incorrect)
                {
                    Snackbar.make(view,getString(R.string.error_fix), Snackbar.LENGTH_LONG)
                        .show()
                }
                else
                {

                    try
                    {
                        save()
                    }
                    catch (e:Exception)
                    {
                        Snackbar.make(view,"Please ensure hours is a whole number",Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
            else
            {
                Snackbar.make(view,getString(R.string.error_input)+" All Missing Values", Snackbar.LENGTH_LONG)
                    .show()
            }

            progress.hide()
        }




//        https://github.com/wdullaer/MaterialDateTimePicker
        // code atributed
        //used thier very own made date picker because it gave access to selection better that google native one
        val dpd = DatePickerDialog()
        date.setOnClickListener{

            if (!dpd.isAdded)
            {
                dpd.show(parentFragmentManager, "Datepickerdialog")
            }

        }


        datelay.setOnClickListener {
            if(!dpd.isAdded)
            {
                dpd.show(parentFragmentManager, "Datepickerdialog")
            }
        }


        dpd.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String =  dayOfMonth.toString() +"/"+(monthOfYear+1)+"/"+year
            date.setText(d)
            startDate=year.toString()+"-"
            if(monthOfYear<9)
            {
                startDate+="0"+(monthOfYear+1)+ "-"
            }
            else
            {
                startDate+=(monthOfYear+1).toString() + "-"
            }
            if (dayOfMonth<10)
            {
                startDate+="0"+dayOfMonth.toString()
            }
            else
            {
                startDate+=dayOfMonth.toString()
            }
            startDate+="T10:28:51.449943+00:00"
            datelay.error=null

        }

        imageView.setOnClickListener{

            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/*"

            // ActivityResultLauncher callback
            imagePickerActivityResult.launch(galleryIntent)


        }
        category.setOnItemClickListener { parent, view, position, id ->
            catlay.error=null
            pos=position
        }


        val sub=UserDetails.categories.map{it.Name }
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item,sub)
        category.setAdapter(adapter)





    }


    fun save()
    {
        progress.visibility=View.VISIBLE

        Log.d("testing","start ")
        Log.d("testing",startDate)
        Log.d("testing","after date before category  ")
        val TScategory=UserDetails.categories[pos].id
        Log.d("testing",TScategory!!)
        Log.d("testing","after category before hours ")
        val TShours:Int=hours.text.toString().toInt()
        Log.d("testing","after hours before des ")
        val TSdes=des.text.toString()
        Log.d("testing","descript before picture")
        if (!link.isNullOrEmpty()) {
            Log.d("testing","entered if")
            val picture = Picture(null,UserDetails.userId,link)
            addPicture(picture)
            Log.d("testing","after picture before timesheet")
            val timeSheet =TimeSheet(
                userId=UserDetails.userId,
                categoryId=TScategory,
                pictureId = link,
                description = TSdes,
                date=startDate
                ,hours=TShours
            )

            Log.d("testing", Gson().toJson(timeSheet) )
            addTimesheet(timeSheet)
            Timer().schedule(2000) {

                activity?.runOnUiThread(Runnable {
                    parentFragmentManager.beginTransaction().replace(R.id.flContent,TimeSheetFragment()).commit()
                })
            }

        }
        else
        {
            //TODO:Pass to database
            Log.d("testing","entered else before time object")
            val timeSheet = TimeSheet(
                userId=UserDetails.userId,
                categoryId = TScategory,
                pictureId = null,
                description = TSdes,
                date = startDate,
                hours = TShours
            )
            Log.d("testing", Gson().toJson(timeSheet) )
            addTimesheet(timeSheet)
            Timer().schedule(2000) {

                   activity?.runOnUiThread(Runnable {
               parentFragmentManager.beginTransaction().replace(R.id.flContent,TimeSheetFragment()).commit()
                })
           }
        }

    }


    //Code attributed
    //https://www.geeksforgeeks.org/android-upload-an-image-on-firebase-storage-with-kotlin/
    //used as a reference to add image file to firebase
    //changed for all formats and t also get download link
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection

        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_CANCELED  ) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data
                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(requireContext(), imageUri!!)

                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageRef.child("${UserDetails.userId}/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storageRef.child("${UserDetails.userId}/$sd").downloadUrl.addOnSuccessListener {

                        Glide.with(this@CreateTs)
                            .load(it)
                            .into(imageView)
                        link=it.toString()
                        Log.d("testing",link)
                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }





    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

    //simple method fed into the string class
    fun String.isNumber():Boolean
    {
        val pattern=Regex("\\d+(\\.\\d+)*")
        return matches(pattern)

    }

    private fun addPicture(pic: Picture)
    {
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ pic.toString())
        GlobalScope.launch{
            timewiseapi.addPic(pic).enqueue(
                object : Callback<Picture> {

                    override fun onFailure(call: Call<Picture>, t: Throwable) {
                        Log.d("testing", "Failure")
                    }

                    override fun onResponse(call: Call<Picture>, response: Response<Picture>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail pic")
                    }

                })
        }
    }

    private fun addTimesheet(ts:TimeSheet)
    {
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ ts.toString())
        Log.d("testing",Gson().toJson(ts))
        GlobalScope.launch{
            timewiseapi.addTS(ts).enqueue(
                object : Callback<TimeSheet> {

                    override fun onFailure(call: Call<TimeSheet>, t: Throwable) {
                        UserDetails.ts+ts
                        Log.d("testing", "Failure")
                        getTS()
                        getUserCatHours()
                    }

                    override fun onResponse(call: Call<TimeSheet>, response: Response<TimeSheet>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail ts")
                    }

                })
        }
    }

    private fun getTS()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:List<TimeSheet> = timeWiseApi.getAllTimesheets(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }

                Log.d("testing", call.toString())
                UserDetails.ts=call

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
    }

    private fun getUserCatHours()
    {
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

                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
    }
    

}