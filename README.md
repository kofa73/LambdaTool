# LambdaTool

Find out which method is used in a lambda expression or method reference. Only works with interfaces.

This is done by creating a proxy for the interface, and capturing the method with a `MethodInvocationHandler`.
`LambdaTool` provides several implementations of the method `whichMethod` (`T` is the type of the interface):
- one taking a `Consumer<T>` to process calls to methods that take no arguments;
- one taking a `BiConsumer<T, ?>` to support methods with a single argument;
- one taking `MethodCallWithTwoParameters<T, ?, ?>` to capture calls with two arguments.

One could keep adding more, but there is little point. For special cases (more arguments or methods taking
some primitive types as arguments), one can always use a lambda instead of a method reference, or sometimes
use a cast to avoid ambiguity. Check out `LambdaToolTest` for examples.
