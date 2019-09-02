package es.developer.achambi.tsproject

import androidx.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.MainExecutor
import es.developer.achambi.coreframework.ui.pagination.PaginatedBuilder
import es.developer.achambi.tsproject.query.QueryPresenter
import es.developer.achambi.tsproject.query.QueryScreenInterface
import es.developer.achambi.tsproject.usecase.VehiclesUseCase
import es.developer.achambi.tsproject.views.builder.VehicleOverviewBuilder
import es.developer.achambi.tsproject.views.presentation.SearchVehicleBuilder

class QueryPresenterFactory(private val mainExecutor: MainExecutor,
                            private val useCase: VehiclesUseCase,
                            private val presentationBuilder: VehicleOverviewBuilder,
                            private val paginatedBuilder: PaginatedBuilder,
                            private val searchHeaderBuilder: SearchVehicleBuilder) {
    fun createPresenter(screen: QueryScreenInterface, lifecycle: Lifecycle) : QueryPresenter{
        return QueryPresenter( useCase, screen, lifecycle, mainExecutor, presentationBuilder,
                paginatedBuilder, searchHeaderBuilder )
    }
}