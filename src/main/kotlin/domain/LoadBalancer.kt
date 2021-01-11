package domain

import domain.invocationAlgorithm.InvocationAlgorithmRandom
import java.util.*

class LoadBalancer(
    private val registryHealthy: ProviderRegistryHealthy = ProviderRegistryHealthy(),
    private val quarantine: ProviderRegistryQuarantine = ProviderRegistryQuarantine(),
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

        val provider = registryHealthy.getProviderAccordingToTheAlgorithm(
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
        registryHealthy.forEach {
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
        if (registryHealthy.size == maximumNumberOfProvidersAcceptedFromTheLoadBalancer) {
            throw SorryCannotAddProviderBecauseOfMaxLimit()
        }

        registryHealthy.push(provider)
    }

    fun excludeProviderFromBalancer(providerIdentifier: ProviderIdentifier) {
        registryHealthy.removeAll {
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

        addUnhealthyProvidersToQuarantineSoTheyCanRecoverThere()

        removeUnhealthyProvidersFromTheLoadbalancer()

        checkQuarantineAndRecoverProvidersIfTheyReportHealthy()
    }

    private fun checkQuarantineAndRecoverProvidersIfTheyReportHealthy() {
        val toBeDeletedFromDeadRegistry = Stack<Provider>()

        quarantine.forEach {

            if (it.check()) {

                // the candidate is already there in the list, means this is the second check for it
                if (candidatesToBeHealthy.search(it) != -1) {
                    registryHealthy.push(it)
                    toBeDeletedFromDeadRegistry.push(it)
                }

                // adding the candidate to the quarantine for the first time
                candidatesToBeHealthy.push(it)
            }
        }

        toBeDeletedFromDeadRegistry.forEach {
            quarantine.remove(it)
        }
    }

    private fun addUnhealthyProvidersToQuarantineSoTheyCanRecoverThere() {
        val (_, unhealthyProviders) = registryHealthy.partition {
            it.check()
        }

        unhealthyProviders.forEach {
            quarantine.push(it)
        }
    }

    private fun removeUnhealthyProvidersFromTheLoadbalancer() {
        registryHealthy.removeAll { !it.check() }
    }
}