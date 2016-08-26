package spacemadness.com.lunarconsole.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ClassUtils
{
    public static List<Field> listFields(Class<?> cls, FieldFilter filter)
    {
        final Field[] fields = cls.getDeclaredFields();
        final List<Field> result = new ArrayList<>(fields.length);

        for (Field field : fields)
        {
            if (filter.accept(field))
            {
                result.add(field);
            }
        }

        return result;
    }

    public interface FieldFilter
    {
        boolean accept(Field field);
    }
}
