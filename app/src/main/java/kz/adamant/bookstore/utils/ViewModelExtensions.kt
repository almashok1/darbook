package kz.adamant.bookstore.utils

import androidx.annotation.NavigationRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

inline fun <reified T: ViewModel> NavController.viewModel(@NavigationRes navGraphId: Int): T {
    val storeOwner = getViewModelStoreOwner(navGraphId)
    return ViewModelProvider(storeOwner)[T::class.java]
}

inline fun <reified T: ViewModel> NavController.viewModel(@NavigationRes navGraphId: Int, factory: ViewModelProvider.Factory): T {
    val storeOwner = getViewModelStoreOwner(navGraphId)
    return ViewModelProvider(storeOwner, factory)[T::class.java]
}