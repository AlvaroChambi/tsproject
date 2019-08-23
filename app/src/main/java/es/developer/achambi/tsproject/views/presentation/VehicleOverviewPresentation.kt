package es.developer.achambi.tsproject.views.presentation

import android.os.Parcel
import android.os.Parcelable
import es.developer.achambi.coreframework.ui.presentation.SearchListData

data class VehicleOverviewPresentation(val vehicle: VehiclePresentation, val percent: String,
                                  val year: String, val depreciationValue: String,
                                  val percentIncreaseValue: String) : SearchListData, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(VehiclePresentation::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun getViewType(): Int {
        return 0
    }

    override fun getId(): Long {
        return vehicle.getId()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(vehicle, flags)
        parcel.writeString(percent)
        parcel.writeString(year)
        parcel.writeString(depreciationValue)
        parcel.writeString(percentIncreaseValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VehicleOverviewPresentation> {
        override fun createFromParcel(parcel: Parcel): VehicleOverviewPresentation {
            return VehicleOverviewPresentation(parcel)
        }

        override fun newArray(size: Int): Array<VehicleOverviewPresentation?> {
            return arrayOfNulls(size)
        }
    }
}


