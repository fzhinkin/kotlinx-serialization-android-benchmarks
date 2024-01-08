/*
 * Copyright 2017-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.runner.AndroidJUnit4
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.discardingSink
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.write
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.kotlinx.io.encodeToBufferedSink
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@RunWith(AndroidJUnit4::class)
open class Benchmarks {
    @get:Rule
    val benchmarkingRule = BenchmarkRule()

    private val twitterMacro = Json.decodeFromString(MacroTwitterFeed.serializer(),
        Benchmarks::class.java.getResource("/twitter_macro.json").readBytes().decodeToString())
    private val twitter = Json.decodeFromString(Twitter.serializer(),
        Benchmarks::class.java.getResource("/twitter.json").readBytes().decodeToString())
    private val citm = Json.decodeFromString(CitmCatalog.serializer(),
        Benchmarks::class.java.getResource("/citm_catalog.json").readBytes().decodeToString())
    private val sink = discardingSink().buffered()
    private val fileSink = SystemFileSystem.sink(Path("/dev/null")).buffered()
    private val fileChannelSink = FileChannel.open(Paths.get("/dev/null"), StandardOpenOption.WRITE)

    @After
    fun closeFiles() {
        fileSink.close()
        fileChannelSink.close()
    }

    @Test
    fun testTwitterMacro() {
        benchmarkingRule.measureRepeated {
            Json.encodeToBufferedSink(MacroTwitterFeed.serializer(), twitterMacro, sink)
        }
    }

    @Test
    fun testTwitter() {
        benchmarkingRule.measureRepeated {
            Json.encodeToBufferedSink(Twitter.serializer(), twitter, sink)
        }
    }

    @Test
    fun testCitm() {
        benchmarkingRule.measureRepeated {
            Json.encodeToBufferedSink(CitmCatalog.serializer(), citm, sink)
        }
    }

    @Test
    fun testTwitterMacroFile() {
        benchmarkingRule.measureRepeated {
            Json.encodeToBufferedSink(MacroTwitterFeed.serializer(), twitterMacro, fileSink)
            fileSink.flush()
        }
    }

    @Test
    fun testTwitterFile() {
        benchmarkingRule.measureRepeated {
            Json.encodeToBufferedSink(Twitter.serializer(), twitter, fileSink)
            fileSink.flush()
        }
    }

    @Test
    fun testCitmFile() {
        benchmarkingRule.measureRepeated {
            Json.encodeToBufferedSink(CitmCatalog.serializer(), citm, fileSink)
            fileSink.flush()
        }
    }

    @Test
    fun testTwitterMacroFileChannel() {
        benchmarkingRule.measureRepeated {
            val buffer = Buffer()
            Json.encodeToBufferedSink(MacroTwitterFeed.serializer(), twitterMacro, buffer)
            fileChannelSink.write(buffer)
        }
    }

    @Test
    fun testTwitterFileChannel() {
        benchmarkingRule.measureRepeated {
            val buffer = Buffer()
            Json.encodeToBufferedSink(Twitter.serializer(), twitter, buffer)
            fileChannelSink.write(buffer)
        }
    }

    @Test
    fun testCitmFileChannel() {
        benchmarkingRule.measureRepeated {
            val buffer = Buffer()
            Json.encodeToBufferedSink(CitmCatalog.serializer(), citm, buffer)
            fileChannelSink.write(buffer)
        }
    }
}