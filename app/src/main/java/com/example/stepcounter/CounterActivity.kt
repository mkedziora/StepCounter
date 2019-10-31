package com.example.stepcounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        var steps = 0
        stepBtn.setOnClickListener {
            steps++
            textView2.text = steps.toString()
        }
    }
}
