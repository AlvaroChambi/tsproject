package es.developer.achambi.tsproject.models

import org.junit.Test

import org.junit.Assert.*

class PaginationHandlerTest {
    private val paginationHandler = PaginationHandler()

    @Test
    fun `test next range negative offset`() {
        val result = paginationHandler.getNextPageRange(-1, 0, 0)
        assertEquals( Pair(0,0), result )
    }

    @Test
    fun `test next range negative count`() {
        val result = paginationHandler.getNextPageRange(0, -1, 0)
        assertEquals( Pair(0,0), result )
    }

    @Test
    fun `test next range negative pageSize`() {
        val result = paginationHandler.getNextPageRange(0, 0, -1)
        assertEquals( Pair(0,0), result )
    }

    @Test
    fun `test next page range page size larger than count page size`() {
        val result = paginationHandler.getNextPageRange(0, 10, 20)
        assertEquals( Pair(0, 10), result )
    }

    @Test
    fun`test next range offset larger than count`() {
        val result = paginationHandler.getNextPageRange(20, 10, 10 )
        assertEquals( Pair(10, 10), result )
    }

    @Test
    fun `test end page negative values`() {
        val result = paginationHandler.isEndPage(-1, -1, -1 )
        assertEquals( false, result )
    }

    @Test
    fun `test end page`() {
        val result = paginationHandler.isEndPage(0, 10, 10 )
        assertEquals( true, result )
    }
}