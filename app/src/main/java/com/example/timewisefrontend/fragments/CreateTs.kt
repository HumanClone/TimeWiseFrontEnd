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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.Picture
import com.example.timewisefrontend.models.TimeSheet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*





class CreateTs : Fragment() {

    lateinit var imageView: ShapeableImageView
    lateinit var date:TextInputEditText
    lateinit var category:AutoCompleteTextView
    lateinit var hours:TextInputEditText
    lateinit var des:TextInputEditText
    var link:String=""
    var Pdes:String=""
    val storageRef= Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
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


        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabCS)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click
            save()
        }


        imageView=view.findViewById(R.id.ImageField)
        date =view.findViewById(R.id.DateField)
        category=view.findViewById(R.id.CatField)
        hours=view.findViewById(R.id.HoursField)
        des=view.findViewById(R.id.DesField)
        date.inputType=(InputType.TYPE_NULL)
        date.setKeyListener(null)



//        https://github.com/wdullaer/MaterialDateTimePicker
        val dpd = DatePickerDialog()
        date.setOnClickListener{

            if (!dpd.isAdded)
            {
                dpd.show(parentFragmentManager, "Datepickerdialog")
            }

        }


        val lay:TextInputLayout=view.findViewById(R.id.DateLay)
        lay.setOnClickListener {
            if(!dpd.isAdded)
            {
                dpd.show(parentFragmentManager, "Datepickerdialog")
            }
        }


       dpd.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
           val d:String =  dayOfMonth.toString() +"/"+(monthOfYear+1)+"/"+year
           date.setText(d)

       }

        imageView.setOnClickListener{

            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/*"

            // ActivityResultLauncher callback
            imagePickerActivityResult.launch(galleryIntent)


        }

        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        category.setAdapter(adapter)
        //TODO: set category right

//        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
//        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    }


    fun save()
    {
        Log.d("testing","start ")
        val formatter=SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        Log.d("testing",date.text.toString())
        val TSdate:Date=formatter.parse(date.text.toString())
        Log.d("testing",TSdate.toString())
        Log.d("testing","after date before category  ")
        val TScategory=category.text.toString()
        Log.d("testing","after category before hours ")
        val TShours:Double=hours.text.toString().toDouble()
        Log.d("testing","after hours before des ")
        val TSdes=des.text.toString()
        Log.d("testing","after des before category creation  ")
        //TODO:get id of cetgory
        val category=Category(id="", Name=TScategory, hours = null)
        Log.d("testing","category created before picture")
        //TODO:send picture object to realtime then send to time object
        if (!link.isNullOrEmpty()) {
            Log.d("testing","entered if")
            val picture = Picture(UserId = null, Description = Pdes, url = link)
            Log.d("testing","after picture before timesheet")

            val timeSheet =TimeSheet(
                category=category,
                picture = picture,
                description = TSdes,
                date=TSdate,hours=TShours
            )

            Log.d("testing", Gson().toJson(timeSheet) )
        }
        else
        {
            //TODO:Pass to database
            Log.d("testing","entered else before time object")
            val timeSheet = TimeSheet(
                category = category,
                picture = null,
                description = TSdes,
                date = TSdate,
                hours = TShours
            )
            Log.d("testing", Gson().toJson(timeSheet) )
        }

        // TODO:upon sucesss
        //parentFragmentManager.beginTransaction().replace(R.id.flContent,TimeSheetFragment()).commit()
    }



//    https://www.geeksforgeeks.org/android-upload-an-image-on-firebase-storage-with-kotlin/
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
                //TODO:Replace file with userid
                val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storageRef.child("file/$sd").downloadUrl.addOnSuccessListener {

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




}