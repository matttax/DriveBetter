import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Api {

    private val client = HttpClient()

    suspend fun getResponse(): String {
        println("getResponse")
        val response = client.get("https://ktor.io/docs/")
        return response.bodyAsText()
    }
}