package application

import domain.Provider
import domain.ProviderIdentifier
import kotlin.random.Random

class ProviderExample : Provider {

    var manuallyAssignedProviderIdentifier: String = ""

    fun setProviderIdentifier(id: String) {
        manuallyAssignedProviderIdentifier = id
    }

    override fun get(): ProviderIdentifier {
        return ProviderIdentifier(manuallyAssignedProviderIdentifier)
    }

    override fun check(): Boolean {
        return Random.nextBoolean()
    }

    override fun howManyParallelRequestsCanThisProviderProcess(): Int {
        return 1
    }
}