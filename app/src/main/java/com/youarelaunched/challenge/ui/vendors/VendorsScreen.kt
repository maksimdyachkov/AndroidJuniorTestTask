package com.youarelaunched.challenge.ui.vendors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youarelaunched.challenge.R
import com.youarelaunched.challenge.ui.theme.VendorsTheme

@Composable
fun VendorsScreen(
    modifier: Modifier = Modifier,
    viewModel: VendorsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VendorsContent(
        uiState = uiState,
        onRetry = viewModel::loadVendors,
        onFavoriteClick = viewModel::onFavoriteClick,
        modifier = modifier
    )
}

@Composable
private fun VendorsContent(
    uiState: VendorsUiState,
    onRetry: () -> Unit,
    onFavoriteClick: (vendorId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        VendorsUiState.Loading -> CenteredContent(modifier) {
            CircularProgressIndicator()
        }

        VendorsUiState.Empty -> CenteredContent(modifier) {
            Text(text = stringResource(R.string.vendors_empty))
        }

        VendorsUiState.Error -> CenteredContent(modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = stringResource(R.string.vendors_error))
                Button(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }

        is VendorsUiState.Success -> LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
            contentPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Vertical)
                .add(WindowInsets(left = 16.dp, top = 24.dp, right = 16.dp, bottom = 24.dp))
                .asPaddingValues(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(items = uiState.vendors, key = { it.id }) { vendor ->
                VendorCard(vendor = vendor, onFavoriteClick = { onFavoriteClick(vendor.id) })
            }
        }
    }
}

@Composable
private fun CenteredContent(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun VendorsContentSuccessPreview() {
    VendorsTheme {
        VendorsContent(
            uiState = VendorsUiState.Success(listOf(PreviewVendor, PreviewVendor.copy(id = 2))),
            onRetry = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VendorsContentErrorPreview() {
    VendorsTheme {
        VendorsContent(uiState = VendorsUiState.Error, onRetry = {}, onFavoriteClick = {})
    }
}
