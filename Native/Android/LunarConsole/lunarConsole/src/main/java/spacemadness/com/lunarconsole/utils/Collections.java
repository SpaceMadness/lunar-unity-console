package spacemadness.com.lunarconsole.utils;

import java.util.ArrayList;
import java.util.List;

public class Collections {
	public static <In, Out> List<Out> map(List<In> list, Map<In, Out> map) {
		List<Out> result = new ArrayList<>(list.size());
		for (In in : list) {
			result.add(map.map(in));
		}
		return result;
	}

	public interface Map<In, Out> {
		Out map(In in);
	}
}
