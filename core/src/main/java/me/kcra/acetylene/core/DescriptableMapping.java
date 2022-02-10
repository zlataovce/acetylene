package me.kcra.acetylene.core;

/**
 * A record representing a descriptable mapping (field, method).
 */
public record DescriptableMapping(String original, String mapped, String descriptor,
                                  String mappedDescriptor) implements Descriptable {
}
