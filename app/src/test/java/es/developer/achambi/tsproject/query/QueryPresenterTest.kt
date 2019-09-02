package es.developer.achambi.tsproject.query

import androidx.lifecycle.Lifecycle
import es.developer.achambi.coreframework.threading.*
import es.developer.achambi.coreframework.ui.pagination.PaginatedBuilder
import es.developer.achambi.coreframework.ui.pagination.PaginatedPresentation
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.usecase.PaginatedVehicles
import es.developer.achambi.tsproject.usecase.VehiclesUseCase
import es.developer.achambi.tsproject.views.builder.VehicleOverviewBuilder
import es.developer.achambi.tsproject.views.presentation.SearchVehicleBuilder
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation
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
    @Mock
    lateinit var presentationBuilder: VehicleOverviewBuilder
    @Mock
    lateinit var paginatedBuilder: PaginatedBuilder
    @Mock
    lateinit var searchVehicleBuilder: SearchVehicleBuilder
    lateinit var queryPresenter: QueryPresenter
    val pagePresentation = ArrayList<PaginatedPresentation>()
    val presentations = ArrayList<VehicleOverviewPresentation>()
    @Before
    fun setup() {
        queryPresenter = QueryPresenter(useCase, screen, lifecycle, executor,presentationBuilder,
                paginatedBuilder, searchVehicleBuilder)
        `when`(lifecycle.currentState).thenReturn( lifecycleState )
        `when`(lifecycleState.isAtLeast(Lifecycle.State.STARTED)).thenReturn(true)
        pagePresentation.add(PaginatedPresentation(0, false))
    }

    @Test
    fun `query items success`() {
        val queryParams = QueryParams.Builder().build()
        val result = PaginatedVehicles()
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenReturn(result)
        `when`(presentationBuilder.build(result.data)).thenReturn(presentations)
        `when`(paginatedBuilder.buildPageInfo(result)).thenReturn(pagePresentation)

        queryPresenter.performSearchSelected(queryParams)

        verify(screen, times(1)).collapseKeyboard()
        verify(screen, times(1)).showLoading()
        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).stopLoading()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayVehicles(presentations,
                pagePresentation)
    }

    @Test
    fun `query items success expanded advanced search`() {
        val queryParams = QueryParams.Builder().build()
        val result = PaginatedVehicles()
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenReturn(result)
        `when`(presentationBuilder.build(result.data)).thenReturn(presentations)
        `when`(paginatedBuilder.buildPageInfo(result)).thenReturn(pagePresentation)

        queryPresenter.performSearchSelected(queryParams)

        verify(screen, times(1)).collapseKeyboard()
        verify(screen, times(1)).showLoading()
        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).stopLoading()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayVehicles(presentations,
                pagePresentation)
    }

    @Test
    fun `query items error`() {
        val error = Error()
        val queryParams = QueryParams.Builder().build()
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenThrow(error)

        queryPresenter.performSearchSelected(queryParams)

        verify(screen, times(1)).collapseKeyboard()
        verify(screen, times(1)).showLoading()
        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).stopLoading()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayError(error)
    }

    @Test
    fun `query next page success`() {
        val queryParams = QueryParams.Builder().build()
        val result = PaginatedVehicles()
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenReturn(result)
        `when`(presentationBuilder.build(result.data)).thenReturn(presentations)
        `when`(paginatedBuilder.buildPageInfo(result)).thenReturn(pagePresentation)

        queryPresenter.queryNextPage(queryParams, 0)

        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayVehicles(presentations,
                pagePresentation)
    }

    @Test
    fun `query next page error`() {
        val error = Error()
        val queryParams = QueryParams.Builder().build()
        pagePresentation.clear()
        pagePresentation.add(PaginatedPresentation(0, true))
        `when`(useCase.retrieveVehicles(queryParams, 0)).thenThrow(error)
        `when`(paginatedBuilder.buildPageInfoError()).thenReturn(pagePresentation)

        queryPresenter.queryNextPage(queryParams, 0)

        verify(screen, times(1)).disableSearchButton()
        verify(screen, times(1)).enableSearchButton()
        verify(screen, times(1)).displayNextPageError(pagePresentation)
    }
}