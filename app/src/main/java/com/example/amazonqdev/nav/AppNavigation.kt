package com.example.amazonqdev.nav

enum class AppScreen(
    val route: String,
    val title: String,
    val icon: String
) {
    HOME("home", "홈", "📷"),
    CALENDAR("calendar", "캘린더", "📅"),
    SETTINGS("settings", "설정", "⚙️")
}