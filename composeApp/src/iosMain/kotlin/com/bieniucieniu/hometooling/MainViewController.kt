package com.bieniucieniu.hometooling

import androidx.compose.ui.window.ComposeUIViewController
import com.bieniucieniu.hometooling.feat.storage.database.dbModule
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication({ modules(koinExampleModule, dbModule) }) {
        App()
    }
}