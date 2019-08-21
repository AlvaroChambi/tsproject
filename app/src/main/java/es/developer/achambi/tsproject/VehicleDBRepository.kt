package es.developer.achambi.tsproject

import es.developer.achambi.tsproject.database.AppDatabase
import es.developer.achambi.tsproject.database.model.data

class VehicleDBRepository(private val database: AppDatabase) {
    private lateinit var rowData: List<data>

    fun requestVehicles() : List<data> {
        if(!::rowData.isInitialized) {
            rowData = database.rowDao.queryAll()
        }
        return rowData
    }
}
