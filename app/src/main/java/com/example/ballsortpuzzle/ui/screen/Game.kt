package com.example.ballsortpuzzle.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ballsortpuzzle.modelo.Jogo
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    numCores: Int,
    onBackToMenu: () -> Unit,
    onNewGame: () -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val jogo = remember { Jogo(numCores) }
    var mensagem by remember { mutableStateOf("Selecione um frasco") }
    var frascoSelecionado by remember { mutableIntStateOf(-1) }
    var showVictoryDialog by remember { mutableStateOf(false) }
    var showRestartConfirmDialog by remember { mutableStateOf(false) }
    var showBackConfirmDialog by remember { mutableStateOf(false) }

    // Cores para os elementos
    val cores = remember {
        mapOf(
            1 to Color.Red,
            2 to Color.Green,
            3 to Color.Blue,
            4 to Color.Yellow,
            5 to Color.Magenta,
            6 to Color.Cyan,
            7 to Color(0xFF6c1ac9), // Roxo
            8 to Color(0xFFFFA500) // Laranja
        )
    }

    // Reiniciar o jogo
    LaunchedEffect(numCores) {
        jogo.reiniciarJogo(numCores)
        frascoSelecionado = -1
        mensagem = "Selecione um frasco"
        showVictoryDialog = false
    }

    // Verificar vitória - usando o metodo corrigido verificarVitoria()
    LaunchedEffect(jogo.getListaPilhas()) {
        if (jogo.verificarVitoria() && !showVictoryDialog) {
            delay(500) // Pequeno atraso para o jogador ver o estado final
            showVictoryDialog = true
        }
    }

    // Diálogo de confirmação para Reiniciar
    if (showRestartConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showRestartConfirmDialog = false },
            title = { Text("Reiniciar Jogo") },
            text = { Text("Tem certeza que deseja reiniciar o jogo? Todo o progresso atual será perdido.") },
            confirmButton = {
                Button(
                    onClick = {
                        showRestartConfirmDialog = false
                        jogo.reiniciarJogo()
                        frascoSelecionado = -1
                        mensagem = "Novo jogo iniciado"
                        showVictoryDialog = false
                    }
                ) {
                    Text("Sim, Reiniciar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showRestartConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de confirmação para Voltar
    if (showBackConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBackConfirmDialog = false },
            title = { Text("Voltar ao Menu") },
            text = { Text("Tem certeza que deseja voltar ao menu? O progresso atual será perdido.") },
            confirmButton = {
                Button(
                    onClick = {
                        showBackConfirmDialog = false
                        onBackToMenu()
                    }
                ) {
                    Text("Sim, Voltar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showBackConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Exibir diálogo de vitória
    if (showVictoryDialog) {
        // Definimos um modificador para os botões com tamanho fixo
        val buttonModifier = Modifier
            .width(130.dp) // Largura fixa um pouco maior para acomodar o texto "Novo Jogo"
            .height(48.dp) // Altura fixa para ambos os botões

        // Definimos um modificador para o conteúdo dentro dos botões
        val buttonContentModifier = Modifier
            .fillMaxSize() // Preencher o espaço do botão por completo

        AlertDialog(
            onDismissRequest = { }, // Deixando vazio para não fechar ao clicar fora
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🏆", // Emoji de troféu
                        fontSize = 36.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("PARABÉNS!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "Você venceu o jogo!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showVictoryDialog = false
                        onNewGame()
                    },
                    modifier = buttonModifier
                ) {
                    Box(
                        modifier = buttonContentModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Novo Jogo",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showVictoryDialog = false
                        onBackToMenu()
                    },
                    modifier = buttonModifier
                ) {
                    Box(
                        modifier = buttonContentModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Menu",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ball Sort Puzzle") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { showBackConfirmDialog = true }) { // Agora mostra o diálogo
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar ao menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showRestartConfirmDialog = true }) { // Agora mostra o diálogo
                        Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar jogo")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF202030))
        ) {
            // Caixa de mensagem na parte inferior
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
                    .padding(horizontal = 24.dp)
                    .background(Color(0xFF203040).copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Text(
                    text = mensagem,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Canvas para desenhar os frascos e bolinhas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // Calcular posições dos frascos e detectar qual foi tocado
                            val pilhas = jogo.getListaPilhas()
                            val frascosTotais = pilhas.size
                            val colunas = if (numCores <= 5) 4 else 5
                            val linhas = (frascosTotais + colunas - 1) / colunas

                            val width = size.width
                            val height = size.height
                            val margemHorizontal = width * 0.05f
                            val margemVertical = height * 0.15f
                            val espacoDisponivel = width - (2 * margemHorizontal)
                            val larguraFrasco = espacoDisponivel / colunas
                            val alturaFrasco = height * 0.5f / linhas

                            for (i in 0 until frascosTotais) {
                                val linha = i / colunas
                                val coluna = i % colunas

                                val left = margemHorizontal + (coluna * larguraFrasco) + (larguraFrasco * 0.1f)
                                val top = margemVertical + (linha * (alturaFrasco + 20f))
                                val right = left + larguraFrasco * 0.8f
                                val bottom = top + alturaFrasco

                                val rect = Rect(left, top, right, bottom)

                                if (rect.contains(offset)) {
                                    // Lógica de seleção de frasco
                                    if (frascoSelecionado == -1) {
                                        // Nenhum frasco selecionado ainda
                                        val pilha = jogo.getPilha(i) ?: continue  // com verificação de nulo
                                        if (!pilha.pilhaVazia()) {
                                            frascoSelecionado = i
                                            mensagem = "Selecione o destino"
                                        } else {
                                            mensagem = "Frasco de origem vazio"
                                        }
                                    } else {
                                        // Já tem um frasco selecionado
                                        if (frascoSelecionado != i) {
                                            // Usando o metodo melhorado realizarJogada()
                                            val (_, msg) = jogo.realizarJogada(frascoSelecionado, i)
                                            mensagem = msg

                                            // Verifica vitória usando o metodo corrigido
                                            if (jogo.verificarVitoria()) {
                                                mensagem = "VOCÊ VENCEU!"
                                                showVictoryDialog = true
                                            }
                                        } else {
                                            mensagem = "Selecione outro frasco"
                                        }

                                        // Desselecionar o frasco
                                        frascoSelecionado = -1
                                    }
                                    break
                                }
                            }
                        }
                    }
            ) {
                // Desenhar os frascos e bolinhas
                val pilhas = jogo.getListaPilhas()
                val frascosTotais = pilhas.size
                val colunas = if (jogo.getNumeroCores() <= 5) 4 else 5
                val linhas = (frascosTotais + colunas - 1) / colunas

                val margemHorizontal = size.width * 0.05f
                val margemVertical = size.height * 0.15f
                val espacoDisponivel = size.width - (2 * margemHorizontal)
                val larguraFrasco = espacoDisponivel / colunas
                val alturaFrasco = size.height * 0.5f / linhas

                // Desenhar os frascos
                for (i in 0 until frascosTotais) {
                    val linha = i / colunas
                    val coluna = i % colunas

                    val left = margemHorizontal + (coluna * larguraFrasco) + (larguraFrasco * 0.1f)
                    val top = margemVertical + (linha * (alturaFrasco + 100f))
                    val right = left + larguraFrasco * 0.8f
                    val bottom = top + alturaFrasco

                    // Desenhar número do frasco
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "${i + 1}",
                        topLeft = Offset(left + (right - left) / 2 - 10, top - 70),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Desenhar o frasco (retângulo)
                    drawRect(
                        color = Color(0xFF707070),
                        topLeft = Offset(left, top),
                        size = Size(right - left, bottom - top)
                    )

                    // Desenhar borda do frasco
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(left, top),
                        size = Size(right - left, bottom - top),
                        style = Stroke(width = 5f)
                    )

                    // Destacar o frasco selecionado
                    if (i == frascoSelecionado) {
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(left - 4f, top - 4f),
                            size = Size(right - left + 8f, bottom - top + 8f),
                            style = Stroke(width = 8f)
                        )
                    }

                    // Desenhar as bolinhas
                    val elementos = pilhas[i].getElementos()
                    val maxElementos = 4 // Número máximo de elementos em um frasco

                    // Calcular o espaço vertical disponível para cada bola
                    val espacoVerticalPorBola = (bottom - top) / maxElementos

                    // Calcular o diâmetro da bola para que elas fiquem encostadas
                    // mas não sobrepostas (deixando um pequeno espaço)
                    val diametroBola = minOf(
                        (right - left) * 0.8f,  // 80% da largura do frasco
                        espacoVerticalPorBola * 0.95f  // 95% do espaço vertical por bola
                    )

                    for (j in elementos.indices) {
                        val elementoValor = elementos[j]
                        if (elementoValor != -1) {
                            // Centralizar a bola horizontalmente no frasco
                            val centroX = (left + right) / 2

                            // Calcular a posição vertical, distribuindo uniformemente
                            // para ocupar o espaço disponível por completo
                            val posicaoY = j * espacoVerticalPorBola
                            val centroY = bottom - posicaoY - (espacoVerticalPorBola / 2)

                            // Desenhar a bolinha como um círculo perfeito
                            drawCircle(
                                color = cores[elementoValor] ?: Color.Black,
                                center = Offset(centroX, centroY),
                                radius = diametroBola / 2
                            )

                            // Borda da bolinha
                            drawCircle(
                                color = Color.Black,
                                center = Offset(centroX, centroY),
                                radius = diametroBola / 2,
                                style = Stroke(width = 2f)
                            )
                        }
                    }
                }
            }
        }
    }
}