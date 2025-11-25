package com.ragnar.expensetracker.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    val id: String? = null,
    val amount: Double,
    val description: String,
    val date: String, // Format: "YYYY-MM-DD"
    val category: String,
    val userId: String
) : Parcelable

data class SignupRequest(
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String? = null,
    val userId: String? = null,
    val message: String? = null
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)

data class ExpenseRequest(
    val amount: Double,
    val description: String,
    val date: String,
    val category: String,
    val userId: String
)