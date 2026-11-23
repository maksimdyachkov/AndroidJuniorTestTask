package com.youarelaunched.challenge.ui.vendors

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VendorCard(vendor: Vendor, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box {
            AsyncImage(
                model = vendor.coverPhotoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .shadow(elevation = 4.dp, shape = CoverShape)
                    .clip(CoverShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            FavoriteBadge(
                isFavorite = vendor.isFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )
        if (vendor.categories.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 8.dp)
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
private fun FavoriteBadge(isFavorite: Boolean, modifier: Modifier = Modifier) {
    val background = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White
    val tint = if (isFavorite) Color.White else MaterialTheme.colorScheme.primary
    @DrawableRes val icon = if (isFavorite) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
    val description = stringResource(if (isFavorite) R.string.vendor_saved else R.string.vendor_not_saved)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(36.dp)
            .background(background, CircleShape)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun AreaChip(area: String, modifier: Modifier = Modifier) {
    Text(
        text = area,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .background(Color.White.copy(alpha = 0.8f), CircleShape)
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
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VendorCardPreview() {
    VendorsTheme {
        VendorCard(vendor = PreviewVendor, modifier = Modifier.padding(16.dp))
    }
}

internal val PreviewVendor = Vendor(
    id = 1,
    companyName = "Bill & Jane Grocery",
    areaServed = "Birmingham",
    coverPhotoUrl = "",
    isFavorite = true,
    categories = listOf(
        Category(id = 1, name = "Green Grocers", iconUrl = ""),
        Category(id = 2, name = "Cafe & Restaurant", iconUrl = ""),
        Category(id = 3, name = "Homeware", iconUrl = ""),
        Category(id = 4, name = "Health & Wellbeing", iconUrl = "")
    ),
    tags = listOf("Best vegetables in town", "Discounts for customers")
)
