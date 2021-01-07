package domain

interface Provider {

    /**
     * Generate a domain.Provider that, once invoked on his get() method, retrieve an unique identifier (string) of the
     * provider instance
     */
    fun get() {}

    fun identifier(): ProviderIdentifier {
        return ProviderIdentifier()
    }

    /**
     * The load balancer should invoke every X seconds each of its registered providers on a special method called
     * check() to discover if they are alive – if not, it should exclude the provider node from load balancing.
     */
    fun check() {
        /**
         * If a node has been previously excluded from the balancing it should be re-included if it has successfully
         * been “heartbeat checked” for 2 consecutive times
         */
    }

    /**
     * Assuming that each provider can handle a maximum number of Y parallel requests, the Balancer should not
     * accept any further request when it has (Y*aliveproviders) incoming requests running simultaneously
     */
    fun howManyParallelRequestsCanThisProviderProcess(): Int {
        return 1
    }
}