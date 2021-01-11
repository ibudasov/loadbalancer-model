package domain.invocationAlgorithm

import domain.Provider
import domain.ProviderRegistry

interface InvocationAlgorithm {
    fun getProviderFromProviderRegistry(registry: ProviderRegistry): Provider
}