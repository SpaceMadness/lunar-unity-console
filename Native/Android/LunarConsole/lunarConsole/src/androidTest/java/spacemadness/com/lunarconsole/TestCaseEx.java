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