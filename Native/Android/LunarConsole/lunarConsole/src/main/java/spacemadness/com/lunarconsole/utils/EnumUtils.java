package spacemadness.com.lunarconsole.utils;

public class EnumUtils {
	public static Object[] listValues(Enum<?> e) {
		return e.getDeclaringClass().getEnumConstants();
	}
}
