package com.example.quicknotes
import AddNoteScreen
import EditNoteScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quicknotes.ui.theme.QuickNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickNotesTheme {
                val navController = rememberNavController()

                // Initialize the static list of notes
                val initialNotes = listOf(
                    Note(1, "Tuesday Class", "Telehealth, AI algorithm, Software Systems Design, Unix/Linux Operating Systems"),
                    Note(2, "Monday's Class", "Data Security & Privacy, IT project Management, Web Application Development"),
                    Note(3, "Wednesday", "Mobile Apps Development, Leadership/Contemp.Workplace, Discrete Mathematics"),
                    Note(4, "Thursday's Class", "Software Development, Advanced Database Concepts, C# Programming, College Communications")
                )

                // State to hold the mutable list of notes
                val notes = remember { mutableStateListOf(*initialNotes.toTypedArray()) }

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            notes = notes,
                            onNoteClick = { note ->
                                navController.navigate("view_edit/${note.id}")
                            },
                            onAddNoteClick = {
                                navController.navigate("create")
                            }
                        )
                    }
                    composable("create") {
                        AddNoteScreen(onSaveClick = { title, content ->
                            val newNote = Note(
                                id = notes.size + 1,
                                title = title,
                                content = content
                            )
                            notes.add(newNote)
                            navController.popBackStack()
                        })
                    }
                    composable("view_edit/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()
                        val note = notes.first { it.id == noteId }
                        EditNoteScreen(note = note, onSaveClick = { title, content ->
                            note.title = title
                            note.content = content
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}
