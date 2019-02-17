//
//  TestCase.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole

import org.junit.After
import org.junit.Before

import java.util.ArrayList

import spacemadness.com.lunarconsole.utils.StringUtils

import org.junit.Assert.assertEquals

abstract class TestCase {
    private var results: MutableList<String>? = null

    val resultList: List<String>?
        get() = results

    //////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    @Before
    @Throws(Exception::class)
    fun setUp() {
        results = ArrayList()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    //////////////////////////////////////////////////////////////////////////////
    // Assert helpers

    protected fun assertResults(vararg expected: String) {
        assertResults(results, *expected)
        results!!.clear()
    }

    protected fun assertResults(actual: List<String>?, vararg expected: String) {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.size.toLong(), actual!!.size.toLong())

        for (i in expected.indices) {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                    "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i])
        }
    }

    protected fun assertResults(actual: Array<String>, vararg expected: String) {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual),
                expected.size.toLong(), actual.size.toLong())

        for (i in expected.indices) {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                    "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i])
        }
    }

    protected fun assertResults(actual: IntArray, vararg expected: Int) {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual),
                expected.size.toLong(), actual.size.toLong())

        for (i in expected.indices) {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                    "\nActual: " + StringUtils.Join(actual),
                    expected[i].toLong(), actual[i].toLong())
        }
    }

    protected fun assertResults(actual: FloatArray, vararg expected: Float) {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.size.toLong(), actual.size.toLong())

        for (i in expected.indices) {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                    "\nActual: " + StringUtils.Join(actual),
                    expected[i].toDouble(), actual[i].toDouble())
        }
    }

    protected fun assertResults(actual: BooleanArray, vararg expected: Boolean) {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.size.toLong(), actual.size.toLong())

        for (i in expected.indices) {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                    "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i])
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Results

    protected fun addResult(format: String, vararg args: Any) {
        addResult(String.format(format, *args))
    }

    protected fun addResult(result: String) {
        results!!.add(result)
    }

    protected fun clearResult() {
        results!!.clear()
    }
}