package spacemadness.com.lunarconsole.console;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class DefaultStyleFactory implements StyleFactory {
    @Override
    public StyleSpan createItalic() {
        return new StyleSpan(Typeface.ITALIC);
    }

    @Override
    public StyleSpan createBold() {
        return new StyleSpan(Typeface.BOLD);
    }

    @Override
    public StyleSpan createBoldItalic() {
        return new StyleSpan(Typeface.BOLD_ITALIC);
    }

    @Override
    public CharacterStyle createCharacterStyle(int color) {
        return new ForegroundColorSpan(color);
    }
}