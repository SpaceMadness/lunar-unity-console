package spacemadness.com.lunarconsole.console.actions;

import spacemadness.com.lunarconsole.utils.ObjectUtils;
import spacemadness.com.lunarconsole.utils.StringUtils;

/**
 * Created by alementuev on 12/13/16.
 */
public class LUCVar extends LUEntry
{
    private final LUCVarType type;
    private String value;
    private String defaultValue;

    public LUCVar(int entryId, String name, String value, String defaultValue, LUCVarType type)
    {
        super(entryId, name);

        this.value = value;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public LUCVarType type()
    {
        return type;
    }

    public String value()
    {
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Default value

    public void resetToDefaultValue()
    {
        value = defaultValue;
    }

    public boolean isDefaultValue()
    {
        return ObjectUtils.areEqual(value, defaultValue);
    }
}
