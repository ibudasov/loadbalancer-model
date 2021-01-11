package domain

import java.util.*

class HealthChecker {
    private val candidatesToBeHealthy = Stack<Provider>()

    fun check(registry: ProviderRegistry, quarantine: ProviderQuarantine) {
        addUnhealthyProvidersToQuarantineSoTheyCanRecoverThere(quarantine, registry)

        removeUnhealthyProvidersFromTheLoadbalancer(registry)

        checkQuarantineAndRecoverProvidersIfTheyReportHealthy(quarantine, registry)
    }

    private fun checkQuarantineAndRecoverProvidersIfTheyReportHealthy(
        quarantine: ProviderQuarantine,
        registry: ProviderRegistry
    ) {
        val toBeDeletedFromDeadRegistry = Stack<Provider>()

        quarantine.forEach {

            if (it.check()) {

                // the candidate is already there in the list, means this is the second check for it
                if (candidatesToBeHealthy.search(it) != -1) {
                    registry.push(it)
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

    private fun addUnhealthyProvidersToQuarantineSoTheyCanRecoverThere(
        quarantine: ProviderQuarantine,
        registry: ProviderRegistry
    ) {
        registry.forEach {
            if (!it.check()) quarantine.push(it)
        }
    }

    private fun removeUnhealthyProvidersFromTheLoadbalancer(registry: ProviderRegistry) {
        registry.removeAll { !it.check() }
    }
}