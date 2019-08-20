package es.developer.achambi.tsproject.query

import android.arch.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.*
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.usecase.PaginatedVehicles
import es.developer.achambi.tsproject.usecase.VehiclesUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QueryPresenterTest {
    private val executor = MockExecutor()
    @Mock
    lateinit var lifecycle: Lifecycle
    @Mock
    lateinit var screen: QueryScreenInterface
    @Mock
    lateinit var useCase: VehiclesUseCase
    @Mock
    lateinit var lifecycleState: Lifecycle.State

    lateinit var queryPresenter: QueryPresenter

    @Before
    fun setup() {
        queryPresenter = QueryPresenter(useCase, screen, lifecycle, executor)
        `when`(lifecycle.currentState).thenReturn( lifecycleState )
        `when`(lifecycleState.isAtLeast(Lifecycle.State.STARTED)).thenReturn(true)
    }

    @Test
    fun `query items success`() {
        val queryParams = QueryParams.Builder().build()
        val result = PaginatedVehicles()
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenReturn(result)

        queryPresenter.queryNextPage(queryParams, 0)

        verify(screen, times(1)).showLoading()
        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).stopLoading()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayVehicles(result.vehicles, any())
    }

    @Test
    fun `query items error`() {
        val error = Error()
        val queryParams = QueryParams.Builder().build()
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenThrow(error)

        queryPresenter.queryNextPage(queryParams, 0)

        verify(screen, times(1)).showLoading()
        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).stopLoading()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayError(error)
    }
}