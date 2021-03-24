package com.open.weather.demo.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset

class HttpHandleIntercept : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder().build()
        //printPostmanFormattedLog(request)
        val response: Response?
        response = chain.proceed(request)
        //Log.e("@@@", "$response")
        return response
    }

    private fun printPostmanFormattedLog(request: Request) {
        try {
            val allParams: String = if (request.method == "GET" || request.method == "DELETE") {
                request.url.toString().substring(
                    request.url.toString().indexOf("?") + 1,
                    request.url.toString().length
                )
            } else {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                buffer.readString(Charset.forName("UTF-8"))
            }
            val params =
                allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val paramsString = StringBuilder("\n")
            for (param in params) {
                paramsString.append(decode(param.replace("=", ":")))
                paramsString.append("\n")
            }
            Log.e("@@@", paramsString.toString())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun decode(url: String): String {
        return try {
            var prevURL = ""
            var decodeURL = url.replace("%", "%25")
            while (prevURL != decodeURL) {
                prevURL = decodeURL
                decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
            }
            decodeURL
        } catch (e: UnsupportedEncodingException) {
            "Issue while decoding" + e.message
        }
    }
}