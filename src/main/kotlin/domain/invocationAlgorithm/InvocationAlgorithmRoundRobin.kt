package domain.invocationAlgorithm

import domain.Provider
import domain.ProviderRegistryHealthy

/**
 * when invoking multiple times the Load Balancer on its get() method, should cause the round-robin (sequential)
 * invocation of the get() method of the registered providers.
 */
class InvocationAlgorithmRoundRobin : InvocationAlgorithm {

    var previousCallReturnedElementAt = 0

    override fun getProviderFromProviderRegistry(providerRegistryHealthy: ProviderRegistryHealthy): Provider {

        if (previousCallReturnedElementAt == providerRegistryHealthy.size) {
            previousCallReturnedElementAt = 0
        }

        val providerToReturn = providerRegistryHealthy
            .toList()
            .get(previousCallReturnedElementAt)

        previousCallReturnedElementAt++

        return providerToReturn
    }
}