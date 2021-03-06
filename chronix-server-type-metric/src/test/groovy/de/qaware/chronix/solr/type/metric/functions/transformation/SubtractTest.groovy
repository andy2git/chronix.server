/*
 * Copyright (C) 2018 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.chronix.solr.type.metric.functions.transformation

import de.qaware.chronix.server.functions.FunctionCtx
import de.qaware.chronix.solr.type.metric.ChronixMetricTimeSeries
import de.qaware.chronix.timeseries.MetricTimeSeries
import spock.lang.Specification

/**
 * Unit test for the subtract transformation
 * @author f.lautenschlager
 */
class SubtractTest extends Specification {
    def "test transform"() {
        given:
        def timeSeriesBuilder = new MetricTimeSeries.Builder("Sub", "metric")
        10.times {
            timeSeriesBuilder.point(it * 100, it + 10)
        }
        timeSeriesBuilder.point(10 * 100, -10)
        def timeSeries = new ChronixMetricTimeSeries("", timeSeriesBuilder.build())
        def analysisResult = new FunctionCtx(1, 1, 1)


        def sub = new Subtract()
        sub.setArguments(["4"] as String[])
        when:
        sub.execute([timeSeries] as List, analysisResult)

        then:
        timeSeries.getRawTimeSeries().size() == 11
        timeSeries.getRawTimeSeries().getValue(1) == (1 + 10 - 4)

        timeSeries.getRawTimeSeries().getValue(10) == -14
    }

    def "test getType"() {
        expect:
        def sub = new Subtract()
        sub.setArguments(["4"] as String[])
        sub.getQueryName() == "sub"
    }

    def "test getArguments"() {
        expect:
        def sub = new Subtract()
        sub.setArguments(["4"] as String[])
        sub.getArguments()[0] == "value=4.0"
    }

    def "test equals and hash code"() {
        given:

        def sub = new Subtract()
        sub.setArguments(["4"] as String[])

        def sameSub = new Subtract()
        sameSub.setArguments(["4"] as String[])

        def otherSub = new Subtract()
        otherSub.setArguments(["2"] as String[])

        expect:
        sub != null
        sub != new Object()
        sub == sub
        sub == sameSub
        sub.hashCode() == sameSub.hashCode()
        sub.hashCode() != otherSub.hashCode()
    }

    def "test string representation"() {
        expect:
        def sub = new Subtract()
        sub.setArguments(["4"] as String[])
        sub.toString().contains("value")
    }
}
