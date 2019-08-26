package es.developer.achambi.tsproject.views.presentation

import android.content.Context
import es.developer.achambi.tsproject.R
import es.developer.achambi.tsproject.usecase.PaginatedVehicles

class SearchVehicleHeaderPresentation( val countDescription: String )
class SearchVehicleBuilder(private val context: Context) {
    fun build(vehicles: PaginatedVehicles) : SearchVehicleHeaderPresentation{
        return SearchVehicleHeaderPresentation(context.resources.getQuantityString(
                R.plurals.search_result_count_description, vehicles.count, vehicles.count))
    }
}