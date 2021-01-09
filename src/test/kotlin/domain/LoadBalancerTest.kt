package domain

import application.ProviderExample
import application.ProviderExampleWhichIsAlwaysDead
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
    fun `provider can be excluded out of registry manually`() {

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

    @Test
    fun `provider is excluded out of registry if it's not healthy`() {

        val registry = ProviderRegistry()
        val deadRegistry = ProviderRegistryOfExcludedProviders()
        val balancer = LoadBalancer(registry, deadRegistry)

        val healthyProvider = ProviderExample()
        healthyProvider.setProviderIdentifier("healthy-provider")
        balancer.includeProviderIntoBalancer(healthyProvider)

        val deadProvider = ProviderExampleWhichIsAlwaysDead()
        deadProvider.setProviderIdentifier("dead-provider")
        balancer.includeProviderIntoBalancer(deadProvider)

        assertEquals(2, registry.size, "Initially the loadBalancer should have 2 providers")

        balancer.healthCheck()

        assertEquals(1, registry.size, "After running the health check, the dead provider should be gone")
        assertEquals("healthy-provider", balancer.get().get().toString())
    }

    @Test
    fun `loadBalancer does not accept more requests than it can process`() {
        val registry = ProviderRegistry()
        val balancer = LoadBalancer(registry)

        // let's make cluster capacity = 2
        for (i in 1..2) {
            val `provider$i` = ProviderExample()
            `provider$i`.setProviderIdentifier("provider$i")
            balancer.includeProviderIntoBalancer(`provider$i`)
        }

        balancer.get()
        balancer.get()
        assertFailsWith<SorryCannotAcceptRequestDueToClusterCapacityLimit> {
            balancer.get()
        }
    }


}