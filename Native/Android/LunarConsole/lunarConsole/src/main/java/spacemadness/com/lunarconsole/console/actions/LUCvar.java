package spacemadness.com.lunarconsole.console.actions;

/**
 * Created by alementuev on 12/13/16.
 */
public class LUCVar extends LUEntry
{
    public static final String LUCVarTypeNameBoolean = "Boolean";
    public static final String LUCVarTypeNameInteger = "Integer";
    public static final String LUCVarTypeNameFloat   = "Float";
    public static final String LUCVarTypeNameString  = "String";
    public static final String LUCVarTypeNameUnknown = "Unknown";

    public LUCVar(int actionId, String name)
    {
        super(actionId, name);
    }
}
