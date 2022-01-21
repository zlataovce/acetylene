package me.kcra.acetylene.test.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class Timer implements AutoCloseable {
    private final long startingTime = System.currentTimeMillis();
    private final String name;

    @Override
    public void close() {
        System.out.println("Phase '" + name + "' took " + (System.currentTimeMillis() - startingTime) + "ms.");
    }
}
