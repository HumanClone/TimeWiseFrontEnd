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
import com.example.timewisefrontend.models.user
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
import java.text.SimpleDateFormat
import java.util.*





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
    lateinit var categories:List<Category>
    var pos:Int=-1

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
        progress.hide()

        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabCS)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click
            progress.show()
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
            else if(hours.text.toString().toDouble()>20)
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
                        Snackbar.make(view,getString(R.string.error_idk),Snackbar.LENGTH_LONG)
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


        //TODO:replace with actual list
        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        category.setAdapter(adapter)


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
        //TODO: Change on implement
        val TScategory=category.text.toString()
        //val TScategory=categories[pos].id
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
            val picture = Picture(UserId = user.userId, Description = Pdes, url = link)
            Log.d("testing","after picture before timesheet")

            val timeSheet =TimeSheet(
                userId=user.userId,
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
                userId=user.userId,
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

    fun String.isNumber():Boolean
    {
        val pattern=Regex("\\d+(\\.\\d+)*")
        return matches(pattern)

    }


}