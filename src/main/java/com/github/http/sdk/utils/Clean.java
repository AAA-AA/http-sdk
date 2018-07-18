package com.github.http.sdk.utils;

import com.github.http.sdk.exception.HttpException;
import com.github.http.sdk.function.TExecutor;
import com.github.http.sdk.function.VoidExecutor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;


/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:06
 */
public final class Clean {

    private static final Random RANDOM = new Random();
    private static final String[] CAUSE_METHOD_NAMES = {"getCause", "getNextException", "getTargetException", "getException", "getSourceException",
            "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable",};

    public static <T> T newIns(Class<T> clazz) {
        return uncheck(clazz::newInstance);
    }

    public static String toString(byte[] bytes, String charset) {
        return new String(bytes, charset == null ? Charset.defaultCharset() : Charset.forName(charset));
    }

    public static void uncheck(VoidExecutor executor) {
        try {
            executor.execute();
        } catch (Throwable e) {
            throw new HttpException("系统异常", e);
        }
    }


    public static <T> T uncheck(TExecutor<T> executor) {
        try {
            return executor.execute();
        } catch (Throwable e) {
            throw new HttpException("系统异常", e);
        }
    }

    @SafeVarargs
    public static <T> T pick(Supplier<T>... gs) {
        if (isEmpty(gs)) {
            return null;
        }
        for (Supplier<T> g : gs) {
            T v = g.get();
            if (null != v) {
                return v;
            }
        }
        return null;
    }

    public static boolean isEmpty(Map<?, ?> parameters) {
        return null == parameters || parameters.isEmpty();
    }

    public static boolean isEmpty(Collection<?> parameters) {
        return null == parameters || parameters.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> parameters) {
        return !isEmpty(parameters);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean isEmpty(T[] parameters) {
        return null == parameters || parameters.length < 1;
    }

    static Class<?>[] BASIC = new Class[]{String.class, Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class};


    public static boolean isBasicType(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        for (Class<?> b : BASIC) {
            if (b.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static String randomNumeric(int size) {
        char[] chars = new char[size];
        for (int i = 0; i < size; i++) {
            chars[i] = (char) (48 + RANDOM.nextInt(10));
        }
        return new String(chars);
    }

    public static int indexOfThrowable(Throwable e, Class<? extends Throwable> clazz) {
        if (null == e || null == clazz) {
            return -1;
        }
        if (clazz.isAssignableFrom(e.getClass())) {
            return 0;
        }
        int count = 0;
        Throwable te = getTargetCause(e);
        while (null != te) {
            count++;
            if (clazz.isAssignableFrom(te.getClass())) {
                return count;
            }
            te = getTargetCause(te);
        }
        return -1;
    }

    private static Throwable getTargetCause(Throwable e) {
        for (String methodName : CAUSE_METHOD_NAMES) {
            try {
                Method method = e.getClass().getMethod(methodName);
                if (null != method && Throwable.class.isAssignableFrom(method.getReturnType())) {
                    return (Throwable) method.invoke(e);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {
            }
        }
        return null;
    }

    public static byte[] toByteArray(InputStream input) {
        return uncheck(() -> {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        });
    }

    public static boolean isProxyClass(Class<?> clazz) {
        return isJDKProxy(clazz) || isCGLIBProxy(clazz);
    }

    public static boolean isJDKProxy(Class<?> clazz) {
        return clazz != null && Proxy.isProxyClass(clazz);
    }

    public static boolean isCGLIBProxy(Class<?> clazz) {
        return clazz != null && null != clazz.getName() && clazz.getName().contains("$$");
    }

    public static boolean expectGreedScan(Class<?> beanClass) {
        return null != beanClass && beanClass != Object.class && !isBasicType(beanClass)
                && !isProxyClass(beanClass) && !beanClass.isInterface() && !beanClass.isEnum()
                && !beanClass.isAnnotation() && null != beanClass.getPackage()
                && !isBlank(beanClass.getPackage().getName())
                && !beanClass.getPackage().getName().startsWith("java");
    }
}
