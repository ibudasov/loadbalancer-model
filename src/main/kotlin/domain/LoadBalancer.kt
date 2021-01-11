package domain

import domain.invocationAlgorithm.InvocationAlgorithmRandom
import java.util.*

class LoadBalancer(
    private val registry: ProviderRegistry = ProviderRegistry(),
    private val deadRegistry: ProviderRegistryOfExcludedProviders = ProviderRegistryOfExcludedProviders(),
) {

    private val candidatesToBeHealthy = Stack<Provider>()

    /**
     * the maximum number of providers accepted from the load balancer is 10
     */
    private val maximumNumberOfProvidersAcceptedFromTheLoadBalancer = 10

    private var clusterCapacityLimit = 0

    private var numberOfCurrentlyRunningRequests = 0

    fun get(): Provider {

        guardAgainstOverflowingClusterCapacityLimit()

        val provider = registry.getProviderAccordingToTheAlgorithm(
            InvocationAlgorithmRandom()
        )

        // todo: get rid of this
        // unregisterIncomingRequestAsItIsProcessed()

        return provider
    }

    private fun guardAgainstOverflowingClusterCapacityLimit() {

        updateClusterCapacityLimit()
        registerIncomingRequest()

        if (numberOfCurrentlyRunningRequests > clusterCapacityLimit) {
            throw SorryCannotAcceptRequestDueToClusterCapacityLimit()
        }
    }

    private fun updateClusterCapacityLimit() {
        clusterCapacityLimit = 0
        registry.forEach {
            clusterCapacityLimit += it.howManyParallelRequestsCanThisProviderProcess()
        }
    }

    private fun unregisterIncomingRequestAsItIsProcessed() {
        numberOfCurrentlyRunningRequests--
    }

    private fun registerIncomingRequest() {
        numberOfCurrentlyRunningRequests++
    }

    fun includeProviderIntoBalancer(provider: Provider) {
        if (registry.size == maximumNumberOfProvidersAcceptedFromTheLoadBalancer) {
            throw SorryCannotAddProviderBecauseOfMaxLimit()
        }

        registry.push(provider)
    }

    fun excludeProviderFromBalancer(providerIdentifier: ProviderIdentifier) {
        registry.removeAll {
            it.get().toString() == providerIdentifier.toString()
        }
    }

    /**
     * The load balancer should invoke every X seconds each of its registered providers on a special method called
     * check() to discover if they are alive – if not, it should exclude the provider node from load balancing.
     *
     * this method might be called b y something like cron in a recurring manner
     */
    fun healthCheck() {
        val (_, deadProviders) = registry.partition {
            it.check()
        }

        unregisterAllTheUnhealthyProvidersFromTheLoadbalancer()

        addAllTheDeadProvidersToTheDeadRegistrySoTheyCanRecoverThere(deadProviders)

        // todo: once alive - add back to the alive register
        val toBeDeletedFromDeadRegistry = Stack<Provider>()

        deadRegistry.forEach {

            if (it.check()) {

                // the candidate is already there in the list, means this is the second check for it
                if (candidatesToBeHealthy.search(it) != -1) {
                    registry.push(it)
                    toBeDeletedFromDeadRegistry.push(it)
                }

                // adding the candidate to the list for the first time
                candidatesToBeHealthy.push(it)
            }
        }

        toBeDeletedFromDeadRegistry.forEach {
            deadRegistry.remove(it)
        }
    }

    private fun addAllTheDeadProvidersToTheDeadRegistrySoTheyCanRecoverThere(deadProviders: List<Provider>) {
        deadProviders.forEach {
            deadRegistry.push(it)
        }
    }

    private fun unregisterAllTheUnhealthyProvidersFromTheLoadbalancer() {
        registry.removeAll { !it.check() }
    }
}