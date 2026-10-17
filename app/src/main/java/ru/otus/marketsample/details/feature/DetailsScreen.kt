package ru.otus.marketsample.details.feature

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.otus.marketsample.R

private val PurpleGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFBB86FC), Color(0xFF6200EE)),
)
private val DiscountShape = RoundedCornerShape(
    topStart = 40.dp,
    topEnd = 10.dp,
    bottomEnd = 40.dp,
    bottomStart = 40.dp,
)

@Composable
internal fun DetailsScreen(viewModel: DetailsViewModel) {
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
            DetailsContent(details = state.detailsState)
        }
    }
}

@Composable
private fun DetailsContent(details: DetailsState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        AsyncImage(
            model = details.image,
            contentDescription = details.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = details.name,
            modifier = Modifier.wrapContentWidth(),
            color = Color.Black,
            fontSize = 24.sp,
        )

        if (details.hasDiscount) {
            Text(
                text = details.discount,
                modifier = Modifier
                    .align(Alignment.End)
                    .background(PurpleGradient, DiscountShape)
                    .border(2.dp, Color.White, DiscountShape)
                    .padding(horizontal = 10.dp),
                color = Color.White,
                fontSize = 20.sp,
            )
        }

        Text(
            text = stringResource(R.string.price_with_arg, details.price),
            modifier = Modifier
                .align(Alignment.End)
                .padding(14.dp),
            color = Color(0xFF6200EE),
            fontSize = 18.sp,
        )

        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.End)
                .padding(10.dp),
            contentPadding = PaddingValues(14.dp),
        ) {
            Text(
                text = "Add to cart",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}
