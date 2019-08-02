package es.developer.achambi.tsproject.usecase

import es.developer.achambi.tsproject.database.AppDatabase
import es.developer.achambi.tsproject.database.model.RowDao
import es.developer.achambi.tsproject.database.model.data
import es.developer.achambi.tsproject.models.QueryParams
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.lang.NumberFormatException

@RunWith(MockitoJUnitRunner::class)
class VehiclesUseCaseTest {
    @Mock
    private lateinit var database : AppDatabase
    @Mock
    private lateinit var rowDao : RowDao
    private lateinit var useCase : VehiclesUseCase
    private var validData = data()
    private var validDataB = data()
    private lateinit var queryParams : QueryParams
    private val corruptedData = data()
    private val dataResult = ArrayList<data>()

    @Before
    fun setup() {
        useCase = VehiclesUseCase( database )
        queryParams = QueryParams.Builder().build()
        `when`(rowDao.queryAll()).thenReturn(dataResult)
        `when`(database.rowDao).thenReturn(rowDao)

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

        val result = useCase.retrieveVehicles( queryParams )

        assertTrue( result.isEmpty() )
    }

    @Test
    fun `empty query params`() {
        dataResult.add( validData )

        val result = useCase.retrieveVehicles( queryParams )

        assertFalse( result.isEmpty() )
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

        val result = useCase.retrieveVehicles( queryParams )

        assertEquals( 1, result.size )
        assertEquals( "model", result[0].vehicle.modelo )
    }

    @Test(expected = NumberFormatException::class)
    fun `invalid period format`() {
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .period("bad_format")
                .build()

        useCase.retrieveVehicles( queryParams )
    }

    @Test
    fun `valid period does match`() {
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .period("2015")
                .build()

        val result = useCase.retrieveVehicles( queryParams )

        assertFalse( result.isEmpty() )
    }

    @Test
    fun `valid period does not match`() {
        dataResult.add( validData )
        queryParams = QueryParams.Builder()
                .period("2035")
                .build()

        val result = useCase.retrieveVehicles( queryParams )

        assertTrue( result.isEmpty() )
    }

    @Test
    fun `valid cvf does match`() {
        dataResult.add( validData )
        dataResult.add( validDataB )

        queryParams = QueryParams.Builder()
                .cvf("30.5")
                .build()

        val result = useCase.retrieveVehicles( queryParams )

        assertEquals( 1, result.size )
        assertEquals( "30,5", result[0].vehicle.cvf )
    }
}