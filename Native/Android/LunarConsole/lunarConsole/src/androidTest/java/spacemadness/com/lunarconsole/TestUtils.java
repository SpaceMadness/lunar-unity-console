package spacemadness.com.lunarconsole;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils {
	public static String readTextAsset(Context context, String path) throws IOException {
		try (final InputStream stream = context.getAssets().open(path)) {
			return readText(stream);
		}
	}

	private static String readText(InputStream stream) throws IOException {
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			final StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				if (result.length() > 0) {
					result.append('\n');
				}
				result.append(line);
			}
			return result.toString();
		}
	}
}
