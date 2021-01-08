package domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ProviderIdentifierTest {

    @Test
    fun `toString converts to the string properly`() {
        val identifierUnderTheTest = ProviderIdentifier("banana")

        assertEquals("banana", identifierUnderTheTest.toString())
    }
}