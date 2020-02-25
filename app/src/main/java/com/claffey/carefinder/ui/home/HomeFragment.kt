package com.claffey.carefinder.ui.home


import android.app.Activity
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController

import com.claffey.carefinder.R
import com.claffey.carefinder.daos.UserDao
import com.claffey.carefinder.models.User
import com.claffey.carefinder.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val viewModel by viewModels<LoginViewModel>()

    lateinit var adminButton : Button

    var endPoint =""
    var completeEP =""
    val TAG = "testing"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = search_spinner




        spinner.onItemSelectedListener = this
        //spinner.isEnabled
        // Create an ArrayAdapter using the string array and a default spinner layout
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.endPoint_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }
        val inputText: EditText = user_inputET
        val searchBtn: Button = search_btn

        searchBtn.setOnClickListener {
            if(!endPoint.equals("/")) {
                completeEP = endPoint + inputText.text
            }
            else{
                completeEP = endPoint
            }
           Log.d(TAG,completeEP)
//
            val ep:String? = endPoint
            val content:String? = inputText.text.toString()

//
           val bundle = bundleOf("endPoint" to ep, "content" to content)
          //  view!!.findNavController().navigate(R.id.action_navigation_home_to_hospitalListFragment, bundle)
            view!!.findNavController().navigate(R.id.action_navigation_home_to_hospitalListFragment,bundle)




        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            endPoint = when(parent.getItemAtPosition(pos)){
                "All" ->  "/"
                "City" ->  "/city/"
                "State" -> "/state/"
                "Zip" -> "/zip/"
                "County"->  "/county/"
                "Name" -> "/name/"
                "Pid" -> "/pid/"
                "id" ->  "/id/"
                "Emergency" -> "/emergency/"
                else ->""

            }

            Toast.makeText(context, "$endPoint", Toast.LENGTH_SHORT).show()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback

        }



}
