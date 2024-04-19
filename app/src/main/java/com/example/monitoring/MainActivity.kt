package com.example.monitoring

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monitoring.ui.theme.MonitoringTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitoringTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginPage(navController)
                    }
                    composable("adminDashboard") {
                        AdminDashboard()
                    }
                    composable("teacherDashboard") {
                        TeacherDashboard()
                    }
                }
            }
        }
    }
}

@Composable
fun LoginPage(navController: NavController) {
    // Implement login UI using Jetpack Compose components
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                var clickCount by remember { mutableStateOf(0) }
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Snackbar # ${++clickCount}"
                            )
                        }
                    }
                ) { Text("Show snackbar") }
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions { /* Handle next action if needed */ }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions { /* Handle done action if needed */ }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        val nameEmail = "$email@gmail.com"
                        auth.signInWithEmailAndPassword(nameEmail, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Authentication successful, navigate to appropriate screen
                                    val user = auth.currentUser
                                    if (user != null) {
                                        Log.d("user",user.toString()    )
                                        val userId = user.uid
                                        val usersRef =
                                            FirebaseFirestore.getInstance().collection("user")
                                        usersRef.document(userId).get()
                                            .addOnSuccessListener { document ->
                                                if (document != null) {
                                                    val role = document.getString("role")
                                                    if (role == "admin") {
                                                        navController.navigate("adminDashboard")
                                                    } else if (role == "guru") {
                                                        navController.navigate("teacherDashboard")

                                                    } else {
                                                        // User doesn't have required role, show error message
                                                        CoroutineScope(Dispatchers.Main).launch {
                                                            snackbarHostState.showSnackbar("Unauthorized access")
                                                        }
                                                    }
                                                } else {
                                                    // Document doesn't exist
                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        snackbarHostState.showSnackbar("User document not found")
                                                    }
                                                }
                                            }

                                    } else {
                                        // Authentication failed, show error message
                                        val errorMessage =
                                            task.exception?.message ?: "Unknown error"
                                        CoroutineScope(Dispatchers.Main).launch {
                                            snackbarHostState.showSnackbar(errorMessage)
                                        }
                                    }
                                }
                            }
                    }) {
                        Text("Login")
                    }
                }
            }
        )

}


@Composable
fun AdminDashboard() {
    // Implement admin dashboard UI
    Text(text = "admin")
}

@Composable
fun TeacherDashboard() {
    // Implement teacher dashboard UI
    Text(text = "guru")
}