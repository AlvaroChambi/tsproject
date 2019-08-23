package es.developer.achambi.tsproject

import android.app.Application
import es.developer.achambi.coreframework.threading.MainExecutor
import es.developer.achambi.coreframework.ui.pagination.PaginatedBuilder
import es.developer.achambi.tsproject.database.AppDatabase
import es.developer.achambi.tsproject.models.PaginationHandler
import es.developer.achambi.tsproject.usecase.VehiclesUseCase
import es.developer.achambi.tsproject.views.builder.VehicleOverviewBuilder
import es.developer.achambi.tsproject.views.presentation.VehicleBuilder

class TSApplication : Application() {
    companion object{
        lateinit var queryPresenterFactory : QueryPresenterFactory
    }

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.buildDatabase(this)
        val executor = MainExecutor.buildExecutor()
        val repository = VehicleDBRepository(database)
        val useCase = VehiclesUseCase(repository, PaginationHandler())

        queryPresenterFactory = QueryPresenterFactory(executor, useCase,
                VehicleOverviewBuilder(this, VehicleBuilder(this) ),
                PaginatedBuilder(this))
    }
}
