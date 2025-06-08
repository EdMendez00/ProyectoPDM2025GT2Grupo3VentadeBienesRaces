package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.utils

/**
 * Clase sellada para representar resultados de operaciones
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
