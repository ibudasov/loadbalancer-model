package application

import domain.Provider
import domain.ProviderIdentifier

class ProviderExampleWhichIsAlwaysDead : Provider {

    var manuallyAssignedProviderIdentifier: String = ""

    fun setProviderIdentifier(id: String) {
        manuallyAssignedProviderIdentifier = id
    }

    override fun get(): ProviderIdentifier {
        return ProviderIdentifier(manuallyAssignedProviderIdentifier)
    }

    override fun check(): Boolean {
        return false
    }

    override fun howManyParallelRequestsCanThisProviderProcess(): Int {
        return 1
    }
}