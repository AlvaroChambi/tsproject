package es.developer.achambi.tsproject.usecase

import es.developer.achambi.tsproject.database.AppDatabase
import es.developer.achambi.tsproject.database.model.data
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.models.VehicleOverview

class VehiclesUseCase( private val database: AppDatabase ) {
    private lateinit var rawData : List<data>

    fun retrieveVehicles( queryParams: QueryParams ) : ArrayList<VehicleOverview> {
        val vehicles = ArrayList<VehicleOverview>()
        rawData = fetchData()

        val filtered = applyFilters( rawData, queryParams )
        filtered.forEach {
            val vehicle = VehicleOverview()
            vehicle.vehicle = it
            vehicle.year = queryParams.period
            vehicles.add(vehicle)
        }

        return vehicles
    }

    private fun fetchData() : List<data> {
        if(::rawData.isInitialized) {
            return rawData
        }
        return database.rowDao.queryAll()
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