package spacemadness.com.lunarconsole.core;

import java.util.Map;

public class Notification
{
    private final String name;
    private final Map<String, Object> userData;

    public Notification(String name, Map<String, Object> userData)
    {
        if (name == null)
        {
            throw new NullPointerException("Name is null");
        }

        this.name = name;
        this.userData = userData;
    }

    public String getName()
    {
        return name;
    }

    public Map<String, Object> getUserData()
    {
        return userData;
    }

    public Object getUserData(String key)
    {
        return userData != null ? userData.get(key) : null;
    }
}
