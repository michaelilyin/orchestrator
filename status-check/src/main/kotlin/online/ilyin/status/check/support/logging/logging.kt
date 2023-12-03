package online.ilyin.status.check.support.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.jvm.jvmName

fun logger(caller: () -> Unit): Logger {
    return LoggerFactory.getLogger(caller::class.java)
}

inline fun Logger.info(message: () -> String) {
    if (isInfoEnabled) {
        info(log(this, message))
    }
}

inline fun Logger.error(e: Throwable, message: () -> String) {
    if (isErrorEnabled) {
        error(log(this, message), e)
    }
}

inline fun log(logger: Logger, message: () -> String): String {
    return try {
        message()
    } catch (e: Exception) {
        logger.warn("Failed to get log message", e)
        "Failed to get log message ${e.message}"
    }
}