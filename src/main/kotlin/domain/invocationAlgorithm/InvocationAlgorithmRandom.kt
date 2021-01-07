package domain.invocationAlgorithm

/**
 * when invoking multiple times the Load Balancer on its get() method, should cause the random invocation of the get()
 * method of any registered provider instance.
 */
class InvocationAlgorithmRandom: InvocationAlgorithm {
}