package com.example.ballsortpuzzle.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onStartGame: (Int) -> Unit,
    onExit: () -> Unit
) {
    var numCores by remember { mutableIntStateOf(5) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF202030)) // Mesma cor de fundo do GameScreen
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Título
        Text(
            text = "Ball Sort Puzzle",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 40.dp)
        )

        // Seção do slider
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Número de Cores: $numCores",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Slider(
                value = numCores.toFloat(),
                onValueChange = { numCores = it.toInt() },
                valueRange = 2f..8f,
                steps = 5,  // Isso resultará em 7 posições (2,3,4,5,6,7,8), o que significa 6 steps
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            )
        }

        // Botões
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 60.dp)
        ) {
            Button(
                onClick = { onStartGame(numCores) },
                modifier = Modifier
                    .width(250.dp)
                    .height(70.dp)
            ) {
                Text(
                    text = "Iniciar Jogo",
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onExit,
                modifier = Modifier
                    .width(250.dp)
                    .height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Sair do Jogo",
                    fontSize = 22.sp
                )
                }
            }
        }
    }
}