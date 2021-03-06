package com.ibm.bcb;

import lombok.SneakyThrows;
import org.junit.Test;
import org.objectweb.asm.Type;

import java.io.File;
import java.lang.invoke.MethodHandle;

import static com.ibm.bcb.BCB.*;
import static com.ibm.bcb.BCB.load;
import static org.junit.Assert.assertEquals;

public class BCBTest {

    @Test
    @SneakyThrows
    public void testFMA() {
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

        assertEquals(10f, (float) fmaMethodHandle.invoke(2, 2, 6), 0.0001);
    }

    @Test
    @SneakyThrows
    public void testMax() {
        BCBMethod method = BCBMethod.builder()
                .name("Test")
                .declaringClass("TestClass")
                .returnType(Type.FLOAT_TYPE)
                .arg("a", Type.FLOAT_TYPE)
                .arg("b", Type.FLOAT_TYPE)
                .body(
                        /* if (a > b) return a; else return b; */
                        ifStmt(
                                load("a").gt(load("b")),
                                ret(
                                        load("a")
                                ),
                                ret(
                                        load("b")
                                )
                        )
                ).build();

        final MethodHandle fmaMethodHandle = method.toMethodHandle();

        assertEquals(10f, (float) fmaMethodHandle.invoke(6f, 10f), 0.0001);
        assertEquals(10f, (float) fmaMethodHandle.invoke(10f, 3f), 0.0001);
    }
}
