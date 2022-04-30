# Bytecode Builder

BCB is a framework for generating java bytecode. The goal of this project is to simplify java bytecode generation.


## Project Layout

- [bcb](/bcb/src/main/java/com/ibm/bcb) - the core bcb project

- [examples](/bcb-examples/src/main/java/com/ibm/bcb) - bcb code generation examples

## Fused-Multiple-Add (FMA) Example

```java
public class Test {
    public static void main(String[] args) {
        BCBMethod method = BCBMethod.builder()
                .name("Test")
                .declaringClass("TestClass")
                .returnType(Type.FLOAT_TYPE)
                .arg("a", Type.FLOAT_TYPE)
                .arg("b", Type.FLOAT_TYPE)
                .arg("c", Type.FLOAT_TYPE)
                .body(
                        ret(
                                add(
                                        mul(
                                                load("a"),
                                                load("b")
                                        ),
                                        load("c")
                                )
                        )
                ).build();

        final MethodHandle fmaMethodHandle = method.toMethodHandle();

        System.out.println(fmaMethodHandle.invoke(2, 2, 6));
    }
}
```

### Generated bytecode (from javap)

```
public class TestClass {
  public static float Test(float, float);
    Code:
       0: fload_0
       1: fload_1
       2: fmul
       3: fload_0
       4: fload_1
       5: fmul
       6: fadd
       7: freturn
}
```