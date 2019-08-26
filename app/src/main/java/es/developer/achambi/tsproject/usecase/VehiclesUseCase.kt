package es.developer.achambi.tsproject.usecase

import es.developer.achambi.coreframework.threading.Error
import es.developer.achambi.coreframework.ui.pagination.PaginatedData
import es.developer.achambi.tsproject.VehicleDBRepository
import es.developer.achambi.tsproject.database.model.data
import es.developer.achambi.tsproject.models.PaginationHandler
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.models.VehicleOverview

class PaginatedVehicles : PaginatedData<VehicleOverview>()

class VehiclesUseCase( private val repository: VehicleDBRepository,
                       private val paginationHandler: PaginationHandler ) {
    private val pageSize = 20
    private var currentQueryParams = QueryParams.Builder().build()
    private val paginatedVehicles = PaginatedVehicles()

    @Throws(Error::class)
    fun retrieveVehicles( queryParams: QueryParams, nextPageIndex: Int )
            : PaginatedVehicles {
        if(nextPageIndex < 0) {
            throw Error("Invalid nextPageIndex, index should be in bounds")
        }
        if( currentQueryParams == queryParams
                && nextPageIndex != paginatedVehicles.nextPageIndex ){
            return paginatedVehicles
        }
        resolveCache(queryParams)
        this.currentQueryParams = queryParams
        val filtered = applyFilters( repository.requestVehicles(), queryParams )

        val( start, end ) = paginationHandler.getNextPageRange(nextPageIndex,filtered.size,
                pageSize)
        for (i in start until end) {
            val vehicle = VehicleOverview()
            vehicle.vehicle = filtered[i]
            vehicle.year = queryParams.period
            paginatedVehicles.data.add(vehicle)
        }
        paginatedVehicles.count = filtered.size
        paginatedVehicles.endPage = paginationHandler.isEndPage(nextPageIndex,filtered.size,pageSize)
        paginatedVehicles.nextPageIndex = end
        return paginatedVehicles
    }

    /**
     * Clear previous cache if the query params doesn't match
     */
    private fun resolveCache(queryParams: QueryParams) {
        if(queryParams != currentQueryParams) {
            paginatedVehicles.data.clear()
            paginatedVehicles.endPage = false
            paginatedVehicles.count = 0
            paginatedVehicles.nextPageIndex = 0
        }
    }

    private fun applyFilters( rawVehicles : List<data>, queryParams: QueryParams ) : ArrayList<data>{
        val filtered = ArrayList<data>()

        rawVehicles.forEach {
            if( isDataValid( it ) && doesMatchCriteria(it, queryParams) ) {
                filtered.add( it )
            }
        }

        return filtered
    }

    private fun isDataValid( data: data ) : Boolean {
        return data.cilindros != null &&
                data.potencia != null &&
                data.cv != null &&
                data.gD != null &&
                data.periodo != null &&
                data.marca != null &&
                data.cc != null &&
                data.cvf != null &&
                data.valor != null
    }

    private fun doesMatchCriteria( vehicle : data, queryParams: QueryParams ) : Boolean {
        return (vehicle.marca.toLowerCase().contains(queryParams.brand.toLowerCase()) &&
                vehicle.modelo.toLowerCase().contains(queryParams.model.toLowerCase()) &&
                vehicle.gD.toLowerCase().contains(queryParams.gd.toLowerCase()) &&
                vehicle.cvf.toLowerCase().contains(formatValue(queryParams.cvf).toLowerCase()) &&
                vehicle.cc.toLowerCase().contains(queryParams.cc.toLowerCase()) &&
                isInPeriod(queryParams.period, vehicle.periodo) &&
                vehicle.cilindros.toLowerCase()
                        .contains(queryParams.cylinders.toLowerCase()) &&
                vehicle.cv.toLowerCase()
                        .contains(queryParams.cv.toLowerCase()) &&
                vehicle.potencia.toLowerCase()
                        .contains(queryParams.pkw.toLowerCase()))
    }

    private fun isInPeriod(year: String, period: String): Boolean {
        if (year.isEmpty()) {
            return true
        }
        val firstYear = period.substring(0, period.lastIndexOf("-"))
        val secondYear = period.substring(period.lastIndexOf("-") + 1, period.length)
        return if (secondYear.isEmpty()) {
            year == firstYear
        } else {
            val first = Integer.valueOf(firstYear)
            val second = Integer.valueOf(secondYear)
            val yearToFind = Integer.valueOf(year)
            yearToFind in first..second
        }
    }

    private fun formatValue(cvf: String): String {
        return if (cvf.contains(".")) {
            cvf.replace('.', ',')
        } else cvf
    }
}