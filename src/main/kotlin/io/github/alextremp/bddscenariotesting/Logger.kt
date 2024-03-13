package io.github.alextremp.bddscenariotesting

import java.util.logging.Logger

object Logger {

    private val log = Logger.getLogger(Logger::class.java.name)

    fun info(message: String) {
        log.info { "\u001B[34m$message\u001B[0m" }
    }

    fun error(message: String) {
        log.severe { "\u001B[31m$message\u001B[0m" }
    }

    fun success(message: String) {
        log.info { "\u001B[32m$message\u001B[0m" }
    }
}