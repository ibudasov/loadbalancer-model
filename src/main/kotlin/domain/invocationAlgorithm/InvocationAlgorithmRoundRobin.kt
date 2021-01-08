package domain.invocationAlgorithm

import domain.Provider
import domain.ProviderRegistry

/**
 * when invoking multiple times the Load Balancer on its get() method, should cause the round-robin (sequential)
 * invocation of the get() method of the registered providers.
 */
class InvocationAlgorithmRoundRobin : InvocationAlgorithm {

    var previousCallReturnedElementAt = 0

    override fun getProviderFromProviderRegistry(providerRegistry: ProviderRegistry): Provider {

        if (previousCallReturnedElementAt == providerRegistry.size) {
            previousCallReturnedElementAt = 0
        }

        var providerToReturn = providerRegistry
            .toList()
            .get(previousCallReturnedElementAt)

        previousCallReturnedElementAt++

        return providerToReturn
    }
}