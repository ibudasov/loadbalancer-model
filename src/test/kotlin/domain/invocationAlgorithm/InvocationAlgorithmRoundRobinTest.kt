package domain.invocationAlgorithm

import application.ProviderExample
import domain.ProviderRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

class InvocationAlgorithmRoundRobinTest {

    @Test
    fun `given lets say 5 providers, they will be returned one by one`() {
        val registry = ProviderRegistry()

        for (i in 1..5) {
            val `provider$i` = ProviderExample()
            `provider$i`.setProviderIdentifier("provider$i")
            registry.push(`provider$i`)
        }

        val algorithm = InvocationAlgorithmRoundRobin()

        assertEquals("provider1", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider2", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider3", algorithm.getProviderFromProviderRegistry(registry).get().toString())
    }

    @Test
    fun `given lets say 5 providers, when requested for the 6thh time, then 1st is returned`() {
        val registry = ProviderRegistry()

        for (i in 1..5) {
            val `provider$i` = ProviderExample()
            `provider$i`.setProviderIdentifier("provider$i")
            registry.push(`provider$i`)
        }


        val algorithm = InvocationAlgorithmRoundRobin()

        assertEquals("provider1", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider2", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider3", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider4", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider5", algorithm.getProviderFromProviderRegistry(registry).get().toString())
        assertEquals("provider1", algorithm.getProviderFromProviderRegistry(registry).get().toString())
    }
}