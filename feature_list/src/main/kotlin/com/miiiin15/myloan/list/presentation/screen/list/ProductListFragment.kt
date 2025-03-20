package com.miiiin15.myloan.list.presentation.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import org.koin.androidx.navigation.koinNavGraphViewModel

class ProductListFragment : BaseFragment() {

    private val viewModel: ProductListViewmodel by koinNavGraphViewModel(R.id.listNavGraph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.fetchApplicant()
        return ComposeView(requireContext()).apply {
            setContent {
                ProductListScreen()
            }
        }
    }
}

@Composable
fun ProductListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BasicText(text = "Product List")
        Spacer(modifier = Modifier.height(8.dp))
        // 예시 데이터
        val products = listOf("Product 1", "Product 2", "Product 3")
        products.forEach { product ->
            BasicText(text = product)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    ProductListScreen()
}