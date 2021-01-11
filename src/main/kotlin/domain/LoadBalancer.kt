package domain

import domain.capacityLimit.CapacityLimiter
import domain.invocationAlgorithm.InvocationAlgorithmRandom

class LoadBalancer(
    private val registry: ProviderRegistry = ProviderRegistry(),
    private val quarantine: ProviderQuarantine = ProviderQuarantine(),
    private val healthChecker: HealthChecker = HealthChecker(),
    private val capacityLimiter: CapacityLimiter = CapacityLimiter(),
) {
    /**
     * the maximum number of providers accepted from the load balancer is 10
     */
    private val maximumNumberOfProvidersAcceptedFromTheLoadBalancer = 10


    fun get(): Provider {

        capacityLimiter.guardAgainstOverflowingClusterCapacityLimit(registry)

        val provider = registry.getProviderAccordingToTheAlgorithm(
            InvocationAlgorithmRandom()
        )

        // todo: get rid of this
        // capacityLimiter.unregisterIncomingRequestAsItIsProcessed()

        return provider
    }

    fun includeProviderIntoBalancer(provider: Provider) {
        if (registry.size == maximumNumberOfProvidersAcceptedFromTheLoadBalancer) {
            throw SorryCannotAddProviderBecauseOfMaxLimit()
        }

        registry.push(provider)
    }

    fun excludeProviderFromBalancer(providerIdentifier: ProviderIdentifier) {
        registry.removeAll {
            it.get().toString() == providerIdentifier.toString()
        }
    }

    /**
     * The load balancer should invoke every X seconds each of its registered providers on a special method called
     * check() to discover if they are alive – if not, it should exclude the provider node from load balancing.
     *
     * this method might be called b y something like cron in a recurring manner
     */
    fun healthCheck() {
        healthChecker.check(registry, quarantine)
    }
}