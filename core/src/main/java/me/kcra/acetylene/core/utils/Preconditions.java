package me.kcra.acetylene.core.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Preconditions {
    public <T> @NotNull T checkNotNull(@Nullable T elem) {
        if (elem == null) {
            throw new NullPointerException();
        }
        return elem;
    }

    public <T> @NotNull T checkNotNull(@Nullable T elem, @Nullable Object msg) {
        if (elem == null) {
            throw new NullPointerException(String.valueOf(msg));
        }
        return elem;
    }

    public void checkArgument(boolean arg) {
        if (!arg) {
            throw new IllegalArgumentException();
        }
    }

    public void checkArgument(boolean arg, @Nullable Object msg) {
        if (!arg) {
            throw new IllegalArgumentException(String.valueOf(msg));
        }
    }
}
