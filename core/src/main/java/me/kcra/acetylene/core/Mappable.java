package me.kcra.acetylene.core;

import org.jetbrains.annotations.NotNull;

/**
 * Interface representing something that can be mapped.
 */
public interface Mappable {
    /**
     * Gets the original (obfuscated) mapping.
     *
     * @return the original mapping
     */
    @NotNull
    String original();

    /**
     * Gets the non-obfuscated mapping.
     *
     * @return the mapping
     */
    @NotNull
    String mapped();
}
