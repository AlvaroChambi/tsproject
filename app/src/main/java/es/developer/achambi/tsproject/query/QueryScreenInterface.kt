package es.developer.achambi.tsproject.query

import es.developer.achambi.coreframework.threading.Error
import es.developer.achambi.coreframework.ui.pagination.PaginatedPresentation
import es.developer.achambi.coreframework.ui.Screen
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation

interface QueryScreenInterface : Screen {
    fun showLoading()
    fun stopLoading()
    fun disableSearchButton()
    fun enableSearchButton()
    fun expandAdvancedSearch()
    fun collapseAdvancedSearch()
    fun collapseKeyboard()
    fun displayVehicles( vehicles: ArrayList<VehicleOverviewPresentation>,
                         paginatedData : ArrayList<PaginatedPresentation> )
    fun displayError( error: Error )
    fun displayNextPageError( paginatedData : ArrayList<PaginatedPresentation> )
}