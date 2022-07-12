
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun main() {
    val people = 10
    val doors = 4
    println(
                "В комнате 4 двери, в каждую из которрых входит $people человек.\n" +
                "В итоге ${peopleInTheRoom(people, doors)} человек вошли и находятся в комнате"
    )
}

fun peopleInTheRoom(people: Int, doors: Int): Int {
    val result = AtomicInteger(0)
    val thread = mutableListOf<Thread>()
    for (d in 1..doors) {
        thread.add(
            thread {
                for (h in 1..people) {
                    result.incrementAndGet()
                }
            }
        )
    }
    thread.forEach {
        it.join()
    }
    return result.get()
}