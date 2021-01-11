package domain.invocationAlgorithm

import application.ProviderExample
import domain.ProviderRegistry
import org.junit.Assert.assertNotSame
import org.junit.Test

class InvocationAlgorithmRandomTest {

    @Test
    fun `given lets say 5 providers, they will be returned randomly`() {
        val registry = ProviderRegistry()

        for (i in 1..5) {
            val `provider$i` = ProviderExample()
            `provider$i`.setProviderIdentifier("provider$i")
            registry.push(`provider$i`)
        }


        val algorithm = InvocationAlgorithmRandom()

        val invocation1 = algorithm.getProviderFromProviderRegistry(registry)
        val invocation2 = algorithm.getProviderFromProviderRegistry(registry)

        // this assertion is not perfect, and sometimes it will fail. Randomness needs to be abstracted away
        assertNotSame(invocation1, invocation2)
    }
}