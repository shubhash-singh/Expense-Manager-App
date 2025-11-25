package com.ragnar.expensetracker.data.api


import com.ragnar.expensetracker.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ExpenseApi {

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("expenses")
    suspend fun createExpense(@Body request: ExpenseRequest): Response<Expense>

    @GET("expenses")
    suspend fun getExpenses(
        @Query("category") category: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<Expense>>

    @PUT("expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: String,
        @Body request: ExpenseRequest
    ): Response<Expense>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<Unit>
}
