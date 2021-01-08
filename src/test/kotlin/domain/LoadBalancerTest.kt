package domain

import application.ProviderExample
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoadBalancerTest {

    @Test
    fun `provider can be included into the balancer when there is enough space for it`() {

        val balancer = LoadBalancer()
        val provider = ProviderExample()

        balancer.includeProviderIntoBalancer(provider)
    }

    @Test
    fun `provider can NOT be included into the balancer when there is enough providers already`() {

        val balancer = LoadBalancer()
        val provider = ProviderExample()

        for (i in 1..10) {
            balancer.includeProviderIntoBalancer(provider)
        }

        assertFailsWith<SorryCannotAddProviderBecauseOfMaxLimit> {
            balancer.includeProviderIntoBalancer(provider)
        }
    }

    @Test
    fun `provider can be excluded out of registry`() {

        val registry = ProviderRegistry()
        val balancer = LoadBalancer(registry)

        for (i in 1..5) {
            val `provider$i` = ProviderExample()
            `provider$i`.setProviderIdentifier("provider$i")
            balancer.includeProviderIntoBalancer(`provider$i`)
        }

        assertEquals(5, registry.size, "Right after we added 5 providers, their count should be 5")

        balancer.excludeProviderFromBalancer(ProviderIdentifier("provider3"))

        assertEquals(4, registry.size, "We just removed a provider, count of providers should change")
    }

}