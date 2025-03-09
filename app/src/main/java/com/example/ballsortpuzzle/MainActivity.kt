package com.example.ballsortpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ballsortpuzzle.ui.screen.GameScreen
import com.example.ballsortpuzzle.ui.screen.MenuScreen
import com.example.ballsortpuzzle.ui.theme.BallSortPuzzleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BallSortPuzzleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BallSortApp()
                }
            }
        }
    }
}

// Adicionando previews para as telas principais
@Preview(showBackground = true, name = "Menu Screen")
@Composable
fun MenuScreenPreview() {
    BallSortPuzzleTheme {
        MenuScreen(
            onStartGame = {},
            onExit = {}
        )
    }
}

@Preview(showBackground = true, name = "Game Screen")
@Composable
fun GameScreenPreview() {
    BallSortPuzzleTheme {
        GameScreen(
            numCores = 4,
            onBackToMenu = {},
            onNewGame = {}
        )
    }
}

// Preview do aplicativo completo
@Preview(showBackground = true, name = "Full App Preview")
@Composable
fun BallSortAppPreview() {
    BallSortPuzzleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BallSortApp() // Corrigido para usar BallSortApp em vez de BallSortAppMain
        }
    }
}
