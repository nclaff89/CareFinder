package com.claffey.carefinder.ui.ListView


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.claffey.carefinder.R
import com.claffey.carefinder.models.Hospital
import com.claffey.carefinder.viewModels.HospitalViewModel
import kotlinx.android.synthetic.main.fragment_hospital_list_view.*
import kotlinx.coroutines.channels.consumesAll

/**
 * A simple [Fragment] subclass.
 */
class HospitalListFragment : Fragment(R.layout.fragment_hospital_list_view) {

    private val viewModel by viewModels<HospitalViewModel>()

    val adapter = HospitalAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val queryContent = arguments?.get("content")
        val queryEndPoint = arguments?.get("endPoint")

        hospital_recycler_view.adapter = adapter
        hospital_recycler_view.layoutManager = LinearLayoutManager(hospital_recycler_view.context)


        if (queryEndPoint!! == "/"){
            viewModel.getAllHospitals().observe(this, androidx.lifecycle.Observer { hospitalList ->
                hospitalList?.let {
                    adapter.updateList(it)
                }
            })
    }
        else if(queryEndPoint!! =="/id/"){
            viewModel.getHospital(queryContent as String).observe(this, androidx.lifecycle.Observer { hospitalList ->
                hospitalList?.let {
                    adapter.updateList(it)
                }
            })

        }
        else if(queryEndPoint!! == "/city/") {
            var cityName = queryContent.toString().toUpperCase()
            viewModel.getHospitalByCity(cityName)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })
        }
        else if(queryEndPoint!! == "/state/"){
            var stateName = queryContent.toString().toUpperCase()
            viewModel.getHospitalByState(stateName)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })

        }
        else if(queryEndPoint!! == "/county/"){
            var countyName = queryContent.toString().toUpperCase()
            viewModel.getHospitalByCounty(countyName)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })
        }
        else if(queryEndPoint!! == "/name/"){
            var name = queryContent.toString().toUpperCase()
            viewModel.getHospitalByName(name)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })
        }

        else if(queryEndPoint!! == "/emergency/"){
            viewModel.getHospitalByEmergency(true)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })
        }

        else if(queryEndPoint!! == "/zip/"){
            viewModel.getHospitalByZip(queryContent as String)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })
        }

        else if(queryEndPoint!! == "/pid/"){
            viewModel.getHospitalByProviderID(queryContent as String)
                .observe(this, androidx.lifecycle.Observer { hospitalList ->
                    hospitalList?.let {
                        adapter.updateList(it)
                    }
                })
        }


    }

    class HospitalAdapter: RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>(){

        private var hospitals: List<Hospital> = listOf()

        fun updateList(newList: List<Hospital>) {
            hospitals = newList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): HospitalViewHolder {
            val childView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_hospital, parent, false)
            return HospitalViewHolder(childView)
        }


        override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {

            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            // holder.monument= monuments!!.get(position)
            holder.hospital = hospitals!![position]

        }

        override fun getItemCount(): Int {
            return hospitals!!.size
        }

        class HospitalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

            private val hospitalTextView: TextView

            init{

                itemView.setOnClickListener(this)

                hospitalTextView = itemView.findViewById(R.id.hospitalTitleTv)
            }

            private var hospitalTitle = itemView.findViewById<TextView>(R.id.hospitalTitleTv)
            private var hospitalAddress = itemView.findViewById<TextView>(R.id.hospital_addressTV)
            var hospital: Hospital? = null
                set(value) {
                    field = value
                    hospitalTitle.text = value!!.hospitalName

                    var adr = "${value.address}, ${value.city} ${value.state}, ${value.zipCode} "
                    hospitalAddress.text = adr


                }


            override fun onClick(view: View) {

            var lat = hospital?.latitude
            var lon = hospital?.longitude
            var coords = "$lat,$lon"
            val intent = Intent(Intent.ACTION_VIEW).apply{
                data = Uri.parse("geo:$coords")
            }
            //context.startActivity(intent)


            }

        }

    }






}
