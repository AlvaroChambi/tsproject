package es.developer.achambi.tsproject.views.presentation

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

import es.developer.achambi.coreframework.ui.presentation.SearchListData
import es.developer.achambi.tsproject.R
import es.developer.achambi.tsproject.database.model.data

data class VehiclePresentation(val id: Int, val brand: String, val model: String, val value: String,
                          val gd: String, val cv: String, val cvf: String, val pkW: String,
                          val period: String, val cc: String, val cylinders: String)
    : SearchListData, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun getViewType(): Int {
        return 0
    }

    override fun getId(): Long {
        return id.toLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeString(value)
        parcel.writeString(gd)
        parcel.writeString(cv)
        parcel.writeString(cvf)
        parcel.writeString(pkW)
        parcel.writeString(period)
        parcel.writeString(cc)
        parcel.writeString(cylinders)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VehiclePresentation> {
        override fun createFromParcel(parcel: Parcel): VehiclePresentation {
            return VehiclePresentation(parcel)
        }

        override fun newArray(size: Int): Array<VehiclePresentation?> {
            return arrayOfNulls(size)
        }
    }
}

class VehicleBuilder(private val context: Context) {
    fun build(data: data, id: Int): VehiclePresentation {
        return VehiclePresentation(
                id,
                data.marca,
                data.modelo,
                context.resources.getString(R.string.vehicle_value_label, data.valor),
                data.gD,
                data.cv,
                data.cvf,
                data.potencia,
                data.periodo,
                data.cc,
                data.cilindros
        )
    }
}
