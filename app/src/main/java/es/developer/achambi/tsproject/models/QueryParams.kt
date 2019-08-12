package es.developer.achambi.tsproject.models

import android.os.Parcel
import android.os.Parcelable

class QueryParams(
    val brand : String,
    val model : String,
    val period : String,
    val gd : String,
    val cv : String,
    val cvf : String,
    val pkw : String,
    val cylinders : String,
    val cc : String ) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    data class Builder(
            var brand : String = "",
            var model : String = "",
            var period : String = "",
            var gd : String = "",
            var cv : String = "",
            var cvf : String = "",
            var pkw : String = "",
            var cylinders : String = "",
            var cc : String = "") {
        fun brand( brand : String ) = apply{ this.brand = brand }
        fun model( model : String ) = apply { this.model = model }
        fun period( period : String ) = apply { this.period = period }
        fun gd( gd : String ) = apply { this.gd = gd }
        fun cv( cv : String ) = apply { this.cv = cv }
        fun cvf( cvf: String ) = apply { this.cvf = cvf }
        fun pkw( pkw : String ) = apply { this.pkw = pkw }
        fun cylinders( cylinders: String ) = apply { this.cylinders = cylinders }
        fun cc( cc: String ) = apply { this.cc = cc }

        fun build() = QueryParams( brand, model, period, gd, cv, cvf, pkw, cylinders, cc )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeString(period)
        parcel.writeString(gd)
        parcel.writeString(cv)
        parcel.writeString(cvf)
        parcel.writeString(pkw)
        parcel.writeString(cylinders)
        parcel.writeString(cc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QueryParams> {
        override fun createFromParcel(parcel: Parcel): QueryParams {
            return QueryParams(parcel)
        }

        override fun newArray(size: Int): Array<QueryParams?> {
            return arrayOfNulls(size)
        }
    }
}
