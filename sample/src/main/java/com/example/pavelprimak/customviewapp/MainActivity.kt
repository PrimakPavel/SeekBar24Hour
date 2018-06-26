package com.example.pavelprimak.customviewapp

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.pavelprimak.seekbar24hour.OnSeek24BarChangeListener
import com.pavelprimak.seekbar24hour.customView.SeekBar24HourView
import com.pavelprimak.seekbar24hour.model.Event

class MainActivity : AppCompatActivity(), OnSeek24BarChangeListener {

    companion object {
        const val TAG = "Sample"
    }

    private val seekBarView: SeekBar24HourView by lazy {
        findViewById<SeekBar24HourView>(R.id.seek_bar)
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


    private val btnAddEvent: Button by lazy {
        findViewById<Button>(R.id.btn_draw)
    }
    private val mainEventsList = ArrayList<Event>()
    private val markEventsList = ArrayList<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainEventsList.clear()
        markEventsList.clear()

        //Create main events list. Add event in SECONDS
        mainEventsList.add(Event(3600, 3600))
        val lineGraphView = seekBarView.lineGraphView
        lineGraphView?.setMainEventList(mainEventsList)
        //Change CENTER MARKER drawable
        seekBarView.setCursorDrawable(ContextCompat.getDrawable(this, R.drawable.marker_center)!!)

        btnAddEvent.setOnClickListener({
            addEventBtnClick()
        })
    }

    private fun addEventBtnClick() {
        val lineGraphView = seekBarView.lineGraphView
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
        } else {
            markEventsList.add(Event(startPosition, duration))
            lineGraphView?.setMarkEventList(markEventsList)
        }

    }

    override fun onProgressChanged(progressInPercents: Float, fromUser: Boolean) {
        Log.d(TAG, "onProgressChanged. ProgressInPercents=$progressInPercents. IsFromUser =$fromUser")
    }

    override fun onStartTrackingTouch(progressInPercents: Float) {
        Log.d(TAG, "onStartTrackingTouch. ProgressInPercents=$progressInPercents")
    }

    override fun onStopTrackingTouch(progressInPercents: Float) {
        Log.d(TAG, "onStopTrackingTouch. ProgressInPercents=$progressInPercents")
    }
}
