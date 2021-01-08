package domain

import application.ProviderExample
import org.junit.Test
import kotlin.test.assertFailsWith

class LoadBalancerTest {

    @Test
    fun `provider can be included into the balancer when there is enough space for it`() {

        val registry = ProviderRegistry()
        val balancer = LoadBalancer(registry)
        val provider = ProviderExample()

        balancer.includeProviderIntoBalancer(provider)
    }

    @Test
    fun `provider can NOT be included into the balancer when there is enough providers already`() {

        val registry = ProviderRegistry()
        val balancer = LoadBalancer(registry)
        val provider = ProviderExample()

        for (i in 1..10) {
            balancer.includeProviderIntoBalancer(provider)
        }

        assertFailsWith<SorryCannotAddProviderBecauseOfMaxLimit> {
            balancer.includeProviderIntoBalancer(provider)
        }
    }
}