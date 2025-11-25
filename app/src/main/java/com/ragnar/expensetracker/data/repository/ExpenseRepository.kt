package com.ragnar.expensetracker.data.repository

import com.ragnar.expensetracker.data.api.RetrofitClient
import com.ragnar.expensetracker.data.model.*

class ExpenseRepository {

    private val api = RetrofitClient.api

    suspend fun signup(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.signup(SignupRequest(email, password))
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Exception("Signup failed: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Exception("Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createExpense(expense: Expense): Result<Expense> {
        val request = ExpenseRequest(
            amount = expense.amount,
            description = expense.description,
            date = expense.date,
            category = expense.category,
            userId = expense.userId
        )

        return try {
            val response = api.createExpense(request)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Exception("Failed to create expense"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExpenses(
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<Expense>> {
        return try {
            val response = api.getExpenses(category, startDate, endDate)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Exception("Failed to fetch expenses"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExpense(id: String, expense: Expense): Result<Expense> {
        val request = ExpenseRequest(
            amount = expense.amount,
            description = expense.description,
            date = expense.date,
            category = expense.category,
            userId = expense.userId
        )

        return try {
            val response = api.updateExpense(id, request)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Exception("Failed to update expense"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExpense(id: String): Result<Unit> {
        return try {
            val response = api.deleteExpense(id)
            if (response.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception("Failed to delete expense"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
