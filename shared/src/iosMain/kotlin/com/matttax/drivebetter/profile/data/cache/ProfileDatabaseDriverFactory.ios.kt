package com.matttax.drivebetter.profile.data.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.matttax.drivebetter.AccountDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = AccountDatabase.Schema,
            name = "AccountDatabase.db"
        )
    }
}
