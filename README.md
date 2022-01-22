# acetylene
A Java library for managing obfuscation mappings.

## Usage
```java
final List<TypedMappingFile> files = new ArrayList<>();
for (...) {
    files.add(SrgUtilsMappingLoader.of(mappingFile1, mappingFile2).loadTyped());
}
final ClassAncestorTree classTree = ClassAncestorTree.of("mapped/class/name", files);
final DescriptableAncestorTree fieldTree = classTree.fieldAncestors("mappedField");
// index is based on the files list
System.out.println(classTree.mapping(1));
System.out.println(fieldTree.mapping(1));
```
Check out [the tests](core/src/test/java/me/kcra/acetylene/test/ClassAncestorTreeTest.java) for a more in-depth example.