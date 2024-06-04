package com.Tmax.tmaxPlugin.action

import com.Tmax.tmaxPlugin.dialog.CodeSummaryDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import javax.swing.Icon
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages


class CodeSummaryAction : AnAction(){

    private var warningIcon: Icon? = null


    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val editor = event.getRequiredData(CommonDataKeys.EDITOR)
        val selectedCode = editor.selectionModel.selectedText

        if (selectedCode.isNullOrBlank()) {
            showSelectDialog(project)
            return
        }

        val dialog = CodeSummaryDialog(editor, project, selectedCode)

        dialog.show()
    }

    private fun showSelectDialog(project: Project) {
        Messages.showMessageDialog(
            project,
            "Please Select Code",
            "Code Not Selected",
            Messages.getWarningIcon()
        )
    }

}
