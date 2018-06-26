package com.pavelprimak.seekbar24hour.model

import android.os.Parcel
import android.os.Parcelable
import android.view.View


class PositionSavedState : View.BaseSavedState {

    var position: Float = 0f

    constructor(superState: Parcelable?) : super(superState) {}

    private constructor(input: Parcel) : super(input) {
        this.position = input.readFloat()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeFloat(this.position)
    }

    companion object {

        val CREATOR: Parcelable.Creator<PositionSavedState> = object : Parcelable.Creator<PositionSavedState> {
            override fun newArray(size: Int): Array<PositionSavedState?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(input: Parcel): PositionSavedState {
                return PositionSavedState(input)
            }

        }
    }
}