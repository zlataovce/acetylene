package me.kcra.acetylene.core;

import org.jetbrains.annotations.NotNull;

public interface Mappable {
    @NotNull
    String original();
    @NotNull
    String mapped();
}
