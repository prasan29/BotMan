package com.bot.man.model.data

data class Response(
        val ok: Boolean,
        val result: List<Result>
)

data class Result(
        val message: Message,
        val update_id: Int
)

data class Message(
        val chat: Chat,
        val date: Int,
        val from: From,
        val message_id: Int,
        val text: String
)

data class Chat(
        val first_name: String,
        val id: Int,
        val type: String,
        val username: String
)

data class From(
        val first_name: String,
        val id: Int,
        val is_bot: Boolean,
        val language_code: String,
        val username: String
)