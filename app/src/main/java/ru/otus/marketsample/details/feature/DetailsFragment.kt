package ru.otus.marketsample.details.feature

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import ru.otus.common.di.findDependencies
import ru.otus.marketsample.details.feature.di.DaggerDetailsComponent
import javax.inject.Inject

internal const val PRODUCT_ID_ARG = "productId"

class DetailsFragment : Fragment() {

    @Inject
    lateinit var factory: DetailsViewModelFactory

    private val viewModel: DetailsViewModel by viewModels { factory }

    private val productId: String
        get() = requireArguments().getString(PRODUCT_ID_ARG).orEmpty()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerDetailsComponent.factory()
            .create(
                dependencies = findDependencies(),
                productId = productId,
            )
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = content {
        DetailsScreen(viewModel = viewModel)
    }
}
