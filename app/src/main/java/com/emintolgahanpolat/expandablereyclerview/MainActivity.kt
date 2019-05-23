package com.emintolgahanpolat.expandablereyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.emintolgahanpolat.expandablelistcheckbox.CityModel
import com.emintolgahanpolat.expandablelistcheckbox.CountyModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCityList.layoutManager=layoutManager
        val expandableAdapter=ExpandableAdapter(this,createDummyData(15,5) as MutableList<Any>)
        rvCityList.adapter=expandableAdapter
    }
    var cityList= mutableListOf<CityModel>()

    private fun createDummyData(c0: Int, c1: Int) :MutableList<CityModel> {

        for (i in 0..c0){
            var selectedCountyList= mutableListOf<CountyModel>()
            var countyList= mutableListOf<CountyModel>()
            for (j in 0..c1){
                countyList.add(CountyModel("$i $j","$i $j County",false))
            }
            cityList.add(CityModel("$i","$i City",countyList))
        }
        return cityList
    }
}
