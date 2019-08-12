package es.developer.achambi.tsproject.query

import android.arch.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.Error
import es.developer.achambi.coreframework.threading.MainExecutor
import es.developer.achambi.coreframework.threading.Request
import es.developer.achambi.coreframework.threading.ResponseHandler
import es.developer.achambi.coreframework.ui.Presenter
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.models.VehicleOverview
import es.developer.achambi.tsproject.usecase.VehiclesUseCase

class QueryPresenter( private val useCase: VehiclesUseCase,
                      screen: QueryScreenInterface,
                      lifecycle : Lifecycle,
                      executor: MainExecutor )
    : Presenter<QueryScreenInterface>( screen, lifecycle, executor ) {

    fun queryVehicles(queryParams: QueryParams ) {
        screen.showLoading()
        screen.disableSearchButton()
        val responseHandler = object: ResponseHandler<ArrayList<VehicleOverview>> {
            override fun onSuccess(response: ArrayList<VehicleOverview>) {
                screen.stopLoading()
                screen.enableSearchButton()
                screen.displayVehicles(response)
            }

            override fun onError(error: Error) {
                screen.stopLoading()
                screen.enableSearchButton()
                screen.displayError(error)
            }
        }
        request(queryRequest(queryParams), responseHandler)
    }

    fun performSearchSelected(queryParams: QueryParams, expanded: Boolean) {
        if(expanded) {
            screen.collapseAdvancedSearch()
            screen.collapseKeyboard()
        }
        queryVehicles(queryParams)
    }

    fun switchAdvancedSearchSelected(expanded: Boolean) {
        if( expanded ) {
            screen.collapseAdvancedSearch()
        } else {
            screen.expandAdvancedSearch()
        }
    }

    private fun queryRequest(queryParams: QueryParams) : Request<ArrayList<VehicleOverview>>  {
        return object: Request<ArrayList<VehicleOverview>> {
            override fun perform(): ArrayList<VehicleOverview> {
                return useCase.retrieveVehicles(queryParams)
            }
        }
    }
}