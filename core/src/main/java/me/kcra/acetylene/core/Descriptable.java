package me.kcra.acetylene.core;

import org.jetbrains.annotations.NotNull;

/**
 * Interface representing something that can have a mapped descriptor (a field, a method).
 */
public interface Descriptable extends Mappable {
    /**
     * Gets the original (obfuscated) descriptor.
     *
     * @return the original descriptor
     */
    @NotNull
    String descriptor();

    /**
     * Gets the mapped (non-obfuscated) descriptor.
     *
     * @return the mapped descriptor
     */
    @NotNull
    String mappedDescriptor();
}
