package domain.invocationAlgorithm

import domain.Provider
import domain.ProviderRegistryHealthy

interface InvocationAlgorithm {
    fun getProviderFromProviderRegistry(providerRegistryHealthy: ProviderRegistryHealthy): Provider
}