package com.bieniucieniu.hometooling

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform