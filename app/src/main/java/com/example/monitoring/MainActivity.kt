package com.example.monitoring

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.monitoring.ui.theme.MonitoringTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class NavigationItem(
    val route: String,
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon:ImageVector,
    val hasNews:Boolean,
    val badgeCount:Int? = null
)
data class Quadruple<T, U, V, W>(val first: T, val second: U, val third: V, val fourth: W)
data class Quintuple<T, U, V, W,X>(val first: T, val second: U, val third: V, val fourth: W, val fifth:X)
data class Nilai<T, U, V>(val nama: T, val mataPelajaranList: U, val peringkat:V)

data class CardItem(
    val title: String,
    val route:String,
)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                        ScreenWithScaffold(navController) {
                            AdminDashboard(navController,it)
                        }
                    }
                    composable("search") {
                        ScreenWithScaffold(navController) {
                            Search(navController,it)
                        }
                    }
                    composable("settings") {
                        ScreenWithScaffold(navController) {
                            Settings(navController,it)
                        }
                    }
                    composable("dataGuru") {
                        DataGuru(navController)
                    }
                    composable("dataStaff") {
                        DataStaff(navController)
                    }
                    composable("dataSiswa") {
                        DataSiswa(navController)
                    }
                    composable("dataJadwalPelajaran") {
                        DataJadwalPelajaran(navController)
                    }
                    composable("dataJadwalUjian") {
                        DataJadwalUjian(navController)
                    }
                    composable("dataNilai") {
                        DataNilai(navController)
                    }
                    composable("dataPerkembangan") {
                        DataPerkembangan(navController)
                    }
                    composable("dataRekapan") {
                        DataRekapan(navController)
                    }
                    composable("tambahGuru") {
                        TambahGuru(navController)
                    }
                    composable("tambahStaff") {
                        TambahStaff(navController)
                    }
                    composable("tambahSiswa") {
                        TambahSiswa(navController)
                    }
                    composable("teacherDashboard") {
                        TeacherDashboard()
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ScreenWithScaffold(navController: NavController, content: @Composable (PaddingValues) -> Unit) {
    // Wrap the content with Scaffold
    var selectedItemIndex1 by rememberSaveable {
        mutableStateOf(0)
    }
    val activity = LocalContext.current as Activity
    val windowClass = calculateWindowSizeClass(activity)
    val showNavigationRail = windowClass.widthSizeClass == WindowWidthSizeClass.Compact
    val items1 = listOf(
        NavigationItem(
            route = "adminDashboard",
            title= "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews =false
        ),
        NavigationItem(
            route = "search",
            title= "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            hasNews =false
        ),
        NavigationItem(
            route = "settings",
            title= "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
            ,
            hasNews =false
        ),

        )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold (
        bottomBar = {
            if(showNavigationRail){
                NavigationBar {
                    items1.forEach { item ->
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
    val context = LocalContext.current
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
fun NavigationSideBar(
    items: List<NavigationItem>,
    selectedItemIndex:Int,
    onNavigate:(Int)-> Unit,
    padding:PaddingValues
){
NavigationRail(  modifier = Modifier
    .background(MaterialTheme.colorScheme.inverseOnSurface)
    .offset(x = (-1).dp)
    .padding(padding)) {
Column(            modifier = Modifier.fillMaxHeight(),
    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
) {


    items.forEachIndexed { index, item ->
        NavigationRailItem(
            selected = selectedItemIndex == index,
            onClick = { onNavigate(index) },
            icon = {
                NavigationIcon(
                    item = item,
                    selected = selectedItemIndex == index
                )
            },
            label = { Text(text = item.title, textAlign = TextAlign.Center ) })
    }
}
}
}
@Composable
fun NavigationIcon(
    item:NavigationItem,
    selected:Boolean
){
    BadgedBox(
        badge = {
            if(item.badgeCount != null){
                Badge{
                    Text(text = item.badgeCount.toString())
                }
            } else if(item.hasNews){
                Badge()
            }
        }
    ){
Icon(imageVector = if(selected) item.selectedIcon else item.unselectedIcon,
    contentDescription = item.title
    )
    }
}
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdminDashboard(navController: NavController,it:PaddingValues) {

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
                Text(text = "Dashboard Admin", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                val cards= listOf(CardItem(
                    title = "Data Guru",
                    route = "dataGuru"
                ),
                    CardItem(
                        title = "Data Staff",
                        route ="dataStaff"
                    ),
                            CardItem(
                            title = "Data Siswa",
                                route = "dataSiswa"
                ),
                    CardItem(
                        title = "Data Jadwal Pelajaran",
                        route ="dataJadwalPelajaran"
                    ),
                    CardItem(
                        title = "Data Jadwal Ujian",
                        route ="dataJadwalUjian"
                    ),
                    CardItem(
                        title = "Data Nilai dan Peringkat Siswa",
                        route ="dataNilai"
                    ),
                    CardItem(
                        title = "Data Perkembangan Nilai",
                        route ="dataPerkembangan"
                    ),
                    CardItem(
                        title = "Data Rekapan Kehadiran Siswa",
                        route ="dataRekapan"
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
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Person"
                                )
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
fun Search(navController: NavController,it: PaddingValues){
    Text(text = "Search")
}
@Composable
fun Settings(navController: NavController,it: PaddingValues){
    Text(text = "Settings")
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataGuru(
    navController: NavController
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Triple<String, String, String>>() }


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
                dataList.add(Triple(name, keterangan, imageUrl))
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

navController.navigate("tambahGuru")
            }
        ) { Text("+", fontSize = 24.sp) }
    },
) { innerPadding ->

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Data Guru", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        // Display the data fetched from Firestore
        dataList.forEach { (name,keterangan, imageUrl) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = name, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = keterangan,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Divider()
        }
    }
}
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataSiswa(
    navController: NavController
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Triple<String, String, String>>() }


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
                dataList.add(Triple(name, keterangan, imageUrl))
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

                    navController.navigate("tambahSiswa")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Siswa", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (name,keterangan, imageUrl) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = name, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = keterangan,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Divider()
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataJadwalPelajaran(
    navController: NavController
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Quadruple<String, String, String,String>>() }


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
                dataList.add(Quadruple(nama,jam,kelas,hari))
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

                    navController.navigate("tambahJadwalPelajaran")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Jadwal Pelajaran", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (nama,jam,kelas,hari) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {


                    Column {
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
                Divider()
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataJadwalUjian(
    navController: NavController
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Quintuple<String, String, String,String,String>>() }


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
                dataList.add(Quintuple(nama,jam,kelas,hari,tanggal))
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

                    navController.navigate("tambahJadwalPelajaran")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Jadwal Ujian", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (nama,jam,kelas,hari,tanggal) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {


                    Column {
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tanggal,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Divider()
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataNilai(
    navController: NavController
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Nilai<String, List<Pair<String, String>>,String>>() }


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
                dataList.add(Nilai(nama, mataPelajaranList,peringkat))
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

                    navController.navigate("tambahStaff")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Nilai dan Peringkat Siswa", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (nama,matapelajaran,peringkat) ->

                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = nama, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))



                            matapelajaran.forEach { (pelajaran, nilai) ->
                                Text(
                                    text = "$pelajaran: $nilai",
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1
                                )}


                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = peringkat,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                Divider()
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
    val dataList = remember { mutableStateListOf<Nilai<String, List<Pair<String, String>>,String>>() }


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
                dataList.add(Nilai(nama, mataPelajaranList,peringkat))
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

                    navController.navigate("tambahStaff")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Nilai dan Peringkat Siswa", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (nama,matapelajaran,peringkat) ->

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = nama, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))



                    matapelajaran.forEach { (pelajaran, nilai) ->
                        Text(
                            text = "$pelajaran: $nilai",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )}


                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = peringkat,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Divider()
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
    val dataList = remember { mutableStateListOf<Nilai<String, List<Pair<String, String>>,String>>() }


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
                dataList.add(Nilai(nama, mataPelajaranList,peringkat))
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

                    navController.navigate("tambahStaff")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Nilai dan Peringkat Siswa", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (nama,matapelajaran,peringkat) ->

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = nama, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))



                    matapelajaran.forEach { (pelajaran, nilai) ->
                        Text(
                            text = "$pelajaran: $nilai",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )}


                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = peringkat,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Divider()
            }
        }
    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DataStaff(
    navController: NavController
){

    val firestore = FirebaseFirestore.getInstance()
    val dataList = remember { mutableStateListOf<Triple<String, String, String>>() }


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
                dataList.add(Triple(name, keterangan, imageUrl))
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

                    navController.navigate("tambahStaff")
                }
            ) { Text("+", fontSize = 24.sp) }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Data Staff", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Display the data fetched from Firestore
            dataList.forEach { (name,keterangan, imageUrl) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = name, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = keterangan,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Divider()
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


    Column(modifier = Modifier.fillMaxSize()) {
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
        Button(onClick = { getContent.launch("image/*") }) {
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


    Column(modifier = Modifier.fillMaxSize()) {
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
        Button(onClick = { getContent.launch("image/*") }) {
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


    Column(modifier = Modifier.fillMaxSize()) {
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
        Button(onClick = { getContent.launch("image/*") }) {
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

) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Dashboard Guru", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text(text = "Data Guru")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Staff")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Siswa")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Jadwal Pelajaran")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Jadwal Ujian")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Nilai dan Peringkat Siswa")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Pekembangan Nilai")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
            Text(text = "Data Rekapan Kehadiran Siswa")
        }
    }
}
