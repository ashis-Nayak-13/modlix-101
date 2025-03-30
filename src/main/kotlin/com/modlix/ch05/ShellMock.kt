import java.io.File
import java.util.Scanner

class SimpleShell {
    private var currentDirectory: File = File(System.getProperty("user.dir"))

    fun start() {
        val scanner = Scanner(System.`in`)
        println("Welcome to SimpleShell! Type 'help' for available commands.")

        while (true) {
            print("$ ")
            val input = scanner.nextLine().trim()
            if (input.isEmpty()) continue
            val args = input.split(" ")
            when (args[0]) {
                "pwd" -> println(currentDirectory.absolutePath)
                "cd" -> changeDirectory(args.getOrNull(1))
                "ls" -> listFiles()
                "echo" -> println(args.drop(1).joinToString(" "))
                "type" -> printFileType(args.getOrNull(1))
                "help" -> printHelp()
                "exit" -> {
                    println("Goodbye!")
                    return
                }
                else -> executeCommand(args)
            }
        }
    }

    private fun changeDirectory(path: String?) {
        if (path == null) {
            println("shell: cd: missing operand")
            return
        }
        val newDir = File(currentDirectory, path).canonicalFile
        if (newDir.exists() && newDir.isDirectory) {
            currentDirectory = newDir
        } else {
            println("shell: cd: $path: No such file or directory")
        }
    }

    private fun listFiles() {
        currentDirectory.listFiles()?.forEach { print("${it.name} ") }
        println()
    }

    private fun printFileType(filename: String?) {
        if (filename == null) {
            println("shell: type: missing operand")
            return
        }
        val file = File(currentDirectory, filename)
        when {
            file.isFile -> println("$filename: Regular file")
            file.isDirectory -> println("$filename: Directory")
            else -> println("$filename: Unknown type")
        }
    }

    private fun printHelp() {
        println("Available commands:")
        println("  pwd        - Print current directory")
        println("  cd <dir>   - Change directory")
        println("  ls         - List files and directories")
        println("  echo <msg> - Print message to stdout")
        println("  type <file> - Show file type")
        println("  exit       - Terminate the shell")
        println("  help       - Show this help message")
    }

    private fun executeCommand(args: List<String>) {
        try {
            val process = ProcessBuilder(args)
                .directory(currentDirectory)
                .inheritIO()
                .start()
            process.waitFor()
        } catch (e: Exception) {
            println("shell: command not found: ${args[0]}")
        }
    }
}

fun main() {
    SimpleShell().start()
}
