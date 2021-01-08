package domain

interface Provider {

    /**
     * Generate a domain.Provider that, once invoked on his get() method, retrieve an unique identifier (string) of the
     * provider instance
     */
    fun get(): ProviderIdentifier

    /**
     * The load balancer should invoke every X seconds each of its registered providers on a special method called
     * check() to discover if they are alive – if not, it should exclude the provider node from load balancing.
     *
     * True represents happy and full of life provider
     * False means that the provider is dead
     */
    fun check(): Boolean

    /**
     * Assuming that each provider can handle a maximum number of Y parallel requests, the Balancer should not
     * accept any further request when it has (Y * aliveproviders) incoming requests running simultaneously
     */
    fun howManyParallelRequestsCanThisProviderProcess(): Int
}