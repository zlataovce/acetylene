/*
 * This file is part of acetylene, licensed under the MIT License.
 *
 * Copyright (c) 2022 Matouš Kučera
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
