package dev.ell.pillora

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform