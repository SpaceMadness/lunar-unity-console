package spacemadness.com.lunarconsole.console;

import android.content.Context;

import spacemadness.com.lunarconsole.core.StringProvider;

public class ContextStringProvider implements StringProvider {
    private final Context context;

    public ContextStringProvider(Context context) {
        this.context = context; // FIXME: check for null
    }

    @Override
    public String getString(int id) {
        return context.getString(id);
    }
}
