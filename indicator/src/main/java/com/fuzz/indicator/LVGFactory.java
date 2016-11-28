package com.fuzz.indicator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.reflect.Modifier.isPublic;

/**
 * Reflection-based utility class for creating {@link LayeredViewGenerator}s on
 * the fly. May be called by the constructors in {@link CutoutViewIndicator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LVGFactory {

    private static final Class<?>[] VOID_ARRAY = null;

    public static void constructGeneratorFrom(
            @NonNull Context context,
            @NonNull AttributeSet attrs,
            int defStyleAttr,
            @NonNull String generatorName,
            @NonNull ConstructorCallback callback
    ) {
        LayeredViewGenerator generator = null;

        String message = "";
        try {
            generator = constructFrom(context, attrs, defStyleAttr, generatorName);
        } catch (ClassNotFoundException ignored) {
            message = "The specified LayeredViewGenerator cannot be found." +
                    " Make sure the class reference on rcv_generator_class_name" +
                    " (\'" + generatorName + "\')" +
                    " matches a class in the same ClassLoader as this layout's" +
                    " context.";
        } catch (InvocationTargetException ite) {
            message = "The constructor of the new LayeredViewGenerator threw this: " + ite.getCause();
        } catch (Exception e) {
            message = "The following arose while trying to resolve the" +
                    " the rcv_generator_class_name=\"" + generatorName +
                    "\" attribute as a LayeredViewGenerator: " + e.toString();
        } finally {
            if (generator == null) {
                message += "\nCutoutViewIndicator didn't have access to a working" +
                        " constructor on the specified class (" + generatorName + ")." +
                        " ImageViewGenerator will be used instead.";
                callback.onFailed(message);
            } else {
                callback.onGenerated(generator);
            }
        }
    }

    public static LayeredViewGenerator constructFrom(
            @NonNull Context context,
            @NonNull AttributeSet attrs,
            int defStyleAttr,
            @NonNull String generatorName
    ) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<? extends LayeredViewGenerator> generatorClass;

        LVGFactory factory = new LVGFactory();
        generatorClass = factory.loadClassIn(generatorName, context.getClassLoader());

        return factory.constructWithReflectionFrom(generatorClass, context, attrs, defStyleAttr);
    }

    @NonNull
    private Class<? extends LayeredViewGenerator> loadClassIn(
            @NonNull String generatorName,
            @NonNull ClassLoader classLoader
    ) throws ClassNotFoundException, ClassCastException {
        Class<?> loaded = classLoader.loadClass(generatorName);
        if (LayeredViewGenerator.class.isAssignableFrom(loaded)) {
            //noinspection unchecked - the isAssignableFrom call above guarantees that this cast is safe
            return (Class<? extends LayeredViewGenerator>) loaded;
        } else {
            throw new ClassCastException("The class " + generatorName + " does not extend LayeredViewGenerator.");
        }
    }

    @Nullable
    protected <T extends LayeredViewGenerator> T constructWithReflectionFrom(
            @NonNull Class<T> generatorClass,
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> constructor;

        constructor = constructorWithParams(generatorClass, Context.class, AttributeSet.class, int.class);
        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance(context, attrs, defStyleAttr);
        } else {
            constructor = constructorWithParams(generatorClass, Context.class, AttributeSet.class);
        }

        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance(context, attrs);
        } else {
            constructor = constructorWithParams(generatorClass, Context.class);
        }

        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance(context);
        } else {
            constructor = constructorWithParams(generatorClass, VOID_ARRAY);
        }

        if (constructor != null && isPublic(constructor.getModifiers())) {
            return constructor.newInstance();
        }
        return null;
    }

    @Nullable
    private <T> Constructor<T> constructorWithParams(@NonNull Class<T> generatorClass, Class<?>... parameterTypes) {
        Constructor<T> constructor;
        try {
            constructor = generatorClass.getConstructor(parameterTypes);
        } catch (NoSuchMethodException ignored) {
            constructor = null;
        }
        return constructor;
    }
}
