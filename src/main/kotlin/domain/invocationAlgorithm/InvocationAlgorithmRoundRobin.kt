package domain.invocationAlgorithm

import domain.Provider
import domain.ProviderRegistry

/**
 * when invoking multiple times the Load Balancer on its get() method, should cause the round-robin (sequential)
 * invocation of the get() method of the registered providers.
 */
class InvocationAlgorithmRoundRobin : InvocationAlgorithm {
    override fun getProviderFromProviderRegistry(providerRegistry: ProviderRegistry): Provider {
        TODO("Not yet implemented")
    }
}