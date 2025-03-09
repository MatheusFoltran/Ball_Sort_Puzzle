package com.example.ballsortpuzzle.modelo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Pilha {
    // Usando mutableStateListOf para reatividade com Compose
    private val elementos: SnapshotStateList<Int> = mutableStateListOf(-1, -1, -1, -1)

    // Verifica se a pilha está vazia
    fun pilhaVazia(): Boolean = elementos.all { it == -1 }

    // Verifica se a pilha está cheia
    fun pilhaCheia(): Boolean = elementos.none { it == -1 }

    // Retorna a quantidade de elementos na pilha
    fun quantidadeElementos(): Int = elementos.count { it != -1 }

    // Retorna o elemento do topo da pilha
    fun elementoDoTopo(): Int {
        val index = elementos.indexOfLast { it != -1 }
        return if (index != -1) {
            elementos[index]
        } else {
            throw IllegalStateException("Erro: Pilha Vazia")
        }
    }

    // Empilha um elemento na pilha (adiciona no topo)
    fun empilha(x: Int) {
        if (!pilhaCheia()) {
            val index = elementos.indexOfFirst { it == -1 }
            elementos[index] = x
        }
    }

    // Desempilha um elemento da pilha (remove do topo)
    fun desempilha(): Int {
        val index = elementos.indexOfLast { it != -1 }
        return if (index != -1) {
            val x = elementos[index]
            elementos[index] = -1
            x
        } else {
            throw IllegalStateException("Erro: Pilha Vazia")
        }
    }

    // Verifica se todos os elementos são iguais (útil para verificar se o puzzle está resolvido)
    fun todosElementosIguais(): Boolean {
        val elementosNaoVazios = elementos.filter { it != -1 }
        return elementosNaoVazios.isNotEmpty() &&
                elementosNaoVazios.all { it == elementosNaoVazios.first() }
    }

    // Verifica se é possível transferir um elemento para outra pilha
    fun podeTransferirPara(outraPilha: Pilha): Boolean {
        if (pilhaVazia()) return false
        if (outraPilha.pilhaCheia()) return false

        return outraPilha.pilhaVazia() ||
                (elementoDoTopo() == outraPilha.elementoDoTopo())
    }

    // Retorna uma cópia dos elementos da pilha
    fun getElementos(): List<Int> = elementos.toList()

    // Clona a pilha
    fun clone(): Pilha {
        val novaPilha = Pilha()
        for (i in elementos.indices) {
            novaPilha.elementos[i] = elementos[i]
        }
        return novaPilha
    }
}