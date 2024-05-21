package com.Tmax.tmaxPlugin.dialog

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBScrollPane
import com.Tmax.tmaxPlugin.dto.Refactored
import com.Tmax.tmaxPlugin.service.RefactorGptService
import java.awt.BorderLayout
import javax.swing.Action
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.SwingUtilities

class RefactorGptDialog(
    private val editor: Editor,
    private val project: Project,
    private val selectedCode: String
) : DialogWrapper(project) {
    private val loadingPanel = JBLoadingPanel(BorderLayout(), disposable)
    private val applyButton = JButton("Apply")
    private val editorFactory: EditorFactory = EditorFactory.getInstance()
    private val originalCodeEditor: EditorEx =
        editorFactory.createViewer(editorFactory.createDocument(selectedCode.trimIndent()), project) as EditorEx
    private val refactoredCodeEditor: EditorEx =
        editorFactory.createEditor(editorFactory.createDocument(""), project) as EditorEx
    private val chatGptService = RefactorGptService()
    private val fileExtension = FileDocumentManager.getInstance().getFile(editor.document)?.extension ?: "txt"

    val onApply = { refactoredCode: String ->
        replaceEditorToRefactoredCode(editor, refactoredCode)
    }

    init {
        title = "RefactorGPT"
        isResizable = true
        initEditors()
        init()
        setSize(1300, 600)
    }

    override fun createActions(): Array<Action> = emptyArray()

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())

        val originalCodeLabel = JBLabel("Original code:")
        val refactoredCodeLabel = JBLabel("Refactored code:")

        originalCodeLabel.border = BorderFactory.createEmptyBorder(0, 10, 10, 0)
        refactoredCodeLabel.border = BorderFactory.createEmptyBorder(0, 10, 10, 0)

        val originalCodeScrollPane = JBScrollPane(originalCodeEditor.component)
        val refactoredCodeScrollPane = JBScrollPane(refactoredCodeEditor.component)

        originalCodeScrollPane.horizontalScrollBar.value = 0
        refactoredCodeScrollPane.horizontalScrollBar.value = 0

        val originalCodePanel = JPanel(BorderLayout())
        originalCodePanel.add(originalCodeLabel, BorderLayout.NORTH)
        originalCodePanel.add(originalCodeScrollPane, BorderLayout.CENTER)

        val refactoredCodePanel = JPanel(BorderLayout())
        refactoredCodePanel.add(refactoredCodeLabel, BorderLayout.NORTH)
        refactoredCodePanel.add(refactoredCodeScrollPane, BorderLayout.CENTER)

        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, originalCodePanel, refactoredCodePanel)
        splitPane.apply {
            resizeWeight = 0.5
            isContinuousLayout = true
            dividerSize = 15
        }
        SwingUtilities.invokeLater { splitPane.setDividerLocation(0.5) }

        val contentPanel = JPanel(BorderLayout())
        contentPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        contentPanel.add(splitPane, BorderLayout.CENTER)

        applyButton.isEnabled = false
        applyButton.addActionListener {
            onApply.invoke(refactoredCodeEditor.document.text)
            close(OK_EXIT_CODE)
        }

        val buttonPanel = JPanel()
        buttonPanel.add(applyButton)

        loadingPanel.add(contentPanel)
        loadingPanel.add(buttonPanel, BorderLayout.SOUTH)

        panel.add(loadingPanel, BorderLayout.CENTER)

        return panel
    }

    override fun show() {
         Thread {
            runCatching {
                chatGptService.refactorCode(fileExtension, selectedCode)
            }.fold(
                onSuccess = { refactored ->
                    SwingUtilities.invokeLater {
                        updateDialogWithRefactoredCode(refactored)
                        setLoading(false)
                    }
                },
                onFailure = { exception ->
                    SwingUtilities.invokeLater {
                        Messages.showErrorDialog(
                            project,
                            "Failed to refactor code: ${exception.message}",
                            "Refactor Error"
                        )
                        setLoading(false)
                        super.close(OK_EXIT_CODE)
                    }
                }
            )
        }.start()

        setLoading(true)
        super.show()
    }

    private fun updateDialogWithRefactoredCode(refactored: Refactored) {
        updateRefactoredCode(refactored.code)
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingPanel.startLoading()
            applyButton.isEnabled = false
        } else {
            loadingPanel.stopLoading()
            applyButton.isEnabled = true
        }
    }

    private fun updateRefactoredCode(refactoredCode: String) {
        WriteCommandAction.runWriteCommandAction(project) {
            refactoredCodeEditor.document.setText(refactoredCode.trimIndent())
        }
    }

    private fun initEditors() {
        val fileType = FileTypeManager.getInstance().getFileTypeByExtension(fileExtension)
        val editorHighlighterFactory = EditorHighlighterFactory.getInstance()

        originalCodeEditor.apply {
            highlighter = editorHighlighterFactory.createEditorHighlighter(
                fileType,
                EditorColorsManager.getInstance().globalScheme,
                project
            )
            isOneLineMode = false
            isEmbeddedIntoDialogWrapper = true
        }

        refactoredCodeEditor.apply {
            highlighter = editorHighlighterFactory.createEditorHighlighter(
                fileType,
                EditorColorsManager.getInstance().globalScheme,
                project
            )
            isOneLineMode = false
            isEmbeddedIntoDialogWrapper = true
        }
    }

    private fun replaceEditorToRefactoredCode(editor: Editor, refactoredCode: String) {
        WriteCommandAction.runWriteCommandAction(project) {
            with(editor.document) {
                val selectionModel = editor.selectionModel
                val startOffset = selectionModel.selectionStart
                val endOffset = selectionModel.selectionEnd

                replaceString(startOffset, endOffset, refactoredCode)

                PsiDocumentManager.getInstance(project).commitDocument(this)

                PsiDocumentManager.getInstance(project).getPsiFile(this)
                    ?.let { file ->
                        CodeStyleManager.getInstance(project).reformatText(file, startOffset, endOffset)
                    }
            }
        }
    }
}
