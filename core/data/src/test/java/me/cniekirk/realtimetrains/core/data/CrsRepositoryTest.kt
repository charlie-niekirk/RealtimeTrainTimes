package me.cniekirk.realtimetrains.core.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import me.cniekirk.realtimetrains.core.common.Error
import me.cniekirk.realtimetrains.core.common.Result
import me.cniekirk.realtimetrains.core.data.repository.CrsRepositoryImpl
import me.cniekirk.realtimetrains.core.database.TrainStation
import me.cniekirk.realtimetrains.core.database.TrainStationDao
import me.cniekirk.realtimetrains.core.network.apis.HuxleyApi
import me.cniekirk.realtimetrains.core.network.models.huxley.StationCrs
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CrsRepositoryTest {

    private val huxleyApi = mockk<HuxleyApi>()
    private val trainStationDao = mockk<TrainStationDao>()

    private lateinit var crsRepository: CrsRepositoryImpl

    @Before
    fun setup() {
        crsRepository = CrsRepositoryImpl(huxleyApi, trainStationDao)
    }

    @Test
    fun `verify when database is empty & api call is made successfully then database is updated`() = runTest {
        // Given
        coEvery { huxleyApi.getAllStations() } returns Response.success(stationList)
        every { trainStationDao.getAll() } returnsMany listOf(listOf(), listFromDb)
        justRun { trainStationDao.insertAll(any()) }

        // When
        val stations = crsRepository.getAllStations()

        // Then
        verify(exactly = 1) { trainStationDao.insertAll(*mappedList) }
        assertTrue(stations is Result.Success)

        val expected = persistentListOf(StationCrs(crsCode = CRS, stationName = NAME))
        assertEquals(expected, stations.data)
    }

    @Test
    fun `verify when database is empty & api call fails then database is not updated and Failure returned`() = runTest {
        // Given
        coEvery { huxleyApi.getAllStations() } returns Response.error(400, "".toResponseBody())
        every { trainStationDao.getAll() } returns listOf()

        // When
        val stations = crsRepository.getAllStations()

        // Then
        verify(exactly = 0) { trainStationDao.insertAll(any()) }
        assertTrue(stations is Result.Failure)
        assertEquals(Error.UnknownError, stations.error)
    }

    @Test
    fun `verify when database is not empty then no api call made and Success Result with stations returned`() = runTest {
        // Given
        every { trainStationDao.getAll() } returns listFromDb

        // When
        val stations = crsRepository.getAllStations()

        // Then
        coVerify(exactly = 0) { huxleyApi.getAllStations() }
        verify(exactly = 0) { trainStationDao.insertAll(any()) }
        assertTrue(stations is Result.Success)

        val expected = persistentListOf(StationCrs(crsCode = CRS, stationName = NAME))
        assertEquals(expected, stations.data)
    }

    companion object {
        const val CRS = "WAT"
        const val NAME = "London Waterloo"

        val stationList = listOf(StationCrs(crsCode = CRS, stationName = NAME))
        val mappedList = arrayOf(TrainStation(stationCrs = CRS, stationName = NAME))
        val listFromDb = listOf(TrainStation(stationCrs = CRS, stationName = NAME))
    }
}