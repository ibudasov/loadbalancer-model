package domain

import java.util.*

/**
 * This class meant to host all the providers which didn't succeed the health check,
 * so we put all of them here.
 * Health checker also checks all the guys here, and if they report good health twice -
 * they are back to the registry of healthy providers
 */
class ProviderRegistryQuarantine : Stack<Provider>()