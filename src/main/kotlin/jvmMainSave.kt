import java.io.File

fun main() {
    soyoustartHtml()
}

fun save(content: String) {
    val file = indexHtmlFile()
    file.writeText(content)
}
val soyoustartHref = "https://www.soyoustart.com/it/server-essential/"

fun soyoustartHtml(): String {
    return Retriever().use { retriever ->
        retriever.get(soyoustartHref)
            .also { content -> save(content) }
    }
}

fun indexHtmlFile() = file("index.html")
fun previousStateFile() = file("previous-state.txt")
fun logFile() = file("log.txt")

fun file(file: String): File {
    File("data").mkdirs()
    return File("data/$file")
}