package me.cniekirk.realtimetrains.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RecentSearchesSerializer : Serializer<RecentSearches> {

    override val defaultValue: RecentSearches = RecentSearches.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RecentSearches {
        try {
            return RecentSearches.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: RecentSearches, output: OutputStream) = t.writeTo(output)
}