package es.developer.achambi.tsproject.query

import es.developer.achambi.coreframework.threading.Error
import es.developer.achambi.coreframework.ui.pagination.PaginatedPresentation
import es.developer.achambi.coreframework.ui.Screen
import es.developer.achambi.tsproject.views.presentation.SearchVehicleHeaderPresentation
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation

interface QueryScreenInterface : Screen {
    fun showLoading()
    fun stopLoading()
    fun disableSearchButton()
    fun enableSearchButton()
    fun collapseKeyboard()
    fun displayVehicles( vehicles: ArrayList<VehicleOverviewPresentation>,
                         paginatedData : ArrayList<PaginatedPresentation> )
    fun displaySearchResultsCount( presentation: SearchVehicleHeaderPresentation )
    fun displayError( error: Error )
    fun displayNextPageError( paginatedData : ArrayList<PaginatedPresentation> )
}