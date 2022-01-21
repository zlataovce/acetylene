package me.kcra.acetylene.core;

import org.jetbrains.annotations.NotNull;

public interface Descriptable extends Mappable {
    @NotNull
    String descriptor();
    @NotNull
    String mappedDescriptor();
}
