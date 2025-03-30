import java.util.*

class HuffmanNode(val char: Char, val freq: Int, val left: HuffmanNode? = null, val right: HuffmanNode? = null) : Comparable<HuffmanNode> {
    override fun compareTo(other: HuffmanNode) = this.freq - other.freq
}

fun buildHuffmanTree(freqMap: Map<Char, Int>): HuffmanNode {
    val pq = PriorityQueue<HuffmanNode>()
    freqMap.forEach { (char, freq) -> pq.add(HuffmanNode(char, freq)) }

    while (pq.size > 1) {
        val left = pq.poll()
        val right = pq.poll()
        pq.add(HuffmanNode('\u0000', left.freq + right.freq, left, right))
    }
    return pq.poll()
}

fun generateHuffmanCodes(node: HuffmanNode?, prefix: String, codeMap: MutableMap<Char, String>) {
    if (node == null) return
    if (node.char != '\u0000') {
        codeMap[node.char] = prefix
    }
    generateHuffmanCodes(node.left, prefix + "0", codeMap)
    generateHuffmanCodes(node.right, prefix + "1", codeMap)
}

fun compress(input: String): Pair<String, Map<Char, String>> {
    if (input.isEmpty()) return "" to emptyMap()

    val freqMap = input.groupingBy { it }.eachCount()
    val root = buildHuffmanTree(freqMap)
    val codeMap = mutableMapOf<Char, String>()
    generateHuffmanCodes(root, "", codeMap)

    val encoded = input.map { codeMap[it] }.joinToString("")
    return encoded to codeMap
}

fun decompress(encoded: String, codeMap: Map<Char, String>): String {
    if (encoded.isEmpty()) return ""

    val reverseCodeMap = codeMap.entries.associateBy({ it.value }, { it.key })
    val decompressed = StringBuilder()
    var temp = ""

    for (bit in encoded) {
        temp += bit
        if (temp in reverseCodeMap) {
            decompressed.append(reverseCodeMap[temp])
            temp = ""
        }
    }
    return decompressed.toString()
}

fun main() {
    val original = "こんにちは世界🌍🌎🌏"
    val (compressed, codeMap) = compress(original)
    println("codeMap: $codeMap")
    val decompressed = decompress(compressed, codeMap)

    println("Original: $original")
    println("Compressed: $compressed")
    println("Decompressed: $decompressed")
    println("Is lossless: ${original == decompressed}")

}