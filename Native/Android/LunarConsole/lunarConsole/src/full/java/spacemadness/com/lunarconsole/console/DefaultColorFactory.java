package spacemadness.com.lunarconsole.console;

import android.content.Context;

public class DefaultColorFactory implements ColorFactory {
    public DefaultColorFactory(Context context) {
    }

    @Override
    public int fromValue(String value) {
        return 0xff0000; // fixme: resolve real color
    }
}
