package domain

import domain.invocationAlgorithm.InvocationAlgorithm
import java.util.*

class ProviderRegistry : Stack<Provider>() {
    fun getProviderAccordingToTheAlgorithm(invocationAlgorithm: InvocationAlgorithm): Provider {
        return invocationAlgorithm.getProviderFromProviderRegistry(this)
    }
}