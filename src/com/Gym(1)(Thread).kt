package com

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.random.Random

/**
Качалка.

3 тренера.
20 клиентов приходят через рандомное время. Thread.sleep()
тренировка занимает рандомное время. Thread.sleep()
Все клиенты должны быть обслужены.
Тренировка проходит в отдельном потоке thread {}
 */
fun main() {
    val gym = Gym()
    val coaches = listOf(
        Coach(name = "Alex", gym = gym, sport = Sport.BODYBUILDING),
        Coach(name = "Anton", gym = gym, sport = Sport.BOX),
        Coach(name = "Dimonchik", gym = gym, sport = Sport.POWERLIFTING)
    )
    val visitors = listOf(
        Visitor(name = "Saneck", sport = Sport.BODYBUILDING),
        Visitor(name = "Vasyan", sport = Sport.BOX),
        Visitor(name = "Alesha", sport = Sport.POWERLIFTING),
        Visitor(name = "Petrovich", sport = Sport.BOX),
        Visitor(name = "Shloma", sport = Sport.BODYBUILDING),
        Visitor(name = "Adolf", sport = Sport.POWERLIFTING),
        Visitor(name = "Kolyan", sport = Sport.POWERLIFTING),
        Visitor(name = "Tolyan", sport = Sport.BODYBUILDING),
        Visitor(name = "Vovan", sport = Sport.BOX),
    )

    gym.start(coaches = coaches)

    val visitorsThread = thread(name = "Visitors chain thread") {
        visitors.forEach { visitor ->
            gym.visit(visitor)
            Thread.sleep(Random.nextLong(from = 10, until = 51))
        }
    }
    visitorsThread.join()
    visitors.forEach {
        it.await()
    }
    gym.finish()
    println("\t\tРабочий день окончен.")
    println("\t\tТренажерный зал принял [${visitors.size}] посетителей.")
}

private class Gym {
    private var coaches: List<Coach> = emptyList()

    fun start(coaches: List<Coach>) {
        this.coaches = coaches
    }

    fun visit(visitor: Visitor) {
        firstAvailableCoach(visitor).askThePurposeOfTheVisitor(visitor = visitor)
    }

    fun finish() {
        coaches.forEach {
            it.finish(true)
        }
    }

    fun firstAvailableCoach(visitor: Visitor): Coach {
        val coach = coaches
            .filter { !it.visitorAtGym.get() }
            .filter { it.sport == visitor.sport }
            .shuffled()
            .minByOrNull { it.busyLevel }
        if (coach == null) {
            Thread.sleep(100)
            return firstAvailableCoach(visitor)
        } else {
            return coach
        }
    }
}

private class Visitor(
    name: String,
    val sport: Sport
) : Person<Visitor.Result, Visitor.Task>(name) {

    override val startMessage: String = "\t$name пришел в тренажерный зал"
    override val endMessage: String = "\t$name покинул тренражерный зал."

    override fun onResult(result: Result) {
        when (result) {
            Result.TellGoals -> Unit
            Result.TrainAccordingToPlan -> Unit
        }
    }

    fun tellTheCoachAboutTheGoal(coach: Coach) {
        addTask(Task.TellGoals(visitor = this, coach = coach, sport = sport))
    }

    fun getAWorkoutPlanAndStartExercising(coach: Coach) {
        addTask(Task.TrainAccordingToPlan(visitor = this, coach = coach, sport = sport))
    }

    sealed class Task : Person.Task<Result> {
        class TellGoals(
            val visitor: Visitor,
            val coach: Coach,
            val sport: Sport
        ) : Task() {
            override fun execute(): Result {
                println("`${visitor.name}` рассказывает тренеру ${coach.name} что хочет заниматься `${sport.name}`.")
                Thread.sleep(Random.nextLong(5, 16))
                println("`${visitor.name}` пошел переодеваться `${coach.name}` составлет план тренировки.")
                coach.createAWorkoutPlan(visitor)
                return Result.TellGoals
            }
        }

        class TrainAccordingToPlan(
            val visitor: Visitor,
            val coach: Coach,
            val sport: Sport
        ) : Task() {
            override fun execute(): Result {
                coach.visitorTheControl(visitor)
                println("`${visitor.name}` приступил к тренировки по `${sport.name}` c `${coach.name}`.")
                Thread.sleep(Random.nextLong(50, 101))
                println("`${visitor.name}` закончил тренировку c `${coach.name}`")
                coach.finishedTraining(visitor)
                return Result.TrainAccordingToPlan
            }
        }
    }

    sealed class Result : Person.Task.Result {
        object TellGoals : Result()
        object TrainAccordingToPlan : Result()
    }

}

private class Coach(
    name: String,
    private val gym: Gym,
    val sport: Sport,
) : Person<Coach.Result, Coach.Task>(name) {

    override val startMessage: String = "\t$name приступил к работе."
    override val endMessage: String = "\t$name окончил работу."

    val visitorAtGym = AtomicBoolean(false)

    override fun onResult(result: Result) {
        when (result) {
            Result.GettingTheVisitorGoal -> Unit
            Result.CreatingAWorkoutPlan -> Unit
            Result.VisitorTheControl -> Unit
            Result.FinishedTraining -> Unit
        }
    }

    fun askThePurposeOfTheVisitor(visitor: Visitor) {
        addTask(Task.GettingTheVisitorGoal(coach = this, visitor = visitor))
    }

    fun createAWorkoutPlan(visitor: Visitor) {
        addTask(Task.CreatingAWorkoutPlan(coach = this, visitor = visitor))
    }

    fun visitorTheControl(visitor: Visitor) {
        addTask(Task.VisitorTheControl(coach = this, visitor = visitor))
    }

    fun finishedTraining(visitor: Visitor) {
        addTask(Task.FinishedTraining(coach = this, visitor = visitor))
    }

    sealed class Task : Person.Task<Result> {
        class GettingTheVisitorGoal(
            val coach: Coach,
            val visitor: Visitor
        ) : Task() {
            override fun execute(): Result {
                coach.visitorAtGym.set(true)
                visitor.tellTheCoachAboutTheGoal(coach)
                Thread.sleep(Random.nextLong(10, 21))
                return Result.GettingTheVisitorGoal
            }
        }

        open class CreatingAWorkoutPlan(
            val coach: Coach,
            val visitor: Visitor
        ) : Task() {
            override fun execute(): Result {
                println("Тренее ${coach.name} начал составление плана тренировок для ${visitor.name}.")
                Thread.sleep(5)
                println("Тренировочный план для ${visitor.name} от тренера ${coach.name} готов.")
                visitor.getAWorkoutPlanAndStartExercising(coach)
                return Result.CreatingAWorkoutPlan
            }
        }

        class VisitorTheControl(
            val coach: Coach,
            val visitor: Visitor
        ) : Task() {
            override fun execute(): Result {
                println(
                    "Тренер ${coach.name} проводит тренировку в соотвтетствии с составленным планом " +
                            "и контролирует технику движений у ${visitor.name}"
                )
                return Result.VisitorTheControl
            }
        }

        class FinishedTraining(
            val coach: Coach,
            val visitor: Visitor
        ) : Task() {
            override fun execute(): Result {
                println("Тренер ${coach.name} провел тренировку для ${visitor.name}.")
                visitor.finish(true)
                coach.visitorAtGym.set(false)
                return Result.FinishedTraining
            }
        }
    }

    sealed class Result : Person.Task.Result {
        object GettingTheVisitorGoal : Result()
        object CreatingAWorkoutPlan : Result()
        object VisitorTheControl : Result()
        object FinishedTraining : Result()
    }

}

enum class Sport {
    POWERLIFTING, BOX, BODYBUILDING
}

private abstract class Person<R : Person.Task.Result, T : Person.Task<R>>(val name: String) {

    open val startMessage: String? = null
    open val endMessage: String? = null

    val busyLevel: Int get() = commandQueue.size

    private var work = AtomicBoolean(true)

    private val commandQueue = ConcurrentLinkedQueue<T>()

    private val thread: Thread

    init {
        thread = thread(name = "$name thread") {
            startMessage?.let { println(it) }
            while (work.get()) {
                val task: T? = commandQueue.poll()
                task?.execute()?.let(::onResult)
                Thread.sleep(10)
            }
        }
    }

    fun finish(await: Boolean) {
        work.set(false)
        if (await) {
            await()
        }
        endMessage?.let { println(it) }
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