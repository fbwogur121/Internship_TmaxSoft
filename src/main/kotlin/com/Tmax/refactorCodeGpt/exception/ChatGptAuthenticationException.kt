package com.Tmax.refactorCodeGpt.exception

class ChatGptAuthenticationException(
    message: String? = """
        Authentication failed. Check API key settings.
        """
) : Exception(message)
