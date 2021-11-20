package org.kovacstelekes.lambdatool;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class LambdaToolTest {
    @Test
    void whichMethod_extractsCorrectMethod() {
        LambdaTool<TestInterface> lambdaTool = LambdaTool.forType(TestInterface.class);
        assertSoftly(softly -> {
            assertThat(
                    lambdaTool.whichMethod(TestInterface::parameterlessVoid)
            ).extracting(Method::getName).isEqualTo("parameterlessVoid");

            assertThat(
                    lambdaTool.whichMethod(TestInterface::singleObjectParameterToVoid)
            ).extracting(Method::getName).isEqualTo("singleObjectParameterToVoid");

            assertThat(
                    lambdaTool.whichMethod(i -> i.singleIntParameterToVoid(0))
            ).extracting(Method::getName).isEqualTo("singleIntParameterToVoid");

            assertThat(
                    lambdaTool.whichMethod(TestInterface::twoObjectParametersToVoid)
            ).extracting(Method::getName).isEqualTo("twoObjectParametersToVoid");

            assertThat(
                    lambdaTool.whichMethod(i -> i.intAndObjectParametersToVoid(0, null))
            ).extracting(Method::getName).isEqualTo("intAndObjectParametersToVoid");

            assertThat(
                    lambdaTool.whichMethod(TestInterface::parameterlessWithReturnType)
            ).extracting(Method::getName).isEqualTo("parameterlessWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(TestInterface::singleObjectParameterWithReturnType)
            ).extracting(Method::getName).isEqualTo("singleObjectParameterWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(TestInterface::twoObjectParametersWithReturnType)
            ).extracting(Method::getName).isEqualTo("twoObjectParametersWithReturnType");

            assertThat(
                    lambdaTool.whichMethod(i -> i.intAndObjectParametersWithReturnType(0, null))
            ).extracting(Method::getName).isEqualTo("intAndObjectParametersWithReturnType");
        });
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
    }
}
