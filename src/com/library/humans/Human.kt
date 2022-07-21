package com.library.humans

import com.library.utills.ConsoleColors
import com.library.utills.colorPrintln
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

abstract class Human<R : Human.Task.Result, T : Human.Task<R>>(
    id: String,
    open val name: String,
    open val surname: String,
) {
    open val startMessage: String? = null
    open val endMessage: String? = null

    private val commandQueue = ConcurrentLinkedQueue<T>()
    val busyLevel: Int get() = commandQueue.size

    private val work = AtomicBoolean(true)

    private val thread: Thread

    init {
        thread = thread(name = "Thread human {$id}") {
            startMessage?.let { colorPrintln(ConsoleColors.GREEN) { it } }
            while (work.get()) {
                val task: T? = commandQueue.poll()
                task?.execute()?.let(::onResult)
                Thread.sleep(10)
            }
        }
    }

    fun finish() {
        work.set(false)
        thread.join()
        endMessage?.let { colorPrintln(ConsoleColors.CYAN) { it } }
    }

    fun await() {
        thread.join()
    }

    fun addTask(task: T) {
        commandQueue.add(task)
    }

    abstract fun onResult(result: R)

    interface Task<out R : Task.Result> {
        fun execute(): R
        interface Result
    }
}
