# acetylene
![Maven releases](https://repo.kcra.me/api/badge/latest/releases/me/kcra/acetylene/core)
![Maven snapshots](https://repo.kcra.me/api/badge/latest/snapshots/me/kcra/acetylene/core)  

A Java library for managing obfuscation mappings.

## Usage
```java
final List<TypedMappingFile> files = new ArrayList<>();
for (...) {
    files.add(SrgUtilsMappingLoader.of(mappingFile1, mappingFile2).loadTyped());
}
final ClassAncestorTree classTree = ClassAncestorTree.of("mapped/class/name", files);
final DescriptableAncestorTree fieldTree = classTree.fieldAncestors("mappedField");
// indexes are deduced from the ordering of the supplied list
// the supplied list should be sorted from the newest version to the oldest
System.out.println(classTree.mapping(1));
System.out.println(fieldTree.mapping(1));
```
Check out [the tests](core/src/test/java/me/kcra/acetylene/test/ClassAncestorTreeTest.java) for a more in-depth example.
