package spacemadness.com.lunarconsole.core;

import spacemadness.com.lunarconsole.utils.StringUtils;

public final class Check {
    public static <T> T notNullArg(T arg) {
        return notNullArg(arg, "Argument");
    }

    public static <T> T notNullArg(T arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " is null");
        }
        return arg;
    }

    public static String notEmptyArg(String arg) {
        return notEmptyArg(arg, "Argument");
    }

    public static String notEmptyArg(String arg, String name) {
        if (StringUtils.isNullOrEmpty(arg)) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
        return arg;
    }
}
