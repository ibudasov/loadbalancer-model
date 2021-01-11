package domain

import domain.invocationAlgorithm.InvocationAlgorithm
import java.util.*

/**
 * Here we hold all of the providers which are successful at health check.
 */
class ProviderRegistry : Stack<Provider>() {
    fun getProviderAccordingToTheAlgorithm(invocationAlgorithm: InvocationAlgorithm): Provider {
        return invocationAlgorithm.getProviderFromProviderRegistry(this)
    }
}