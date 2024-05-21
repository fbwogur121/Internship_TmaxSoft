package com.Tmax.tmaxPlugin.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager

class ChatGptAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("ChatGptToolWindow")

        toolWindow?.let {
            if (it.isVisible) {
                it.hide(null)
            } else {
                it.show(null)
            }
        }
    }
}
