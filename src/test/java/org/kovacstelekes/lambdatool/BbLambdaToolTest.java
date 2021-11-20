package org.kovacstelekes.lambdatool;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class BbLambdaToolTest {
    @Test
    void whichMethod_extractsCorrectMethod() {
        BbLambdaTool<TestClass> lambdaTool = BbLambdaTool.forType(TestClass.class);
        assertSoftly(softly -> {
            assertThat(
                    lambdaTool.whichMethod(TestClass::parameterlessVoid)
            ).extracting(Method::getName).isEqualTo("parameterlessVoid");

            assertThat(
                    lambdaTool.whichMethod(TestClass::singleObjectParameterToVoid)
            ).extracting(Method::getName).isEqualTo("singleObjectParameterToVoid");

            assertThat(
                    lambdaTool.whichMethod(i -> i.singleIntParameterToVoid(0))
            ).extracting(Method::getName).isEqualTo("singleIntParameterToVoid");

            assertThat(
                    lambdaTool.whichMethod(TestClass::twoObjectParametersToVoid)
            ).extracting(Method::getName).isEqualTo("twoObjectParametersToVoid");

            assertThat(
                    lambdaTool.whichMethod(i -> i.intAndObjectParametersToVoid(0, null))
            ).extracting(Method::getName).isEqualTo("intAndObjectParametersToVoid");

            assertThat(
                    lambdaTool.whichMethod(TestClass::parameterlessWithReturnType)
            ).extracting(Method::getName).isEqualTo("parameterlessWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(TestClass::singleObjectParameterWithReturnType)
            ).extracting(Method::getName).isEqualTo("singleObjectParameterWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(TestClass::twoObjectParametersWithReturnType)
            ).extracting(Method::getName).isEqualTo("twoObjectParametersWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(i -> i.intAndObjectParametersWithReturnType(0, null))
            ).extracting(Method::getName).isEqualTo("intAndObjectParametersWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(i -> i.manyArguments(null, null, null, null, null, null, null))
            ).extracting(Method::getName).isEqualTo("manyArguments");

            assertThat(
                    lambdaTool.whichMethod(i -> i.varArgs())
            ).extracting(Method::getName).isEqualTo("varArgs");

            assertThat(
                    lambdaTool.whichMethod((Consumer<TestClass>) TestClass::varArgs)
            ).extracting(Method::getName).isEqualTo("varArgs");

            assertThat(
                    lambdaTool.whichMethod(i -> i.someArgumentsAndVarArgs(0, ""))
            ).extracting(Method::getName).isEqualTo("someArgumentsAndVarArgs");
        });
    }

    // methods need to be public or protected
    public static class TestClass {
        protected void parameterlessVoid() {}

        protected void singleObjectParameterToVoid(Object o) {}

        protected void singleIntParameterToVoid(int i) {}

        protected void twoObjectParametersToVoid(Object o1, Object o2) {}

        protected void intAndObjectParametersToVoid(int i, Object o) {}

        protected String parameterlessWithReturnType() {return null;}

        protected String singleObjectParameterWithReturnType(Object o) {return null;}

        protected String twoObjectParametersWithReturnType(Object o1, Object o2) {return null;}

        protected String intAndObjectParametersWithReturnType(int i, Object o) {return null;}

        protected void manyArguments(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {}

        protected void varArgs(Object... arguments) {}

        protected void someArgumentsAndVarArgs(int i, String s, Double... doubles) {}
    }
}
