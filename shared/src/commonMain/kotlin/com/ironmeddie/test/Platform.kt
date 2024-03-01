package com.ironmeddie.test

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform