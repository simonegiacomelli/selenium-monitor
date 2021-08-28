import java.lang.Exception
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress
import java.lang.RuntimeException
import java.util.*
import javax.mail.Message
import javax.mail.Session

/* Simone 02/04/2014 11:06 */

class Gmail(properties: Properties) {
    val username: String
    val password: String
    private val props = Properties()

    init {
        ensureProp(properties)
        props.putAll(properties)
        username = props.getProperty(MAIL_SMTP_USER)
        password = props.getProperty(MAIL_SMTP_PASSWORD)
    }

    constructor(from: String, pass: String) : this(getProperties(from, pass)) {}

    fun send(subject: String, body: String) {
        send(subject, body, listOf(username))
    }

    fun send(subject: String, body: String, to: List<String>) {
        try {
            val session = Session.getInstance(props)
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            for (recipient in to) message.addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
            message.subject = subject
            message.setText(body)
            val transport = session.getTransport("smtp")
            transport.connect(props.getProperty(MAIL_SMTP_HOST), username, password)
            transport.sendMessage(message, message.allRecipients)
            transport.close()
        } catch (ex: Exception) {
            throw GmailSendException(ex)
        }
    }

    private class GmailSendException(ex: Exception?) : RuntimeException(ex)
    companion object {
        const val MAIL_SMTP_HOST = "mail.smtp.host"
        const val MAIL_SMTP_USER = "mail.smtp.user"
        const val MAIL_SMTP_PASSWORD = "mail.smtp.password"

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val from = "auto@example.com"
            val pass = "secret"
            val to = arrayOf("info@example.com")
            val subject = "Java send mail example"
            val body = "Welcome to JavaMail!"
            send(from, pass, subject, body, *to)
        }

        private fun send(from: String, pass: String, subject: String, body: String, vararg to: String) {
            Gmail(from, pass).send(subject, body, listOf(*to))
        }

        private fun getProperties(from: String, pass: String): Properties {
            val props = Properties()
            props[MAIL_SMTP_USER] = from
            props[MAIL_SMTP_PASSWORD] = pass
            ensureProp(props)
            return props
        }

        private fun ensureProp(props: Properties) {
            putIfMissing(props, "mail.smtp.starttls.enable", "true")
            putIfMissing(props, MAIL_SMTP_HOST, "smtp.gmail.com")
            putIfMissing(props, "mail.smtp.port", "587")
            putIfMissing(props, "mail.smtp.auth", "true")

            putIfMissing(props, "mail.debug", "true")
        }

        private fun putIfMissing(props: Properties, key: String, value: String) {
            if (!props.containsKey(key)) props[key] = value
        }
    }


}