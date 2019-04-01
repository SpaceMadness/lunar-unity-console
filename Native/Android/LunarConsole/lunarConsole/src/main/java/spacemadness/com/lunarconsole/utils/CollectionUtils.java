package spacemadness.com.lunarconsole.utils;

import java.lang.reflect.Array;

public class CollectionUtils {
	public static <T> int indexOf(T[] array, T value) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i] == value) {
				return i;
			}
		}
		return -1;
	}

	public static <In, Out> Out[] map(In[] in, Map<In, Out> map) {
		Out[] out = null;
		for (int i = 0; i < in.length; ++i) {
			Out value = map.map(in[i]);
			if (out == null) {
				//noinspection unchecked
				out = (Out[]) Array.newInstance(value.getClass(), in.length);
			}
			out[i] = value;
		}

		return out;
	}

	public interface Map<In, Out> {
		Out map(In in);
	}
}