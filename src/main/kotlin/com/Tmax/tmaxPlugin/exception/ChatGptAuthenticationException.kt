package com.Tmax.tmaxPlugin.exception

class ChatGptAuthenticationException(
    message: String? = """
        Authentication failed. Check API key settings.
        """
) : Exception(message)
