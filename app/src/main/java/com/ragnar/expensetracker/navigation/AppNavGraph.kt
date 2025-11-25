package com.ragnar.expensetracker.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ragnar.expensetracker.data.model.Expense
import com.ragnar.expensetracker.ui.auth.LoginScreen
import com.ragnar.expensetracker.ui.auth.SignupScreen
import com.ragnar.expensetracker.ui.home.*
import com.ragnar.expensetracker.ui.viewmodel.ExpenseViewModel

/**
 * Navigation routes
 */
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val ADD_EXPENSE = "add_expense"
    const val EDIT_EXPENSE = "edit_expense"
    const val DASHBOARD = "dashboard"
}

/**
 * Main navigation graph for the app
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: ExpenseViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        // Clear back stack so user can't go back to login
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Routes.SIGNUP)
                },
                viewModel = viewModel
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Routes.HOME) {
                        // Clear back stack
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onAddExpense = {
                    navController.navigate(Routes.ADD_EXPENSE)
                },
                onEditExpense = { expense ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("expense", expense)
                    navController.navigate(Routes.EDIT_EXPENSE)
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.DASHBOARD)
                },
                viewModel = viewModel
            )
        }

        composable(Routes.ADD_EXPENSE) {
            AddExpenseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(Routes.EDIT_EXPENSE) {
            val expense = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Expense>("expense")

            if (expense != null) {
                EditExpenseScreen(
                    expense = expense,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    viewModel = viewModel
                )
            }
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}