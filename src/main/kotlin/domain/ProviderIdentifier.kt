package domain

class ProviderIdentifier(val uniqueString: String) {
    override fun toString(): String {
        return uniqueString
    }
}