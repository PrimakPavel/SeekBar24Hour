package com.example.pavelprimak.customviewapp

interface OnSeek24BarChangeListener {
    fun onProgressChanged(progressInPercents: Float, fromUser: Boolean)
    fun onStartTrackingTouch(progressInPercents: Float)
    fun onStopTrackingTouch(progressInPercents: Float)
}