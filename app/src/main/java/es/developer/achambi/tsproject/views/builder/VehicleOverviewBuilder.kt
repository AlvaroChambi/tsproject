package es.developer.achambi.tsproject.views.builder

import android.content.Context
import es.developer.achambi.tsproject.R
import es.developer.achambi.tsproject.models.VehicleOverview
import es.developer.achambi.tsproject.views.presentation.VehicleBuilder
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation
import java.math.BigDecimal
import java.util.ArrayList

class VehicleOverviewBuilder(private val context: Context,
                             private val vehicleBuilder: VehicleBuilder) {
    companion object {
        private const val VALUE_DECIMAL_PLACE = 2
    }

    fun build(vehicleOverviews: ArrayList<VehicleOverview>): ArrayList<VehicleOverviewPresentation> {
        val presentations = ArrayList<VehicleOverviewPresentation>()
        for (i in vehicleOverviews.indices) {
            val overview = vehicleOverviews[i]
            presentations.add(build(overview, i))
        }
        return presentations
    }

    private fun build(vehicleOverview: VehicleOverview, id: Int): VehicleOverviewPresentation {
        return VehicleOverviewPresentation(
                vehicleBuilder.build(vehicleOverview.vehicle, id),
                buildPercentString(VehicleOverview.TAX_VALUE),
                buildYearString(vehicleOverview.year),
                buildValue(vehicleOverview.depreciationValue),
                buildValue(vehicleOverview.percentIncreaseValue))
    }

    private fun buildPercentString(percent: Int): String {
        return percent.toString() + context.resources.getString(R.string.vehicle_percent_label)
    }

    private fun buildYearString(year: String?): String {
        return if (year == null || year.isEmpty()) {
            context.resources.getString(R.string.empty_value)
        } else {
            year
        }
    }

    private fun buildValue(value: Float?): String {
        return if (value != null) {
            context.resources.getString(R.string.vehicle_value_label,
                    round(value).toString())
        } else {
            context.resources.getString(R.string.empty_value)
        }
    }

    private fun round(d: Float): Float {
        var bd = BigDecimal(java.lang.Float.toString(d))
        bd = bd.setScale(VALUE_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP)
        return bd.toFloat()
    }
}


