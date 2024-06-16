package me.cniekirk.realtimetrains.core.network

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockAppDispatcher(
    private val context: Context
) : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return if (request.path?.startsWith("/search") == true) {
            MockResponse().setResponseCode(200)
                .setBody(context.readJsonFileToString("success_response.json"))
        } else if (request.path?.startsWith("/crs") == true) {
            MockResponse().setResponseCode(200)
                .setBody(context.readJsonFileToString("success_crs_response.json"))
        } else {
            MockResponse().setResponseCode(400)
        }
    }
}