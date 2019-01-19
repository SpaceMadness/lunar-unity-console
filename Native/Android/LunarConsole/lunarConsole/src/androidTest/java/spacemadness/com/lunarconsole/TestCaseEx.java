//
//  TestCaseEx.java
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

package spacemadness.com.lunarconsole;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.utils.StringUtils;

public class TestCaseEx extends TestCase
{
    private List<String> results;

    //////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        results = new ArrayList<>();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Assert helpers

    protected void assertResult(String... expected)
    {
        assertResult(results, expected);
        results.clear();
    }

    protected void assertResult(List<String> actual, String... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                "\nActual: " + StringUtils.Join(actual), expected.length, actual.size());

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual.get(i));
        }
    }

    protected void assertResult(String[] actual, String... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(int[] actual, int... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual),
                expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(float[] actual, float... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual), expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    protected void assertResult(boolean[] actual, boolean... expected)
    {
        assertEquals("\nExpected: " + StringUtils.Join(expected) +
                        "\nActual: " + StringUtils.Join(actual), expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i)
        {
            assertEquals("\nExpected: " + StringUtils.Join(expected) +
                            "\nActual: " + StringUtils.Join(actual),
                    expected[i], actual[i]);
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Results

    protected void addResult(String format, Object... args)
    {
        addResult(String.format(format, args));
    }

    protected void addResult(String result)
    {
        results.add(result);
    }

    protected void clearResult()
    {
        results.clear();
    }

    public List<String> getResultList()
    {
        return results;
    }
}