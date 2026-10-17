package ru.otus.marketsample.products.feature

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.core.os.bundleOf
import ru.otus.common.di.findDependencies
import ru.otus.marketsample.products.feature.di.DaggerProductListComponent
import javax.inject.Inject

class ProductListFragment : Fragment() {

    @Inject
    lateinit var factory: ProductListViewModelFactory

    private val viewModel: ProductListViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerProductListComponent.factory()
            .create(findDependencies())
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = content {
        ProductListContent(
            viewModel = viewModel,
            onProductClick = { productId ->
                requireActivity().supportFragmentManager.setFragmentResult(
                    PRODUCT_CLICK_RESULT,
                    bundleOf(PRODUCT_ID_RESULT_ARG to productId),
                )
            },
        )
    }
}
