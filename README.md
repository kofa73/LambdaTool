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

# CgLib version
`CgLibLambdaTool` also supports classes; however, with CgLib not being compatible with the latest Java versions
(the required methods are not 'opened' to the public), you won't be able to use that with Java 17. 

See https://github.com/cglib/cglib/issues/191 for details.

# ByteBuddy version
`BbLambdaTool`, using ByteBuddy, overcomes the JDK issues; but, so far, I have been unable to get it to work
with package-private methods (i.e., they need to be `protected` or `public`). The reason is that ByteBuddy
needs a new classloader that can access the bytecode of the generated class, therefore even though the
package names match, the packages (identified by name + classloader) will be different; therefore, the package-private
method in the generated class will not override the package-private method in the original one.
See https://stackoverflow.com/a/26861830 and a related Mockito issue at
https://github.com/mockito/mockito/issues/796#issuecomment-266785414.
(Thanks to [Peter Verhás](https://javax0.wordpress.com/) for the tip.)
