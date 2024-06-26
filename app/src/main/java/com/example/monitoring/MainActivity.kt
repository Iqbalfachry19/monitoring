package com.example.monitoring

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.annotation.ExperimentalCoilApi

import com.example.monitoring.ui.theme.MonitoringTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.UUID

data class NavigationItem<T>(
    val route: T,

    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon:ImageVector,
    val hasNews:Boolean,
    val badgeCount:Int? = null
)
data class Quadruple<T, U, V, W>(val first: T, val second: U, val third: V, val fourth: W)
data class Quintuple<T, U, V, W,X>(val first: T, val second: U, val third: V, val fourth: W, val fifth:X)
data class Sextuple<T, U, V, W,X,Y>(val first: T, val second: U, val third: V, val fourth: W, val fifth:X,val sixth:Y)
data class Septuple<T, U, V, W,X,Y,Z>(val first: T, val second: U, val third: V, val fourth: W, val fifth:X,val sixth:Y,val seven:Z)
data class Nilai<T, U, V, W,X,Y,Z>(val id:T,val nama: U, val mataPelajaranList: V, val semester:W,val kelas:X, val peringkat:Y,val rata:Z)
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data class TeacherDashboard(val role: String) : Screen
    @Serializable
    data class OrtuDashboard(val role: String) : Screen
    @Serializable
    data class Settings(val role: String) : Screen

    @Serializable
    data class AdminDashboard(val role:String) : Screen


    @Serializable
    data class DataGuru(val role:String) : Screen

    @Serializable
    data class ExportData(val role:String) : Screen

    @Serializable
    data class DataSiswa(val role: String) : Screen

    @Serializable
    data class DataJadwalPelajaran(val role: String) : Screen

    @Serializable
    data class DataJadwalUjian(val role: String)  : Screen
    @Serializable
    data class DataNilai(val role: String) : Screen
    @Serializable
    data class DataJadwalKegiatan(val role: String) : Screen

    @Serializable
    data class DataRekapan(val role: String) : Screen

    @Serializable
    data object TambahGuru : Screen

    @Serializable
    data object TambahStaff : Screen

    @Serializable
    data object TambahSiswa: Screen
    @Serializable
    data object TambahJadwalPelajaran: Screen


    @Serializable
    data object TambahJadwalUjian: Screen
    @Serializable
    data object TambahNilai: Screen

    @Serializable
    data object TambahJadwalKegiatan: Screen

    @Serializable
    data object TambahRekapan: Screen

    @Serializable
    data class EditGuru(val id:String): Screen

    @Serializable
    data class EditStaff(val id:String): Screen

    @Serializable
    data class EditSiswa(val id:String): Screen
    @Serializable
    data class EditJadwalPelajaran(val id:String): Screen
    @Serializable
    data class EditJadwalUjian(val id:String): Screen

    @Serializable
    data class EditNilai(val id:String): Screen
    @Serializable
    data class EditJadwalKegiatan(val id:String): Screen

    @Serializable
    data class EditRekapan(val id:String): Screen

}

data class CardItem<T>(
    val title: String,
    val route:T,
    val icon:ImageVector?=Icons.Filled.Person,
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MonitoringTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                NavHost(navController, startDestination = Screen.Login) {
                    composable<Screen.Login> {
                        LoginPage(navController)
                    }
                    composable<Screen.AdminDashboard> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.AdminDashboard>().role
                        ScreenWithScaffold(navController,role) {
                            AdminDashboardPage(navController,it,role)
                        }
                    }

                    composable<Screen.Settings>{
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.Settings>().role
                        ScreenWithScaffold(navController,role) {
                            SettingsPage(navController,it,role)
                        }
                    }
                    composable<Screen.DataGuru> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataGuru>().role
                        DataGuruPage(navController,role)
                    }
                    composable<Screen.ExportData> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.ExportData>().role
                        ExportData(navController,role)
                    }
                    composable<Screen.DataSiswa> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataSiswa>().role
                        DataSiswa(navController,role)
                    }
                    composable<Screen.DataJadwalPelajaran> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataJadwalPelajaran>().role

                        DataJadwalPelajaran(navController,role)
                    }
                    composable<Screen.DataJadwalUjian> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataJadwalUjian>().role

                        DataJadwalUjian(navController,role)
                    }
                    composable<Screen.DataNilai> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataNilai>().role

                        DataNilai(navController,role)
                    }
                    composable<Screen.DataJadwalKegiatan>{
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataJadwalKegiatan>().role

                        DataJadwalKegiatan(navController,role)
                    }
                    composable<Screen.DataRekapan> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataRekapan>().role

                        DataRekapan(navController,role)
                    }
                    composable<Screen.TambahGuru> {
                        TambahGuru(navController)
                    }

                    composable<Screen.TambahSiswa> {
                        TambahSiswa(navController)
                    }
                    composable<Screen.TambahJadwalPelajaran> {
                        TambahJadwalPelajaran(navController)
                    }
                    composable<Screen.TambahJadwalUjian> {
                        TambahJadwalUjian(navController)
                    }
                    composable<Screen.TambahNilai> {
                        TambahNilai(navController)
                    }
                    composable<Screen.TambahJadwalKegiatan> {
                        TambahJadwalKegiatan(navController)
                    }
                    composable<Screen.TambahRekapan> {
                        TambahRekapan(navController)
                    }
                    composable<Screen.EditGuru> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditGuru>().id
                        EditGuru(navController,id)
                    }
                    composable<Screen.EditSiswa> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditSiswa>().id
                        EditSiswa(navController,id)
                    }

                    composable<Screen.EditJadwalPelajaran> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditJadwalPelajaran>().id
                        EditJadwalPelajaran(navController,id)
                    }
                    composable<Screen.EditJadwalUjian> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditJadwalUjian>().id
                        EditJadwalUjian(navController,id)
                    }
                    composable<Screen.EditNilai> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditNilai>().id
                        EditNilai(navController,id)
                    }
                    composable<Screen.EditJadwalKegiatan> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditJadwalKegiatan>().id
                        EditJadwalKegiatan(navController,id)
                    }
                    composable<Screen.EditRekapan> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditRekapan>().id
                        EditRekapan(navController,id)
                    }
                    composable<Screen.TeacherDashboard> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.TeacherDashboard>().role
                        ScreenWithScaffold(navController,role) {
                            TeacherDashboard(navController,it,role)
                        }
                    }
                    composable<Screen.OrtuDashboard> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.OrtuDashboard>().role
                        ScreenWithScaffold(navController,role) {
                            OrtuDashboard(navController,it,role)
                        }
                    }
                }
            }
        }
    }
}
fun NavBackStackEntry?.fromRoute(role: String): Screen {
    this?.destination?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let {
            when (it) {
                Screen.TeacherDashboard::class.simpleName -> return Screen.TeacherDashboard(role)
                Screen.OrtuDashboard::class.simpleName -> return Screen.OrtuDashboard(role)
                Screen.AdminDashboard::class.simpleName -> return Screen.AdminDashboard(role)
                Screen.Settings::class.simpleName -> return Screen.Settings(role)
                else -> return Screen.AdminDashboard(role)
            }
        }
    return Screen.AdminDashboard(role)
}
@Composable
fun ScreenWithScaffold(navController: NavController,role: String, content: @Composable (PaddingValues) -> Unit) {


    val items1 = listOf(
        NavigationItem(
            route = when (role) {
                "admin" -> Screen.AdminDashboard(role)
                "guru 4", "guru 5", "guru 6" -> Screen.TeacherDashboard(role)
                "ortu" -> Screen.OrtuDashboard(role)
                else -> Screen.AdminDashboard(role)
            },
            title= "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews =false
        ),
//        NavigationItem(
//            route = Screen.Search,
//
//            title= "Search",
//            selectedIcon = Icons.Filled.Search,
//            unselectedIcon = Icons.Outlined.Search,
//            hasNews =false
//        ),
        NavigationItem(
            route = Screen.Settings(role),

            title= "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
            ,
            hasNews =false
        ),

        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.fromRoute(role)
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color( 0xFF003C43),

        bottomBar = {

                NavigationBar {
                    items1.forEach { item ->
                        Log.d("current",currentRoute.toString())
                        Log.d("current1",item.route.toString())
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(

                                indicatorColor = MaterialTheme.colorScheme.tertiary,
                            ),

                            selected = currentRoute == item.route,
                            onClick = {

                                navController.navigate(item.route){


                                    restoreState = true
                                }
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if(item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if(item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (currentRoute == item.route) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }

            }
        },
        content = content
    )

}
@Composable
fun LoginPage(navController: NavController) {
    // Implement login UI using Jetpack Compose components
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    // Function to check user role and navigate
    fun checkUserRoleAndNavigate(userId: String) {
        val usersRef = FirebaseUtil.firestore.collection("user")
        usersRef.document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    when (val role = document.getString("role")) {
                        "ortu" -> {
                            navController.navigate(Screen.OrtuDashboard(role)) {
                                popUpTo(Screen.Login) { inclusive = true }
                            }
                        }
                        "admin" -> {
                            navController.navigate(Screen.AdminDashboard(role)) {
                                popUpTo(Screen.Login) { inclusive = true }
                            }
                        }
                        "guru 4" -> {
                            navController.navigate(Screen.TeacherDashboard(role)) {
                                popUpTo(Screen.Login) { inclusive = true }
                            }
                        }
                        "guru 5" -> {
                            navController.navigate(Screen.TeacherDashboard(role)) {
                                popUpTo(Screen.Login) { inclusive = true }
                            }
                        }
                        "guru 6" -> {
                            navController.navigate(Screen.TeacherDashboard(role)) {
                                popUpTo(Screen.Login) { inclusive = true }
                            }
                        }
                        else -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Unauthorized access")
                            }
                        }
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("User document not found")
                    }
                }
            }
            .addOnFailureListener {
                scope.launch {
                    snackbarHostState.showSnackbar("Error fetching user role")
                }
            }
    }


    LaunchedEffect(Unit) {
        FirebaseUtil.auth.currentUser?.let { user ->
            isLoggedIn = true
            checkUserRoleAndNavigate(user.uid)
        }
    }

    if (!isLoggedIn) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
          modifier = Modifier.fillMaxSize(),
            containerColor = Color( 0xFF003C43),
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )
                    val (emailFocusRequester, passwordFocusRequester) = FocusRequester.createRefs()

                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color(0xFFE3FEF7),
                            unfocusedBorderColor = Color(0xFFE3FEF7),
                            unfocusedLabelColor = Color(0xFFE3FEF7),
                            focusedTextColor = Color(0xFFE3FEF7),
                        ),
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Nama") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp, vertical = 8.dp)
                        .focusRequester(emailFocusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            passwordFocusRequester.requestFocus()
                        })
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color(0xFFE3FEF7),
                            unfocusedBorderColor = Color(0xFFE3FEF7),
                            unfocusedLabelColor = Color(0xFFE3FEF7),
                            focusedTextColor = Color(0xFFE3FEF7),
                        ),
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp, vertical = 8.dp)
                        .focusRequester(passwordFocusRequester),

                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            handleLogin(email, password, navController, snackbarHostState, scope)
                        }),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        handleLogin(email, password, navController, snackbarHostState, scope)
                    }) {
                        Text("Login")
                    }
                }
            }
        )
    }
}
fun handleLogin(
    email: String,
    password: String,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    if (email.isBlank() || password.isBlank()) {
        // Show error message if email or password is empty
        scope.launch {
            snackbarHostState.showSnackbar("Email and password cannot be empty")
        }
    } else {
        val nameEmail = "$email@gmail.com"
        FirebaseUtil.auth.signInWithEmailAndPassword(nameEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Authentication successful, navigate to appropriate screen
                    val user = FirebaseUtil.auth.currentUser
                    if (user != null) {
                        Log.d("user", user.toString())
                        val userId = user.uid
                        val usersRef = FirebaseUtil.firestore.collection("user")
                        usersRef.document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val role = document.getString("role")
                                    when (role) {
                                        "admin" -> {
                                            navController.navigate(Screen.AdminDashboard(role)) {
                                                popUpTo(Screen.Login) { inclusive = true }
                                            }
                                        }
                                        "ortu" -> {
                                            navController.navigate(Screen.OrtuDashboard(role)) {
                                                popUpTo(Screen.Login) { inclusive = true }
                                            }
                                        }
                                        "guru 4", "guru 5", "guru 6" -> {
                                            navController.navigate(Screen.TeacherDashboard(role)) {
                                                popUpTo(Screen.Login) { inclusive = true }
                                            }
                                        }
                                        else -> {
                                            // User doesn't have required role, show error message
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Unauthorized access")
                                            }
                                        }
                                    }
                                } else {
                                    // Document doesn't exist
                                    scope.launch {
                                        snackbarHostState.showSnackbar("User document not found")
                                    }
                                }
                            }
                    }
                } else {
                    // Authentication failed, show error message
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    scope.launch {
                        snackbarHostState.showSnackbar(errorMessage)
                    }
                }
            }
    }
}
//@Composable
//fun NavigationSideBar(
//    items: List<NavigationItem<Objects>>,
//    selectedItemIndex:Int,
//    onNavigate:(Int)-> Unit,
//    padding:PaddingValues
//){
//NavigationRail(  modifier = Modifier
//    .background(MaterialTheme.colorScheme.inverseOnSurface)
//    .offset(x = (-1).dp)
//    .padding(padding)) {
//Column(            modifier = Modifier.fillMaxHeight(),
//    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
//) {
//
//
//    items.forEachIndexed { index, item ->
//        NavigationRailItem(
//            selected = selectedItemIndex == index,
//            onClick = { onNavigate(index) },
//            icon = {
//                NavigationIcon(
//                    item = item,
//                    selected = selectedItemIndex == index
//                )
//            },
//            label = { Text(text = item.title, textAlign = TextAlign.Center ) })
//    }
//}
//}
//}
//@Composable
//fun NavigationIcon(
//    item:NavigationItem<Objects>,
//    selected:Boolean
//){
//    BadgedBox(
//        badge = {
//            if(item.badgeCount != null){
//                Badge{
//                    Text(text = item.badgeCount.toString())
//                }
//            } else if(item.hasNews){
//                Badge()
//            }
//        }
//    ){
//Icon(imageVector = if(selected) item.selectedIcon else item.unselectedIcon,
//    contentDescription = item.title
//    )
//    }
//}

@Composable
fun AdminDashboardPage(navController: NavController,it:PaddingValues,role: String) {



//    val items = listOf(
//
//        NavigationBottomItem(
//            title= "Data Guru",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),
//
//        NavigationBottomItem(
//            title= "Data Staff",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person
//            ,
//            hasNews =false
//        ),
//        NavigationBottomItem(
//            title= "Data Siswa",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),
//        NavigationItem(
//            title= "Data \nJadwal \nPelajaran",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),
//        NavigationItem(
//            title= "Data \nJadwal Ujian",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),NavigationItem(
//            title= "Data \nNilai \ndan \nPeringkat\n Siswa",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),NavigationItem(
//            title= "Data \nPerkembangan \nNilai",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),NavigationItem(
//            title= "Data \nRekapan \nKehadiran Siswa",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person,
//            hasNews =false
//        ),
//    )


//        Row {
//
//            if(showNavigationRail){
//                NavigationSideBar(items = items, selectedItemIndex =selectedItemIndex, onNavigate = {selectedItemIndex = it},padding=it )
//
//            }

    // Implement admin dashboard UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = "Dashboard Admin", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
            Spacer(modifier = Modifier.width(65.dp))

        }
        Spacer(modifier = Modifier.height(8.dp))
        val cards= listOf(CardItem(
            title = "Data Guru",
            route = Screen.DataGuru(role)
        ),
            CardItem(
                title = "Export Data",
                route = Screen.ExportData(role),
                icon = Icons.Filled.Share
            ),
            CardItem(
                title = "Data Siswa",
                route = Screen.DataSiswa(role),

                ),
            CardItem(
                title = "Data Jadwal Pelajaran",
                route =Screen.DataJadwalPelajaran(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Jadwal Ujian",
                route =Screen.DataJadwalUjian(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Nilai dan Peringkat Siswa",
                route =Screen.DataNilai(role),
                icon = Icons.Filled.Star
            ),
            CardItem(
                title = "Data Kegiatan",
                route =Screen.DataJadwalKegiatan(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Rekapan Kehadiran Siswa",
                route =Screen.DataRekapan(role),
                icon = Icons.Filled.Face
            ),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(cards) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,

                        ),
                    modifier = Modifier
                        .size(width = 200.dp, height = 100.dp)
                        .clickable { navController.navigate(it.route) }
                        .padding(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        it.icon?.let { it1 ->
                            Icon(
                                imageVector = it1,
                                contentDescription = "Person"
                            )
                        }
                        Text(
                            text = it.title,
                            textAlign = TextAlign.Center
                        )
                    }   }
            }

        }

    }
//






}

@Composable
fun SettingsPage(navController: NavController,it: PaddingValues,role:String){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(it), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))

        Button(onClick = {

            FirebaseUtil.auth.signOut()
            navController.navigate(Screen.Login) {
                popUpTo(Screen.AdminDashboard(role)) { inclusive = true }
            }
        }){Text(text = "Logout")}
    }

}

@Composable
fun DataGuruPage(
    navController: NavController,
    role: String
) {

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Quadruple<String, String, String, String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }


    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("guru")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val name = document.getString("nama") ?: ""
                val keterangan = document.getString("keterangan") ?: ""
                val lp = document.getString("lp") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Quadruple(document.id, name, keterangan, lp))
            }
            dataList.sortBy { it.second }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("guru").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        floatingActionButton = {
if(role != "ortu" && role != "guru 4" && role != "guru 5" && role != "guru 6") {

    ExtendedFloatingActionButton(
        onClick = {
            // show snackbar as a suspend function

            navController.navigate(Screen.TambahGuru)
        }, containerColor = Color(0xFF77B0AA)
    ) {
        Text(
            "+",
            fontSize = 24.sp,
            color = Color(0xFFE3FEF7)
        )
    }
}
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Data Guru",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFFE3FEF7)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF77B0AA))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "L/P",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Ket.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Nama",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.weight(3f)
                    )
                    if (role == "admin") {
                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            items(dataList.size) { index ->
                val item = dataList[index]
                val (id, name, keterangan, lp) = item

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {


                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = lp,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = keterangan,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(3f)
                        )
                    }
                    if (role == "admin") {
                        IconButton(
                            onClick = {
                                // Navigate to Edit screen with the document id
                                navController.navigate(Screen.EditGuru(id))
                            }
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Gray
                            )
                        }


                        IconButton(
                            onClick = {
                                documentIdToDelete = id
                                showDialog = true
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }

                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp)) // Adjust the height as needed
            }
        }
    }
}




@Composable
fun DataSiswa(
    navController: NavController,
    role: String
){

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Quadruple<String,String, String, String>>() }
    var showDialog by remember { mutableStateOf(false) }

    var documentIdToDelete by remember { mutableStateOf<String?>(null) }
    var selectedFilter by remember { mutableStateOf("All") }
    var showDropdownMenu by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("siswa")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val name = document.getString("nama") ?: ""
                val keterangan = document.getString("keterangan") ?: ""
                val lp = document.getString("lp") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Quadruple(document.id, name, keterangan, lp))
            }
            dataList.sortBy { it.second }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("siswa").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(

        floatingActionButton = {
            if (role != "ortu") {
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function
                        navController.navigate(Screen.TambahSiswa)
                    },
                    containerColor = Color(0xFF77B0AA)
                ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Filter by class: ",
                    color = Color(0xFFE3FEF7)
                )
                Box {
                    IconButton(onClick = { showDropdownMenu = !showDropdownMenu }) {
                        Icon(
                            Icons.Default.MoreVert, contentDescription = "Filter", tint =
                            Color(0xFFE3FEF7)
                        )
                    }
                    DropdownMenu(
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text("All") }, onClick = {
                            selectedFilter = "All"
                            showDropdownMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text("Kelas 4")
                        }, onClick = {
                            selectedFilter = "4"
                            showDropdownMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text("Kelas 5")
                        }, onClick = {
                            selectedFilter = "5"
                            showDropdownMenu = false
                        })
                        DropdownMenuItem(text = {
                            Text("Kelas 6")
                        }, onClick = {
                            selectedFilter = "6"
                            showDropdownMenu = false
                        })
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Data Siswa",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFFE3FEF7)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(Color(0xFF77B0AA))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "L/P",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Kelas",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Nama",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            modifier = Modifier.weight(3f)
                        )
                        val anyEditable = dataList.any { (_, _, keterangan, _) ->
                            when (role) {
                                "admin" -> true
                                "guru 4" -> keterangan == "4" && (selectedFilter == "All" || selectedFilter == "4")
                                "guru 5" -> keterangan == "5" && (selectedFilter == "All" || selectedFilter == "5")
                                "guru 6" -> keterangan == "6" && (selectedFilter == "All" || selectedFilter == "6")
                                else -> false
                            }
                        }
                        if (anyEditable) {
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                        }

                    }
                }

                val filteredList = if (selectedFilter == "All") {
                    dataList
                } else {
                    dataList.filter { it.third == selectedFilter }
                }
                items(filteredList.size, key = { index -> filteredList[index].first }) { index ->
                    val (id, name, keterangan, lp) = filteredList[index]
                    val canEdit = when (role) {
                        "admin" -> true
                        "guru 4" -> keterangan == "4"
                        "guru 5" -> keterangan == "5"
                        "guru 6" -> keterangan == "6"
                        else -> false
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {


                        Row(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = lp,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = keterangan,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(3f)
                            )
                        }
                        if (canEdit) {
                            IconButton(
                                onClick = {
                                    // Navigate to Edit screen with the document id
                                    navController.navigate(Screen.EditSiswa(id))
                                }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )
                            }
                        }
                        if (role == "admin") {
                            IconButton(
                                onClick = {
                                    documentIdToDelete = id
                                    showDialog = true
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }

                    }
                }

                item {
                    Spacer(modifier = Modifier.height(72.dp)) // Adjust the height as needed
                }

            }
        }
    }
}
@Composable
fun DropdownMenuButton(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expandedState by remember { mutableStateOf(expanded) }

    Button(
        onClick = { expandedState = true },
        modifier = modifier
    ) {
        Text(buttonText)
    }

    DropdownMenu(
        expanded = expandedState,
        onDismissRequest = { expandedState = false },
        modifier = modifier
    ) {
        options.forEach { option ->
            DropdownMenuItem({
                Text(text = option)
            },onClick = {
                onOptionSelected(option)
                expandedState = false
            })
        }
    }
}

@Composable
fun DataJadwalPelajaran(
    navController: NavController,
    role: String
){

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Sextuple<String,String, String, String,String,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }
    val classOptions = listOf("4", "5", "6")
    val semesterOptions = listOf("1", "2")
    var selectedClass by remember { mutableStateOf(classOptions[0]) }
    var selectedSemester by remember { mutableStateOf(semesterOptions[0]) }
    // Function to filter data based on selected class and semester
    fun filterDataByClassAndSemester(dataList: List<Sextuple<String, String, String, String, String, String>>): List<Sextuple<String, String, String, String, String, String>> {
        return dataList.filter { (_, _, _, kelas, _, semester) ->
            kelas == selectedClass && semester == selectedSemester
        }
    }
    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("jadwalPelajaran")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val nama = document.getString("nama") ?: ""
                val jam = document.getString("jam") ?: ""
                val kelas = document.getString("kelas") ?: ""
                val hari = document.getString("hari") ?: ""
                val semester = document.getString("semester") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Sextuple(document.id,nama,jam,kelas,hari,semester))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("jadwalPelajaran").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        floatingActionButton = {
            if(role != "ortu" && role != "guru 4" && role != "guru 5" && role != "guru 6") {
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function

                        navController.navigate(Screen.TambahJadwalPelajaran)
                    }, containerColor = Color(0xFF77B0AA)) {
                    Text(
                        "+",
                        fontSize = 24.sp,
                        color = Color(0xFFE3FEF7)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            // Dropdown for class selection
            Text(
                text = "Data Jadwal Pelajaran",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFE3FEF7)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Kelas $selectedClass",
                    options = classOptions,
                    onOptionSelected = { selectedClass = it }
                )

                // Button for semester selection
                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Semester $selectedSemester",
                    options = semesterOptions,
                    onOptionSelected = { selectedSemester = it }
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Hari",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE3FEF7),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Mata Pelajaran",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE3FEF7),
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            "Jam",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE3FEF7),
                            modifier = Modifier.weight(2f)
                        )
                        if (role == "admin") {
                            Text(
                                "Edit",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFFE3FEF7),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    val daysOfWeek = listOf("senin", "selasa", "rabu", "kamis", "jumat", "sabtu")

                    daysOfWeek.forEach { day ->
                        val filteredData =
                            filterDataByClassAndSemester(dataList).filter { it.fifth == day }

                        if (filteredData.isNotEmpty()) {
                            filteredData.forEach { (id, nama, jam, _, hari, _) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        day,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFFE3FEF7),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        nama,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFFE3FEF7),
                                        modifier = Modifier.weight(2f)
                                    )
                                    Text(
                                        jam,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFFE3FEF7),
                                        modifier = Modifier.weight(2f)


                                    )


                                    if (role == "admin") {
                                        IconButton(
                                            onClick = {
                                                navController.navigate(Screen.EditJadwalPelajaran(id))
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Edit",
                                                tint = Color.Gray
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                documentIdToDelete = id
                                                showDialog = true
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DataJadwalUjian(
    navController: NavController,
    role: String
){

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Septuple<String,String, String, String,String,String,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }
    val classOptions = listOf("4", "5", "6")
    val semesterOptions = listOf("1", "2")
    var selectedClass by remember { mutableStateOf(classOptions[0]) }
    var selectedSemester by remember { mutableStateOf(semesterOptions[0]) }
    // Function to filter data based on selected class and semester
    fun filterDataByClassAndSemester(dataList: List<Septuple<String, String, String, String, String, String,String>>): List<Septuple<String, String, String, String,String, String, String>> {
        return dataList.filter { (_, _, _, kelas,_, _, semester) ->
            kelas == selectedClass && semester == selectedSemester
        }
    }


    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("jadwalUjian")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val nama = document.getString("nama") ?: ""
                val jam = document.getString("jam") ?: ""
                val kelas = document.getString("kelas") ?: ""
                val hari = document.getString("hari") ?: ""
                val tanggal = document.getString("tanggal") ?: ""
                val semester = document.getString("semester") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Septuple(document.id,nama,jam,kelas,hari,tanggal,semester))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("jadwalUjian").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        floatingActionButton = {
            if(role != "ortu" && role != "guru 4" && role != "guru 5" && role != "guru 6") {
            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahJadwalUjian)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
        }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Data Jadwal Ujian",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFE3FEF7)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Kelas $selectedClass",
                    options = classOptions,
                    onOptionSelected = { selectedClass = it }
                )

                // Button for semester selection
                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Semester $selectedSemester",
                    options = semesterOptions,
                    onOptionSelected = { selectedSemester = it }
                )
            }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    if (role == "admin") {
                        Text(
                            "Edit",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE3FEF7),
                            modifier = Modifier.padding(end = 42.dp)
                        )
                    }
                }
                // Display the data fetched from Firestore
                filterDataByClassAndSemester(dataList).forEach { (id, nama, jam, kelas, hari, tanggal, semester) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(2f)
                        ) {


                                Text(
                                    text = nama,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color=Color.White,
                                )



                                Text(
                                    text = jam,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color=Color.White
                                )

                                Text(
                                    text = hari,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color=Color.White
                                )

                                Text(
                                    text = tanggal,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color=Color.White
                                )

                            }

                        if (role == "admin") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .weight(1f)
                            ) {
                                IconButton(
                                    onClick = {
                                        // Navigate to Edit screen with the document id
                                        navController.navigate(Screen.EditJadwalUjian(id))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.Gray
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        documentIdToDelete = id
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
                }
            }
        }
    }
}
fun calculateAverage(matapelajaran: List<Pair<String, String>>): Double {
    var totalNilai = 0.0
    var jumlahPelajaran = 0

    // Iterate through each mata pelajaran and sum up the nilai
    matapelajaran.forEach { (_, nilai) ->
        totalNilai += nilai.toDoubleOrNull() ?: 0.0
        jumlahPelajaran++
    }

    // Calculate the average nilai
    return if (jumlahPelajaran > 0) {
        totalNilai / jumlahPelajaran
    } else {
        0.0 // Return 0 if there are no pelajaran or if nilai cannot be parsed
    }
}
@Composable
fun DataNilai(
    navController: NavController,
    role: String
){

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Nilai<String,String, List<Pair<String, String>>,String,String,String,Double>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }
    val classOptions = listOf("4", "5", "6")
    val semesterOptions = listOf("1", "2")
    var selectedClass by remember { mutableStateOf(classOptions[0]) }
    var selectedSemester by remember { mutableStateOf(semesterOptions[0]) }
    // Function to filter data based on selected class and semester
    fun filterDataByClassAndSemester(dataList: List<Nilai<String,String, List<Pair<String, String>>,String,String,String,Double>>): List<Nilai<String,String, List<Pair<String, String>>,String,String,String,Double>> {
        return dataList.filter { (_, _, _, semester, kelas, _,_) ->
            kelas == selectedClass && semester == selectedSemester
        }
    }

    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("nilai")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val nama = document.getString("nama") ?: ""
                val mataPelajaranArray = document.get("mata_pelajaran") as? ArrayList<HashMap<String, String>> ?: arrayListOf()

                // Iterate through each item in the mataPelajaranArray and extract nama and nilai
                val mataPelajaranList = mutableListOf<Pair<String, String>>()
                mataPelajaranArray.forEach { mataPelajaranMap ->
                    val namaPelajaran = mataPelajaranMap["nama"] ?: ""
                    val nilaiPelajaran = mataPelajaranMap["nilai"] ?: ""
                    mataPelajaranList.add(Pair(namaPelajaran, nilaiPelajaran))
                }
                val semester = document.getString("semester") ?: ""
                val kelas = document.getString("kelas") ?: ""
                val peringkat = document.getString("peringkat") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Nilai(document.id,nama, mataPelajaranList,semester, kelas,peringkat,
                    calculateAverage(mataPelajaranList)))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("nilai").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        floatingActionButton = {
            if (role != "ortu") {
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function

                        navController.navigate(Screen.TambahNilai)
                    }, containerColor = Color(0xFF77B0AA)) {
                    Text(
                        "+",
                        fontSize = 24.sp,
                        color = Color(0xFFE3FEF7)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            // Dropdown for class selection
            Text(
                text = "Data Nilai dan Peringkat Siswa",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFE3FEF7),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Kelas $selectedClass",
                    options = classOptions,
                    onOptionSelected = { selectedClass = it }
                )

                // Button for semester selection
                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Semester $selectedSemester",
                    options = semesterOptions,
                    onOptionSelected = { selectedSemester = it }
                )
            }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (role == "admin") {
                        Text(
                            "Edit",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE3FEF7),
                            modifier = Modifier.padding(end = 42.dp)
                        )
                    }
                }

                // Display the data fetched from Firestore
                filterDataByClassAndSemester(dataList).forEach { (id, nama, matapelajaran, semester, kelas, peringkat,rata) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,

                                ),

                            modifier = Modifier
                                .width(width = 250.dp)
                                .padding(8.dp)
                                .wrapContentHeight()
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            ) {
                                Text(
                                    text = "NAMA: $nama",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))



                                matapelajaran.forEach { (pelajaran, nilai) ->
                                    Text(
                                        text = "$pelajaran: $nilai",
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        color = Color.White
                                    )
                                }


                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Nilai rata-rata: %.2f".format(rata),
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "PERINGKAT: $peringkat",
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White
                                )
                            }
                        }

                        if (role == "admin") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .weight(1f)
                            ) {
                                IconButton(
                                    onClick = {
                                        // Navigate to Edit screen with the document id
                                        navController.navigate(Screen.EditNilai(id))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.Gray
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        documentIdToDelete = id
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
            }
        }
    }
}


@Composable
fun DataJadwalKegiatan(
    navController: NavController,
    role: String
){

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Sextuple<String,String, String, String,String,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }



    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("jadwalKegiatan")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val nama = document.getString("nama") ?: ""
                val jam = document.getString("jam") ?: ""
                val kelas = document.getString("kelas") ?: ""
                val hari = document.getString("hari") ?: ""
                val semester = document.getString("semester") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Sextuple(document.id,nama,jam,kelas,hari,semester))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("jadwalKegiatan").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        floatingActionButton = {
            if(role != "ortu" && role != "guru 4" && role != "guru 5" && role != "guru 6") {
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function

                        navController.navigate(Screen.TambahJadwalKegiatan)
                    }, containerColor = Color(0xFF77B0AA)) {
                    Text(
                        "+",
                        fontSize = 24.sp,
                        color = Color(0xFFE3FEF7)
                    )
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Text(text = "Data Kegiatan", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
                Spacer(modifier = Modifier.height(16.dp))
                // Display the data fetched from Firestore
                dataList.forEach { (id,nama,jam,kelas,hari,semester) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,

                                ),

                            modifier = Modifier
                                .width(width = 200.dp)
                                .wrapContentHeight()
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center,  modifier = Modifier.fillMaxSize().padding(8.dp)) {
                                Text(text = nama, style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = jam,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = hari,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )


                            }
                        }
                        if (role == "admin") {
                            IconButton(
                                onClick = {
                                    // Navigate to Edit screen with the document id
                                    navController.navigate(Screen.EditJadwalKegiatan(id))
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
                            }

                            IconButton(
                                onClick = {

                                    documentIdToDelete = id
                                    showDialog = true
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
val monthIndexMap = mapOf(
    "januari" to 1,
    "februari" to 2,
    "maret" to 3,
    "april" to 4,
    "mei" to 5,
    "juni" to 6,
    "juli" to 7,
    "agustus" to 8,
    "september" to 9,
    "oktober" to 10,
    "november" to 11,
    "desember" to 12
)
fun parseTanggal(tanggal: String): Triple<List<Int>, List<String>, Int> {
    val days = mutableListOf<Int>()
    val months = mutableListOf<String>()
    var year = 0

    // Split the tanggal string by ", " or " dan "
    val parts = tanggal.split(", ", " dan ")

    parts.forEach { part ->
        // Extract day and month
        val regexResult = Regex("(\\d+) ([a-zA-Z]+)").find(part)
        if (regexResult != null) {
            val (dayStr, monthStr) = regexResult.destructured
            val day = dayStr.toIntOrNull()
            if (day != null) {
                days.add(day)
                months.add(monthStr)
            }
        }

        // Extract year (assuming it's always at the end of the string)
        val yearRegexResult = Regex("\\d{4}$").find(part)
        if (yearRegexResult != null) {
            year = yearRegexResult.value.toIntOrNull() ?: 0
        }
    }

    return Triple(days, months, year)
}
@Composable
fun  DataRekapan(
    navController: NavController,
    role: String
){

    val firestore = FirebaseUtil.firestore
    val dataList = remember { mutableStateListOf<Sextuple<String,String, String, String,String,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }
    val classOptions = listOf("4", "5", "6")
    val semesterOptions = listOf("1", "2")
    var selectedClass by remember { mutableStateOf(classOptions[0]) }
    var selectedSemester by remember { mutableStateOf(semesterOptions[0]) }
    fun filterDataByClassAndSemester(dataList: List<Sextuple<String, String, String, String, String, String>>): List<Sextuple<String, String, String, String, String, String>> {
        return dataList.filter { (_, _, _, kelas, _, semester) ->
            kelas == selectedClass && semester == selectedSemester
        }.sortedBy { item ->
            val (_, months, _) = parseTanggal(item.third)
            months.map { month -> monthIndexMap[month.toLowerCase(Locale.ROOT)] ?: 0 }.minOrNull()
        }
    }

    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("absensi")

        // Listen for real-time updates to the Firestore collection
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the previous data before adding new data
            dataList.clear()

            // Add the new data to the list
            snapshot?.documents?.forEach { document ->
                val nama = document.getString("nama") ?: ""
                val tanggal = document.getString("tanggal") ?: ""
                val kelas = document.getString("kelas") ?: ""
                val keterangan = document.getString("keterangan") ?: ""
                val semester = document.getString("semester") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Sextuple(document.id,nama,tanggal,kelas,keterangan,semester))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        documentIdToDelete?.let { id ->
                            firestore.collection("absensi").document(id).delete()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        floatingActionButton = {
            if (role != "ortu") {
                ExtendedFloatingActionButton(
                    onClick = {
                        // show snackbar as a suspend function

                        navController.navigate(Screen.TambahRekapan)
                    }, containerColor = Color(0xFF77B0AA)) {
                    Text(
                        "+",
                        fontSize = 24.sp,
                        color = Color(0xFFE3FEF7)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Data Rekapan Siswa",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFE3FEF7)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Kelas $selectedClass",
                    options = classOptions,
                    onOptionSelected = { selectedClass = it }
                )

                // Button for semester selection
                DropdownMenuButton(
                    expanded = false, // Initially closed
                    onDismissRequest = {},
                    modifier = Modifier.padding(8.dp),
                    buttonText = "Semester $selectedSemester",
                    options = semesterOptions,
                    onOptionSelected = { selectedSemester = it }
                )
            }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                  // Display the data fetched from Firestore
                filterDataByClassAndSemester(dataList).forEach  { (id, nama, tanggal, kelas, keterangan, semester) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,

                                ),

                            modifier = Modifier
                                .width(width = 300.dp)
                                .padding(8.dp)
                                .wrapContentHeight()
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            ) {
                                Text(
                                    text = "Nama: $nama",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tanggal: $tanggal",
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Keterangan: $keterangan",
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )


                            }
                        }
                        if (role == "admin") {
                            IconButton(
                                onClick = {
                                    // Navigate to Edit screen with the document id
                                    navController.navigate(Screen.EditRekapan(id))
                                }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )
                            }

                            IconButton(
                                onClick = {

                                    documentIdToDelete = id
                                    showDialog = true
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
                }
            }
        }
    }
}




@Composable
fun ExportData(
    navController: NavController,
    role: String
) {
    val viewModel = viewModel<MainViewModel>()
    val context = LocalContext.current as Activity
    val firestore = FirebaseUtil.firestore
    var nilaiData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var absensiData by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var selectedClass by remember { mutableStateOf("4") }
    var exportType by remember { mutableStateOf<String?>(null) }
    val classOptions = listOf("4", "5", "6")
    val pdfSaveSuccess = remember { mutableStateOf(false) }
    val savedFilePath = remember { mutableStateOf<String?>(null) }

    // Function to request storage permission
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    // Handle permission requests on initial launch
    LaunchedEffect(Unit) {
        multiplePermissionResultLauncher.launch(permissionsToRequest)
    }

    // Function to handle PDF export success
    val handleExportSuccess: (File) -> Unit = { file ->
        pdfSaveSuccess.value = true
        savedFilePath.value = file.absolutePath
    }

    // Function to handle PDF export failure
    val handleExportError: (String) -> Unit = { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    // Fetch data based on export type and selected class
    LaunchedEffect(exportType, selectedClass) {
        if (exportType != null) {
            val collection = when (exportType) {
                "nilai" -> "nilai"
                "absensi" -> "absensi"
                else -> null
            }
            if (collection != null) {
                val documents = firestore.collection(collection).get().await()
                val fetchedData = documents.map { it.data }
                if (exportType == "nilai") {
                    nilaiData = fetchedData
                } else if (exportType == "absensi") {
                    absensiData = fetchedData
                }
            }
        }
    }

    // Filter data based on selected class
    val filteredNilaiData = nilaiData.filter { it["kelas"] == selectedClass }.sortedBy { it["nama"].toString() }
    val filteredAbsensiData = absensiData.filter { it["kelas"] == selectedClass }.sortedBy { it["nama"].toString() }

    // Show AlertDialog on PDF save success
    if (pdfSaveSuccess.value) {
        AlertDialog(
            onDismissRequest = {
                pdfSaveSuccess.value = false
            },
            title = { Text("File Saved") },
            text = {
                Column {
                    Text("PDF saved to ${savedFilePath.value}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                // Implement view action if needed
                                openPdfFile(context, savedFilePath.value)
                                pdfSaveSuccess.value = false
                            }
                        ) {
                            Text("View")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                // Implement ignore action if needed
                                pdfSaveSuccess.value = false
                            }
                        ) {
                            Text("Ignore")
                        }
                    }
                }
            },
            confirmButton = {

            }
        )
    }

    // Composable UI
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Export Data Siswa",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 200.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Dropdown menu for class selection
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true }) {
                Text("Pilih Kelas: $selectedClass")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                classOptions.forEach { classOption ->
                    DropdownMenuItem({
                        Text("Kelas $classOption")
                    },onClick = {
                        selectedClass = classOption
                        expanded = false
                    })
                }
            }
        }

        // Button to export nilai to PDF
        Button(
            onClick = {
                exportType = "nilai"
                if (role == "admin") {
                    exportDataToPDF(context, filteredNilaiData, exportType!!, selectedClass, handleExportSuccess, handleExportError)
                } else {
                    Toast.makeText(context, "Only admin can perform export", Toast.LENGTH_SHORT).show()
                }
                      },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Export Nilai to PDF")
        }

        // Button to export absensi to PDF
        Button(
            onClick = {
                exportType = "absensi"
                if (role == "admin") {
                    exportDataToPDF(context, filteredNilaiData, exportType!!, selectedClass, handleExportSuccess, handleExportError)
                } else {
                    Toast.makeText(context, "Only admin can perform export", Toast.LENGTH_SHORT).show()
                }
                      },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Export Absensi to PDF")
        }
    }
}

fun openPdfFile(context: Context, filePath: String?) {
    if (filePath.isNullOrEmpty()) {
        Toast.makeText(context, "File path is null or empty", Toast.LENGTH_SHORT).show()
        return
    }

    val file = File(filePath)
    if (!file.exists()) {
        Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show()
        return
    }

    val uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
    }
}

fun exportDataToPDF(
    context: Context,
    staffData: List<Map<String, Any>>,
    type: String,
    selectedClass:String,
    onSuccess: (file: File) -> Unit,
    onError: (String) -> Unit
) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val paint = Paint().apply {
        textSize = 12f
        isAntiAlias = true
    }

    var yPos = 50f
    var currentPage = pdfDocument.startPage(pageInfo)
    var canvas = currentPage.canvas

    staffData.forEach { data ->
        if (yPos > 800f) {
            pdfDocument.finishPage(currentPage)
            currentPage = pdfDocument.startPage(pageInfo)
            canvas = currentPage.canvas
            yPos = 50f
        }

        canvas.drawText("Nama: ${data["nama"] ?: ""}", 50f, yPos, paint)
        yPos += 20f
        canvas.drawText("Kelas: ${data["kelas"] ?: ""}", 50f, yPos, paint)

        if (type == "absensi") {
            yPos += 20f
            canvas.drawText("Keterangan: ${data["keterangan"] ?: ""}", 50f, yPos, paint)
            yPos += 20f
            canvas.drawText("Semester: ${data["semester"] ?: ""}", 50f, yPos, paint)
            yPos += 20f
            canvas.drawText("Tanggal: ${data["tanggal"] ?: ""}", 50f, yPos, paint)
        } else if (type == "nilai") {
            yPos += 20f
            canvas.drawText("Semester: ${data["semester"] ?: ""}", 50f, yPos, paint)
            yPos += 20f
            canvas.drawText("Peringkat: ${data["peringkat"] ?: ""}", 50f, yPos, paint)
            yPos += 20f
            val mataPelajaran = data["mata_pelajaran"] as? List<Map<String, Any>> ?: emptyList()
            mataPelajaran.forEach { pelajaran ->
                canvas.drawText("${pelajaran["nama"] ?: ""}: ${pelajaran["nilai"] ?: ""}", 50f, yPos, paint)
                yPos += 20f
            }
        }

        yPos += 40f
    }

    pdfDocument.finishPage(currentPage)
    val safeFilename = "export_${type}_kelas_${selectedClass}.pdf"

    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (downloadsDir != null) {
        val file = File(downloadsDir, safeFilename)
        try {
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()
            onSuccess(file) // Invoke success callback
        } catch (e: Exception) {
            pdfDocument.close()
            onError("Failed to save PDF: ${e.message}")
        }
    } else {
        onError("Failed to access downloads directory")
    }
}




@Composable
fun TambahSiswa(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var lp by remember { mutableStateOf("") }



    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Tambah Data Siswa", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        // Display the selected image




        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Keterangan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = lp,
            onValueChange = { lp = it },
            label = { Text("L/P") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseSiswa(name, description, lp,navController,dataId= "siswa")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}


@Composable
fun TambahJadwalPelajaran(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Jadwal Pelajaran", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        // Display the selected image

        // Button to select image


        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Mata Pelajaran") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = jam,
            onValueChange = { jam = it },
            label = { Text("Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = hari,
            onValueChange = { hari = it },
            label = { Text("Hari") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabasePelajaran(name, kelas,jam,hari,semester,navController)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}
@Composable
fun TambahJadwalKegiatan(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }



    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Kegiatan", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        // Display the selected image

        // Button to select image
        TextField(
            value = hari,
            onValueChange = { hari = it.lowercase() },
            label = { Text("Hari") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Kegiatan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        TextField(
            value = jam,
            onValueChange = { jam = it },
            label = { Text("Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )



        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseKegiatan(name, kelas,jam,hari,semester,navController)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}


@Composable
fun TambahRekapan(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Rekapan Kehadiran Siswa", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        // Display the selected image

        // Button to select image


        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Siswa") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            label = { Text("Keterangan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseAbsensi(name, kelas,tanggal,keterangan,semester,navController)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}



@Composable
fun TambahNilai(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var mataPelajaran by remember { mutableStateOf("") }
    var nilai by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var peringkat by remember { mutableStateOf("") }
    var mataPelajaranList by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Tambah Data Nilai dan Peringkat Siswa",
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Siswa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (isEditing) {
                EditNilai(
                    mataPelajaranList = mataPelajaranList,
                    index = editingIndex,
                    onDone = { updatedEntry ->
                        mataPelajaranList = mataPelajaranList.toMutableList().apply {
                            set(editingIndex, updatedEntry)
                        }
                        isEditing = false
                        editingIndex = -1
                    }
                )
            } else {
                TextField(
                    value = mataPelajaran,
                    onValueChange = { mataPelajaran = it },
                    label = { Text("Mata Pelajaran") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                TextField(
                    value = nilai,
                    onValueChange = { nilai = it },
                    label = { Text("Nilai") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                    Button(
                        onClick = {
                            if (mataPelajaran.isNotBlank() && nilai.isNotBlank()) {
                                val newEntry = mapOf("nama" to mataPelajaran, "nilai" to nilai)
                                mataPelajaranList = mataPelajaranList + newEntry
                                mataPelajaran = ""
                                nilai = ""
                            }
                        },
                        modifier = Modifier

                            .padding(16.dp)
                    ) {
                        Text("Tambah Mata Pelajaran")
                    }
                }
            }

            if (mataPelajaranList.isNotEmpty()) {
                Column {
                    Text("Daftar Mata Pelajaran:", modifier = Modifier.padding(16.dp))
                    mataPelajaranList.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item["nama"]} - ${item["nilai"]}",
                            )
                            Row {


                                Button(onClick = {
                                    isEditing = true
                                    editingIndex = index
                                }) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    mataPelajaranList = mataPelajaranList.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
            TextField(
                value = semester,
                onValueChange = { semester = it },
                label = { Text("Semester") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextField(
                value = kelas,
                onValueChange = { kelas = it },
                label = { Text("Kelas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextField(
                value = peringkat,
                onValueChange = { peringkat = it },
                label = { Text("Peringkat") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                Button(
                    onClick = {
                        submitDataToDatabaseNilai(
                            name,
                            mataPelajaranList,
                            semester,
                            kelas,
                            peringkat,
                            navController
                        )
                    },
                    modifier = Modifier

                        .padding(16.dp)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
@Composable
fun EditNilai(
    mataPelajaranList: List<Map<String, String>>,
    index: Int,
    onDone: (Map<String, String>) -> Unit
) {
    var mataPelajaran by remember { mutableStateOf(mataPelajaranList[index]["nama"] ?: "") }
    var nilai by remember { mutableStateOf(mataPelajaranList[index]["nilai"] ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Edit Data Nilai",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        TextField(
            value = mataPelajaran,
            onValueChange = { mataPelajaran = it },
            label = { Text("Mata Pelajaran") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = nilai,
            onValueChange = { nilai = it },
            label = { Text("Nilai") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                val updatedEntry = mapOf("nama" to mataPelajaran, "nilai" to nilai)
                onDone(updatedEntry)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Simpan Perubahan")
        }
    }
}

@Composable
fun TambahJadwalUjian(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }



    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Jadwal Ujian", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        // Display the selected image

        // Button to select image
        TextField(
            value = hari,
            onValueChange = { hari = it },
            label = { Text("Hari") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Mata Pelajaran") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = jam,
            onValueChange = { jam = it },
            label = { Text("Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseUjian(name, kelas,jam,hari,tanggal,semester,navController)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TambahGuru(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var lp by remember { mutableStateOf("") }



    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        // Display the selected image
        Text(text="Tambah Data Guru", modifier = Modifier
            .align(Alignment.CenterHorizontally))



        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Keterangan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Text field for description
        TextField(
            value =lp,
            onValueChange = { lp = it },
            label = { Text("L/P") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, lp,navController,dataId= "guru")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}
@Composable
fun EditNilai(
    navController: NavController,
    nilaiId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var mataPelajaran by remember { mutableStateOf("") }
    var nilai by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var peringkat by remember { mutableStateOf("") }
    var mataPelajaranList by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }

    // Fetch existing data from Firestore
    LaunchedEffect(nilaiId) {
        val document = firestore.collection("nilai").document(nilaiId).get().await()
        name = document.getString("nama") ?: ""
        peringkat = document.getString("peringkat") ?: ""
        kelas = document.getString("kelas") ?: ""
        semester = document.getString("semester") ?: ""
        val mataPelajaranArray = document.get("mata_pelajaran") as? List<Map<String, String>>
        mataPelajaranList = mataPelajaranArray ?: listOf()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                Text(
                    text = "Edit Data Nilai dan Peringkat",
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Text field for student name
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Siswa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Display list of mata pelajaran with edit option
            if (mataPelajaranList.isNotEmpty()) {
                Column {
                    Text("Daftar Mata Pelajaran:", modifier = Modifier.padding(16.dp))
                    mataPelajaranList.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item["nama"]} - ${item["nilai"]}",
                            )
                            Row {
                                Button(onClick = {
                                    isEditing = true
                                    editingIndex = index
                                    mataPelajaran = item["nama"] ?: ""
                                    nilai = item["nilai"] ?: ""
                                }) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    mataPelajaranList = mataPelajaranList.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }

            // Form fields for mata pelajaran and nilai
            TextField(
                value = mataPelajaran,
                onValueChange = { mataPelajaran = it },
                label = { Text("Mata Pelajaran") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            TextField(
                value = nilai,
                onValueChange = { nilai = it },
                label = { Text("Nilai") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Button to save or update mata pelajaran
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                Button(
                    onClick = {
                        if (mataPelajaran.isNotBlank() && nilai.isNotBlank()) {
                            val newEntry = mapOf("nama" to mataPelajaran, "nilai" to nilai)
                            if (isEditing && editingIndex >= 0) {
                                mataPelajaranList = mataPelajaranList.toMutableList().apply {
                                    this[editingIndex] = newEntry
                                }
                                isEditing = false
                                editingIndex = -1
                            } else {
                                mataPelajaranList = mataPelajaranList + newEntry
                            }
                            mataPelajaran = ""
                            nilai = ""
                        }
                    },
                    modifier = Modifier

                        .padding(16.dp)
                ) {
                    Text(if (isEditing) "Update Mata Pelajaran" else "Tambah Mata Pelajaran")
                }
            }
            // Text field for peringkat
            TextField(
                value = semester,
                onValueChange = { semester = it },
                label = { Text("Semester") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            // Text field for peringkat
            TextField(
                value = kelas,
                onValueChange = { kelas = it },
                label = { Text("Kelas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            // Text field for peringkat
            TextField(
                value = peringkat,
                onValueChange = { peringkat = it },
                label = { Text("Peringkat") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        submitUpdatedDataToDatabaseNilai(
                            name,
                            mataPelajaranList,
                            semester,
                            kelas,
                            peringkat,
                            navController,
                            nilaiId
                        )
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Save Changes")
                }
            }
        }
    }

}
@Composable
fun EditRekapan(
    navController: NavController,
    rekapanId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }

    var tanggal by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    // Fetch existing data from Firestore
    LaunchedEffect(rekapanId) {
        val document = firestore.collection("absensi").document(rekapanId).get().await()
        name = document.getString("nama") ?: ""
        kelas = document.getString("kelas") ?: ""
        tanggal = document.getString("tanggal") ?: ""
        keterangan = document.getString("keterangan") ?: ""
        semester = document.getString("semester") ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Edit Data Rekapan Kehadiran Siswa", modifier = Modifier.align(Alignment.CenterHorizontally))

        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Siswa") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for class
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for day
        TextField(
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            label = { Text("Keterangan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value =semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        // Button to submit data
        Button(
            onClick = {
                submitUpdatedDataToDatabaseAbsensi(name, kelas,  tanggal,keterangan,semester, navController, rekapanId)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}


fun submitUpdatedDataToDatabaseAbsensi(
    name: String,
    kelas: String,
    tanggal: String,
    keterangan: String,
    semester: String,
    navController: NavController,
    rekapanId: String
) {
    val firestore = FirebaseUtil.firestore

    // Update the Firestore document with the new data
    firestore.collection("absensi").document(rekapanId)
        .update(
            mapOf(
                "nama" to name,
                "kelas" to kelas,
                "tanggal" to tanggal,
                "keterangan" to keterangan,
                "semester" to semester
            )
        )
        .addOnSuccessListener {
            // Navigate back after successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle the error
            Log.e("EditNilai", "Error updating document", e)
        }
}

fun submitUpdatedDataToDatabaseNilai(
    name: String,
    mataPelajaranList: List<Map<String, String>>,
    semester: String,
    kelas: String,
    peringkat: String,
    navController: NavController,
    nilaiId: String
) {
    val firestore = FirebaseUtil.firestore

    // Update the Firestore document with the new data
    firestore.collection("nilai").document(nilaiId)
        .update(
            mapOf(
                "nama" to name,
                "mata_pelajaran" to mataPelajaranList,
                "semester" to semester,
                "kelas" to kelas,
                "peringkat" to peringkat
            )
        )
        .addOnSuccessListener {
            // Navigate back after successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle the error
            Log.e("EditNilai", "Error updating document", e)
        }
}

@Composable
fun EditJadwalUjian(
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    // Fetch existing data from Firestore
    LaunchedEffect(jadwalId) {
        val document = firestore.collection("jadwalUjian").document(jadwalId).get().await()
        name = document.getString("nama") ?: ""
        kelas = document.getString("kelas") ?: ""
        jam = document.getString("jam") ?: ""
        hari = document.getString("hari") ?: ""
        tanggal = document.getString("tanggal") ?: ""
        semester = document.getString("semester") ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Edit Data Jadwal Ujian", modifier = Modifier.align(Alignment.CenterHorizontally))
        // Text field for day
        TextField(
            value = hari,
            onValueChange = { hari = it },
            label = { Text("Hari") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Mata Pelajaran") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for class
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for time
        TextField(
            value = jam,
            onValueChange = { jam = it },
            label = { Text("Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        // Text field for day
        TextField(
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitUpdatedDataToDatabaseUjian(name, kelas, jam, hari, tanggal,semester, navController, jadwalId)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}
@Composable
fun EditJadwalPelajaran(
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    // Fetch existing data from Firestore
    LaunchedEffect(jadwalId) {
        val document = firestore.collection("jadwalPelajaran").document(jadwalId).get().await()
        name = document.getString("nama") ?: ""
        kelas = document.getString("kelas") ?: ""
        jam = document.getString("jam") ?: ""
        hari = document.getString("hari") ?: ""
        semester = document.getString("semester") ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Edit Data Jadwal Pelajaran", modifier = Modifier.align(Alignment.CenterHorizontally))

        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Mata Pelajaran") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for class
        TextField(
            value = kelas,
            onValueChange = { kelas = it },
            label = { Text("Kelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for time
        TextField(
            value = jam,
            onValueChange = { jam = it },
            label = { Text("Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for day
        TextField(
            value = hari,
            onValueChange = { hari = it },
            label = { Text("Hari") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitUpdatedDataToDatabasePelajaran(name, kelas, jam, hari,semester, navController, jadwalId)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}
@Composable
fun EditJadwalKegiatan(
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    // Fetch existing data from Firestore
    LaunchedEffect(jadwalId) {
        val document = firestore.collection("jadwalKegiatan").document(jadwalId).get().await()
        name = document.getString("nama") ?: ""
        kelas = document.getString("kelas") ?: ""
        jam = document.getString("jam") ?: ""
        hari = document.getString("hari") ?: ""
        semester = document.getString("semester") ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Edit Data Kegiatan", modifier = Modifier.align(Alignment.CenterHorizontally))


        // Text field for day
        TextField(
            value = hari,
            onValueChange = { hari = it.lowercase() },
            label = { Text("Hari") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Kegiatan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )



        // Text field for time
        TextField(
            value = jam,
            onValueChange = { jam = it },
            label = { Text("Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )



        // Button to submit data
        Button(
            onClick = {
                submitUpdatedDataToDatabaseKegiatan(name, kelas, jam, hari,semester, navController, jadwalId)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}

fun submitUpdatedDataToDatabaseUjian(
    name: String,
    kelas: String,
    jam: String,
    hari: String,
    tanggal: String,
    semester: String,
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseUtil.firestore
    val dataMap = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "tanggal" to tanggal,
        "semester" to semester,
    )

    firestore.collection("jadwalUjian").document(jadwalId).set(dataMap)
        .addOnSuccessListener {
            // Navigate back to the list screen after a successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors
            Log.e("EditJadwalPelajaran", "Error updating document", e)
        }
}
// Function to submit updated data to Firestore
fun submitUpdatedDataToDatabasePelajaran(
    name: String,
    kelas: String,
    jam: String,
    hari: String,
    semester: String,
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseUtil.firestore
    val dataMap = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "semester" to semester
    )

    firestore.collection("jadwalPelajaran").document(jadwalId).set(dataMap)
        .addOnSuccessListener {
            // Navigate back to the list screen after a successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors
            Log.e("EditJadwalPelajaran", "Error updating document", e)
        }
}
fun submitUpdatedDataToDatabaseKegiatan(
    name: String,
    kelas: String,
    jam: String,
    hari: String,
    semester: String,
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseUtil.firestore
    val dataMap = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "semester" to semester
    )

    firestore.collection("jadwalKegiatan").document(jadwalId).set(dataMap)
        .addOnSuccessListener {
            // Navigate back to the list screen after a successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors
            Log.e("EditJadwalPelajaran", "Error updating document", e)
        }
}
//@OptIn(ExperimentalCoilApi::class)
//@Composable
//fun EditStaff(
//    navController: NavController,
//    guruId: String
//) {
//    val firestore = FirebaseFirestore.getInstance()
//    var name by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
//    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
//        uri?.let {
//            uploadImageToFirebase(it) { downloadUrl ->
//                imageDownloadUrl = downloadUrl
//            }
//        }
//    }
//
//    // Fetch existing data from Firestore
//    LaunchedEffect(guruId) {
//        val document = firestore.collection("staff").document(guruId).get().await()
//        name = document.getString("nama") ?: ""
//        description = document.getString("keterangan") ?: ""
//        imageDownloadUrl = document.getString("imageUrl")
//    }
//
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//
//        Text(text="Edit Data Staff", modifier = Modifier.align(Alignment.CenterHorizontally))
//
//        // Display the selected image
//        imageDownloadUrl?.let { imageUrl ->
//            Image(
//                painter = rememberImagePainter(imageUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(150.dp)
//                    .padding(16.dp),
//                alignment = Alignment.Center
//            )
//        }
//
//        // Button to select image
//        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
//            Text("Select Image")
//        }
//
//        // Text field for name
//        TextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Nama") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//
//        // Text field for description
//        TextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Keterangan") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//
//        // Button to submit data
//        Button(
//            onClick = {
//                submitDataToDatabase(name, description, imageDownloadUrl, navController, guruId, data = "staff")
//            },
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(16.dp)
//        ) {
//            Text("Save Changes")
//        }
//    }
//}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditGuru(
    navController: NavController,
    guruId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var lp by remember { mutableStateOf("") }


    // Fetch existing data from Firestore
    LaunchedEffect(guruId) {
        val document = firestore.collection("guru").document(guruId).get().await()
        name = document.getString("nama") ?: ""
        description = document.getString("keterangan") ?: ""
        lp = document.getString("lp")?:""
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Edit Data Guru", modifier = Modifier.align(Alignment.CenterHorizontally))



        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Keterangan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Text field for description
        TextField(
            value = lp,
            onValueChange = { lp = it },
            label = { Text("L/P") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, lp, navController, guruId, data = "guru")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditSiswa(
    navController: NavController,
    guruId: String
) {
    val firestore = FirebaseUtil.firestore
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var lp by remember { mutableStateOf("") }


    // Fetch existing data from Firestore
    LaunchedEffect(guruId) {
        val document = firestore.collection("siswa").document(guruId).get().await()
        name = document.getString("nama") ?: ""
        description = document.getString("keterangan") ?: ""
        lp = document.getString("lp")?:""
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Edit Data Siswa", modifier = Modifier.align(Alignment.CenterHorizontally))



        // Text field for name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Text field for description
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Keterangan") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = lp,
            onValueChange = { lp = it },
            label = { Text("L/P") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseSiswa(name, description, lp, navController, guruId, data = "siswa")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}

// Function to submit data to Firestore
fun submitDataToDatabaseSiswa(
    name: String,
    description: String,
    lp: String,
    navController: NavController,
    documentId: String,
    data: String
) {
    val firestore = FirebaseUtil.firestore
    val dataMap = hashMapOf(
        "nama" to name,
        "keterangan" to description,
        "lp" to lp
    )

    firestore.collection(data).document(documentId).set(dataMap)
        .addOnSuccessListener {
            // Navigate back to the list screen after a successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors
            Log.e("EditGuru", "Error updating document", e)
        }
}
fun submitDataToDatabase(
    name: String,
    description: String,
    lp: String,
    navController: NavController,
    documentId: String,
    data: String
) {
    val firestore = FirebaseUtil.firestore
    val dataMap = hashMapOf(
        "nama" to name,
        "keterangan" to description,
        "lp" to lp
    )

    firestore.collection(data).document(documentId).set(dataMap)
        .addOnSuccessListener {
            // Navigate back to the list screen after a successful update
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors
            Log.e("EditGuru", "Error updating document", e)
        }
}
private fun submitDataToDatabaseSiswa(name: String, description: String, lp:String,
                                      navController: NavController,dataId:String) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection(dataId)

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "keterangan" to description,
        "lp" to lp
    )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}
private fun submitDataToDatabase(name: String, description: String, lp: String,
                                 navController: NavController,dataId:String) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection(dataId)

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "keterangan" to description,
        "lp" to lp
    )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}
private fun submitDataToDatabaseNilai(name: String,mataPelajaran: List<Map<String, String>>,semester:String,kelas:String,peringkat:String,
                                      navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection("nilai")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "mata_pelajaran" to mataPelajaran,
        "semester" to semester,
        "kelas" to kelas,
        "peringkat" to peringkat,

        )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}
private fun submitDataToDatabaseAbsensi(name: String, kelas:String,tanggal:String,keterangan: String,semester: String,
                                        navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection("absensi")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "tanggal" to tanggal,
        "keterangan" to keterangan,
        "semester" to semester
    )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}
private fun submitDataToDatabaseUjian(name: String, kelas:String,jam:String ,hari:String,tanggal:String,semester: String,
                                      navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection("jadwalUjian")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "tanggal" to tanggal,
        "semester" to semester
    )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}
private fun submitDataToDatabasePelajaran(name: String, kelas:String,jam:String ,hari:String,semester: String,
                                          navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection("jadwalPelajaran")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "semester" to semester
    )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}
private fun submitDataToDatabaseKegiatan(name: String, kelas:String,jam:String ,hari:String,semester: String,
                                         navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = FirebaseUtil.firestore
    val collectionRef = firestore.collection("jadwalKegiatan")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "semester" to semester
    )

    // Set the data for the document
    documentRef.set(data)
        .addOnSuccessListener {
            // Document successfully written
            Log.d("Firestore", "DocumentSnapshot successfully written!")
            navController.popBackStack()
        }
        .addOnFailureListener { e ->
            // Handle any errors that may occur while writing the document
            Log.w("Firestore", "Error writing document", e)
        }
}

fun uploadImageToFirebase(uri: Uri, callback: (String) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val imagesRef = storageRef.child("images/${UUID.randomUUID()}")

    val uploadTask = imagesRef.putFile(uri)

    uploadTask.addOnSuccessListener {
        // Image uploaded successfully
        // Now, retrieve the download URL
        imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
            val imageUrl = downloadUri.toString()
            // Pass the download URL to the callback function
            callback(imageUrl)
        }.addOnFailureListener { exception ->
            // Handle any errors that may occur while getting the download URL
            Log.e("FirebaseStorage", "Failed to get download URL: ${exception.message}")
        }
    }.addOnFailureListener { exception ->
        // Handle any errors that may occur while uploading the image
        Log.e("FirebaseStorage", "Failed to upload image: ${exception.message}")
    }
}
@Composable
fun OrtuDashboard(
    navController: NavController,
    it: PaddingValues,
    role: String
) {




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = "Dashboard Ortu", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
            Spacer(modifier = Modifier.width(65.dp))

        }
        Spacer(modifier = Modifier.height(8.dp))
        val cards= listOf(CardItem(
            title = "Data Guru",
            route = Screen.DataGuru(role)
        ),
            CardItem(
                title = "Export Data",
                route = Screen.ExportData(role),
                icon = Icons.Filled.Share
            ),
            CardItem(
                title = "Data Siswa",
                route = Screen.DataSiswa(role)
            ),
            CardItem(
                title = "Data Jadwal Pelajaran",
                route =Screen.DataJadwalPelajaran(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Jadwal Ujian",
                route =Screen.DataJadwalUjian(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Nilai dan Peringkat Siswa",
                route =Screen.DataNilai(role),
                icon = Icons.Filled.Star
            ),
            CardItem(
                title = "Data Kegiatan",
                route =Screen.DataJadwalKegiatan(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Rekapan Kehadiran Siswa",
                route =Screen.DataRekapan(role),
                icon = Icons.Filled.Face
            ),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(cards) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,

                        ),
                    modifier = Modifier
                        .size(width = 200.dp, height = 100.dp)
                        .clickable { navController.navigate(it.route) }
                        .padding(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        it.icon?.let { it1 ->
                            Icon(
                                imageVector = it1,
                                contentDescription = "Person"
                            )
                        }
                        Text(
                            text = it.title,
                            textAlign = TextAlign.Center
                        )
                    }   }
            }

        }

    }
//


}
@Composable
fun TeacherDashboard(
    navController: NavController,
    it: PaddingValues,
    role: String
) {



    // Implement admin dashboard UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = "Dashboard Guru", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
            Spacer(modifier = Modifier.width(65.dp))

        }
        Spacer(modifier = Modifier.height(8.dp))
        val cards= listOf(CardItem(
            title = "Data Guru",
            route = Screen.DataGuru(role)
        ),
            CardItem(
                title = "Export Data",
                route = Screen.ExportData(role),
                icon = Icons.Filled.Share
            ),
            CardItem(
                title = "Data Siswa",
                route = Screen.DataSiswa(role)
            ),
            CardItem(
                title = "Data Jadwal Pelajaran",
                route =Screen.DataJadwalPelajaran(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Jadwal Ujian",
                route =Screen.DataJadwalUjian(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Nilai dan Peringkat Siswa",
                route =Screen.DataNilai(role),
                icon = Icons.Filled.Star
            ),
            CardItem(
                title = "Data Kegiatan",
                route =Screen.DataJadwalKegiatan(role),
                icon = Icons.Filled.DateRange
            ),
            CardItem(
                title = "Data Rekapan Kehadiran Siswa",
                route =Screen.DataRekapan(role),
                icon = Icons.Filled.Face
            ),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(cards) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,

                        ),
                    modifier = Modifier
                        .size(width = 200.dp, height = 100.dp)
                        .clickable { navController.navigate(it.route) }
                        .padding(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        it.icon?.let { it1 ->
                            Icon(
                                imageVector = it1,
                                contentDescription = "Person"
                            )
                        }
                        Text(
                            text = it.title,
                            textAlign = TextAlign.Center
                        )
                    }   }
            }

        }

    }
//


}
@Preview(showBackground = true, name = "Login Page - Dark Mode")
@Composable
fun PreviewLoginPage() {
    MonitoringTheme(darkTheme = true) {

            LoginPage(navController = rememberNavController())

    }
}
@Preview(showBackground = true, name = "Rekapan Page - Dark Mode")
@Composable
fun PreviewRekapanPage() {


    MonitoringTheme(darkTheme = true) {

       DataRekapan(navController = rememberNavController(),"admin")

    }
}
@Preview(showBackground = true, name = "Dashboard Admin Page - Dark Mode")
@Composable
fun PreviewDashboardAdminPage() {
    MonitoringTheme(darkTheme = true) {
        ScreenWithScaffold(rememberNavController(),"admin") {
            AdminDashboardPage(rememberNavController(),it,"admin")
        }
    }
}