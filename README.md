# Expense Tracker App - Setup Guide

A beginner-friendly Android expense tracking application built with Jetpack Compose.

## 📱 Features

- User authentication (Login/Signup)
- Add, edit, and delete expenses
- Filter expenses by category
- View spending dashboard with totals and category breakdown
- Clean Material 3 UI design

## 🛠️ Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **MVVM Architecture** - Simple ViewModel pattern
- **Navigation Component** - Compose navigation
- **Retrofit** - REST API communication
- **Coroutines** - Asynchronous operations
- **Material 3** - Modern design system

## 📁 Project Structure

```
app/
├── data/
│   ├── api/
│   │   └── ExpenseApi.kt           # Retrofit API interface
│   │   └── RetrofitClient.kt       # Retrofit API Client
│   ├── model/
│   │   └── Expense.kt              # Data models
│   └── repository/
│       └── ExpenseRepository.kt    # Data operations
│
├── ui/
│   ├── auth/
│   │   ├── LoginScreen.kt          # Login UI
│   │   └── SignupScreen.kt         # Signup UI
│   ├── home/
│   │   ├── HomeScreen.kt           # Expenses list
│   │   ├── AddExpenseScreen.kt     # Add new expense
│   │   ├── EditExpenseScreen.kt    # Edit expense
│   │   └── DashboardScreen.kt      # Spending summary
│   ├── components/
│   │   └── ExpenseCard.kt          # Reusable expense card
│   ├── viewmodel/
│   │   └── ExpenseViewModel.kt     # Business logic
│   └── theme/
│       └── Theme.kt                # Material 3 theme
│
├── navigation/
│   └── AppNavGraph.kt              # Navigation setup
│
└── MainActivity.kt                  # Single Activity
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 8 or higher
- Android SDK (API level 24+)

### Installation Steps

1. **Clone or download this project**

2. **Point Retrofit to your backend**
   
   Open `app/src/main/java/com/ragnar/expensetracker/data/api/RetrofitClient.kt`
   
   Update the `BASE_URL` to match the Spring Boot server that exposes `/auth` and `/expenses`.  
   Use the machine address that the Android emulator/device can reach (e.g. `http://192.168.x.x:8080/`), not `localhost`.

3. **Sync Gradle**
   
   Open the project in Android Studio and sync Gradle files.

4. **Run the app**
   
   Click the "Run" button or press `Shift + F10`

5. **Make sure the backend is online**
   
   Start the Spring Boot project (`ExpenseManager_Backend`) so the Android client can authenticate and sync expenses. The emulator/device and backend host must be on the same network.

## 🔧 Configuration

### API Endpoints

The app expects the following API endpoints:

#### Authentication
- `POST /auth/signup` - Create new account
- `POST /auth/login` - Login existing user

#### Expenses
- `POST /expenses` - Create expense
- `GET /expenses` - Get all expenses (with optional filters)
- `PUT /expenses/{id}` - Update expense
- `DELETE /expenses/{id}` - Delete expense

### Request/Response Examples

**Signup/Login Request:**
```json
{
  "email": "demo@example.com",
  "password": "password123"
}
```

**Create/Update Expense Request:**
```json
{
  "amount": 42.50,
  "description": "Lunch with team",
  "date": "2025-01-12",
  "category": "Food",
  "userId": "user-123"
}
```

**Auth Response:**
```json
{
  "message": "Login successful",
  "userId": "user-123",
  "token": "user-123"
}
```

**Get Expenses (with filters):**
```
GET /expenses?category=Food&startDate=2025-01-01&endDate=2025-01-31
```

## 📝 Key Components Explained

### 1. ExpenseViewModel
The single ViewModel that manages all app state:
- Authentication state
- Expenses list
- Loading states
- Error handling

### 2. Navigation
Simple navigation between 6 screens:
- Login → Signup
- Login → Home (after auth)
- Home → Add Expense
- Home → Edit Expense
- Home → Dashboard

### 3. StateFlow
Used throughout the app to observe state changes:
```kotlin
val expenses by viewModel.expensesState.collectAsState()
```

### 4. Categories
Hardcoded category list (as per requirements):
- Food
- Travel
- Bills
- Shopping
- Entertainment
- Other


## 🔍 How to Use

1. **Sign up** or **Login** with email and password
2. **View expenses** on the home screen
3. **Filter** expenses by category using the dropdown
4. **Add new expense** using the floating action button (+)
5. **Edit** any expense by clicking the edit icon
6. **Delete** expenses by clicking the delete icon
7. **View dashboard** to see spending totals and category breakdown
