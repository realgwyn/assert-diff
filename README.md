TODO[![CI](https://github.com/cronn/validation-file-assertions/workflows/CI/badge.svg)](https://github.com/cronn/validation-file-assertions/actions)
TODO[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.cronn/validation-file-assertions/badge.svg)](http://maven-badges.herokuapp.com/maven-central/de.cronn/validation-file-assertions)
[![Apache 2.0](https://img.shields.io/github/license/cronn/validation-file-assertions.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Valid Gradle Wrapper](https://github.com/cronn/validation-file-assertions/workflows/Validate%20Gradle%20Wrapper/badge.svg)](https://github.com/cronn/validation-file-assertions/actions/workflows/gradle-wrapper-validation.yml)

### TODOs:

[x] Refactor ValidationFileAssertions interface into AssertDiff static class  
[x] Fix all tests  
[ ] change package name from `de.cronn.assertions.validationfile` to `de.cronn.assertdiff`  
[x] AssertDiff should be a static builder with configuration methods (withName, withNameSuffix, withOutputDir, withOutputActualDir, withOutputExpectedDir, withFieldMasking(json field masker), withMasking(regex masker))  
[x] Create global configuration
[ ] Create test class level configuration
[x] configure your own object mapper  
[ ] configure date time serialization format
[ ] configure location of output files  
[x] choose way to serialize json(default), xml or csv (add custom serializer)  
[ ] change naming for normalizers, (field masking?)  
[ ] change directories `output` to `actual` and `validation` to `expected`  
[ ] ability to disable globally defined masker for specific test case (in some cases we do want to validate `MASKED_ID/MASKED_DATE`)  
[x] do something with FileExtension - ability to add new extensions? Or modify method to accept extension as a String and not this FileExtension enum  
[ ] give ability to access default object mapper from outside (to use it for other things in tests)  
[ ] readme should have masking usage examples  
[ ] readme should have creating custom mask and usage example  
[ ] first run should fail with error message: `"First test run of <test name> - created snapshot: <file path>".` Snapshot should not have this part: `-=== new file \"data/test/validation/file.txt\" ===\n"`


## New awesome features

**Asserts now work with any POJOs**  
You no longer need to declare and configure your own ObjectMapper if you want to test POJOs, AssertDiff now comes with jackson ObjectMapper!

**Configure stuff that was not configurable before**  
Those properties can be now also configured [globally](#global-config), on [test class level](#test-class-config) or even [inline](#inline-config) for current assertion only.

| Option                             | Default value          | Description                                                         |
|------------------------------------|------------------------|---------------------------------------------------------------------|
| `outputDir(File)`                  | `data/test`            | Directory where  snapshots are saved                                |
| `actualSnapshotDir(File)`          | `data/test/actual`     | Directory where actual snapshot is saved                            |
| `expectedSnapshotDir(File)`        | `data/test/expected`   | Directory where expected snapshot is saved                          |
| `withSerializer(ObjectSerializer)` | `jackson ObjectMapper` | Configure custom `Object` to `String` [serializer](#serializers.md) |
| `withFieldMasking(FieldMasking)`   | N/A                    | Configure [field masking](#field-masking.md)                        |
| `withMasking(Masking)`             | N/A                    |                                                                     |
| `withFileSuffix(String)`           | N/A                    | Append [custom suffix](#file-name-suffix) to snapshots              |
| `withFileExtension(String)`        | `.txt`                 | Set file extension (Default: `.txt`)                                |

## New readme
Main goal here was to focus on explaining validating with files concept and show use cases and adventages comaring to junit/assertj usages  

New and fresh [Getting Started](#getting-started) examples  

## Naming changes  

Our best people at API UX and marketing department decided to spice things up a little bit and also came up with namings that are more relatable in our industry  

* Project name changed from validation-file-assertions to **AssertDiff**
* `validation file` term is now `snapshot`  
* Changed directories `output` to `actual` and `validation` to `expected`  
* Main entry method `assertWithFile()` is now named `assertSnapshot()`
* `Normalizer` was renamed to `Masking`
* Package name `de.cronn.assertions.validationfile` is changed to `de.cronn.assertdiff`





-------------------------

# AssertDiff - file based object assertions for Java

Idea of AssertDiff can be explained in 3 simple steps:

Instead of this:
```java
TODO: asserting every single pojo field with junit/assertj
```

Do this:
```java
TODO usage of AssertDiff
```

POJO Snapshot, png of diff
```json

```

Then if your test fails, you will get this:
<image of diff>

And to fix your test, you can do this:
<gif of saving new value in diff viewer>





-------------------------



### Save snapshot of your object

Let’s consider the following test:

```java
import de.cronn.validationfile.junit5.JUnit5ValidationFileAssertions;

class MyTest implements JUnit5ValidationFileAssertions {
    @Test
    void myTestMethod() {
        assertWithFile("actual value");
    }
}
```

When we run this test for the first time, it will create the following files:
 - `data/test/output/MyTest_myTestMethod.txt` (this one always contains the actual value from the last run; this file should be ignored in your SCM) with
   ```
   actual value
   ```
 - `data/test/validation/MyTest_myTestMethod.txt` (this one is prefilled automatically once during the first test run; on consecutive runs it will not be modified automatically) containing
   ```
   === new file "data/test/validation/MyTest_myTestMethod.txt" ===
   actual value
   ```

The test will fail as long as these two files are different. It’s the developer’s job to manually review the content of the `validation` file and remove the "new file" marker to make the test passing.
The reviewed validation file is then committed to the SCM repository together with the test itself.
Starting from this point `validation` file is the expected value of the test.

Any change to `"actual value"` will cause the test to fail and in this case the developer has to compare the content of `output` file with `validation` and if satisfied copy `output` to `validation` - this makes the test green.

We recommend using a good diffing tool, such as the built-in differ of IntelliJ or [Meld][meld], that allows you to diff the two directories `data/test/actual` vs `data/test/validation`.
We also provide an [IntelliJ plugin][intellij_plugin] that diffs the two directories with one shortcut.

### Quickstart
* Add the dependency

#### Gradle
```gradle
testImplementation 'de.cronn:validation-file-assertions:{version}'
```

#### Maven
```xml
<dependency>
    <groupId>de.cronn</groupId>
    <artifactId>validation-file-assertions</artifactId>
    <version>{version}</version>
    <scope>test</scope>
</dependency>
```

* Configure your SCM

    `.gitignore`
    ```
    data/test/output/
    data/test/tmp/
    ```

* Let your test class implement the `JUnit5ValidationFileAssertions` interface if you are using JUnit5, otherwise use `ValidationFileAssertions`

* Pick suitable `assertWithFile` method and enjoy your first validation file assertion.

### See also

* [Intellij plugin for validation file comparison][intellij_plugin]

[meld]: https://meldmerge.org/
[intellij_plugin]: https://plugins.jetbrains.com/plugin/12931-validation-file-comparison
