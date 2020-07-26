package spacemadness.com.lunarconsole.console;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static spacemadness.com.lunarconsole.console.DefaultRichTextFactory.*;

public class RichTextFactoryTest {
    private StyleSpan bold;
    private StyleSpan italic;
    private StyleSpan boldItalic;

    private DefaultRichTextFactory richTextFactory;

    @Before
    public void setup() {
        ColorFactory colorFactory = new DefaultColorFactory(getContext());
        richTextFactory = new DefaultRichTextFactory(colorFactory);

        // need to use exact styles: otherwise SpannedString equality fails
        bold = richTextFactory.bold;
        italic = richTextFactory.italic;
        boldItalic = richTextFactory.boldItalic;
    }

    @Test
    public void testNullString() {
        CharSequence actual = fromRichText(null);
        assertNull(actual);
    }

    @Test
    public void testEmptyString() {
        CharSequence expected = "";
        CharSequence actual = fromRichText("");
        assertEquals(expected, actual);
    }

    @Test
    public void testNoRichTags() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testBoldTags1() {
        CharSequence expected = createSpanned("This is text.", new Span(bold, 5, 2));
        CharSequence actual = fromRichText("This <b>is</b> text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testBoldTags2() {
        CharSequence expected = createSpanned("This is text.", new Span(bold, 0, 7));
        CharSequence actual = fromRichText("<b>This is</b> text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testBoldTags3() {
        CharSequence expected = createSpanned("This is text.", new Span(bold, 8, 5));
        CharSequence actual = fromRichText("This is <b>text.</b>");
        assertEquals(expected, actual);
    }

    @Test
    public void testBoldTags4() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is <b></b>text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testBoldTags5() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is <b><b></b></b>text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testItalicTags1() {
        CharSequence expected = createSpanned("This is text.", new Span(italic, 5, 2));
        CharSequence actual = fromRichText("This <i>is</i> text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testItalicTags2() {
        CharSequence expected = createSpanned("This is text.", new Span(italic, 0, 7));
        CharSequence actual = fromRichText("<i>This is</i> text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testItalicTags3() {
        CharSequence expected = createSpanned("This is text.", new Span(italic, 8, 5));
        CharSequence actual = fromRichText("This is <i>text.</i>");
        assertEquals(expected, actual);
    }

    @Test
    public void testItalicTags4() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is <i></i>text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testItalicTags5() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is <i><i></i></i>text.");
        assertEquals(expected, actual);
    }

    /*
    @Test public void testColorTags1() {
        ConsoleLogEntry tags = [LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(5, 2))];
        CharSequence expected = createSpanned("This is text.", new );
        CharSequence actual = fromRichText("This <color=red>is</color> text.");
        assertEquals(expected, actual);
    }
    
    @Test public void testColorTags2() {
        ConsoleLogEntry tags = [LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(0, 7))];
        CharSequence expected = createSpanned("This is text.");
        CharSequence actual = fromRichText("<color=red>This is</color> text.");
        assertEquals(expected, actual);
    }
    
    @Test public void testColorTags3() {
        ConsoleLogEntry tags = [LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(8, 5))];
        CharSequence expected = createSpanned("This is text.");
        CharSequence actual = fromRichText("This is <color=red>text.</color>");
        assertEquals(expected, actual);
    }
    
    @Test public void testColorTags4() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is <color=red></color>text.");
        assertEquals(expected, actual);
    }
    
    @Test public void testColorTags5() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This is <color=red><color=red></color></color>text.");
        assertEquals(expected, actual);
    }
    
    @Test public void testMultipleTags1() {
        ConsoleLogEntry tags = [
            LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(5, 2)),
            LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(5, 2))
        ];
        CharSequence expected = createSpanned("This is text.");
        CharSequence actual = fromRichText("This <color=red><b>is</b></color> text.");
        assertEquals(expected, actual);
    }
    
    @Test public void testMultipleTags2() {
        ConsoleLogEntry tags = [
            LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(12, 4)),
            LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(8, 19))
        ];
        CharSequence expected = createSpanned("This is red bold attributed text.");
        CharSequence actual = fromRichText("This is <color=red>red <b>bold</b> attributed</color> text.");
        assertEquals(expected, actual);
    }
    
    @Test public void testMultipleTags3() {
        ConsoleLogEntry tags = [
            LURichTextStyleTag(style: LURichTextStyleBoldItalic, range: NSMakeRange(17, 3)),
            LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(12, 11)),
            LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(8, 26))
        ];
        CharSequence expected = createSpanned("This is red bold italic attributed text.");
        CharSequence actual = fromRichText("This is <color=red>red <b>bold <i>ita</i>lic</b> attributed</color> text.");
        assertEquals(expected, actual);
    }
    
     */

    @Test
    public void testMalformedTags1() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This <b>is text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testMalformedTags2() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This <b>is<b> text.");
        assertEquals(expected, actual);
    }

    @Test
    public void testMalformedTags3() {
        CharSequence expected = "This is text.";
        CharSequence actual = fromRichText("This <b>is</i> text.");
        assertEquals(expected, actual);
    }


    @Test
    public void testMalformedTags4() {
        CharSequence expected = "This is malformed text.";
        CharSequence actual = fromRichText("This <b>is <b>malformed</b> text.");
        assertEquals(expected, actual);
    }

    private CharSequence fromRichText(String text) {
        return richTextFactory.createRichText(text);
    }

    private CharSequence createSpanned(String text, Span... spans) {
        SpannableString string = new SpannableString(text);
        for (Span tag : spans) {
            string.setSpan(tag.style, tag.startIndex, tag.startIndex + tag.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return string;
    }
}
