package com.example.monitoring

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.monitoring.ui.theme.MonitoringTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import java.util.Objects
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
data class Nilai<T, U, V, W>(val id:T,val nama: U, val mataPelajaranList: V, val peringkat:W)
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data class TeacherDashboard(val role: String) : Screen

    @Serializable
    data class Settings(val role: String) : Screen

    @Serializable
    data class AdminDashboard(val role:String) : Screen


    @Serializable
    data class DataGuru(val role:String) : Screen

    @Serializable
    data class DataStaff(val role:String) : Screen

    @Serializable
    data class DataSiswa(val role: String) : Screen

    @Serializable
    data class DataJadwalPelajaran(val role: String) : Screen

    @Serializable
    data class DataJadwalUjian(val role: String)  : Screen
    @Serializable
    data class DataNilai(val role: String) : Screen
    @Serializable
    data object DataPerkembangan : Screen

    @Serializable
    data object DataRekapan : Screen

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
    data object TambahPerkembangan: Screen

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
    data class EditPerkembangan(val id:String): Screen

    @Serializable
    data class EditRekapan(val id:String): Screen

}

data class CardItem<T>(
    val title: String,
    val route:T,
    val icon:ImageVector?=Icons.Filled.Person,
)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    composable<Screen.DataStaff> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.DataStaff>().role
                        DataStaffPage(navController,role)
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
                    composable<Screen.DataPerkembangan>{
                        DataPerkembangan(navController)
                    }
                    composable<Screen.DataRekapan> {
                        DataRekapan(navController)
                    }
                    composable<Screen.TambahGuru> {
                        TambahGuru(navController)
                    }
                    composable<Screen.TambahStaff> {
                        TambahStaff(navController)
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
                    composable<Screen.TambahPerkembangan> {
                        TambahPerkembangan(navController)
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
                    composable<Screen.EditStaff> {
                            backStackEntry ->
                        val id = backStackEntry.toRoute<Screen.EditStaff>().id
                        EditStaff(navController,id)
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
                    composable<Screen.TeacherDashboard> {
                            backStackEntry ->
                        val role = backStackEntry.toRoute<Screen.TeacherDashboard>().role
                        ScreenWithScaffold(navController,role) {
                            TeacherDashboard(navController,it,role)
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

                Screen.AdminDashboard::class.simpleName -> return Screen.AdminDashboard(role)
                Screen.Settings::class.simpleName -> return Screen.Settings(role)
                else -> return Screen.AdminDashboard(role)
            }
        }
    return Screen.AdminDashboard(role)
}
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ScreenWithScaffold(navController: NavController,role: String, content: @Composable (PaddingValues) -> Unit) {

    val activity = LocalContext.current as Activity
    val windowClass = calculateWindowSizeClass(activity)
    val showNavigationRail = windowClass.widthSizeClass == WindowWidthSizeClass.Compact
    val items1 = listOf(
        NavigationItem(
            route = if (role == "admin") Screen.AdminDashboard(role) else Screen.TeacherDashboard(role) ,

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
        bottomBar = {
            if(showNavigationRail){
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
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    // Function to check user role and navigate
    fun checkUserRoleAndNavigate(userId: String) {
        val usersRef = FirebaseFirestore.getInstance().collection("user")
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

    // Check if the user is already logged in
    LaunchedEffect(auth) {
        auth.currentUser?.let { user ->
            isLoggedIn = true
            checkUserRoleAndNavigate(user.uid)
        }
    }

        if (!isLoggedIn) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
//            floatingActionButton = {
//                var clickCount by remember { mutableStateOf(0) }
//                ExtendedFloatingActionButton(
//                    onClick = {
//                        // show snackbar as a suspend function
//                        scope.launch {
//                            snackbarHostState.showSnackbar(
//                                "Snackbar # ${++clickCount}"
//                            )
//                        }
//                    }
//                ) { Text("Show snackbar") }
//            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions { /* Handle next action if needed */ }
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
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions { /* Handle done action if needed */ },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            // Show error message if email or password is empty
                            scope.launch {
                                snackbarHostState.showSnackbar("Email and password cannot be empty")
                            }
                        } else {
                            val nameEmail = "$email@gmail.com"
                            auth.signInWithEmailAndPassword(nameEmail, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Authentication successful, navigate to appropriate screen
                                        val user = auth.currentUser
                                        if (user != null) {
                                            Log.d("user", user.toString())
                                            val userId = user.uid
                                            val usersRef =
                                                FirebaseFirestore.getInstance().collection("user")
                                            usersRef.document(userId).get()
                                                .addOnSuccessListener { document ->
                                                    if (document != null) {
                                                        val role = document.getString("role")
                                                        if (role == "admin") {
                                                            navController.navigate(Screen.AdminDashboard(role)) {
                                                                popUpTo(Screen.Login) {
                                                                    inclusive = true
                                                                }
                                                            }
                                                        } else if(role ==   "guru 4"){
                                                            navController.navigate(Screen.TeacherDashboard(role)) {
                                                                popUpTo(Screen.Login) { inclusive = true }
                                                            }
                                                        }
                                                        else if(role ==   "guru 5") {
                                                            navController.navigate(Screen.TeacherDashboard(role)) {
                                                                popUpTo(Screen.Login) { inclusive = true }
                                                            }
                                                        }
                                                        else if(role ==   "guru 6") {
                                                            navController.navigate(Screen.TeacherDashboard(role)) {
                                                                popUpTo(Screen.Login) { inclusive = true }
                                                            }


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
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                Row(verticalAlignment = Alignment.CenterVertically,){
                    Text(text = "Dashboard Admin", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
                    Spacer(modifier = Modifier.width(65.dp))
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Person",
                        modifier=Modifier.size(50.dp),
                        tint = Color(0xFFE3FEF7)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                val cards= listOf(CardItem(
                    title = "Data Guru",
                    route = Screen.DataGuru(role)
                ),
                    CardItem(
                        title = "Data Staff",
                        route = Screen.DataStaff(role)
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
                        title = "Data Perkembangan Nilai",
                        route =Screen.DataPerkembangan,
                        icon = Icons.Filled.Star
                    ),
                    CardItem(
                        title = "Data Rekapan Kehadiran Siswa",
                        route =Screen.DataRekapan,
                        icon = Icons.Filled.Face
                    ),
                    )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp)
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
    Column(modifier = Modifier.fillMaxSize().padding(it), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))

        Button(onClick = {
            val auth = Firebase.auth
            auth.signOut()
            navController.navigate(Screen.Login) {
                popUpTo(Screen.AdminDashboard(role)) { inclusive = true }
            }
        }){Text(text = "Logout")}
    }

}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataGuruPage(
    navController: NavController,
    role: String
) {

    val firestore = FirebaseFirestore.getInstance()
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
                val imageUrl = document.getString("imageUrl") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Quadruple(document.id, name, keterangan, imageUrl))
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

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahGuru)
                }, containerColor = Color(0xFF77B0AA)) {
                Text(
                    "+",
                    fontSize = 24.sp,
                    color = Color(0xFFE3FEF7)
                )
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
                // Display the data fetched from Firestore
                dataList.forEach { (id, name, keterangan, imageUrl) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,

                                ),
                            modifier = Modifier
                                .size(width = 200.dp, height = 120.dp)
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(imageUrl),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE3FEF7)
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = keterangan,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color(0xFFE3FEF7)
                                )
                            }
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
            }
        }
    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataSiswa(
    navController: NavController,
    role: String
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Quadruple<String,String, String, String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }

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
                val imageUrl = document.getString("imageUrl") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Quadruple(document.id,name, keterangan, imageUrl))
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

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahSiswa)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
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
        text = "Data Siswa",
        style = MaterialTheme.typography.headlineLarge,
        color = Color(0xFFE3FEF7)
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Display the data fetched from Firestore
    dataList.forEach { (id, name, keterangan, imageUrl) ->
        val canEdit = when (role) {
            "admin" -> true
            "guru 4" -> keterangan == "kelas 4"
            "guru 5" -> keterangan == "kelas 5"
            "guru 6" -> keterangan == "kelas 6"
            else -> false
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,

                    ),
                modifier = Modifier
                    .size(width = 200.dp, height = 120.dp)
                    .clickable { }
                    .padding(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE3FEF7)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = keterangan,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFFE3FEF7)
                    )
                }
            }
            if (canEdit) {
                IconButton(
                    onClick = {
                        // Navigate to Edit screen with the document id
                        navController.navigate(Screen.EditSiswa(id))
                    }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
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
}
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataJadwalPelajaran(
    navController: NavController,
    role: String
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Quintuple<String,String, String, String,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }


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
                // Here you can collect more fields as needed
                dataList.add(Quintuple(document.id,nama,jam,kelas,hari))
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

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahJadwalPelajaran)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
            Text(text = "Data Jadwal Pelajaran", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (id,nama,jam,kelas,hari) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,

                            ),

                        modifier = Modifier
                            .size(width = 200.dp, height = 120.dp)
                            .clickable { }
                            .padding(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,modifier = Modifier.fillMaxSize()) {
                            Text(text = nama, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = kelas,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

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
                                navController.navigate(Screen.EditJadwalPelajaran(id))
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
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataJadwalUjian(
    navController: NavController,
    role: String
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Sextuple<String,String, String, String,String,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }



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
                // Here you can collect more fields as needed
                dataList.add(Sextuple(document.id,nama,jam,kelas,hari,tanggal))
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

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahJadwalUjian)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
            Text(text = "Data Jadwal Ujian", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (id, nama, jam, kelas, hari, tanggal) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,

                            ),

                        modifier = Modifier
                            .size(width = 200.dp, height = 120.dp)
                            .clickable { }
                            .padding(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {


                            Text(
                                text = nama,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = kelas,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = jam,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = hari,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = tanggal,
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
                                navController.navigate(Screen.EditJadwalUjian(id))
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
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataNilai(
    navController: NavController,
    role: String
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Nilai<String,String, List<Pair<String, String>>,String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }


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
                val peringkat = document.getString("peringkat") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Nilai(document.id,nama, mataPelajaranList,peringkat))
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

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahNilai)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
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
                    text = "Data Nilai dan Peringkat Siswa",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFFE3FEF7),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Display the data fetched from Firestore
                dataList.forEach { (id, nama, matapelajaran, peringkat) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,

                                ),

                            modifier = Modifier
                                .size(width = 200.dp, height = 120.dp)
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(text = nama, style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))



                                matapelajaran.forEach { (pelajaran, nilai) ->
                                    Text(
                                        text = "$pelajaran: $nilai",
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1
                                    )
                                }


                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Peringkat $peringkat",
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataPerkembangan(
    navController: NavController
){


    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Nilai<String,String, List<Pair<String, String>>,String>>() }


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
                val peringkat = document.getString("peringkat") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Nilai(document.id,nama, mataPelajaranList,peringkat))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    Scaffold(
        floatingActionButton = {

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahPerkembangan)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
            Text(text = "Data Perkembangan Nilai", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (id,nama, matapelajaran, peringkat) ->

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,

                        ),

                    modifier = Modifier
                        .size(width = 200.dp, height = 120.dp)
                        .clickable { }
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = nama, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))



                        matapelajaran.forEach { (pelajaran, nilai) ->
                            Text(
                                text = "$pelajaran: $nilai",
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1
                            )
                        }


                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Peringkat $peringkat",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                }
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataRekapan(
    navController: NavController
){


    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Nilai<String,String, List<Pair<String, String>>,String>>() }


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
                val peringkat = document.getString("peringkat") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Nilai(document.id,nama, mataPelajaranList,peringkat))
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    Scaffold(
        floatingActionButton = {

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahRekapan)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
            Text(text = "Data Rekapan Kehadiran Siswa", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (id,nama, matapelajaran, peringkat) ->

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,

                        ),

                    modifier = Modifier
                        .size(width = 200.dp, height = 120.dp)
                        .clickable { }
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = nama, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))



                        matapelajaran.forEach { (pelajaran, nilai) ->
                            Text(
                                text = "$pelajaran: $nilai",
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1
                            )
                        }


                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "peringkat $peringkat",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataStaffPage(
    navController: NavController,
    role: String
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Quadruple<String,String, String, String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var documentIdToDelete by remember { mutableStateOf<String?>(null) }


    DisposableEffect(Unit) {
        val collectionRef = firestore.collection("staff")

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
                val imageUrl = document.getString("imageUrl") ?: ""
                // Here you can collect more fields as needed
                dataList.add(Quadruple(document.id,name, keterangan, imageUrl))
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
                            firestore.collection("staff").document(id).delete()
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

            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function

                    navController.navigate(Screen.TambahStaff)
                }
                , containerColor = Color(0xFF77B0AA) ) { Text("+", fontSize = 24.sp, color = Color(0xFFE3FEF7)) }
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
                    text = "Data Staff",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFFE3FEF7)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Display the data fetched from Firestore
                dataList.forEach { (id, name, keterangan, imageUrl) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,

                                ),
                            modifier = Modifier
                                .size(width = 200.dp, height = 120.dp)
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(imageUrl),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE3FEF7)
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = keterangan,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color(0xFFE3FEF7)
                                )
                            }
                        }
                        if (role == "admin") {
                            IconButton(
                                onClick = {
                                    // Navigate to Edit screen with the document id
                                    navController.navigate(Screen.EditStaff(id))
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
@OptIn(ExperimentalCoilApi::class)
@Composable
fun TambahSiswa(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(it) { downloadUrl ->
                imageDownloadUrl = downloadUrl
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

    Text(text="Tambah Data Siswa", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        // Display the selected image
        imageDownloadUrl?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Adjust the size as needed
                    .padding(16.dp) // Add padding for better layout
            )
        }

        // Button to select image
        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier
            .align(Alignment.CenterHorizontally)) {
            Text("Select Image")
        }

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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, imageDownloadUrl,navController,data= "siswa")
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
fun TambahJadwalPelajaran(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    val context = LocalContext.current



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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabasePelajaran(name, kelas,jam,hari,navController)
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
fun TambahRekapan(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    val context = LocalContext.current



    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Rekapan Kehadiran Siswa", modifier = Modifier
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
            value = tanggal,
            onValueChange = { name = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseUjian(name, kelas,jam,hari,tanggal,navController)
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
fun TambahPerkembangan(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    val context = LocalContext.current



    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Perkembangan Nilai", modifier = Modifier
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
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseUjian(name, kelas,jam,hari,tanggal,navController)
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
fun TambahNilai(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var mataPelajaran by remember { mutableStateOf("") }
    var nilai by remember { mutableStateOf("") }
    var peringkat by remember { mutableStateOf("") }
    var mataPelajaranList by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Tambah Data Nilai dan Peringkat Siswa",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
        )

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
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text("Tambah Mata Pelajaran")
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
            value = peringkat,
            onValueChange = { peringkat = it },
            label = { Text("Peringkat") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                submitDataToDatabaseNilai(name, mataPelajaranList, peringkat, navController)
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
    mataPelajaranList: List<Map<String, String>>,
    index: Int,
    onDone: (Map<String, String>) -> Unit
) {
    var mataPelajaran by remember { mutableStateOf(mataPelajaranList[index]["nama"] ?: "") }
    var nilai by remember { mutableStateOf(mataPelajaranList[index]["nilai"] ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Edit Data Nilai",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
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
@OptIn(ExperimentalCoilApi::class)
@Composable
fun TambahJadwalUjian(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    val context = LocalContext.current



    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Tambah Data Jadwal Ujian", modifier = Modifier
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
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabaseUjian(name, kelas,jam,hari,tanggal,navController)
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
fun TambahStaff(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(it) { downloadUrl ->
                imageDownloadUrl = downloadUrl
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Display the selected image
        Text(text="Tambah Data Staff", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        imageDownloadUrl?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Adjust the size as needed
                    .padding(16.dp) // Add padding for better layout
                      ,  alignment = Alignment.Center
            )
        }

        // Button to select image
        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier
            .align(Alignment.CenterHorizontally)) {
            Text("Select Image")
        }

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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, imageDownloadUrl,navController,data= "staff")
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
    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(it) { downloadUrl ->
                imageDownloadUrl = downloadUrl
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

    // Display the selected image
        Text(text="Tambah Data Guru", modifier = Modifier
            .align(Alignment.CenterHorizontally))
        imageDownloadUrl?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Adjust the size as needed
                    .padding(16.dp) // Add padding for better layout
                , alignment = Alignment.Center
            )
        }

        // Button to select image
        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier
            .align(Alignment.CenterHorizontally)) {
            Text("Select Image")
        }

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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, imageDownloadUrl,navController,data= "guru")
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
    val firestore = FirebaseFirestore.getInstance()
    var name by remember { mutableStateOf("") }
    var mataPelajaran by remember { mutableStateOf("") }
    var nilai by remember { mutableStateOf("") }
    var peringkat by remember { mutableStateOf("") }
    var mataPelajaranList by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var isEditing by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current

    // Fetch existing data from Firestore
    LaunchedEffect(nilaiId) {
        val document = firestore.collection("nilai").document(nilaiId).get().await()
        name = document.getString("nama") ?: ""
        peringkat = document.getString("peringkat") ?: ""
        val mataPelajaranArray = document.get("mata_pelajaran") as? List<Map<String, String>>
        mataPelajaranList = mataPelajaranArray ?: listOf()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Edit Data Nilai dan Peringkat",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
        )

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
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(if (isEditing) "Update Mata Pelajaran" else "Tambah Mata Pelajaran")
        }

        // Text field for peringkat
        TextField(
            value = peringkat,
            onValueChange = { peringkat = it },
            label = { Text("Peringkat") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                SubmitUpdatedDataToDatabaseNilai(name, mataPelajaranList, peringkat, navController, nilaiId)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}



fun SubmitUpdatedDataToDatabaseNilai(
    name: String,
    mataPelajaranList: List<Map<String, String>>,
    peringkat: String,
    navController: NavController,
    nilaiId: String
) {
    val firestore = FirebaseFirestore.getInstance()

    // Update the Firestore document with the new data
    firestore.collection("nilai").document(nilaiId)
        .update(
            mapOf(
                "nama" to name,
                "mata_pelajaran" to mataPelajaranList,
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
    val firestore = FirebaseFirestore.getInstance()
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Fetch existing data from Firestore
    LaunchedEffect(jadwalId) {
        val document = firestore.collection("jadwalUjian").document(jadwalId).get().await()
        name = document.getString("nama") ?: ""
        kelas = document.getString("kelas") ?: ""
        jam = document.getString("jam") ?: ""
        hari = document.getString("hari") ?: ""
        tanggal = document.getString("tanggal") ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text="Edit Data Jadwal Ujian", modifier = Modifier.align(Alignment.CenterHorizontally))

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
        // Text field for day
        TextField(
            value = tanggal,
            onValueChange = { tanggal = it },
            label = { Text("Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                submitUpdatedDataToDatabaseUjian(name, kelas, jam, hari, tanggal, navController, jadwalId)
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
    val firestore = FirebaseFirestore.getInstance()
    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Fetch existing data from Firestore
    LaunchedEffect(jadwalId) {
        val document = firestore.collection("jadwalPelajaran").document(jadwalId).get().await()
        name = document.getString("nama") ?: ""
        kelas = document.getString("kelas") ?: ""
        jam = document.getString("jam") ?: ""
        hari = document.getString("hari") ?: ""
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

        // Button to submit data
        Button(
            onClick = {
                submitUpdatedDataToDatabasePelajaran(name, kelas, jam, hari, navController, jadwalId)
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
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseFirestore.getInstance()
    val dataMap = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "tanggal" to tanggal
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
    navController: NavController,
    jadwalId: String
) {
    val firestore = FirebaseFirestore.getInstance()
    val dataMap = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari
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
@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditStaff(
    navController: NavController,
    guruId: String
) {
    val firestore = FirebaseFirestore.getInstance()
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(it) { downloadUrl ->
                imageDownloadUrl = downloadUrl
            }
        }
    }

    // Fetch existing data from Firestore
    LaunchedEffect(guruId) {
        val document = firestore.collection("staff").document(guruId).get().await()
        name = document.getString("nama") ?: ""
        description = document.getString("keterangan") ?: ""
        imageDownloadUrl = document.getString("imageUrl")
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Edit Data Staff", modifier = Modifier.align(Alignment.CenterHorizontally))

        // Display the selected image
        imageDownloadUrl?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp),
                alignment = Alignment.Center
            )
        }

        // Button to select image
        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Select Image")
        }

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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, imageDownloadUrl, navController, guruId, data = "staff")
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
fun EditGuru(
    navController: NavController,
    guruId: String
) {
    val firestore = FirebaseFirestore.getInstance()
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(it) { downloadUrl ->
                imageDownloadUrl = downloadUrl
            }
        }
    }

    // Fetch existing data from Firestore
    LaunchedEffect(guruId) {
        val document = firestore.collection("guru").document(guruId).get().await()
        name = document.getString("nama") ?: ""
        description = document.getString("keterangan") ?: ""
        imageDownloadUrl = document.getString("imageUrl")
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Edit Data Guru", modifier = Modifier.align(Alignment.CenterHorizontally))

        // Display the selected image
        imageDownloadUrl?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp),
                alignment = Alignment.Center
            )
        }

        // Button to select image
        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Select Image")
        }

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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, imageDownloadUrl, navController, guruId, data = "guru")
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
    val firestore = FirebaseFirestore.getInstance()
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageDownloadUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(it) { downloadUrl ->
                imageDownloadUrl = downloadUrl
            }
        }
    }

    // Fetch existing data from Firestore
    LaunchedEffect(guruId) {
        val document = firestore.collection("siswa").document(guruId).get().await()
        name = document.getString("nama") ?: ""
        description = document.getString("keterangan") ?: ""
        imageDownloadUrl = document.getString("imageUrl")
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Edit Data Siswa", modifier = Modifier.align(Alignment.CenterHorizontally))

        // Display the selected image
        imageDownloadUrl?.let { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp),
                alignment = Alignment.Center
            )
        }

        // Button to select image
        Button(onClick = { getContent.launch("image/*") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Select Image")
        }

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

        // Button to submit data
        Button(
            onClick = {
                submitDataToDatabase(name, description, imageDownloadUrl, navController, guruId, data = "siswa")
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
fun submitDataToDatabase(
    name: String,
    description: String,
    imageUrl: String?,
    navController: NavController,
    documentId: String,
    data: String
) {
    val firestore = FirebaseFirestore.getInstance()
    val dataMap = hashMapOf(
        "nama" to name,
        "keterangan" to description,
        "imageUrl" to imageUrl
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
private fun submitDataToDatabase(name: String, description: String, imageUrl: String?,
                                 navController: NavController,data:String) {
    // Access the Firestore collection where you want to store the data
    val firestore = Firebase.firestore
    val collectionRef = firestore.collection(data)

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "keterangan" to description,
        "imageUrl" to imageUrl
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
private fun submitDataToDatabaseNilai(name: String,mataPelajaran: List<Map<String, String>>,peringkat:String,
                                      navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = Firebase.firestore
    val collectionRef = firestore.collection("nilai")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "mata_pelajaran" to mataPelajaran,
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
private fun submitDataToDatabaseUjian(name: String, kelas:String,jam:String ,hari:String,tanggal:String,
                                          navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = Firebase.firestore
    val collectionRef = firestore.collection("jadwalUjian")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
        "tanggal" to tanggal
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
private fun submitDataToDatabasePelajaran(name: String, kelas:String,jam:String ,hari:String,
                                 navController: NavController) {
    // Access the Firestore collection where you want to store the data
    val firestore = Firebase.firestore
    val collectionRef = firestore.collection("jadwalPelajaran")

    // Create a new document with a unique ID
    val documentRef = collectionRef.document()

    // Create a data object to store the fields
    val data = hashMapOf(
        "nama" to name,
        "kelas" to kelas,
        "jam" to jam,
        "hari" to hari,
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

    uploadTask.addOnSuccessListener { taskSnapshot ->
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
fun TeacherDashboard(
navController: NavController,
it: PaddingValues,
role: String
) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }


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
        Row(verticalAlignment = Alignment.CenterVertically,){
            Text(text = "Dashboard Guru", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFE3FEF7))
            Spacer(modifier = Modifier.width(65.dp))
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Person",
                modifier=Modifier.size(50.dp),
                tint = Color(0xFFE3FEF7)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        val cards= listOf(CardItem(
            title = "Data Guru",
            route = Screen.DataGuru(role)
        ),
            CardItem(
                title = "Data Staff",
                route = Screen.DataStaff(role)
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
                title = "Data Perkembangan Nilai",
                route =Screen.DataPerkembangan,
                icon = Icons.Filled.Star
            ),
            CardItem(
                title = "Data Rekapan Kehadiran Siswa",
                route =Screen.DataRekapan,
                icon = Icons.Filled.Face
            ),
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 200.dp)
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
