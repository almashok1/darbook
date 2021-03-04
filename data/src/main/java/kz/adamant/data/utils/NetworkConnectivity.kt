package kz.adamant.data.utils

class NetworkConnectivity {
    companion object {
        @Volatile
        var isNetworkAvailable: Boolean = false
    }
}