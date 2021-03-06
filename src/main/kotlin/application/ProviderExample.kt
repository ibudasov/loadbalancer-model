package application

import domain.Provider
import domain.ProviderIdentifier

class ProviderExample : Provider {

    var manuallyAssignedProviderIdentifier: String = ""

    fun setProviderIdentifier(id: String) {
        manuallyAssignedProviderIdentifier = id
    }

    override fun get(): ProviderIdentifier {
        return ProviderIdentifier(manuallyAssignedProviderIdentifier)
    }

    override fun check(): Boolean {
        return true
    }

    override fun howManyParallelRequestsCanThisProviderProcess(): Int {
        return 1
    }
}