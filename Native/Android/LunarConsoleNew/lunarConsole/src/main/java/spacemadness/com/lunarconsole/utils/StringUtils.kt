package spacemadness.com.lunarconsole.utils

import java.text.DecimalFormat

fun String?.length(): Int = this?.length ?: 0

fun String?.hasPrefix(prefix: String?, ignoreCase: Boolean = false): Boolean {
    return this != null && prefix != null && this.startsWith(prefix, ignoreCase = ignoreCase)
}

/** A collection of useful string-related functions */
object StringUtils {
    private val FLOATING_POINT_FORMAT = DecimalFormat("0.#")

    //region Parsing

    fun parseInt(str: String, radix: Int = 10): Int? = try {
        Integer.parseInt(str, radix)
    } catch (e: NumberFormatException) {
        null
    }

    fun parseInt(str: String, defaultValue: Int, radix: Int = 10): Int =
        parseInt(str, radix = radix) ?: defaultValue

    fun isValidInteger(str: String): Boolean = parseInt(str) != null

    fun parseFloat(str: String): Float? = try {
        java.lang.Float.parseFloat(str)
    } catch (e: NumberFormatException) {
        null
    }

    fun parseFloat(str: String, defaultValue: Float): Float = parseFloat(str) ?: defaultValue

    fun isValidFloat(str: String): Boolean = parseFloat(str) != null

    fun length(str: String?) = str.length()

    fun contains(str: CharSequence?, cs: CharSequence?) =
        str != null && cs != null && str.contains(cs)

    fun containsIgnoreCase(str: String?, cs: String?) =
        str != null && cs != null && str.contains(cs, ignoreCase = true)

    fun hasPrefix(str: String?, prefix: String?, ignoreCase: Boolean = false): Boolean {
        return str.hasPrefix(prefix, ignoreCase = ignoreCase)
    }

    //region Transformations

    fun camelCaseToWords(string: String): String? {
        if (string.isNullOrEmpty()) return string

        val result = StringBuilder(string.length)
        result.append(Character.toUpperCase(string[0]))

        for (i in 1 until string.length) {
            val chr = string[i]
            if (Character.isUpperCase(chr)) {
                result.append(' ')
            }
            result.append(chr)
        }

        return result.toString()
    }

    //endregion

    //region Nullability

    fun IsNullOrEmpty(str: String?): Boolean {
        return str.isNullOrEmpty()
    }

    fun nullOrNonEmpty(str: String?): String? {
        return if (str.isNullOrEmpty()) null else str
    }

    fun NonNullOrEmpty(str: String?): String {
        return str ?: ""
    }

    //endregion

    fun toString(value: Any?): String {
        return value?.toString() ?: "null"
    }

    fun toString(value: Float): String {
        return FLOATING_POINT_FORMAT.format(value.toDouble())
    }

    fun toString(value: Double): String {
        return FLOATING_POINT_FORMAT.format(value)
    }

    fun <T> Join(list: List<T>): String {
        return Join(list, ",")
    }

    fun <T> Join(list: List<T>, separator: String): String {
        val builder = StringBuilder()

        var i = 0
        for (e in list) {
            builder.append(e)
            if (++i < list.size) builder.append(separator)
        }
        return builder.toString()
    }

    fun <T> Join(array: Array<T>): String {
        return Join(array, ",")
    }

    fun <T> Join(array: Array<T>, separator: String): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: BooleanArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: ByteArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i].toInt())
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: ShortArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i].toInt())
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: CharArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: IntArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: LongArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: FloatArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    @JvmOverloads
    fun Join(array: DoubleArray, separator: String = ","): String {
        val builder = StringBuilder()
        for (i in array.indices) {
            builder.append(array[i])
            if (i < array.size - 1) builder.append(separator)
        }
        return builder.toString()
    }

    fun format(format: String?, vararg args: Any): String? {
        if (format != null && args.isNotEmpty()) {
            try {
                return String.format(format, *args)
            } catch (e: Exception) {
                android.util.Log.e(
                    "Lunar",
                    "Error while formatting String: " + e.message
                ) // FIXME: better system logging
            }

        }

        return format
    }

    fun serializeToString(data: Map<String, *>): String {
        val result = StringBuilder()
        var index = 0
        for ((key, value1) in data) {
            var value = toString(value1)
            value = value.replace("\n", "\\n") // we use new lines as separators
            result.append(key)
            result.append(':')
            result.append(value)

            if (++index < data.size) {
                result.append("\n")
            }
        }

        return result.toString()
    }
}