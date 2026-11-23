package com.youarelaunched.challenge.ui.vendors

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.youarelaunched.challenge.R
import com.youarelaunched.challenge.data.model.Category
import com.youarelaunched.challenge.data.model.Vendor
import com.youarelaunched.challenge.ui.theme.VendorsTheme

private val CoverShape = RoundedCornerShape(10.dp)

private val CoverGradient = Brush.verticalGradient(
    listOf(Color(0x00323232), Color(0xCC131313))
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VendorCard(
    vendor: Vendor,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .shadow(elevation = 4.dp, shape = CoverShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_image_placeholder),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
            AsyncImage(
                model = vendor.coverPhotoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(CoverGradient)
            )
            FavoriteButton(
                isFavorite = vendor.isFavorite,
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )
            AreaChip(
                area = vendor.areaServed,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
        Text(
            text = vendor.companyName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
        if (vendor.categories.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                vendor.categories.forEach { CategoryItem(it) }
            }
        }
        if (vendor.tags.isNotEmpty()) {
            Text(
                text = vendor.tags.joinToString(separator = " ") { "• $it" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val background by animateColorAsState(
        targetValue = if (isFavorite) colors.primary else colors.surface,
        label = "favoriteBackground"
    )
    val tint by animateColorAsState(
        targetValue = if (isFavorite) colors.onPrimary else colors.primary,
        label = "favoriteTint"
    )
    @DrawableRes val icon = if (isFavorite) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .toggleable(
                value = isFavorite,
                interactionSource = null,
                indication = ripple(color = colors.primary),
                role = Role.Checkbox,
                onValueChange = { onClick() }
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .background(background, CircleShape)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(R.string.save_vendor),
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun AreaChip(area: String, modifier: Modifier = Modifier) {
    Text(
        text = area,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
private fun CategoryItem(category: Category) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(category.iconUrl)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_image_placeholder),
            error = painterResource(R.drawable.ic_image_placeholder),
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VendorCardPreview() {
    VendorsTheme {
        VendorCard(vendor = PreviewVendor, onFavoriteClick = {}, modifier = Modifier.padding(16.dp))
    }
}

internal val PreviewVendor = Vendor(
    id = 1,
    companyName = "Bill & Jane Grocery",
    areaServed = "Birmingham",
    coverPhotoUrl = "",
    isFavorite = true,
    categories = listOf(
        Category(name = "Green Grocers", iconUrl = ""),
        Category(name = "Cafe & Restaurant", iconUrl = ""),
        Category(name = "Homeware", iconUrl = ""),
        Category(name = "Health & Wellbeing", iconUrl = "")
    ),
    tags = listOf("Best vegetables in town", "Discounts for customers")
)
