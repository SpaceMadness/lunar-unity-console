package spacemadness.com.lunarconsole.debug;

public class TestHelper // TODO: make a separate flavor for this class
{
    public static final String TEST_EVENT_OVERLAY_ADD_ITEM              = "TEST_EVENT_OVERLAY_ADD_ITEM"; // data: CycleArray<ConsoleEntry>
    public static final String TEST_EVENT_OVERLAY_REMOVE_ITEM           = "TEST_EVENT_OVERLAY_REMOVE_ITEM"; // data: CycleArray<ConsoleEntry>
    public static final String TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL = "TEST_EVENT_OVERLAY_SCHEDULE_ITEM_REMOVAL";

    private static TestHelper instance;
    private final EventListener listener;

    public TestHelper(EventListener listener)
    {
        this.listener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void init(EventListener listener)
    {
        instance = new TestHelper(listener);
    }

    public static void shutdown()
    {
        instance = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Initialization

    public static void testEvent(String name)
    {
        if (instance != null)
        {
            instance.event(name, null);
        }
    }

    public static void testEvent(String name, Object data)
    {
        if (instance != null)
        {
            instance.event(name, data);
        }
    }

    private synchronized void event(String name, Object data)
    {
        listener.onTestEvent(name, data);
    }

    public interface EventListener
    {
        void onTestEvent(String name, Object data);
    }
}
