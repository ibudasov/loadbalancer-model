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
    fun `provider is excluded out of registry if it's not healthy and goes to quarantine`() {

        val registry = ProviderRegistry()
        val quarantine = ProviderQuarantine()
        val balancer = LoadBalancer(registry, quarantine)

        val healthyProvider = ProviderExample()
        healthyProvider.setProviderIdentifier("healthy-provider")
        balancer.includeProviderIntoBalancer(healthyProvider)

        val unhealthyProvider = ProviderExampleWhichIsAlwaysDead()
        unhealthyProvider.setProviderIdentifier("sick-provider")
        balancer.includeProviderIntoBalancer(unhealthyProvider)

        assertEquals(2, registry.size, "Initially the loadBalancer should have 2 providers")

        balancer.healthCheck()

        assertEquals(1, registry.size, "After running the health check, the dead provider should be gone")
        assertEquals("healthy-provider", balancer.get().get().toString())
        assertEquals(1, quarantine.size)
        assertEquals("sick-provider", quarantine.first().get().uniqueString)
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

    @Test
    fun `provider is returned to registry, when health check was successful twice`() {

        val registry = ProviderRegistry()
        val quarantine = ProviderQuarantine()

        val healthyProvider = ProviderExample()
        healthyProvider.setProviderIdentifier("healthy-provider")
        registry.push(healthyProvider)

        val sickProvider = ProviderExample()
        sickProvider.setProviderIdentifier("sick-provider")
        quarantine.push(sickProvider)

        val loadBalancer = LoadBalancer(registry, quarantine)
        loadBalancer.healthCheck()
        loadBalancer.healthCheck()

        assertEquals(
            2,
            registry.size,
            "After running the health check twice in the row, the dead provider, which was reporting healthy, should recover"
        )
        assertEquals("healthy-provider", registry.first().get().toString())
        assertEquals("sick-provider", registry.last().get().toString())

        assertEquals(
            0,
            quarantine.size,
            "quarantine shall be empty, as the dead provider recovered"
        )
    }

}