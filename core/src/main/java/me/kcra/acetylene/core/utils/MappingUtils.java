package me.kcra.acetylene.core.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class MappingUtils {
    public final List<String> PRIMITIVE_TYPES = List.of("int", "void", "long", "float", "double", "boolean", "short", "byte", "char");
    public final Map<String, String> PRIMITIVE_WRAPPER = Map.ofEntries(
            Map.entry("int", "java.lang.Integer"),
            Map.entry("void", "java.lang.Void"),
            Map.entry("long", "java.lang.Long"),
            Map.entry("float", "java.lang.Float"),
            Map.entry("double", "java.lang.Double"),
            Map.entry("boolean", "java.lang.Boolean"),
            Map.entry("short", "java.lang.Short"),
            Map.entry("byte", "java.lang.Byte"),
            Map.entry("char", "java.lang.Character")
    );
    public final Map<String, String> WRAPPER_PRIMITIVE = PRIMITIVE_WRAPPER.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));

    public String convertType(String type) {
        switch (type) {
            case "B":
                return "byte";
            case "C":
                return "char";
            case "D":
                return "double";
            case "F":
                return "float";
            case "I":
                return "int";
            case "J":
                return "long";
            case "S":
                return "short";
            case "Z":
                return "boolean";
            case "V":
                return "void";
            default:
                if (type.startsWith("[")) {
                    return convertType(type.substring(1)) + "[]";
                } else if (type.endsWith(";")) {
                    return type.substring(1, type.length() - 1);
                } else {
                    return type.substring(1);
                }
        }
    }
}
