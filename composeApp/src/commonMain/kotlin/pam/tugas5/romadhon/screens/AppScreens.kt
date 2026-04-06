package pam.tugas5.romadhon.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import tugas5_pam_123140031.composeapp.generated.resources.Res
import tugas5_pam_123140031.composeapp.generated.resources.foto_romadhon

val TemaHijau = Color(0xFF2E7D32)

data class Note(val id: Int, val title: String, val content: String, val date: String, val color: Color)

val dummyNotes = listOf(
    Note(1, "Proyek S.I.G.M.A", "Rencana pengembangan bel sekolah otomatis untuk KKN di Desa Bandarejo. Perlu cek modul suara.", "15 Feb 2026", Color(0xFFE8F5E9)),
    Note(2, "Update BUMH HMIF", "Evaluasi pre-order 126 Jaket Himpunan. Omzet tembus 25jt+, lanjut proyek merchandise.", "10 Mar 2026", Color(0xFFC8E6C9)),
    Note(3, "UI/UX Academora", "Target skor SUS naik ke 90. Perbaiki alur navigasi di bagian dashboard utama.", "05 Apr 2026", Color(0xFFA5D6A7)),
    Note(4, "Go-Wes ITERA", "Maintenance backend Node.js untuk fitur rental sepeda di lingkungan kampus.", "12 Apr 2026", Color(0xFFC8E6C9))
)

val favoriteNoteIds = mutableStateListOf<Int>()

@Composable
fun NoteListScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = TemaHijau,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF5F5F5))) {
            Text(
                text = "Catatan Romadhon",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TemaHijau,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari catatan kamu...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TemaHijau) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = TemaHijau
                ),
                singleLine = true
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredNotes = dummyNotes.filter { it.title.contains(searchQuery, ignoreCase = true) }

                items(filteredNotes) { note ->
                    val isFavorite = favoriteNoteIds.contains(note.id)

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToDetail(note.id) },
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = note.color)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = note.content, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color.DarkGray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = note.date, fontSize = 12.sp, color = TemaHijau)
                            }
                            IconButton(
                                onClick = {
                                    if (isFavorite) favoriteNoteIds.remove(note.id)
                                    else favoriteNoteIds.add(note.id)
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFavorite) Color.Red else TemaHijau
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    val favoriteNotes = dummyNotes.filter { favoriteNoteIds.contains(it.id) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        Text(
            text = "Koleksi Favorit",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TemaHijau,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
        )

        if (favoriteNotes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Belum ada favorit nih", color = Color.Gray, fontSize = 18.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteNotes) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = note.color)
                    ) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = note.content, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = { favoriteNoteIds.remove(note.id) }) {
                                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(140.dp).clip(CircleShape).background(TemaHijau.copy(alpha = 0.1f))
        ) {
            Image(
                painter = painterResource(Res.drawable.foto_romadhon),
                contentDescription = "Muhammad Romadhon Santoso",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("M. Romadhon Santoso", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TemaHijau)
        Text("123140031 | Teknik Informatika ITERA", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = TemaHijau.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        ProfileInfoRow(Icons.Filled.AccountTree, "Student in Teknik Informatika ITERA")
        ProfileInfoRow(Icons.Filled.Code, "Web Developer")
        ProfileInfoRow(Icons.Filled.School, "Institut Teknologi Sumatera")
    }
}

@Composable
fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = TemaHijau, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 15.sp)
    }
}

// ==========================================
// 2. LAYAR DETAIL & FORM
// ==========================================

@Composable
fun NoteDetailScreen(noteId: Int, onNavigateToEdit: (Int) -> Unit, onBack: () -> Unit) {
    val note = dummyNotes.find { it.id == noteId } ?: dummyNotes[0]

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Detail", color = Color(0xFF1B5E20)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color(0xFF1B5E20)) } },
                actions = { IconButton(onClick = { onNavigateToEdit(noteId) }) { Icon(Icons.Filled.Edit, null, tint = Color(0xFF1B5E20)) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = note.color)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(note.color).padding(padding).padding(20.dp)) {
            Text(text = note.title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Text(text = note.date, color = TemaHijau, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = note.content, fontSize = 17.sp, lineHeight = 26.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun AddNoteScreen(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Baru", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TemaHijau)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TemaHijau, focusedLabelColor = TemaHijau)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content, onValueChange = { content = it },
                label = { Text("Isi") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TemaHijau, focusedLabelColor = TemaHijau)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { scope.launch { isLoading = true; delay(1000); onBack() } },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TemaHijau),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Simpan", color = Color.White)
            }
        }
    }
}

@Composable
fun EditNoteScreen(noteId: Int, onBack: () -> Unit) {
    val note = dummyNotes.find { it.id == noteId }
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Edit", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TemaHijau)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TemaHijau, focusedLabelColor = TemaHijau)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content, onValueChange = { content = it },
                label = { Text("Isi") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TemaHijau, focusedLabelColor = TemaHijau)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { scope.launch { isLoading = true; delay(1000); onBack() } },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TemaHijau),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Update", color = Color.White)
            }
        }
    }
}