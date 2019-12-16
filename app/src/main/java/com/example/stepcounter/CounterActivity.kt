package com.example.stepcounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.Matrix
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class CounterActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepDetector: Sensor
    private lateinit var magneticSensor: Sensor
    private lateinit var gravitySensor: Sensor
    private var gravityValues: FloatArray? = null
    private var magneticValues: FloatArray? = null
    var steps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        this.magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        this.gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent) {
        if (gravityValues != null && magneticValues != null
            && event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION
        ) {
            val deviceRelativeAcceleration = FloatArray(4)
            deviceRelativeAcceleration[0] = event.values[0]
            deviceRelativeAcceleration[1] = event.values[1]
            deviceRelativeAcceleration[2] = event.values[2]
            deviceRelativeAcceleration[3] = 0f
            val R = FloatArray(16)
            val I = FloatArray(16)
            val earthAcc = FloatArray(16)
            SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues)
            val inv = FloatArray(16)
            Matrix.invertM(inv, 0, R, 0)
            Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0)
            Log.d(
                "Acceleration",
                "Values: (" + earthAcc[0] + ", " + earthAcc[1] + ", " + earthAcc[2] + ")"
            )
        } else if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            gravityValues = event.values
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values
        } }
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

}
