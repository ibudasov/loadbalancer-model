package domain

class LoadBalancer {

    fun get() {
        // todo: algorithm instance
        // todo:
        // todo:
        // todo:
        // todo:
    }

    fun includeProviderIntoBalancer(provider: Provider) {}

    fun excludeProviderFromBalancer(providerIdentifier: ProviderIdentifier) {}

    /**
     * the maximum number of providers accepted from the load balancer is 10
     */
    fun maxSupportedNumberOfProviders(): Int {
        return 10;
    }

    /**
     * The load balancer should invoke every X seconds each of its registered providers on a special method called
     * check() to discover if they are alive – if not, it should exclude the provider node from load balancing.
     *
     * this method might be called b y something like cron in a recurring manner
     */
    fun healthCheck() {}
}