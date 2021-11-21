package org.kovacstelekes.lambdatool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class LambdaToolTest {
    @Test
    @DisplayName("Test protected parameterless void method.")
    void whichMethod_test1() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(TestInterface::parameterlessVoid)
        ).extracting(Method::getName).isEqualTo("parameterlessVoid");
    }

    @Test
    @DisplayName("Test protected object parameter void method.")
    void whichMethod_test2() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);

        assertThat(
            lambdaTool.whichMethod(TestInterface::singleObjectParameterToVoid)
        ).extracting(Method::getName).isEqualTo("singleObjectParameterToVoid");

    }

    @Test
    @DisplayName("Test protected int parameter void method.")
    void whichMethod_test3() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(i -> i.singleIntParameterToVoid(0))
        ).extracting(Method::getName).isEqualTo("singleIntParameterToVoid");

    }

    @Test
    @DisplayName("Test protected two object parameters void method.")
    void whichMethod_test4() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(TestInterface::twoObjectParametersToVoid)
        ).extracting(Method::getName).isEqualTo("twoObjectParametersToVoid");

    }

    @Test
    @DisplayName("Test protected two, int and object parameters void method.")
    void whichMethod_test5() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(i -> i.intAndObjectParametersToVoid(0, null))
        ).extracting(Method::getName).isEqualTo("intAndObjectParametersToVoid");

    }

    @Test
    @DisplayName("Test protected no argument String returning method.")
    void whichMethod_test6() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(TestInterface::parameterlessWithReturnType)
        ).extracting(Method::getName).isEqualTo("parameterlessWithReturnType");

    }

    @Test
    @DisplayName("Test protected object parameter String returning method.")
    void whichMethod_test7() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(TestInterface::singleObjectParameterWithReturnType)
        ).extracting(Method::getName).isEqualTo("singleObjectParameterWithReturnType");

    }

    @Test
    @DisplayName("Test protected two object parameters String returning method.")
    void whichMethod_test8() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(TestInterface::twoObjectParametersWithReturnType)
        ).extracting(Method::getName).isEqualTo("twoObjectParametersWithReturnType");

    }

    @Test
    @DisplayName("Test protected two, int and object parameters String returning method.")
    void whichMethod_test9() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(i -> i.intAndObjectParametersWithReturnType(0, null))
        ).extracting(Method::getName).isEqualTo("intAndObjectParametersWithReturnType");

    }

    @Test
    @DisplayName("Test protected many parameters void method.")
    void whichMethod_testA() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(i -> i.manyArguments(null, null, null, null, null, null, null))
        ).extracting(Method::getName).isEqualTo("manyArguments");

    }

    @Test
    @DisplayName("Test protected varargs void method.")
    void whichMethod_testB() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(i -> i.varArgs())
        ).extracting(Method::getName).isEqualTo("varArgs");

    }

    @Test
    @DisplayName("Test protected varargs void method.")
    void whichMethod_testC() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod((Consumer<TestInterface>) TestInterface::varArgs)
        ).extracting(Method::getName).isEqualTo("varArgs");

    }

    @Test
    @DisplayName("Test protected arguments and varargs void method.")
    void whichMethod_testD() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(i -> i.someArgumentsAndVarArgs(0, ""))
        ).extracting(Method::getName).isEqualTo("someArgumentsAndVarArgs");
    }

    @Test
    @DisplayName("Returns null when not a method in the interface")
    void whichMethod_testE() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(ti -> Object.class.cast(ti))
        ).isNull();
    }

    @Test
    @DisplayName("Returns null when not a method in the interface even after a successful call, so method was reset()")
    void whichMethod_testF() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertThat(
            lambdaTool.whichMethod(TestInterface::parameterlessVoid)
        ).extracting(Method::getName).isEqualTo("parameterlessVoid");
        assertThat(
            lambdaTool.whichMethod(ti -> Object.class.cast(ti))
        ).isNull();
    }

    private interface TestInterface {
        void parameterlessVoid();

        void singleObjectParameterToVoid(Object o);

        void singleIntParameterToVoid(int i);

        void twoObjectParametersToVoid(Object o1, Object o2);

        void intAndObjectParametersToVoid(int i, Object o);

        String parameterlessWithReturnType();

        String singleObjectParameterWithReturnType(Object o);

        String twoObjectParametersWithReturnType(Object o1, Object o2);

        String intAndObjectParametersWithReturnType(int i, Object o);

        void manyArguments(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7);

        void varArgs(Object... arguments);

        void someArgumentsAndVarArgs(int i, String s, Double... doubles);
    }
}
