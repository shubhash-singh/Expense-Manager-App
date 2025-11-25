package com.ragnar.expensetracker.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragnar.expensetracker.data.model.Expense
import com.ragnar.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ExpenseViewModel : ViewModel() {

    private val repository = ExpenseRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _expensesState = MutableStateFlow<List<Expense>>(emptyList())
    val expensesState: StateFlow<List<Expense>> = _expensesState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    val categories = listOf("Food", "Travel", "Bills", "Shopping", "Entertainment", "Other")

    fun signup(email: String, password: String) {
        // Simple validation
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Email and password cannot be empty"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.signup(email, password)
            result.onSuccess { response ->
                _userId.value = response.userId
                _authState.value = AuthState.Authenticated
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Signup failed"
                _authState.value = AuthState.Error
            }

            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        // Simple validation
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Email and password cannot be empty"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.login(email, password)
            result.onSuccess { response ->
                _userId.value = response.userId
                _authState.value = AuthState.Authenticated
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Login failed"
                _authState.value = AuthState.Error
            }

            _isLoading.value = false
        }
    }

    fun loadExpenses(category: String? = null, startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getExpenses(category, startDate, endDate)
            result.onSuccess { expenses ->
                _expensesState.value = expenses
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to load expenses"
            }

            _isLoading.value = false
        }
    }

    fun addExpense(amount: Double, description: String, date: String, category: String) {
        val currentUserId = _userId.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val expense = Expense(
                amount = amount,
                description = description,
                date = date,
                category = category,
                userId = currentUserId
            )

            val result = repository.createExpense(expense)
            result.onSuccess {
                // Reload expenses after adding
                Log.d("ExpenseViewModel","Expense added successfully")
                loadExpenses()
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to add expense"
                Log.e("ExpenseViewModel","Error: ${error.message}")
            }

            _isLoading.value = false
        }
    }

    fun updateExpense(id: String, amount: Double, description: String, date: String, category: String) {
        val currentUserId = _userId.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val expense = Expense(
                id = id,
                amount = amount,
                description = description,
                date = date,
                category = category,
                userId = currentUserId
            )

            val result = repository.updateExpense(id, expense)
            result.onSuccess {
                loadExpenses()
                Log.d("ExpenseViewModel","Expense updated successfully")
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to update expense"
            }

            _isLoading.value = false
        }
    }

    fun deleteExpense(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.deleteExpense(id)
            result.onSuccess {
                Log.d("ExpenseViewModel","Expense updated successfully")
                loadExpenses()
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to delete expense"
            }

            _isLoading.value = false
        }
    }

    fun getTotalSpending(): Double {
        return _expensesState.value.sumOf { it.amount }
    }

    fun getSpendingByCategory(): Map<String, Double> {
        return _expensesState.value
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Authenticated : AuthState()
    object Error : AuthState()
}