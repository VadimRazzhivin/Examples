package com.library.utills

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService

inline fun <reified T> ExecutorService.await(crossinline block: () -> T): T {
    return submit(Callable { block() }).get() as T
}
