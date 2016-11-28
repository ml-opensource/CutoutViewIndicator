package com.fuzz.indicator.reflect;

import android.content.Context;
import android.util.AttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;

import static com.fuzz.indicator.reflect.ReConstructor.constructFrom;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test for all the reflection-based instantiation code in {@link ReConstructor}
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
@RunWith(MockitoJUnitRunner.class)
public class ReConstructorTest {

    @Mock
    protected Context mockContext;
    @Mock
    protected AttributeSet mockAttrs;

    @Before
    public void setUp() {
        when(mockContext.getClassLoader()).thenReturn(getClass().getClassLoader());
    }

    /**
     * This tests whether {@link ReConstructor} will
     * <ol>
     *     <li>Throw an exception because "invalid class name" is not a fully-qualified class name</li>
     * </ol>
     *
     * @throws Exception (should be ClassNotFoundException, but if test fails could throw a different
     * {@link ReflectiveOperationException} instead)
     */
    @Test(expected = ClassNotFoundException.class)
    public void reconstructClassWithUnknownName() throws Exception {
        constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                "invalid class name"
        );
    }

    /**
     * This tests whether {@link ReConstructor} will
     * <ol>
     *     <li>Throw an exception because {@link com.fuzz.indicator.reflect.subpackage.PackagePrivateClass}
     *     is not accessible from this package, despite having a public constructor</li>
     * </ol>
     *
     * @throws Exception (should be IllegalAccessException, but if test fails could throw a different
     * {@link ReflectiveOperationException} instead)
     */
    @Test(expected = IllegalAccessException.class)
    public void reconstructClassWithInaccessibleName() throws Exception {
        constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                "com.fuzz.indicator.reflect.subpackage.PackagePrivateClass"
        );
    }

    /**
     * This tests whether {@link ReConstructor} will
     * <ol>
     *     <li>Load the PrivateConstructorWithoutInterfaceClass class file</li>
     *     <li>Throw an exception because that class doesn't implement ReflectiveInterface</li>
     * </ol>
     *
     * @throws Exception (should be ClassCastException, but if test fails could throw a
     * {@link ReflectiveOperationException} instead)
     */
    @Test(expected = ClassCastException.class)
    public void reconstructClassWithoutInterfaceWithoutPublicConstructor() throws Exception {
        constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PrivateConstructorWithoutInterfaceClass.class.getName()
        );
    }

    /**
     * This tests whether {@link ReConstructor} will
     * <ol>
     *     <li>Load the PrivateConstructorWithInterfaceAbstractClass class file</li>
     *     <li>Verify that class implements ReflectiveInterface</li>
     *     <li>Throw an exception because that class is abstract</li>
     * </ol>
     *
     * @throws Exception (should be InstantiationException, but if test fails could throw a
     * {@link ReflectiveOperationException} instead)
     */
    @Test(expected = InstantiationException.class)
    public void reconstructAbstractClassWithInterface() throws Exception {
        constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PrivateConstructorWithInterfaceAbstractClass.class.getName()
        );
    }

    /**
     * This tests whether {@link ReConstructor} will
     * <ol>
     *     <li>Load the PublicConstructorThatExceptsClass class file</li>
     *     <li>Verify that class implements ReflectiveInterface</li>
     *     <li>Find the constructor for that class which takes in one Context and one AttributeSet</li>
     *     <li>Throw an exception in
     *     {@link PublicConstructorThatExceptsClass#PublicConstructorThatExceptsClass(Context, AttributeSet)}
     *     because that method is supposed to fail this test
     *     </li>
     * </ol>
     *
     * @throws Exception (should be InstantiationException, but if test fails could throw a
     * {@link ReflectiveOperationException} instead)
     */
    @Test(expected = InvocationTargetException.class)
    public void reconstructClassWithRightInterfaceWithExceptionalPublicConstructor() throws Exception {
        constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PublicConstructorThatExceptsClass.class.getName()
        );
    }

    @Test
    public void reconstructClassWithRightInterfaceWithOnlyPrivateConstructor() throws Exception {
        ReflectiveInterface constructed = constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PrivateConstructorClass.class.getName()
        );

        // Only one constructor, private
        assertNull("ReConstructor shouldn't instantiate a class with only a private constructor.", constructed);
    }

    @Test
    public void reconstructClassWithRightInterfaceWithOnlyPrivateConstructors() throws Exception {
        ReflectiveInterface constructed = constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PrivateConstructorsClass.class.getName()
        );

        // Multiple constructors, all private
        assertNull("ReConstructor shouldn't instantiate a class with only private constructors.", constructed);
    }

    @Test
    public void reconstructClassWithRightInterfaceWithoutUsablePublicConstructor() throws Exception {
        ReflectiveInterface constructed = constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PublicUnusableConstructorClass.class.getName()
        );

        // Only one constructor, public
        assertNull("ReConstructor shouldn't instantiate a class with a public constructor unless it can call that constructor.", constructed);
    }

    @Test
    public void reconstructClassWithRightInterfaceWithPublicConstructor() throws Exception {
        ReflectiveInterface constructed = constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PublicConstructorClass.class.getName()
        );

        // One public no-args constructor, one private constructor
        assertNotNull("ReConstructor should instantiate a class with a valid public constructor.", constructed);
        assertThat(constructed, instanceOf(PublicConstructorClass.class));
        assertNull("ReConstructor should not call any private constructors", ((PublicConstructorClass) constructed).context);
    }

    @Test
    public void reconstructClassWithRightInterfaceWithCorrectParameters() throws Exception {
        ReflectiveInterface constructed = constructFrom(
                ReflectiveInterface.class,
                mockContext,
                mockAttrs,
                0,
                PublicSavedParametersClass.class.getName()
        );

        assertNotNull(constructed);
        assertThat(constructed, instanceOf(PublicSavedParametersClass.class));
        assertEquals(mockContext, ((PublicSavedParametersClass)constructed).context);
        assertEquals(mockAttrs, ((PublicSavedParametersClass)constructed).attrs);
    }

}