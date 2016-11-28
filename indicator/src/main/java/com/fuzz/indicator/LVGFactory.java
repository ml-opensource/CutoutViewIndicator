package com.fuzz.indicator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.lang.reflect.InvocationTargetException;

import static com.fuzz.indicator.reflect.ReConstructor.constructFrom;

/**
 * Reflection-based utility class for creating {@link LayeredViewGenerator}s on
 * the fly. May be called by the constructors in {@link CutoutViewIndicator}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LVGFactory {

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
            generator = constructFrom(LayeredViewGenerator.class, context, attrs, defStyleAttr, generatorName);
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

}
