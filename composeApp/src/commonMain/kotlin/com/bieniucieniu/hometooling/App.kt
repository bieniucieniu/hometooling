package com.bieniucieniu.hometooling

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bieniucieniu.hometooling.feat.storage.database.AppDatabase
import com.bieniucieniu.hometooling.feat.storage.database.TodoEntity
import hometooling.composeapp.generated.resources.Res
import hometooling.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.dsl.module

data class KoinExample(val name: String)

val koinExampleModule = module {
    single { KoinExample("chuj 1 2 3 4") }
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val db: AppDatabase = koinInject()
        val todo = remember { MutableStateFlow<List<TodoEntity>?>(null) }
        val scope = rememberCoroutineScope()
        suspend fun addToDo() {
            val dao = db.getDao()
            dao.insert(TodoEntity(title = "title", content = "content"))
            todo.emit(db.getDao().getAll())
        }

        val state by todo.collectAsState()
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val koinExample = koinInject<KoinExample>()
            Button(onClick = { showContent = !showContent; scope.launch { addToDo() } }) {
                Text("Click me! ${koinExample.name}, ${db::class.simpleName}")
            }


            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
            LazyColumn {
                state?.let { it1 ->
                    items(it1) {
                        Text("${it.title} ${it.content}")
                    }
                } ?: item {
                    Text("loading")
                }
            }
        }
    }
}