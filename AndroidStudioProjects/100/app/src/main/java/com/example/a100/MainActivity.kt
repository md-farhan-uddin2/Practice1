package com.example.a100

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a100.ui.theme._100Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _100Theme {
                App()
            }
        }
    }
}

// 1. Data Class representing a single Contact item
data class Contact(
    val name: String,
    val number: String
)

// 2. ViewModel managing contact list and state
class MyViewModel : ViewModel() {
    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var contactList by mutableStateOf(listOf<Contact>())

    fun newContact() {
        if (name.isNotBlank() && number.isNotBlank()) {
            contactList = contactList + Contact(name, number)
            name = ""
            number = ""
        }
    }
}

// 3. Add Contact Screen (Screen 1)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    navController: NavController,
    myViewModel: MyViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Contact") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = myViewModel.name,
                onValueChange = { myViewModel.name = it },
                label = { Text("Name") },
                placeholder = { Text("Enter your name") },
                modifier = Modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = myViewModel.number,
                onValueChange = { myViewModel.number = it },
                label = { Text("Mobile Number") },
                placeholder = { Text("Enter your mobile number") },
                modifier = Modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    myViewModel.newContact()
                    navController.navigate("screen2")
                }
            ) {
                Text("Save contact")
            }
        }
    }
}

// 4. Contacts List Screen (Screen 2)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(
    navController: NavController,
    myViewModel: MyViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("screen1")
                }
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(myViewModel.contactList) { contact ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Name: ${contact.name}")
                            Text("Number: ${contact.number}")
                        }
                    }
                }
            }
        }
    }
}

// 5. App Setup
@Composable
fun App() {
    val navController = rememberNavController()
    val myViewModel: MyViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "screen1"
    ) {
        composable("screen1") {
            Screen1(navController, myViewModel)
        }
        composable("screen2") {
            Screen2(navController, myViewModel)
        }
    }
}
