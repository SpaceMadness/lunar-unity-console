package spacemadness.com.lunarconsole.console.actions;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.utils.NotImplementedException;

/**
 * Created by alementuev on 12/13/16.
 */

public class LUActionRegistry
{
    private final List<LUAction> actions;
    private final List<LUCVar> variables;
    private Delegate delegate; // FIXME: rename

    public LUActionRegistry()
    {
        actions = new ArrayList<>();
        variables = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Actions

    public LUAction registerActionWithId(int actionId, String name) // FIXME: rename
    {
        throw new NotImplementedException();
    }

    public boolean unregisterActionWithId(int actionId)
    {
        throw new NotImplementedException();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Variables

    public void setVariable(int variableId, String value)
    {
        throw new NotImplementedException();
    }

    public LUCVar variableWithId(int variableId) // FIXME: rename
    {
        throw new NotImplementedException();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public Delegate getDelegate()
    {
        return delegate;
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Delegate

    public interface Delegate // FIXME: rename
    {
        void didAddAction(LUActionRegistry registry, LUAction action);
        void didRemoveAction(LUActionRegistry registry, LUAction action);
        void didRegisterVariable(LUActionRegistry registry, LUCVar variable);
        void didDidChangeVariable(LUActionRegistry registry, LUCVar variable);
    }
}
