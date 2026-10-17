package ru.otus.marketsample.products.feature

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.otus.marketsample.R

private val ProductImageShape = RoundedCornerShape(12.dp)
private val DiscountShape = RoundedCornerShape(
    topStart = 40.dp,
    topEnd = 10.dp,
    bottomEnd = 40.dp,
    bottomStart = 40.dp,
)
private val PurpleGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFBB86FC), Color(0xFF6200EE)),
)
private val PriceBackground = Color(0xFFFFF3E0)
private val PriceTextColor = Color(0xFF6200EE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    viewModel: ProductListViewModel,
    onProductClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.hasError) {
        if (state.hasError) {
            Toast.makeText(
                context,
                state.errorProvider(context),
                Toast.LENGTH_SHORT,
            ).show()
            viewModel.errorHasShown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(
                        items = state.productListState,
                        key = { product -> product.id },
                    ) { product ->
                        ProductRow(
                            product = product,
                            onClick = { onProductClick(product.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductRow(
    product: ProductState,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(130.dp)
                .clip(ProductImageShape),
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            if (product.hasDiscount) {
                androidx.compose.material3.Text(
                    text = product.discount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(PurpleGradient, DiscountShape)
                        .border(2.dp, Color.White, DiscountShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(130.dp)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            androidx.compose.material3.Text(
                text = product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

            androidx.compose.material3.Text(
                text = stringResource(R.string.price_with_arg, product.price),
                modifier = Modifier
                    .align(Alignment.End)
                    .background(PriceBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = PriceTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
