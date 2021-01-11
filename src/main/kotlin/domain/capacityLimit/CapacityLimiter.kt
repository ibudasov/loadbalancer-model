package domain.capacityLimit

import domain.ProviderRegistry

interface CapacityLimiter {
    fun guardAgainstOverflowingClusterCapacityLimit(registry: ProviderRegistry)
    fun unregisterIncomingRequestAsItIsProcessed()
}