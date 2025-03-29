package com.miiiin15.myloan.list.presentation.screen.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.miiii15.myloan.list.R
import com.miiiin15.myloan.base.common.res.Dimen
import com.miiiin15.myloan.base.presentation.activity.BaseFragment
import com.miiiin15.myloan.base.presentation.compose.composable.DataNotFoundAnim
import com.miiiin15.myloan.base.presentation.compose.composable.ExpandableSection
import com.miiiin15.myloan.base.presentation.compose.composable.ProgressIndicator
import com.miiiin15.myloan.base.presentation.compose.composable.TextDynamic
import com.miiiin15.myloan.base.presentation.compose.composable.TextType
import com.miiiin15.myloan.list.domain.model.ProductDetail
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel.UiState
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel.UiState.DetailContent
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel.UiState.Error
import com.miiiin15.myloan.list.presentation.screen.detail.ProductDetailViewModel.UiState.Loading
import org.koin.androidx.navigation.koinNavGraphViewModel

class ProductDetailFragment : BaseFragment() {
    private val viewModel: ProductDetailViewModel by koinNavGraphViewModel(R.id.listNavGraph)
    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val productType = args.producType

        return ComposeView(requireContext()).apply {
            setContent {
                ProductDetailScreen(viewModel, productType)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
    }
}


@Composable
private fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productType: String
) {
    val uiState: UiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchProductDetail(productType)
    }

    uiState.let {
        when (it) {
            is DetailContent -> {
                ProductDetailContent(
                    productDetail = it.productDetail,
                    onDocClick = { /* TODO: 상품설명서 클릭 이벤트 */ },
                    onTermsClick = { /* TODO: 여신거래기본약관 클릭 이벤트 */ },
                    onApplyClick = viewModel::onApplyButtonClick
                )
            }

            Loading -> ProgressIndicator()
            Error -> DataNotFoundAnim()
        }
    }
}

@Composable
private fun ProductDetailContent(
    productDetail: ProductDetail,
    onDocClick: () -> Unit,
    onTermsClick: () -> Unit,
    onApplyClick: () -> Unit
) {


    val scrollState = rememberScrollState()
    var isDetailExpanded by remember { mutableStateOf(true) }
    var isNoticeExpanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = Dimen.spaceXXL) // FloatingButton 공간
        ) {
            // 상단 상품 정보
            ProductHeader(
                productName = productDetail.productName,
                rateRange = productDetail.rateRange,
                duration = productDetail.duration,
                limitAmount = productDetail.limitAmount
            )

            // 상품안내 토글
            ExpandableSection(
                title = "상품안내",
                expanded = isDetailExpanded,
                onToggle = { isDetailExpanded = !isDetailExpanded },
                contentModifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                productDetail.descriptionList.forEach { (title, desc) ->
                    TextDynamic(text = "\u2022 $title", type = TextType.BodyMedium)
                    TextDynamic(text = desc, type = TextType.BodySmall)
                    Spacer(modifier = Modifier.height(Dimen.spaceS))
                }
            }

            // 유의사항 토글
            ExpandableSection(
                title = "유의사항",
                expanded = isNoticeExpanded,
                onToggle = { isNoticeExpanded = !isNoticeExpanded }
            ) {
                productDetail.noticeList.forEach { (title, desc) ->
                    TextDynamic(text = "\u2022 $title", type = TextType.BodyMedium)
                    TextDynamic(text = desc, type = TextType.BodySmall)
                    Spacer(modifier = Modifier.height(Dimen.spaceS))
                }
            }

            // 설명서 및 약관 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimen.spaceL),
                horizontalArrangement = Arrangement.spacedBy(Dimen.spaceS)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    onClick = onDocClick
                ) {
                    Text(text = "상품설명서", color = Color.White)
                }
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
                    onClick = onTermsClick
                ) {
                    Text(text = "여신거래기본약관", color = Color.White)
                }
            }
        }

        // 하단 고정 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(Dimen.spaceL)
        ) {
            Button(
                onClick = onApplyClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "신청")
            }
        }
    }
}

@Composable
fun ProductHeader(
    productName: String,
    rateRange: String,
    duration: String,
    limitAmount: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(Dimen.spaceL)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inversePrimary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.height(100.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                TextDynamic(
                    text = productName,
                    type = TextType.TitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(Dimen.spaceM))
                TextDynamic(
                    text = "대출금리 $rateRange",
                    type = TextType.BodyMedium,
                    modifier = Modifier.padding(top = Dimen.spaceL),
                )
            }
            Image(
                painter = painterResource(id = R.drawable.img_loan),
                contentDescription = null,
                modifier = Modifier.size(Dimen.imageSize)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimen.spaceL),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextDynamic(text = "대출기간 $duration", type = TextType.BodyMedium)
            TextDynamic(
                text = "한도 $limitAmount",
                type = TextType.BodyMedium
            )
        }
    }
}