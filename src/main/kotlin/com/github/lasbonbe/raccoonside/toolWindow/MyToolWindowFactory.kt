package com.github.lasbonbe.raccoonside.toolWindow

import com.github.lasbonbe.raccoonside.MyIconLoader
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import java.io.File
import javax.swing.JComponent

class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("N'oubliez pas de supprimer les fichiers d'exemple inutiles du plugin.xml.")
    }


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow()
        val icon = MyIconLoader.getIcon("/icons/toolwindowIconINV.svg")
        toolWindow.setIcon(icon)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), "Video Player", false)
        toolWindow.contentManager.addContent(content)
    }
}

class MyToolWindow {

    private val browser: JBCefBrowser

    init {
        if (!JBCefApp.isSupported()) {
            throw IllegalStateException("JCEF n'est pas supporté sur cette plateforme.")
        }
        
        val videoHtml = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Subway Surfers</title>
    <style>
        html, body {
            margin: 0;
            padding: 0;
            background: #000;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        .video-container {
            position: relative;
            width: 100%;
            height: 100%;
            background: #000;
            overflow: hidden;
        }

        .video-container iframe {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 177.7777vh; /* 16/9 * 100 = ~177.7777 */
            height: 100vh;
        }

        @media (min-aspect-ratio: 16/9) {
            .video-container iframe {
                width: 100vw;
                height: 56.25vw; /* 9/16 * 100 = 56.25 */
            }
        }
    </style>
</head>
<body>
<div class="video-container">
    <iframe
src="https://www.youtube.com/embed/HUUsg8XuSp8?autoplay=1&mute=1&controls=0&rel=0&disablekb=1&modestbranding=1&iv_load_policy=3&loop=1"            allowfullscreen>
    </iframe>
</div>
</body>
</html>
        """.trimIndent()

        // Charger le HTML directement dans JBCefBrowser
        browser = JBCefBrowser()
        browser.loadHTML(videoHtml)
    }

    fun getContent(): JComponent = browser.component
}
