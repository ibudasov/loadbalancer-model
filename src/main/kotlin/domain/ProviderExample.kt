package domain

import kotlin.random.Random

class ProviderExample : Provider {
    override fun get(): ProviderIdentifier {
        return ProviderIdentifier(java.util.UUID.randomUUID().toString())
    }

    override fun check(): Boolean {
        return Random.nextBoolean()
    }

    override fun howManyParallelRequestsCanThisProviderProcess(): Int {
        return 1
    }
}