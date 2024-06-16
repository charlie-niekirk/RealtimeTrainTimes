package me.cniekirk.realtimetrains.core.network.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Types
import me.cniekirk.realtimetrains.core.common.Error
import me.cniekirk.realtimetrains.core.common.Result
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type
import javax.annotation.Nullable


suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            if (response.code() == 400) {
                Result.Failure(Error.UnknownError)
            } else if (response.code() == 401) {
                Result.Failure(Error.UnknownError)
            } else if (response.code() in 400..499) {
                Result.Failure(Error.UnknownError)
            } else if (response.code() in 500..599) {
                Result.Failure(Error.UnknownError)
            } else {
                Result.Failure(Error.UnknownError)
            }
        }
    } catch (error: Throwable) {
        Timber.e(error)
        Result.Failure(Error.UnknownError)
    }
}


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class SingleToArray {
    class Adapter internal constructor(
        private val delegateAdapter: JsonAdapter<List<Any>>,
        private val elementAdapter: JsonAdapter<Any>
    ) :
        JsonAdapter<List<Any?>?>() {
        @Nullable
        @Throws(IOException::class)
        override fun fromJson(reader: JsonReader): List<Any?>? {
            return if (reader.peek() !== JsonReader.Token.BEGIN_ARRAY) {
                listOf(elementAdapter.fromJson(reader))
            } else delegateAdapter.fromJson(reader)
        }

        @Throws(IOException::class)
        override fun toJson(writer: JsonWriter?, value: List<Any?>?) {
            if (value != null) {
                if (value.size == 1) {
                    if (writer != null) {
                        elementAdapter.toJson(writer, value[0])
                    }
                } else {
                    if (writer != null) {
                        delegateAdapter.toJson(writer, value.filterNotNull())
                    }
                }
            }
        }

        companion object {
            val FACTORY =
                Factory { type, annotations, moshi ->
                    val delegateAnnotations: Set<Annotation?> = Types.nextAnnotations(
                        annotations,
                        SingleToArray::class.java
                    ) ?: return@Factory null
                    require(Types.getRawType(type) === MutableList::class.java) { "Only lists may be annotated with @SingleToArray. Found: $type" }
                    val elementType: Type =
                        Types.collectionElementType(type, MutableList::class.java)
                    val delegateAdapter: JsonAdapter<List<Any>> =
                        moshi.adapter(type, delegateAnnotations)
                    val elementAdapter = moshi.adapter<Any>(elementType)
                    Adapter(delegateAdapter, elementAdapter)
                }
        }
    }
}