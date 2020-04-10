package com.epicnicity322.epicpluginlib.reflection.type;

import java.util.HashMap;

public enum DataType
{
    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    INTEGER(int.class, Integer.class),
    LONG(long.class, Long.class),
    CHARACTER(char.class, Character.class),
    FLOAT(float.class, Float.class),
    DOUBLE(double.class, Double.class),
    BOOLEAN(boolean.class, Boolean.class);

    private static HashMap<Class<?>, DataType> CLASS_MAP = new HashMap<>();

    static {
        for (DataType t : values()) {
            CLASS_MAP.put(t.primitive, t);
            CLASS_MAP.put(t.reference, t);
        }
    }

    private Class<?> primitive;
    private Class<?> reference;

    DataType(Class<?> primitive, Class<?> reference)
    {
        this.primitive = primitive;
        this.reference = reference;
    }

    public static DataType fromClass(Class<?> c)
    {
        return CLASS_MAP.get(c);
    }

    public static Class<?> getPrimitive(Class<?> c)
    {
        DataType t = fromClass(c);

        return t == null ? c : t.getPrimitive();
    }

    public static Class<?> getReference(Class<?> c)
    {
        DataType t = fromClass(c);

        return t == null ? c : t.getReference();
    }

    public static Class<?>[] convertToPrimitive(Class<?>[] classes)
    {
        int length = classes == null ? 0 : classes.length;

        Class<?>[] types = new Class<?>[length];

        for (int i = 0; i < length; i++) {
            types[i] = getPrimitive(classes[i]);
        }

        return types;
    }

    public static Class<?>[] convertToPrimitive(Object[] objects)
    {
        int length = objects == null ? 0 : objects.length;
        Class<?>[] types = new Class<?>[length];

        for (int i = 0; i < length; i++) {
            types[i] = getPrimitive(objects[i].getClass());
        }

        return types;
    }

    public static boolean equalsArray(Class<?>[] a1, Class<?>[] a2)
    {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            return false;
        }

        for (int i = 0; i < a1.length; i++) {
            if (!a1[i].equals(a2[i]) && !a1[i].isAssignableFrom(a2[i])) {
                return false;
            }
        }

        return true;
    }

    public Class<?> getPrimitive()
    {
        return primitive;
    }

    public Class<?> getReference()
    {
        return reference;
    }
}
