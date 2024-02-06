package com.appsbyherman.trafikinformation

import android.os.Parcel
import android.os.Parcelable

class BaseParcelable(var value: Any) : Parcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<BaseParcelable> {

        override fun createFromParcel(parcel: Parcel): BaseParcelable {
            return BaseParcelable(parcel)
        }

        override fun newArray(size: Int): Array<BaseParcelable?> {
            return arrayOfNulls(size)
        }
    }
}