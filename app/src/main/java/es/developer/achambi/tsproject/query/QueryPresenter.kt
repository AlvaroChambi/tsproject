package es.developer.achambi.tsproject.query

import androidx.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.*
import es.developer.achambi.coreframework.ui.pagination.PaginatedBuilder
import es.developer.achambi.coreframework.ui.Presenter
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.usecase.PaginatedVehicles
import es.developer.achambi.tsproject.usecase.VehiclesUseCase
import es.developer.achambi.tsproject.views.builder.VehicleOverviewBuilder
import es.developer.achambi.tsproject.views.presentation.SearchVehicleBuilder

class QueryPresenter( private val useCase: VehiclesUseCase,
                      screen: QueryScreenInterface,
                      lifecycle : Lifecycle,
                      executor: ExecutorInterface,
                      private val presentationBuilder: VehicleOverviewBuilder,
                      private val paginatedBuilder: PaginatedBuilder,
                      private val searchHeaderBuilder: SearchVehicleBuilder)
    : Presenter<QueryScreenInterface>( screen, lifecycle, executor ) {

    fun setupInitialData(queryParams: QueryParams) {
        queryVehicles(queryParams)
    }

    fun queryNextPage(queryParams: QueryParams, index: Int) {
        screen.disableSearchButton()
        val responseHandler = object: ResponseHandler<PaginatedVehicles> {
            override fun onSuccess(response: PaginatedVehicles) {
                screen.enableSearchButton()
                screen.displayVehicles(
                        presentationBuilder.build(response.data),
                        paginatedBuilder.buildPageInfo(response))
            }

            override fun onError(error: Error) {
                screen.enableSearchButton()
                screen.displayNextPageError(paginatedBuilder.buildPageInfoError())
            }
        }
        request(queryRequest(queryParams, index), responseHandler)
    }

    private fun queryVehicles(queryParams: QueryParams) {
        screen.showLoading()
        screen.disableSearchButton()
        screen.collapseKeyboard()
        val responseHandler = object: ResponseHandler<PaginatedVehicles> {
            override fun onSuccess(response: PaginatedVehicles) {
                screen.stopLoading()
                screen.enableSearchButton()
                val list = paginatedBuilder.buildPageInfo(response)
                screen.displayVehicles(presentationBuilder.build(response.data), list)
                screen.displaySearchResultsCount( searchHeaderBuilder.build(response) )
            }

            override fun onError(error: Error) {
                screen.stopLoading()
                screen.enableSearchButton()
                screen.displayError(error)
            }
        }
        request(queryRequest(queryParams, 0), responseHandler)
    }

    fun performSearchSelected(queryParams: QueryParams) {
        queryVehicles(queryParams)
    }

    private fun queryRequest(queryParams: QueryParams, index: Int) : Request<PaginatedVehicles> {
        return object: Request<PaginatedVehicles> {
            override fun perform(): PaginatedVehicles {
                return useCase.retrieveVehicles(queryParams, index)
            }
        }
    }
}