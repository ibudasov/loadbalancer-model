package domain

import application.ProviderExample
import org.junit.Assert.assertEquals
import org.junit.Test

class ProviderExampleTest {
    @Test
    fun `example provider can process only 1 request in parallel`() {
        assertEquals(1, ProviderExample().howManyParallelRequestsCanThisProviderProcess())
    }
}