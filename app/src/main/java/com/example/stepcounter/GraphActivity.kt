package com.example.stepcounter

import android.content.Context
import android.graphics.Color.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_graph.*


class GraphActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        createChart()
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
}
    private fun createChart(){
        val chart = findViewById<LineChart>(R.id.chart)
        chart.setTouchEnabled(false)
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(false)
        chart.setBackgroundColor(WHITE)
        chart.description.text = "Real time accelerometer data"

        val xl = chart.xAxis
        xl.textColor = WHITE
        xl.setDrawGridLines(true)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true

        val leftAxis = chart.axisLeft
        leftAxis.textColor = WHITE
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawGridLines(true)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
        chart.setDrawBorders(false)
        val set = createSet("X Axis", RED)
        val set1 = createSet("Y Axis", GREEN)
        val set2 = createSet("Z Axis", BLUE)
        val dataSets = listOf<ILineDataSet>(set, set1, set2)
        val data = LineData(dataSets)
        chart.data = data
        chart.invalidate()

    }
    private fun addEntry(event: SensorEvent){
        val data = chart.data
        val set = data.getDataSetByIndex(0)
        val set1 = data.getDataSetByIndex(1)
        val set2 = data.getDataSetByIndex(2)
        set.addEntry(Entry(set.entryCount.toFloat(), event.values[0]))
        set1.addEntry(Entry(set1.entryCount.toFloat(), event.values[1]))
        set2.addEntry(Entry(set2.entryCount.toFloat(), event.values[2]))
        data.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.moveViewToX(data.entryCount.toFloat())
        chart.setVisibleXRangeMaximum(30f)
    }
    private fun createSet(label: String, color: Int): LineDataSet {
        val set = LineDataSet(null, label)
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 3f
        set.color = color
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        set.setDrawCircles(false)
        set.setDrawValues(false)
        return set
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer,SENSOR_DELAY_NORMAL )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent) {
                addEntry(event)
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

}
