import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.io.Closeable
import java.io.File

class Retriever : Closeable {
    private val lazy = ClearableLazy({ it.quit() }) { ChromeDriver() }
    val driver: WebDriver by lazy

    fun save(href: String, path: String) {
        File(path).writeText(get(href))
    }

    fun get(href: String): String {
        driver[href]
        return driver.pageSource ?: ""
    }

    override fun close() {
        lazy.close()
    }
}