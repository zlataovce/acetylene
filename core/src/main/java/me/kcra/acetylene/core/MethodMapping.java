package me.kcra.acetylene.core;

public record MethodMapping(String original, String mapped, String originalDescriptor,
                            String mappedDescriptor) implements Mappable {
}
