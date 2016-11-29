package com.fuzz.indicator.reflect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.reflect.Modifier.isPublic;

/**
 * Utility class for instantiating objects by name via reflection.
 * <p>
 *     Call {@link #constructFrom(Class, Context, AttributeSet, int, String)}
 *     to create a new instance of the class specified by {@code targetName}.
 * </p>
 * <p>
 *     Note that this class is intended for use by {@link android.view.View}
 *     subclasses, specifically when parsing xml. It's a bit heavy otherwise.
 * </p>
 * <p>
 *     Supported constructors (in order of precedence - only the first valid constructor will be called):
 *     <ol>
 *         <li>targetName(Context, AttributeSet, int)</li>
 *         <li>targetName(Context, AttributeSet)</li>
 *         <li>targetName(Context)</li>
 *         <li>targetName()</li>
 *     </ol>
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class ReConstructor<T> {

    private static final Class<?>[] VOID_ARRAY = null;

    private Class<T> assignableClass;

    protected ReConstructor(Class<T> assignableClass) {
        this.assignableClass = assignableClass;
    }

    /**
     * Create a new instance of the class {@code targetName}, provided that that
     * class implements or extends {@code assignableClass}.
     *
     * @param assignableClass    the return value will either extend or implement this
     * @param context            the context in which to instantiate this
     * @param attrs              the attributes which the returned object might need to configure itself
     * @param defStyleAttr       the resource id of default attribute values within {@code attrs} (may be 0)
     * @param targetName         the fully-qualified name of the class of object which will be reconstructed
     * @param <R>                the type of {@code assignableClass}
     * @return an instance of {@code targetName}, or null if there are no public constructors of that
     * class
     *
     * @throws ClassNotFoundException if {@code targetName} is unknown to the
     * {@code context}'s {@code ClassLoader} (e.g. it's not a class name at all)
     * @throws IllegalAccessException if {@code targetName} is forbidden to the
     * {@code context}'s {@code ClassLoader}
     * @throws ClassCastException     if {@code targetName} doesn't extend/implement {@code assignableClass}
     * @throws InstantiationException if {@code targetName} is not the name of a class which can
     * be instantiated
     * @throws InvocationTargetException if a {@code targetName} constructor throws an exception
     */
    public static <R> R constructFrom(
            @NonNull Class<R> assignableClass,
            @NonNull Context context,
            @NonNull AttributeSet attrs,
            int defStyleAttr,
            @NonNull String targetName
    ) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<? extends R> generatorClass;

        ReConstructor<R> factory = new ReConstructor<>(assignableClass);
        generatorClass = factory.loadClassIn(targetName, context.getClassLoader());

        return factory.constructWithReflectionFrom(generatorClass, context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private Class<? extends T> loadClassIn(
            @NonNull String targetName,
            @NonNull ClassLoader classLoader
    ) throws ClassNotFoundException, ClassCastException {
        Class<?> loaded = classLoader.loadClass(targetName);
        if (assignableClass.isAssignableFrom(loaded)) {
            // the isAssignableFrom call above guarantees that this cast is safe
            return (Class<? extends T>) loaded;
        } else {
            throw new ClassCastException("The class " + targetName + " does not extend " + assignableClass.getSimpleName() + ".");
        }
    }

    @Nullable
    protected T constructWithReflectionFrom(
            @NonNull Class<? extends T> targetClass,
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends T> constructor;

        constructor = constructorWithParams(targetClass, Context.class, AttributeSet.class, int.class);
        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance(context, attrs, defStyleAttr);
        } else {
            constructor = constructorWithParams(targetClass, Context.class, AttributeSet.class);
        }

        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance(context, attrs);
        } else {
            constructor = constructorWithParams(targetClass, Context.class);
        }

        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance(context);
        } else {
            constructor = constructorWithParams(targetClass, VOID_ARRAY);
        }

        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance();
        }
        return null;
    }

    @Nullable
    private Constructor<? extends T> constructorWithParams(@NonNull Class<? extends T> targetClass, Class<?>... parameterTypes) {
        Constructor<? extends T> constructor;
        try {
            constructor = targetClass.getConstructor(parameterTypes);
        } catch (NoSuchMethodException ignored) {
            constructor = null;
        }
        return constructor;
    }
}
