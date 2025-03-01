package com.github.lasbonbe.raccoonside.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.jfx.JFXPanel
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import java.io.File
import kotlin.random.Random

class RaccoonVideoToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val jfxPanel = JFXPanel()
        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(jfxPanel, "Raccoon Videos", false)
        contentManager.addContent(content)

        Platform.runLater {
            val player = createVideoPlayer()
            jfxPanel.scene = Scene(StackPane(MediaView(player)))
        }
    }

    private fun createVideoPlayer(): MediaPlayer {
        val videoFile = getRandomVideoFile()
        val media = Media(videoFile.toURI().toString())
        val player = MediaPlayer(media)
        player.setOnEndOfMedia {
            Platform.runLater {
                player.stop()
                player.media = Media(getRandomVideoFile().toURI().toString())
                player.play()
            }
        }
        player.play()
        return player
    }

    private fun getRandomVideoFile(): File {
        val videoDir = File(System.getProperty("user.home"), ".rbide/videos")
        if (!videoDir.exists() || !videoDir.isDirectory) {
            videoDir.mkdirs()
        }
        val videos = videoDir.listFiles { file -> file.extension in listOf("mp4", "mov", "avi") } ?: emptyArray()
        return if (videos.isNotEmpty()) videos[Random.nextInt(videos.size)] else File("")
    }
}
