package com.youarelaunched.challenge.ui.vendors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.youarelaunched.challenge.R
import com.youarelaunched.challenge.ui.theme.VendorsTheme

@Composable
fun VendorsScreen(
    modifier: Modifier = Modifier,
    viewModel: VendorsViewModel = viewModel(factory = VendorsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VendorsScreen(uiState = uiState, onRetry = viewModel::loadVendors, modifier = modifier)
}

@Composable
fun VendorsScreen(
    uiState: VendorsUiState,
    onRetry: () -> Unit,
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

        is VendorsUiState.Success -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(items = uiState.vendors, key = { it.id }) { vendor ->
                VendorCard(vendor = vendor)
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
private fun VendorsScreenSuccessPreview() {
    VendorsTheme {
        VendorsScreen(
            uiState = VendorsUiState.Success(listOf(PreviewVendor, PreviewVendor.copy(id = 2))),
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VendorsScreenErrorPreview() {
    VendorsTheme {
        VendorsScreen(uiState = VendorsUiState.Error, onRetry = {})
    }
}
