package de.hglabor.utils.noriskutils;

import org.bukkit.Material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static Object get(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getRecursive(Field field, Object obj, Field... superFields) {
        for (Field superField : superFields) {
            obj = get(superField, obj);
        }
        return get(field, obj);
    }

    public static Object getRecursive(Field field, Object obj, Iterable<Field> superFields) {
        for (Field superField : superFields) {
            obj = get(superField, obj);
        }
        return get(field, obj);
    }

    public static byte getByte(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getByte(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static short getShort(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getShort(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getInt(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getInt(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getLong(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getLong(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static float getFloat(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getFloat(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static double getDouble(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getDouble(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getBool(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getBoolean(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static char getChar(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.getChar(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void set(Field field, Object obj, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setRecursive(Field field, Object obj, Object value, Iterable<Field> superFields) {
        for (Field superField : superFields) {
            obj = get(superField, obj);
        }
        set(field, obj, value);
    }

    public static void setRecursive(Field field, Object obj, Object value, Field... superFields) {
        for (Field superField : superFields) {
            obj = get(superField, obj);
        }
        set(field, obj, value);
    }

    public static Field getField(Class<?> clazz, String name) {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields;
    }

    public static boolean isByte(Field field) {
        return isByte(field.getType());
    }

    public static boolean isShort(Field field) {
        return isShort(field.getType());
    }

    public static boolean isInt(Field field) {
        return isInt(field.getType());
    }

    public static boolean isLong(Field field) {
        return isLong(field.getType());
    }

    public static boolean isFloat(Field field) {
        return isFloat(field.getType());
    }

    public static boolean isDouble(Field field) {
        return isDouble(field.getType());
    }

    public static boolean isBool(Field field) {
        return isBool(field.getType());
    }

    public static boolean isChar(Field field) {
        return isChar(field.getType());
    }

    public static boolean isMaterial(Field field) { return isMaterial(field.getType()); }

    public static boolean isByte(Class<?> clazz) {
        return clazz == byte.class || clazz == Byte.class;
    }

    public static boolean isShort(Class<?> clazz) {
        return clazz == short.class || clazz == Short.class;
    }

    public static boolean isInt(Class<?> clazz) {
        return clazz == int.class || clazz == Integer.class;
    }

    public static boolean isLong(Class<?> clazz) {
        return clazz == long.class || clazz == Long.class;
    }

    public static boolean isFloat(Class<?> clazz) {
        return clazz == float.class || clazz == Float.class;
    }

    public static boolean isDouble(Class<?> clazz) {
        return clazz == double.class || clazz == Double.class;
    }

    public static boolean isBool(Class<?> clazz) {
        return clazz == boolean.class || clazz == Boolean.class;
    }

    public static boolean isChar(Class<?> clazz) {
        return clazz == char.class || clazz == Character.class;
    }

    public static boolean isMaterial(Class<?> clazz) { return clazz.equals(Material.class); }
}
