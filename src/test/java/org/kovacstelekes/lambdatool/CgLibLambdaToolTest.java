package org.kovacstelekes.lambdatool;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CgLibLambdaToolTest {
    @Test
    void whichMethod_extractsCorrectMethod() {
        CgLibLambdaTool<TestClass> lambdaTool = CgLibLambdaTool.forType(TestClass.class);
        assertSoftly(softly -> {

            assertThat(
                lambdaTool.whichMethod(i -> i.singleIntParameterToVoid(0))
            ).extracting(Method::getName).isEqualTo("singleIntParameterToVoid");

            /*!jamal
            // <editor-fold desc="generated code">
            {%@for $method in (parameterlessVoid,singleObjectParameterToVoid,twoObjectParametersToVoid,parameterlessWithReturnType,singleObjectParameterWithReturnType,twoObjectParametersWithReturnType)=
            assertThat(
                    lambdaTool.whichMethod(TestClass::$method)
            ).extracting(Method::getName).isEqualTo("$method");
            %}
            // </editor-fold>
             */
            // <editor-fold desc="generated code">

            assertThat(
                    lambdaTool.whichMethod(TestClass::parameterlessVoid)
            ).extracting(Method::getName).isEqualTo("parameterlessVoid");

            assertThat(
                    lambdaTool.whichMethod(TestClass::singleObjectParameterToVoid)
            ).extracting(Method::getName).isEqualTo("singleObjectParameterToVoid");

            assertThat(
                    lambdaTool.whichMethod(TestClass::twoObjectParametersToVoid)
            ).extracting(Method::getName).isEqualTo("twoObjectParametersToVoid");

            assertThat(
                    lambdaTool.whichMethod(TestClass::parameterlessWithReturnType)
            ).extracting(Method::getName).isEqualTo("parameterlessWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(TestClass::singleObjectParameterWithReturnType)
            ).extracting(Method::getName).isEqualTo("singleObjectParameterWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(TestClass::twoObjectParametersWithReturnType)
            ).extracting(Method::getName).isEqualTo("twoObjectParametersWithReturnType");

            // </editor-fold>
            // __END__


            assertThat(
                    lambdaTool.whichMethod(i -> i.intAndObjectParametersToVoid(0, null))
            ).extracting(Method::getName).isEqualTo("intAndObjectParametersToVoid");

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

    private static class TestClass {
        public TestClass() {
            // needed by CgLib
        }

        void parameterlessVoid() {}

        void singleObjectParameterToVoid(Object o) {}

        void singleIntParameterToVoid(int i) {}

        void twoObjectParametersToVoid(Object o1, Object o2) {}

        void intAndObjectParametersToVoid(int i, Object o) {}

        String parameterlessWithReturnType() {return null;}

        String singleObjectParameterWithReturnType(Object o) {return null;}

        String twoObjectParametersWithReturnType(Object o1, Object o2) {return null;}

        String intAndObjectParametersWithReturnType(int i, Object o) {return null;}

        void manyArguments(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {}

        void varArgs(Object... arguments) {}

        void someArgumentsAndVarArgs(int i, String s, Double... doubles) {}
    }
}
