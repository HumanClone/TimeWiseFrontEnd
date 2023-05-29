package com.example.timewisefrontend.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.timewisefrontend.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateTs.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTs : Fragment() {
    // TODO: Rename and change types of parameters

    private var imageUri: Uri? = null
    val pickImage=100
    lateinit var imageView: ShapeableImageView
    lateinit var date:TextInputEditText
    lateinit var category:AutoCompleteTextView
    lateinit var hours:TextInputEditText

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

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .build()

        imageView=view.findViewById(R.id.ImageField)
        date =view.findViewById(R.id.DateField)
        category=view.findViewById(R.id.CatField)
        hours=view.findViewById(R.id.HoursField)


        date.inputType=(InputType.TYPE_NULL)
        date.setKeyListener(null)

        datePicker.addOnPositiveButtonClickListener{
            date.setText(datePicker.headerText.toString())
        }


        date.setOnClickListener{
            try{datePicker.show(parentFragmentManager,"tag")
            Log.d("texting","hello")}
            catch(e:java.lang.Exception)
            {
                //TODO: Try Standard DateTime Picker https://github.com/wdullaer/MaterialDateTimePicker
            }
        }


        imageView.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        category.setAdapter(adapter)
        //TODO: set category right
        category.setOnItemClickListener { parent, view, position, id ->
            Log.d("testing","$position selected")
            Log.d("testing",category.text.toString())
        }

//        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
//        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    }
//    https://www.tutorialspoint.com/how-to-pick-an-image-from-an-image-gallery-on-android-using-kotlin/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }



    //TODO: make save method
    fun save()
    {
        val TSdate=date.text
        val TScategory=category.text.toString()
        val TShours=hours.text

    }

    fun pictureUpload()
    {

    }

}