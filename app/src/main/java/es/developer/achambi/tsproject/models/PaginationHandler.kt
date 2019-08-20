package es.developer.achambi.tsproject.models

class PaginationHandler {
    fun getNextPageRange(offset: Int, count: Int, pageSize: Int) : Pair<Int,Int> {
        if(count < 0 || offset < 0 || pageSize < 0) {
            return Pair(0, 0)
        }
        val adjustedOffset = if(offset <= count) {
            offset
        } else {
            count
        }
        val remainder = count - adjustedOffset
        val nextPageSize = if( remainder >= pageSize ) {
            pageSize
        } else {
            remainder
        }
        val nextPageIndex = adjustedOffset + nextPageSize
        return Pair( adjustedOffset, nextPageIndex )
    }

    fun isEndPage(offset: Int, count: Int, pageSize: Int) : Boolean {
        if(count < 0 || offset < 0 || pageSize < 0) {
            return false
        }
        val adjustedOffset = if(offset <= count) {
            offset
        } else {
            count
        }

        val remainder = count - adjustedOffset
        return remainder == 0 || remainder <= pageSize
    }
}