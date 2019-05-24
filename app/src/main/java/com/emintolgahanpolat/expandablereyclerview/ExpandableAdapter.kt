package com.emintolgahanpolat.expandablereyclerview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.emintolgahanpolat.expandablelistcheckbox.CityModel
import com.emintolgahanpolat.expandablelistcheckbox.CountyModel

class ExpandableAdapter(val context: Context, var itemList: MutableList<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private val TYPE_CITY = 1
        private val TYPE_COUNTY = 2
    }


    override fun getItemViewType(position: Int): Int {

        if (itemList[position] is CityModel) {
            return TYPE_CITY
        } else if (itemList[position] is CountyModel) {
            return TYPE_COUNTY
        }
        return -1

    }

    override fun getItemCount(): Int {
        if (itemList.size > 0) {
            return itemList.size
        }
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder?
        when (viewType) {
            TYPE_CITY -> {
                val default =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_expandable_list_header, parent, false)
                viewHolder = CityViewHolder(default)
            }
            TYPE_COUNTY -> {
                val default =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_expandeble_list_child, parent, false)
                viewHolder = CountyViewHolder(default)
            }
            else -> viewHolder = null
        }


        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        when (viewType) {
            TYPE_CITY -> {
                (holder as CityViewHolder).showView(position, itemList[position] as CityModel)
            }
            TYPE_COUNTY -> {
                (holder as CountyViewHolder).showView(position, itemList[position] as CountyModel)
            }
        }
    }


    inner class CityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvSubtitle = itemView.findViewById<TextView>(R.id.tvSubtitle)
        private val ivArrow = itemView.findViewById<ImageView>(R.id.ivArrow)
        private val rvCounty = itemView.findViewById<RecyclerView>(R.id.rvCountyList)
        fun showView(position: Int, city: CityModel) {

            tvTitle.text = city.name




            var selectedCountyList = mutableListOf<CountyModel>()
            for (i in city.counties) {
                if (i.isSelected) {
                    selectedCountyList.add(i)
                }
            }




            tvSubtitle.text = printSubtitle(selectedCountyList)

            ivArrow.setImageResource(R.drawable.ic_arrow_down)
            rvCounty.visibility = View.GONE
            var isExpand = false
            itemView.setOnClickListener {
                if (isExpand) {
                    rvCounty.visibility = View.GONE
                    ivArrow.setImageResource(R.drawable.ic_arrow_down)
                    isExpand = false
                } else {
                    rvCounty.visibility = View.VISIBLE
                    ivArrow.setImageResource(R.drawable.ic_arrow_up)
                    isExpand = true
                }

            }


            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvCounty.layoutManager = layoutManager
            val expandableCountyAdapter = ExpandableAdapter(context, city.counties as MutableList<Any>)
            rvCounty.adapter = expandableCountyAdapter



            expandableCountyAdapter.setOnChecked(object : IOnCheckedListener {
                override fun onCheckedListener(position: Int) {

                    if (city.counties[position].isSelected){
                        selectedCountyList.add(city.counties[position])
                    }else{
                        selectedCountyList.remove(city.counties[position])
                    }

                    tvSubtitle.text = printSubtitle(selectedCountyList)
                }
            })


        }
    }

    private fun printSubtitle(selectedCountyList: MutableList<CountyModel>):String {
        var selectedCountiesText = ""
        if(selectedCountyList.size>2){
            for (i in 0..1){
                selectedCountiesText = "$selectedCountiesText ${selectedCountyList[i].name},"
            }
            selectedCountiesText = "$selectedCountiesText ${selectedCountyList.size-2} tane daha"
        }else{
            for (i in 0 until selectedCountyList.size){
                selectedCountiesText = "$selectedCountiesText ${selectedCountyList[i].name}"
                if(i<selectedCountyList.size-1){
                    selectedCountiesText = "$selectedCountiesText,"
                }
            }
        }
        return selectedCountiesText
    }

    inner class CountyViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val cbCounty = itemView.findViewById<CheckBox>(R.id.cbCounty)
        fun showView(position: Int, county: CountyModel) {

            tvTitle.text = "${county.name}"
            cbCounty.isChecked = county.isSelected

            itemView.setOnClickListener {
                var isCh= !cbCounty.isChecked
                cbCounty.isChecked = isCh
                county.isSelected = isCh
                onChecked.onCheckedListener(position)

            }



        }
    }


    private lateinit var onChecked: IOnCheckedListener

    interface IOnCheckedListener {
        fun onCheckedListener(position: Int)
    }

    fun setOnChecked(onChecked: IOnCheckedListener) {
        this.onChecked = onChecked
    }


}


