import org.jsoup.Jsoup
import org.jsoup.nodes.Element

fun main() {
    val content = indexHtmlFile().readText()
    inspect(content)
}

fun inspect(content: String) {
    boxes(content).flatMap { box ->
        box.stringify()
    }.onEach {
        println("=".repeat(100))
        println(it)
//        println(it.html())
//        println(it.ownText())

    }


}

fun boxes(soyoustartHtml: String) = Jsoup.parse(soyoustartHtml).select("div.col-s-12").map { Box(it) }

class Item(val element: Element) {
    val nation: String get() = element.ownText().trim()
    val availability: String get() = element.attr("data-availability")
    val keyValue: String get() = "$nation=$availability"
    override fun toString() = keyValue
}

class Box(val element: Element) {
    val caption: String
        get() {
            return element.select("p.text--large").joinToString { it.ownText().replace("\n", "\n\n") }
        }

    val selectItems by lazy { selectDiveElement().map { Item(it) } }

    fun selectDiveElement() = element.select("div.select-items").flatMap { it.children() }

    fun stringify() = selectItems.map { item -> caption + ", " + item.keyValue }
}
