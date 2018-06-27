package com.example.pavelprimak.customviewapp

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.pavelprimak.seekbar24hour.OnSeek24BarChangeListener
import com.pavelprimak.seekbar24hour.customView.LineGraphView
import com.pavelprimak.seekbar24hour.customView.LineGraphView.Companion.MIN_IN_HOUR
import com.pavelprimak.seekbar24hour.customView.LineGraphView.Companion.SEC_IN_MIN
import com.pavelprimak.seekbar24hour.customView.SeekBar24HourView
import com.pavelprimak.seekbar24hour.model.Event

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Sample"
    }

    private val seekBarView: SeekBar24HourView by lazy {
        findViewById<SeekBar24HourView>(R.id.seek_bar)
    }
    private val seekBarView2: SeekBar24HourView by lazy {
        findViewById<SeekBar24HourView>(R.id.seek_bar2)
    }
    private val eventStartPosition: EditText by lazy {
        findViewById<EditText>(R.id.start_position_input)
    }
    private val eventDuration: EditText by lazy {
        findViewById<EditText>(R.id.duration_input)
    }
    private val eventTypeSpinner: Spinner by lazy {
        findViewById<Spinner>(R.id.event_type_spinner)
    }

    private val positionLabel: TextView by lazy {
        findViewById<TextView>(R.id.position_label)
    }

    private val positionLabel2: TextView by lazy {
        findViewById<TextView>(R.id.position_label2)
    }


    private val btnAddEvent: Button by lazy {
        findViewById<Button>(R.id.btn_draw)
    }
    private val mainEventsList = ArrayList<Event>()
    private val markEventsList = ArrayList<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBarView.setOnSeek24BarChangeListener(object : OnSeek24BarChangeListener {
            override fun onProgressChanged(progressInPercents: Float, fromUser: Boolean) {
                Log.d(TAG, "onProgressChanged. ProgressInPercents=$progressInPercents. IsFromUser =$fromUser")
            }

            override fun onStartTrackingTouch(progressInPercents: Float) {
                Log.d(TAG, "onStartTrackingTouch. ProgressInPercents=$progressInPercents")
            }

            override fun onStopTrackingTouch(progressInPercents: Float) {
                updateLabelPosition(progressInPercents, positionLabel)
                Log.d(TAG, "onStopTrackingTouch. ProgressInPercents=$progressInPercents")
            }
        })


        seekBarView2.setOnSeek24BarChangeListener(object : OnSeek24BarChangeListener {
            override fun onProgressChanged(progressInPercents: Float, fromUser: Boolean) {
                Log.d(TAG, "onProgressChanged. ProgressInPercents=$progressInPercents. IsFromUser =$fromUser")
            }

            override fun onStartTrackingTouch(progressInPercents: Float) {
                Log.d(TAG, "onStartTrackingTouch. ProgressInPercents=$progressInPercents")
            }

            override fun onStopTrackingTouch(progressInPercents: Float) {
                updateLabelPosition(progressInPercents, positionLabel2)
                Log.d(TAG, "onStopTrackingTouch. ProgressInPercents=$progressInPercents")
            }
        })
        mainEventsList.clear()
        markEventsList.clear()

        //Create main events list. Add event in SECONDS
        mainEventsList.add(Event(3600, 3600))
        val lineGraphView = seekBarView.lineGraphView
        val lineGraphView2 = seekBarView2.lineGraphView
        lineGraphView?.setMainEventList(mainEventsList)
        lineGraphView2?.setMainEventList(mainEventsList)
        //Change CENTER MARKER drawable
        seekBarView.setCursorDrawable(ContextCompat.getDrawable(this, R.drawable.marker_center)!!)

        btnAddEvent.setOnClickListener({
            addEventBtnClick()
        })
    }

    override fun onResume() {
        updateLabelPosition(seekBarView.percents, positionLabel)
        updateLabelPosition(seekBarView2.percents, positionLabel2)
        super.onResume()
    }

    private fun updateLabelPosition(progressInPercents: Float, textView: TextView) {
        val progressInSec = (progressInPercents * LineGraphView.WIDTH_SECONDS_GRAPH / 100).toInt()
        val min = progressInSec / SEC_IN_MIN
        val hour = min / MIN_IN_HOUR
        textView.text = String.format("%02d:%02d:%02d", hour, min % MIN_IN_HOUR, progressInSec % SEC_IN_MIN)
    }

    private fun addEventBtnClick() {
        val lineGraphView = seekBarView.lineGraphView
        val lineGraphView2 = seekBarView2.lineGraphView
        var startPosition: Int = 0
        var duration: Int = 0
        try {
            startPosition = eventStartPosition.text?.trim()?.toString()?.toInt() ?: 0
            duration = eventDuration.text?.trim()?.toString()?.toInt() ?: 0
        } catch (e: Exception) {
            Toast.makeText(this, "Wrong input data!", Toast.LENGTH_LONG).show()
        }
        if (eventTypeSpinner.selectedItemPosition == 0) {
            mainEventsList.add(Event(startPosition, duration))
            lineGraphView?.setMainEventList(mainEventsList)
            lineGraphView2?.setMainEventList(mainEventsList)
        } else {
            markEventsList.add(Event(startPosition, duration))
            lineGraphView?.setMarkEventList(markEventsList)
            lineGraphView2?.setMarkEventList(markEventsList)
        }

    }
}
