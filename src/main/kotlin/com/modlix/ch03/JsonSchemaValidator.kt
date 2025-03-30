import org.json.JSONObject
import org.json.JSONArray

fun validateJson(schema: JSONObject, data: Any?, path: String = ""): String? {
    when (schema.getString("type")) {
        "object" -> {
            if (data !is JSONObject) return "Error: Expected an object at '$path'"
            val properties = schema.optJSONObject("properties") ?: JSONObject()
            val required = schema.optJSONArray("required") ?: JSONArray()

            for (i in 0 until required.length()) {
                val key = required.getString(i)
                if (!data.has(key)) return "Error: Missing required field '$key'"
            }

            for (key in data.keys()) {
                properties.optJSONObject(key)?.let {
                    val result = validateJson(it, data.opt(key), "$path.$key".trim('.'))
                    if (result != null) return result
                }
            }
        }
        "string" -> {
            if (data !is String) return "Error: '$path' should be a string"
        }
        "integer" -> {
            if (data !is Int) return "Error: '$path' should be an integer"
            if (schema.has("minimum") && data < schema.getInt("minimum"))
                return "Error: '$path' should be at least ${schema.getInt("minimum")}"
            if (schema.has("maximum") && data > schema.getInt("maximum"))
                return "Error: '$path' should be at most ${schema.getInt("maximum")}"
        }
        "boolean" -> {
            if (data !is Boolean) return "Error: '$path' should be a boolean"
        }
        "array" -> {
            if (data !is JSONArray) return "Error: '$path' should be an array"
            val itemSchema = schema.optJSONObject("items")
            if (itemSchema != null) {
                for (i in 0 until data.length()) {
                    val result = validateJson(itemSchema, data.opt(i), "$path[$i]")
                    if (result != null) return result
                }
            }
        }
    }
    return null
}

fun main() {
    val schema = JSONObject("""
        {
            "type": "object",
            "properties": {
                "name": { "type": "string" },
                "age": { "type": "integer", "minimum": 18 } 
            },
            "required": ["name", "age"]
        }
    """)// rules for min age

    val dataValid = JSONObject("""
        {
            "name": "Alice",
            "age": 23
        }
    """)

    val dataInValidMinAge = JSONObject("""
        {
            "name": "Alice",
            "age": 2
        }
    """)

    val dataInvalid = JSONObject("""
        {
            "name": "Bob",
            "age": "twenty"
        }
    """)

    println(validateJson(schema, dataValid) ?: "Valid JSON") // Implementing a valid json
    println(validateJson(schema, dataInValidMinAge) ?: "Valid JSON") //Implementing a invalid json with min age criteria
    println(validateJson(schema, dataInvalid) ?: "Valid JSON") // datatype mismatch
}
