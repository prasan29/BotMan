package com.bot.man.model.data

import com.google.gson.annotations.SerializedName

data class Response(

        @field:SerializedName("result")
        val result: List<ResultItem?>? = null,

        @field:SerializedName("ok")
        val ok: Boolean? = null
)

data class From(

        @field:SerializedName("language_code")
        val languageCode: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("is_bot")
        val isBot: Boolean? = null,

        @field:SerializedName("first_name")
        val firstName: String? = null,

        @field:SerializedName("username")
        val username: String? = null
)

data class Message(

        @field:SerializedName("date")
        val date: Int? = null,

        @field:SerializedName("chat")
        val chat: Chat? = null,

        @field:SerializedName("message_id")
        val messageId: Int? = null,

        @field:SerializedName("from")
        val from: From? = null,

        @field:SerializedName("text")
        val text: String? = null
)

data class ResultItem(

        @field:SerializedName("update_id")
        val updateId: Int? = null,

        @field:SerializedName("message")
        val message: Message? = null
)

data class Chat(

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("first_name")
        val firstName: String? = null,

        @field:SerializedName("username")
        val username: String? = null
)
