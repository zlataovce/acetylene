package me.kcra.acetylene.core.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A utility class that helps a method or constructor check whether it was invoked correctly (whether its preconditions have been met).
 */
@UtilityClass
public class Preconditions {
    /**
     * Ensures that the supplied element is not null and throws a {@link NullPointerException} if it's null.
     *
     * @param elem the element
     * @param <T> the element type
     * @return the element, never null
     */
    public <T> @NotNull T checkNotNull(@Nullable T elem) {
        if (elem == null) {
            throw new NullPointerException();
        }
        return elem;
    }

    /**
     * Ensures that the supplied element is not null and throws a {@link NullPointerException} with the supplied message if it's null.
     *
     * @param elem the element
     * @param msg the exception message
     * @param <T> the element type
     * @return the element, never null
     */
    public <T> @NotNull T checkNotNull(@Nullable T elem, @Nullable Object msg) {
        if (elem == null) {
            throw new NullPointerException(String.valueOf(msg));
        }
        return elem;
    }

    /**
     * Ensures that the supplied boolean expression is true and throws a {@link IllegalArgumentException} if it's false.
     *
     * @param arg the expression
     */
    public void checkArgument(boolean arg) {
        if (!arg) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Ensures that the supplied boolean expression is true and throws a {@link IllegalArgumentException} with the supplied message if it's false.
     *
     * @param arg the expression
     * @param msg the exception message
     */
    public void checkArgument(boolean arg, @Nullable Object msg) {
        if (!arg) {
            throw new IllegalArgumentException(String.valueOf(msg));
        }
    }
}
