package domain.invocationAlgorithm

import domain.Provider
import domain.ProviderRegistry

/**
 * when invoking multiple times the Load Balancer on its get() method, should cause the random invocation of the get()
 * method of any registered provider instance.
 */
class InvocationAlgorithmRandom : InvocationAlgorithm {
    override fun getProviderFromProviderRegistry(registry: ProviderRegistry): Provider {

        // todo: abstract randomness out of domain package, because it's difficult to test it

        return registry
            .toList()
            .shuffled()
            .take(1)
            .first()
    }
}