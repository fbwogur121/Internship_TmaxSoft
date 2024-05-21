package com.Tmax.refactorCodeGpt.dialog.chatBotDialog

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class ChatGptToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val chatGptPanel = ChatGptPanel()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(chatGptPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
