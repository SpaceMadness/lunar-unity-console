package spacemadness.com.lunarconsole.debug;

import spacemadness.com.lunarconsole.Config;

public class Tag
{
    public final String name;
    public boolean enabled;

    public Tag(String name)
    {
        this(name, Config.DEBUG);
    }

    public Tag(String name, boolean enabled)
    {
        this.name = name;
        this.enabled = enabled;
    }
}
