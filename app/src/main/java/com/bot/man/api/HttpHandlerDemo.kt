package com.bot.man.api

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress
import java.util.concurrent.Executors

class HttpHandlerDemo {

    companion object {
        private const val mPort: Int = 8484
        private const val mHost: String = "localhost"
        private var mServer: HttpServer? = null
        private val httpHandler = HttpHandler {
            kotlin.run {
                when (it.requestMethod) {
                    "GET" -> {
                        sendResponse(it, "Welcome to the HTTP server!")
                    }
                }
            }
        }

        private fun sendResponse(it: HttpExchange, response: String) {
            it.sendResponseHeaders(200, response.length.toLong())
            val os = it.responseBody
            os.write(response.toByteArray())
            os.close()
        }

        fun startServer() {
            run {
                try {
                    mServer = HttpServer.create(InetSocketAddress(mHost, mPort),
                            0)
                    mServer!!.executor = Executors.newCachedThreadPool()
                    mServer!!.createContext("/", httpHandler)
                    mServer!!.createContext("/index", httpHandler)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun stopServer() {
            mServer?.stop(0)
        }
    }
}