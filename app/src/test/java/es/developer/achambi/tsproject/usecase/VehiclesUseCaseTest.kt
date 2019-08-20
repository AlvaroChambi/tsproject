package es.developer.achambi.tsproject.usecase

import es.developer.achambi.tsproject.VehicleDBRepository
import es.developer.achambi.tsproject.database.model.data
import es.developer.achambi.tsproject.models.PaginationHandler
import es.developer.achambi.tsproject.models.QueryParams
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Error
import java.lang.NumberFormatException

@RunWith(MockitoJUnitRunner::class)
class VehiclesUseCaseTest {
    @Mock
    private lateinit var repository: VehicleDBRepository
    @Mock
    private lateinit var paginationHandler: PaginationHandler
    private lateinit var useCase : VehiclesUseCase
    private var validData = data()
    private var validDataB = data()
    private lateinit var queryParams : QueryParams
    private val corruptedData = data()
    private val dataResult = ArrayList<data>()

    @Before
    fun setup() {
        useCase = VehiclesUseCase( repository, paginationHandler )
        queryParams = QueryParams.Builder().build()
        `when`(repository.requestVehicles()).thenReturn(dataResult)
        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 0) )

        corruptedData.modelo = "model"

        validData.modelo = "model"
        validData.marca = "m"
        validData.periodo = "2010-2020"

        validData.cc = "cc"
        validData.cilindros = "cy"
        validData.cv = "cv"
        validData.cvf = "30,5"
        validData.gD = "gd"
        validData.potencia = "p"
        validData.valor = "v"


        validDataB.modelo = "B"
        validDataB.marca = "m"
        validDataB.periodo = "2010-2020"

        validDataB.cc = "cc"
        validDataB.cilindros = "cy"
        validDataB.cv = "cv"
        validDataB.cvf = "20,5"
        validDataB.gD = "gd"
        validDataB.potencia = "p"
        validDataB.valor = "v"
    }

    @Test
    fun `corrupted data retrieved`() {
        dataResult.add( corruptedData )
        val result = useCase.retrieveVehicles( queryParams, 0 )

        assertTrue( result.vehicles.isEmpty() )
    }

    @Test
    fun `empty query params`() {
        dataResult.add( validData )
        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 1) )
        val result = useCase.retrieveVehicles( queryParams, 0 )

        assertFalse( result.vehicles.isEmpty() )
    }

    @Test
    fun `filled query params`() {
        dataResult.add( validData )
        dataResult.add( validDataB )

        queryParams = QueryParams.Builder()
                .model("model")
                .brand("m")
                .cv("cv")
                .cylinders("cy")
                .pkw("p")
                .gd("gd")
                .build()

        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 1) )
        val result = useCase.retrieveVehicles( queryParams, 0 )

        assertEquals( 1, result.vehicles.size )
        assertEquals( "model", result.vehicles[0].vehicle.modelo )
    }

    @Test(expected = NumberFormatException::class)
    fun `invalid period format`() {
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .period("bad_format")
                .build()

        useCase.retrieveVehicles( queryParams, 0 )
    }

    @Test
    fun `valid period does match`() {
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .period("2015")
                .build()

        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 1) )
        val result = useCase.retrieveVehicles( queryParams, 0 )

        assertFalse( result.vehicles.isEmpty() )
    }

    @Test
    fun `valid period does not match`() {
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .period("2035")
                .build()

        val result = useCase.retrieveVehicles( queryParams, 0 )

        assertTrue( result.vehicles.isEmpty() )
    }

    @Test
    fun `valid cvf does match`() {
        dataResult.add( validData )
        dataResult.add( validDataB )

        queryParams = QueryParams.Builder()
                .cvf("30.5")
                .build()

        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 1) )
        val result = useCase.retrieveVehicles( queryParams, 0 )

        assertEquals( 1, result.vehicles.size )
        assertEquals( "30,5", result.vehicles[0].vehicle.cvf )
    }

    @Test
    fun`query with different query params`() {
        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 1) )
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .model("model")
                .build()
        var result = useCase.retrieveVehicles( queryParams, 0 )

        assertEquals( 1, result.vehicles.size )
        assertEquals( "model", result.vehicles[0].vehicle.modelo )

        dataResult.clear()
        dataResult.add( validDataB )
        queryParams = QueryParams.Builder()
                .model("B")
                .build()

        result = useCase.retrieveVehicles(queryParams, 0)

        assertEquals( 1, result.vehicles.size )
        assertEquals( "B", result.vehicles[0].vehicle.modelo )
    }

    @Test(expected = Error::class)
    fun `query with negative next page index`() {
        useCase.retrieveVehicles(queryParams, -1)
    }

    @Test
    fun `test paginated query`() {
        `when`(paginationHandler.getNextPageRange(anyInt(), anyInt(), anyInt()))
                .thenReturn( Pair(0, 1) )
        `when`(paginationHandler.isEndPage(anyInt(), anyInt(), anyInt())).thenReturn(true)

        val result = useCase.retrieveVehicles(queryParams, 0)

        assertEquals( true, result.endPage )
        assertEquals( 1, result.nextPageIndex )
    }
}