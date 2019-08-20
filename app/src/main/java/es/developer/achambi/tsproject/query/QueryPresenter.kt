package es.developer.achambi.tsproject.query

import android.arch.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.*
import es.developer.achambi.coreframework.ui.PagePresentation
import es.developer.achambi.coreframework.ui.Presenter
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.usecase.PaginatedVehicles
import es.developer.achambi.tsproject.usecase.VehiclesUseCase

class QueryPresenter( private val useCase: VehiclesUseCase,
                      screen: QueryScreenInterface,
                      lifecycle : Lifecycle,
                      executor: ExecutorInterface )
    : Presenter<QueryScreenInterface>( screen, lifecycle, executor ) {

    fun setupInitialData(queryParams: QueryParams) {
        queryVehicles(queryParams)
    }

    fun queryNextPage(queryParams: QueryParams, index: Int) {
        screen.disableSearchButton()
        val responseHandler = object: ResponseHandler<PaginatedVehicles> {
            override fun onSuccess(response: PaginatedVehicles) {
                screen.enableSearchButton()
                screen.displayVehicles(response.vehicles, buildPageInfo(response))
            }

            override fun onError(error: Error) {
                screen.enableSearchButton()
                screen.displayError(error)
            }
        }
        request(queryRequest(queryParams, index), responseHandler)
    }

    private fun queryVehicles(queryParams: QueryParams) {
        screen.showLoading()
        screen.disableSearchButton()
        val responseHandler = object: ResponseHandler<PaginatedVehicles> {
            override fun onSuccess(response: PaginatedVehicles) {
                screen.stopLoading()
                screen.enableSearchButton()
                screen.displayVehicles(response.vehicles, buildPageInfo(response))
            }

            override fun onError(error: Error) {
                screen.stopLoading()
                screen.enableSearchButton()
                screen.displayError(error)
            }
        }
        request(queryRequest(queryParams, 0), responseHandler)
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

    private fun buildPageInfo(vehiclesOverviewPage: PaginatedVehicles)
            : ArrayList<PagePresentation> {
        val list = ArrayList<PagePresentation>()
        if( !vehiclesOverviewPage.endPage ) {
            list.add( PagePresentation( vehiclesOverviewPage.nextPageIndex ) )
        }
        return list
    }

    private fun queryRequest(queryParams: QueryParams, index: Int) : Request<PaginatedVehicles> {
        return object: Request<PaginatedVehicles> {
            override fun perform(): PaginatedVehicles {
                return useCase.retrieveVehicles(queryParams, index)
            }
        }
    }
}