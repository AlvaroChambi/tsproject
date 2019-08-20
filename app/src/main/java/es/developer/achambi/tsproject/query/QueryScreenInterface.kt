package es.developer.achambi.tsproject.query

import es.developer.achambi.coreframework.threading.Error
import es.developer.achambi.coreframework.ui.PagePresentation
import es.developer.achambi.coreframework.ui.Screen
import es.developer.achambi.tsproject.models.VehicleOverview

interface QueryScreenInterface : Screen {
    fun showLoading()
    fun stopLoading()
    fun disableSearchButton()
    fun enableSearchButton()
    fun expandAdvancedSearch()
    fun collapseAdvancedSearch()
    fun collapseKeyboard()
    fun displayVehicles( vehicles: ArrayList<VehicleOverview>, paginatedData : ArrayList<PagePresentation> )
    fun displayError( error: Error )
    fun displayNextPageError( paginatedData : ArrayList<PagePresentation> )
}