package com.matttax.drivebetter.profile.data.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.matttax.drivebetter.AccountDatabase
import com.matttax.drivebetter.DriveBetterApp

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AccountDatabase.Schema,
            context = DriveBetterApp.context,
            name = "AccountDatabase.db"
        )
    }
}
