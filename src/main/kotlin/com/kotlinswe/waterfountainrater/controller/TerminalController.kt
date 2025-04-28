package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.cli.CliRunner
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@Controller
@RequestMapping("/admin/terminal")
class TerminalController(
    private val cliRunner: CliRunner,
    private val pendingCommands: MutableMap<String, String> = mutableMapOf(),
    private val sessionOutputs: MutableMap<String, String> = mutableMapOf()
) {

    @GetMapping
    fun terminal(model: Model): String {
        model.addAttribute("command", "")
        model.addAttribute("output", """
            🚰 Water Fountain Rater Web Interface
            Type 'help' for commands
            > 
        """.trimIndent())
        return "terminal"
    }

    @PostMapping("/execute")
    suspend fun executeCommand(
        @RequestParam command: String,
        @RequestParam(required = false) reviewText: String?,
        session: HttpSession
    ): String {
        val sessionId = session.id

        if (command.isBlank()) {
            return "terminal"
        }

        if (pendingCommands.containsKey(sessionId)) {
            val fullCommand = "${pendingCommands[sessionId]} $command"
            pendingCommands.remove(sessionId)
            return processCommand(fullCommand, session)
        }

        if (command.startsWith("rate") && command.split(" ").size < 7) {
            pendingCommands[sessionId] = command
            updateSessionOutput(sessionId, """
                ${getCurrentSessionOutput(sessionId)}
                $command
                Enter rating details (taste flow temp amb usability): 
                > 
            """.trimIndent())
            return "terminal"
        }

        return processCommand(command, session)
    }

    private suspend fun processCommand(command: String, session: HttpSession): String {
        val sessionId = session.id
        val currentOutput = getCurrentSessionOutput(sessionId)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        try {
            cliRunner.executeCommand(command)
        } catch (e: Exception) {
            outputStream.write("⚠️ Error: ${e.message}".toByteArray())
        } finally {
            System.setOut(System.out)
        }

        val newOutput = """
            ${currentOutput.trim()}
            $command
            ${outputStream.toString()}
            > 
        """.trimIndent()

        updateSessionOutput(sessionId, newOutput)
        return "terminal"
    }

    private fun getCurrentSessionOutput(sessionId: String): String {
        return sessionOutputs[sessionId] ?: """
            🚰 Water Fountain Rater Web Interface
            Type 'help' for commands
            > 
        """.trimIndent()
    }

    private fun updateSessionOutput(sessionId: String, output: String) {
        sessionOutputs[sessionId] = output
    }

    @GetMapping("/output")
    @ResponseBody
    fun getCurrentOutput(session: HttpSession): String {
        return getCurrentSessionOutput(session.id)
    }
}