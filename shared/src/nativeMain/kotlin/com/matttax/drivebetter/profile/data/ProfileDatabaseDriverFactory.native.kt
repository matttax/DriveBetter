package com.matttax.drivebetter.profile.data

import app.cash.sqldelight.db.SqlDriver
import com.matttax.drivebetter.AccountDatabase

actual class DatabaseDriverFactory actual constructor() {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = AccountDatabase.Schema,
            name = "AccountDatabase.db"
        )
    }
}
