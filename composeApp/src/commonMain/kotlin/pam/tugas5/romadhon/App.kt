package pam.tugas5.romadhon

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import pam.tugas5.romadhon.components.BottomNavBar
import pam.tugas5.romadhon.navigation.BottomNavItem
import pam.tugas5.romadhon.navigation.Screen
import pam.tugas5.romadhon.screens.AddNoteScreen
import pam.tugas5.romadhon.screens.EditNoteScreen
import pam.tugas5.romadhon.screens.FavoritesScreen
import pam.tugas5.romadhon.screens.NoteDetailScreen
import pam.tugas5.romadhon.screens.NoteListScreen
import pam.tugas5.romadhon.screens.ProfileScreen

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Menu Utama", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("List Catatan") },
                        selected = false,
                        onClick = {
                            navController.navigate(BottomNavItem.Notes.route)
                            scope.launch { drawerState.close() } // Tutup laci setelah diklik
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )

                    NavigationDrawerItem(
                        label = { Text("Profil Pengguna") },
                        selected = false,
                        onClick = {
                            navController.navigate(BottomNavItem.Profile.route)
                            scope.launch { drawerState.close() } // Tutup laci setelah diklik
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        ) {

            Scaffold(
                bottomBar = {
                    BottomNavBar(navController = navController)
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Notes.route,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(BottomNavItem.Notes.route) {
                        NoteListScreen(
                            onNavigateToDetail = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                            onNavigateToAdd = { navController.navigate(Screen.AddNote.route) }
                        )
                    }
                    composable(BottomNavItem.Favorites.route) { FavoritesScreen() }
                    composable(BottomNavItem.Profile.route) { ProfileScreen() }

                    composable(
                        route = Screen.NoteDetail.route,
                        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                        NoteDetailScreen(
                            noteId = noteId,
                            onNavigateToEdit = { id -> navController.navigate(Screen.EditNote.createRoute(id)) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.AddNote.route) {
                        AddNoteScreen(onBack = { navController.popBackStack() })
                    }

                    composable(
                        route = Screen.EditNote.route,
                        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                        EditNoteScreen(
                            noteId = noteId,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}