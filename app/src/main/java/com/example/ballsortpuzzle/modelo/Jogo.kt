package com.example.ballsortpuzzle.modelo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Jogo(private var n: Int) {
    // Usando mutableStateListOf para reatividade com Compose
    private val listaPilhas: SnapshotStateList<Pilha> = mutableStateListOf()

    init {
        inicializarJogo()
    }

    private fun inicializarJogo() {
        // Limpa as pilhas existentes e cria novas
        listaPilhas.clear()

        // Adiciona n+2 pilhas (n para as cores + 2 vazias)
        repeat(n + 2) {
            listaPilhas.add(Pilha())
        }

        // Cria a lista de números e preenche as pilhas
        val listaNumeros = criarListaNumeros()
        encherPilhas(listaNumeros)
    }

    // Criar lista de números para as cores (4 de cada cor)
    private fun criarListaNumeros(): MutableList<Int> {
        val resultado = mutableListOf<Int>()

        // Para cada cor (1 a n), adicionar 4 elementos
        for (cor in 1..n) {
            repeat(4) {
                resultado.add(cor)
            }
        }

        // Embaralhar a lista
        resultado.shuffle()
        return resultado
    }

    // Preencher as pilhas com os números embaralhados
    private fun encherPilhas(listaNumeros: MutableList<Int>) {
        var contador = 0

        // Preencher apenas as primeiras n pilhas (deixar as 2 últimas vazias)
        for (i in 0 until n) {
            repeat(4) {
                listaPilhas[i].empilha(listaNumeros[contador])
                contador++
            }
        }

        // Verificar se a configuração inicial já é vitoriosa
        if (verificarVitoria()) {
            // Limpar as pilhas
            for (pilha in listaPilhas) {
                while (!pilha.pilhaVazia()) {
                    pilha.desempilha()
                }
            }

            // Embaralhar a lista e tentar novamente
            listaNumeros.shuffle()
            encherPilhas(listaNumeros)
        }
    }

    // Verificar se o jogador venceu
    fun verificarVitoria(): Boolean {
        // Para cada pilha no jogo
        for (pilha in listaPilhas) {
            val quantidade = pilha.quantidadeElementos()

            // Se a pilha tem entre 1 e 3 elementos, o jogo não acabou
            if (quantidade in 1..3) {
                return false
            }
            // Se a pilha tem 4 elementos, todos devem ser iguais
            else if (quantidade == 4) {
                // Aproveitando a função adicionada na classe Pilha
                if (!pilha.todosElementosIguais()) {
                    return false
                }
            }
        }
        return true
    }

    // Verificar se uma jogada é válida
    fun jogadaValida(origem: Int, destino: Int): Boolean {
        // Verificar índices válidos
        if (origem < 0 || origem >= listaPilhas.size || destino < 0 || destino >= listaPilhas.size) {
            return false
        }

        val pilhaOrigem = listaPilhas[origem]
        val pilhaDestino = listaPilhas[destino]

        // Usando a função adicionada na classe Pilha
        return pilhaOrigem.podeTransferirPara(pilhaDestino)
    }

    // Executar uma jogada
    fun realizarJogada(origem: Int, destino: Int): Pair<Boolean, String> {
        // Verificar se a jogada é válida
        if (!jogadaValida(origem, destino)) {
            val mensagem = when {
                origem < 0 || origem >= listaPilhas.size || destino < 0 || destino >= listaPilhas.size ->
                    "Índices inválidos"
                listaPilhas[origem].pilhaVazia() ->
                    "Pilha de origem vazia"
                listaPilhas[destino].pilhaCheia() ->
                    "Pilha de destino cheia"
                !listaPilhas[destino].pilhaVazia() &&
                        listaPilhas[origem].elementoDoTopo() != listaPilhas[destino].elementoDoTopo() ->
                    "Elementos do topo são diferentes"
                else ->
                    "Jogada inválida"
            }
            return Pair(false, mensagem)
        }

        // Executar a jogada
        val pilhaOrigem = listaPilhas[origem]
        val pilhaDestino = listaPilhas[destino]

        val elemento = pilhaOrigem.desempilha()
        pilhaDestino.empilha(elemento)

        return Pair(true, "Jogada realizada com sucesso")
    }

    // Obter a lista de pilhas (cópia defensiva)
    fun getListaPilhas(): List<Pilha> {
        return listaPilhas.map { it.clone() }
    }

    // Obter pilha específica pelo índice
    fun getPilha(indice: Int): Pilha? {
        return if (indice in listaPilhas.indices) listaPilhas[indice] else null
    }

    // Reiniciar o jogo com o mesmo número de cores
    fun reiniciarJogo() {
        inicializarJogo()
    }

    // Reiniciar o jogo com um novo número de cores
    fun reiniciarJogo(novoN: Int) {
        n = novoN
        inicializarJogo()
    }

    // Obter o número de cores no jogo atual
    fun getNumeroCores(): Int {
        return n
    }
}