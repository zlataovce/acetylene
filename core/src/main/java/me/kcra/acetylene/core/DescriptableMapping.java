package me.kcra.acetylene.core;

public record DescriptableMapping(String original, String mapped, String descriptor,
                                  String mappedDescriptor) implements Descriptable {
}
