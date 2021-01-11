package infrastructure

import domain.ProviderRegistry
import domain.capacityLimit.CapacityLimiter
import domain.capacityLimit.SorryCannotAcceptRequestDueToClusterCapacityLimit

class CapacityLimiterInMemory : CapacityLimiter {
    private var clusterCapacityLimit = 0

    private var numberOfCurrentlyRunningRequests = 0

    override fun guardAgainstOverflowingClusterCapacityLimit(registry: ProviderRegistry) {

        updateClusterCapacityLimit(registry)
        registerIncomingRequest()

        if (numberOfCurrentlyRunningRequests > clusterCapacityLimit) {
            throw SorryCannotAcceptRequestDueToClusterCapacityLimit()
        }
    }

    override fun unregisterIncomingRequestAsItIsProcessed() {
        numberOfCurrentlyRunningRequests--
    }

    private fun updateClusterCapacityLimit(registry: ProviderRegistry) {
        clusterCapacityLimit = 0
        registry.forEach {
            clusterCapacityLimit += it.howManyParallelRequestsCanThisProviderProcess()
        }
    }

    private fun registerIncomingRequest() {
        numberOfCurrentlyRunningRequests++
    }

}