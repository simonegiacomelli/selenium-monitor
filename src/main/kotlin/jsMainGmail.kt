import java.util.*

fun main() {
    sendEmail("hi, it's selenium-monitor", "a good old body")
}

fun sendEmail(subject: String, body: String) {
    val file = file("smtp.properties")
    if (!file.exists())
        file.writeText("# set properties\n" +
                "# ${Gmail.MAIL_SMTP_USER}=foo@example.com\n" +
                "# ${Gmail.MAIL_SMTP_PASSWORD}=secret\n" +
                "# recipient=bar@example.com")
    val smtpProperties = file.bufferedReader().use { reader -> Properties().also { it.load(reader) } }

    val gmail = Gmail(smtpProperties)
    val recipient = smtpProperties.getProperty("recipient")
    if (gmail.username.isBlank() || gmail.password.isBlank() || recipient.isBlank())
        println("No username, password or recipient. No email will be sent")
    else
        gmail.send(subject, body, listOf(recipient))
}