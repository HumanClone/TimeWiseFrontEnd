package com.example.timewisefrontend.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.toColor
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.UserDetails
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAnimationType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.google.android.material.color.MaterialColors
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import okhttp3.internal.toHexString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class GraphFragment : Fragment() {

    var startDate:String=""
    var endDate:String=""
    var temp:List<GraphData> = emptyList()
    private lateinit var dateStart: TextInputEditText
    private lateinit var dateEnd: TextInputEditText
    private lateinit var datelay1: TextInputLayout
    private lateinit var datelay2: TextInputLayout
    var calStart=Calendar.getInstance()
    var calEnd=Calendar.getInstance()
    private lateinit var chart:AAChartView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateStart=view.findViewById(R.id.DateFieldStart)
        dateEnd=view.findViewById(R.id.DateFieldEnd)
        datelay1=view.findViewById(R.id.DateLay1)
        datelay2=view.findViewById(R.id.DateLay2)
        chart=view.findViewById(R.id.chart_view)
        val format = SimpleDateFormat("yyyy-MM-dd")
        val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
        toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
        toolbar.title=getString(R.string.graph)
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            drawerLayout.open()
        }
        temp= UserDetails.ts.groupBy { it.date }.map { (date, list) ->
            GraphData(
                date = format.parse(date!!.substring(0,10))!!,
                dateString= date.substring(0,10),
                hours = list.sumByDouble { it.hours.toDouble() },
                max = UserDetails.max.toDouble(),
                min = UserDetails.min.toDouble()
            )
        }



        Log.d("testing",temp.toString())
        Log.d("testing",temp[0].date.toString()+"\t"+temp[0].hours.toString())

        drawGraph()


        val dpd = DatePickerDialog()
        val dpd2= DatePickerDialog()
        dateStart.setOnClickListener {
            if (!dpd.isAdded)
            {
                if (!dateEnd.text.isNullOrEmpty())
                {
                    calEnd.add(Calendar.DAY_OF_MONTH,-1)
                    dpd.maxDate=calEnd
                }
                dpd.show(parentFragmentManager,"date range")
            }
        }
        dateEnd.setOnClickListener {
            if (!dpd2.isAdded)
            {
                if (!dateStart.text.isNullOrEmpty())
                {
                    calStart.add(Calendar.DAY_OF_MONTH,+1)
                    dpd2.minDate=calStart
                }
                dpd2.show(parentFragmentManager,"date range")
            }
        }

        dpd.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String=year.toString()+"-"+(monthOfYear+1)+"-"+dayOfMonth
            calStart.set(year,monthOfYear,dayOfMonth)
            startDate=""
            startDate+= "$year-"
            startDate += if(monthOfYear<9) {
                "0"+(monthOfYear+1)+ "-"
            } else {
                (monthOfYear+1).toString() + "-"
            }
            startDate += if (dayOfMonth<10) {
                "0$dayOfMonth"
            } else {
                "$dayOfMonth"
            }

            dateStart.setText(d)
            datelay1.error=null

        }
        dpd2.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String=year.toString()+"-"+(monthOfYear+1)+"-"+dayOfMonth
            calEnd.set(year,monthOfYear,dayOfMonth)
            endDate=""
            endDate+= "$year-"
            endDate += if(monthOfYear<9) {
                "0"+(monthOfYear+1)+ "-"
            } else {
                (monthOfYear+1).toString() + "-"
            }
            endDate += if (dayOfMonth<10) {
                "0$dayOfMonth"
            } else {
                "$dayOfMonth"
            }

            dateEnd.setText(d)
            datelay2.error=null

        }
        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabgraph2)
        extendedFab.setOnClickListener {
            if (dateStart.text.isNullOrEmpty() || dateEnd.text.isNullOrEmpty() )
            {
                Toast.makeText(requireContext(),getString(R.string.enter_date),Toast.LENGTH_SHORT).show()
                if (dateStart.text.isNullOrEmpty())
                {
                    datelay1.error=getString(R.string.error_select) +" Date"
                }
                if (dateEnd.text.isNullOrEmpty())
                {
                    datelay2.error=getString(R.string.error_select) +" Date"
                }
            }
            else
            {
                if (calStart.after(calEnd))
                {
                    Toast.makeText(requireContext(),getString(R.string.date_error),Toast.LENGTH_SHORT).show()
                }
                else
                {
                    updateGraph(startDate,endDate)
                }
            }

        }

        val extendedFab2: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabgraph)
        extendedFab2.setOnClickListener {
            dateStart.text=null
            dateEnd.text=null
            dateEnd.error=null
            dateStart.error=null
            calEnd.set(2023,12,31)
            calStart.set(2023,1,1)
            dpd.maxDate=calEnd
            dpd2.minDate=calStart
            drawGraph()
        }


    }


   private fun updateGraph(start:String, end:String)
    {
        val format = SimpleDateFormat("yyyy-MM-dd")
        temp= UserDetails.ts.filter {
            format.parse(it.date!!.substring(0,10))!!.after(format.parse(start))
                    && format.parse(it.date.substring(0,10))!!.before(format.parse(end))
        }.groupBy { it.date }.map { (date, list) ->
            GraphData(
                date = format.parse(date!!.substring(0,10))!!,
                dateString= date.substring(0,10),
                hours = list.sumOf { it.hours.toDouble() },
                max = UserDetails.max.toDouble(),
                min = UserDetails.min.toDouble()
            )
        }

        val dates=temp.map { it.dateString }

        val color = MaterialColors.getColor(requireContext(),
            com.google.android.material.R.attr.backgroundColor, Color.BLACK).toColor()
        val rgb =  Color.rgb(color.red(), color.green(), color.blue())
        val hex = String.format("#%06X", 0xFFFFFF and rgb)
        val color2 = MaterialColors.getColor(requireContext(),
            android.R.attr.calendarTextColor, Color.BLACK).toColor()
        val rgb2 =  Color.rgb(color2.red(), color2.green(), color2.blue())
        val hex2 = String.format("#%06X", 0xFFFFFF and rgb2)


        val chartModel:AAChartModel = AAChartModel()
            .chartType(AAChartType.Areasplinerange)
            .title(getString(R.string.total_hours))
            .titleStyle(AAStyle().color(hex2))
            .backgroundColor(hex)
            .axesTextColor(hex2)
            .dataLabelsEnabled(true)
            .xAxisLabelsEnabled(true)
            .legendEnabled(true)
            .categories(dates.toTypedArray())
            .yAxisTitle(getString(R.string.th))
            .series(arrayOf(
                AASeriesElement()
                    .name("Max Goal")
                    .type(AAChartType.Line)
                    .data( temp.map { it.max }.toTypedArray())
                    .dataLabels(
                        AADataLabels()
                            .enabled(false)),
                AASeriesElement()
                    .name("Max Goal")
                    .type(AAChartType.Line)
                    .data( temp.map { it.min }.toTypedArray())
                    .dataLabels(
                        AADataLabels()
                            .enabled(false)),
                AASeriesElement()
                    .name("Total Hours")
                    .type(AAChartType.Areaspline)
                    .data(temp.map { it.hours }.toTypedArray())
                    .dataLabels(
                        AADataLabels()
                            .enabled(true))

                )
            )
        chart.aa_drawChartWithChartModel(chartModel)

        Log.d("testing",temp.toString())
        Log.d("testing",temp[0].date.toString()+"\t"+temp[0].hours.toString())
    }


    //Code adapted from https://github.com/AAChartModel/AAChartCore-Kotlin
    // repository is under MIT license
    // AAInfographics is a data visualization framework for Android, iOS, and the Web.
    // used to implement the graph
    private fun drawGraph()
    {
        val color = MaterialColors.getColor(requireContext(),
            com.google.android.material.R.attr.backgroundColor, Color.BLACK).toColor()
        val rgb =  Color.rgb(color.red(), color.green(), color.blue())
        val hex = String.format("#%06X", 0xFFFFFF and rgb)
        val color2 = MaterialColors.getColor(requireContext(),
            android.R.attr.calendarTextColor, Color.BLACK).toColor()
        val rgb2 =  Color.rgb(color2.red(), color2.green(), color2.blue())
        val hex2 = String.format("#%06X", 0xFFFFFF and rgb2)
        Log.d("testing",hex.toString())
        val dates=temp.map { it.dateString }
        val chartModel: AAChartModel = AAChartModel()
            .backgroundColor(hex)
            .axesTextColor(hex2)
            .titleStyle(AAStyle().color(hex2))
            .chartType(AAChartType.Line)
            .animationType(AAChartAnimationType.Elastic)
            .title(getString(R.string.total_hours))
            .dataLabelsEnabled(true)
            .xAxisLabelsEnabled(false)
            .legendEnabled(true)
            .categories(dates.toTypedArray())
            .yAxisTitle(getString(R.string.th))
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Max Goal")
                        .type(AAChartType.Line)
                        .data(temp.map { it.max }.toTypedArray())
                        .dataLabels(AADataLabels().enabled(false)),
                    AASeriesElement()
                        .name("Min Goal")
                        .type(AAChartType.Line)
                        .data(temp.map { it.min }.toTypedArray())
                        .dataLabels(AADataLabels().enabled(false)),
                    AASeriesElement()
                        .name("Total Hours")
                        .type(AAChartType.Areaspline)
                        .data(temp.map { it.hours }.toTypedArray())
                        .dataLabels(AADataLabels().enabled(true))
                )
            )

        chart.aa_drawChartWithChartModel(chartModel)
    }

}

data class GraphData(
    var date: Date,
    var dateString:String,
    var hours:Double,
    var max:Double,
    var min:Double,
)


