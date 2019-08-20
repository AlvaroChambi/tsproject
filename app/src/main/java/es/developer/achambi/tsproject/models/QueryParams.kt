package es.developer.achambi.tsproject.models

data class QueryParams(
    val brand : String,
    val model : String,
    val period : String,
    val gd : String,
    val cv : String,
    val cvf : String,
    val pkw : String,
    val cylinders : String,
    val cc : String ) {

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
}
