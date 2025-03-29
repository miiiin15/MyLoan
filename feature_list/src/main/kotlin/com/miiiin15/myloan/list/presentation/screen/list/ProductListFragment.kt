package com.miiiin15.myloan.list.presentation.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.common.collect.ImmutableList
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import com.miiiin15.myloan.base.presentation.compose.composable.DataNotFoundAnim
import com.miiiin15.myloan.base.presentation.compose.composable.ProgressIndicator
import com.miiiin15.myloan.list.domain.model.Product
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState.Content
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState.Error
import com.miiiin15.myloan.list.presentation.screen.list.ProductListViewmodel.UiState.Loading
import com.miiiin15.myloan.list.presentation.screen.list.component.ProductCard
import org.koin.androidx.navigation.koinNavGraphViewModel

class ProductListFragment : BaseFragment() {

    private val viewModel: ProductListViewmodel by koinNavGraphViewModel(R.id.listNavGraph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ProductListScreen(viewModel)
            }
        }
    }
}

@Composable
private fun ProductListScreen(viewModel: ProductListViewmodel) {
    val uiState: UiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchApplicant()
        viewModel.fetchProductList()
    }

    uiState.let {
        when (it) {
            Error -> DataNotFoundAnim()
            Loading -> ProgressIndicator()
            is Content -> {
                ProductGrid(
                    productList = it.productList,
                    onProductClick = viewModel::onProductClick
                )
            }
        }
    }

}

@Composable
private fun ProductGrid(
    productList: ImmutableList<Product>,
    onProductClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(Dimen.screenContentPadding),
        contentPadding = PaddingValues(Dimen.screenContentPadding),
    ) {
        items(items = productList, key = { it.productType }) { product ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.spaceS)
                    .wrapContentSize(),
                enabled = product.sale,
                onClick = { onProductClick(product.productType) }
            ) {
                ProductCard(product)
            }
        }
    }
}


