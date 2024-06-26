package me.cniekirk.realtimetrains.core.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class TrainStation(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "station_crs") val stationCrs: String,
    @ColumnInfo(name = "station_name") val stationName: String
) : Parcelable