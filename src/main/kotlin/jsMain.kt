import java.text.SimpleDateFormat
import java.util.*


fun main() {
    repeat(1) {
        val states = States(previousState(), liveState())
        states.saveStateFile()

        if (states.changed) {
            println(states.message)
            logFile().appendText(states.message)
            sendEmail("SoYouStart-monitor - ${states.date}", "$soyoustartHref\n\n" + states.message)
        }
    }
}

class States(val old: List<String>, val new: List<String>) {
    val date: String = df.format(Date())
    private val header: String = "=".repeat(50) + " $date " + "=".repeat(50)
    fun saveStateFile() = previousStateFile().writeText(new.joinToString("\n"))
    private val psSet = old.toSet()
    private val lsSet = new.toSet()

    private val prev = diff(psSet, lsSet)
    private val next = diff(lsSet, psSet)

    private fun diff(a: Set<String>, b: Set<String>): String = a.subtract(b).joinToString("\n") { "  $it" }

    val changed: Boolean = prev.isNotEmpty() || next.isNotEmpty()
    val message: String get() = "$header\npreviously:\n$prev\n\nthen:\n$next"
}

var df = SimpleDateFormat("yyyy-MM-dd--HH-mm-ss")
fun liveState(): List<String> {
    val html = soyoustartHtml()
    return boxes(html).flatMap { box -> box.stringify() }
}

fun previousState(): List<String> =
    previousStateFile().let { file -> if (file.exists()) file.readLines() else emptyList() }
