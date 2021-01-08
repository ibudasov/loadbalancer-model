package domain

class LoadBalancer {

    private val registry = ProviderRegistry()

    /**
     * the maximum number of providers accepted from the load balancer is 10
     */
    private val maximumNumberOfProvidersAcceptedFromTheLoadBalancer = 10

    fun get() {
        // todo: check if it's possible to accept one more request
        // todo: algorithm instance
        // todo: get a provider out of registry according to the algorithm
        // todo:
        // todo:
    }

    fun includeProviderIntoBalancer(provider: Provider) {
        if (registry.size == maximumNumberOfProvidersAcceptedFromTheLoadBalancer) {
            throw SorryCannotAddProviderBecauseOfMaxLimit()
        }

        registry.push(provider)
    }

    fun excludeProviderFromBalancer(providerIdentifier: ProviderIdentifier) {
        // todo: pop it out or regystry
        // todo: add it to the dead regystry
    }

    /**
     * The load balancer should invoke every X seconds each of its registered providers on a special method called
     * check() to discover if they are alive – if not, it should exclude the provider node from load balancing.
     *
     * this method might be called b y something like cron in a recurring manner
     */
    fun healthCheck() {
        // todo: check every provider
        // todo: once it's dead - add it to ther dead regystry
        // todo:
        // todo: chhechhk every dead provider
        // todo: once alive - add back to the alive register
    }
}