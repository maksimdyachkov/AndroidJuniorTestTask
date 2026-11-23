package com.youarelaunched.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.youarelaunched.challenge.ui.theme.VendorsTheme
import com.youarelaunched.challenge.ui.vendors.VendorsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            VendorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VendorsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
