package com.example.ballsortpuzzle

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ballsortpuzzle.ui.screen.GameScreen
import com.example.ballsortpuzzle.ui.screen.MenuScreen

@Composable
fun BallSortApp() {
    val navController = rememberNavController()
    val numCores = remember { mutableIntStateOf(4) }
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(
                onStartGame = { cores ->
                    numCores.intValue = cores
                    navController.navigate("game")
                },
                onExit = {
                    // Implementação real para sair do aplicativo
                    (context as? Activity)?.finish()
                }
            )
        }


        composable("game") {
            GameScreen(
                numCores = numCores.intValue,
                onBackToMenu = {
                    navController.navigateToMenu()
                },
                onNewGame = {
                    // Reinicia o jogo atual
                    navController.navigateToGame()
                }
            )
        }
    }
}

// Funções de extensão para tornar a navegação mais limpa
private fun NavController.navigateToGame() {
    this.navigate("game") {
        // Limpar a pilha até o destino inicial
        popUpTo("menu")
        // Evitar múltiplas cópias da mesma tela
        launchSingleTop = true
    }
}

private fun NavController.navigateToMenu() {
    this.navigate("menu") {
        // Limpar a pilha de navegação até chegar ao menu
        popUpTo("menu") {
            inclusive = true
        }
    }
}