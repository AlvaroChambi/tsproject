package es.developer.achambi.tsproject

import android.arch.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.MainExecutor
import es.developer.achambi.tsproject.query.QueryPresenter
import es.developer.achambi.tsproject.query.QueryScreenInterface
import es.developer.achambi.tsproject.usecase.VehiclesUseCase

class QueryPresenterFactory(private val mainExecutor: MainExecutor,
                            private val useCase: VehiclesUseCase) {
    fun createPresenter(screen: QueryScreenInterface, lifecycle: Lifecycle) : QueryPresenter{
        return QueryPresenter( useCase, screen, lifecycle, mainExecutor )
    }
}