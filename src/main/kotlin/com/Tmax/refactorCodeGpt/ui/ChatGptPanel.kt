package com.Tmax.refactorCodeGpt.ui

import com.Tmax.refactorCodeGpt.service.ChatBotService
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JPanel

class ChatGptPanel : JPanel() {
    private val chatGptService = ChatBotService()

    private val inputField = JBTextField()
    private val chatArea = JBTextArea()

    init {
        layout = BorderLayout()

        val sendButton = JButton(object : AbstractAction("Send") {
            override fun actionPerformed(e: ActionEvent?) {
                val userInput = inputField.text
                val chatGptResponse = chatGptService.chatWithGpt(userInput)
                chatArea.append("\nUser: $userInput\nGPT: $chatGptResponse")
                inputField.text = ""
            }
        })

        // send 버튼을 누르지 않고 enter만 눌러도 전송되게 변경
        inputField.addActionListener {
            sendButton.doClick() // sendButton의 클릭 이벤트를 프로그래밍 방식으로 호출
        }

        // chatgpt 응답을 줄바꿈해서 출력
        true.also { chatArea.lineWrap = it }
        true.also { chatArea.wrapStyleWord = it }

        val southPanel = JPanel(BorderLayout())
        southPanel.add(inputField, BorderLayout.CENTER)
        southPanel.add(sendButton, BorderLayout.EAST)

        add(JBScrollPane(chatArea), BorderLayout.CENTER)
        add(southPanel, BorderLayout.SOUTH)
    }
}
