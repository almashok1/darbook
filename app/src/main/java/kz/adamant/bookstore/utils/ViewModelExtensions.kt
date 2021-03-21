package kz.adamant.bookstore.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.androidx.viewmodel.scope.BundleDefinition
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

inline fun <reified VM : ViewModel> Fragment.sharedGraphViewModel(
    @IdRes navGraphId: Int,
    qualifier: Qualifier? = null,
    noinline initialState: BundleDefinition? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy {
    getKoin().getViewModel(
        qualifier = qualifier,
        state = initialState,
        owner = { ViewModelOwner.from(findNavController().getViewModelStoreOwner(navGraphId)) },
        clazz = VM::class,
        parameters = parameters
    )
}