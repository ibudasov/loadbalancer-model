package domain.invocationAlgorithm

/**
 * when invoking multiple times the Load Balancer on its get() method, should cause the round-robin (sequential)
 * invocation of the get() method of the registered providers.
 */
class InvocationAlgorithmRoundRobin: InvocationAlgorithm {
}