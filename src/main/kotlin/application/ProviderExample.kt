package application

import domain.Provider
import domain.ProviderIdentifier
import java.util.*
import kotlin.random.Random

class ProviderExample : Provider {
    override fun get(): ProviderIdentifier {
        return ProviderIdentifier(UUID.randomUUID().toString())
    }

    override fun check(): Boolean {
        return Random.nextBoolean()
    }

    override fun howManyParallelRequestsCanThisProviderProcess(): Int {
        return 1
    }
}