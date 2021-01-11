package domain.capacityLimit

import domain.ProviderRegistry

class CapacityLimiter {
    private var clusterCapacityLimit = 0

    private var numberOfCurrentlyRunningRequests = 0

    fun guardAgainstOverflowingClusterCapacityLimit(registry: ProviderRegistry) {

        updateClusterCapacityLimit(registry)
        registerIncomingRequest()

        if (numberOfCurrentlyRunningRequests > clusterCapacityLimit) {
            throw SorryCannotAcceptRequestDueToClusterCapacityLimit()
        }
    }

    fun unregisterIncomingRequestAsItIsProcessed() {
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