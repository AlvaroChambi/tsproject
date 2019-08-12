package es.developer.achambi.tsproject.query

import es.developer.achambi.coreframework.threading.Error
import es.developer.achambi.coreframework.ui.Screen
import es.developer.achambi.tsproject.models.VehicleOverview

interface QueryScreenInterface : Screen {
    fun showLoading()
    fun stopLoading()
    fun disableSearchButton()
    fun enableSearchButton()
    fun displayVehicles(vehicles: ArrayList<VehicleOverview> )
    fun displayError( error: Error )
}