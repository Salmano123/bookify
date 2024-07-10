import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.bookify.interfaces.Book
import org.json.JSONObject

class ApiService {
    companion object {
        private const val BASE_URL = "https://ipaddress/api"

        fun login(username: String, password: String, callback: (Boolean) -> Unit) {
            // Mock response delay to simulate network call
            Handler(Looper.getMainLooper()).postDelayed({
                // Simulate success response for specific credentials
                val success = username == "admin" && password == "password"
                callback(success)
            }, 2000)

            // Uncomment below code to enable actual API call when ready
            /*
            val url = URL("$BASE_URL/login")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")

                val jsonInputString = JSONObject().apply {
                    put("username", username)
                    put("password", password)
                }.toString()

                OutputStreamWriter(outputStream).use {
                    it.write(jsonInputString)
                }

                val responseCode = responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = StringBuffer()
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var inputLine: String?
                        while (reader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                    }
                    val jsonResponse = JSONObject(response.toString())
                    val success = jsonResponse.getBoolean("success")
                    callback(success)
                } else {
                    callback(false)
                }
            }
            */
        }

        fun register(username: String, password: String, email: String, phoneNumber: String, base64Image: String?, callback: (Boolean) -> Unit) {
            Log.d("ApiService", "Register Params - Username: $username, Password: $password, Email: $email, PhoneNumber: $phoneNumber, Base64Image: $base64Image")
            // Mock response delay to simulate network call
            Handler(Looper.getMainLooper()).postDelayed({
                // Simulate success response
                val success = true // Simulate a successful registration
                callback(success)
            }, 2000)

            // Uncomment below code to enable actual API call when ready
            /*
            val url = URL("$BASE_URL/register")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")

                val jsonInputString = JSONObject().apply {
                    put("username", username)
                    put("password", password)
                    put("email", email)
                    put("phoneNumber", phoneNumber)
                    base64Image?.let {
                        put("image", it)
                    }
                }.toString()

                OutputStreamWriter(outputStream).use {
                    it.write(jsonInputString)
                }

                val responseCode = responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = StringBuffer()
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var inputLine: String?
                        while (reader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                    }
                    val jsonResponse = JSONObject(response.toString())
                    val success = jsonResponse.getBoolean("success")
                    callback(success)
                } else {
                    callback(false)
                }
            }
            */
        }

        fun getPopularBooks(): List<Book> {
            return listOf(
                Book("The Lord of the Rings", "J.R.R. Tolkien", "Book cover description", "https://m.media-amazon.com/images/I/412JSB73D2L.jpg"),
                Book("The Hobbit", "J.R.R. Tolkien", "Book cover description", "https://m.media-amazon.com/images/I/418jD+Rsd5L._SY445_SX342_.jpg"),
                Book("Silmarillion", "J.R.R. Tolkien", "Book cover description", "https://m.media-amazon.com/images/I/41HthpRZluL._SY445_SX342_.jpg")
            )
        }

        fun getBooksByCategories(): List<Book> {
            return listOf(
                Book("The Great Gatsby", "F. Scott Fitzgerald", "", "https://m.media-amazon.com/images/I/61dRoDRubtL._SY522_.jpg"),
                Book("Pride and Prejudice", "Jane Austen", "", "https://m.media-amazon.com/images/I/51G7Ie1hExL._SY445_SX342_.jpg"),
                Book("To Kill a MockingBird", "Harper Lee", "", "https://m.media-amazon.com/images/I/51IXWZzlgSL._SY445_SX342_.jpg")
            )
        }
    }
}
