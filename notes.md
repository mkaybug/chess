1/10/2024 
Java Intro
-	30 years old
-	Cross client -> will run across platforms
-	Syntactically similar to C++ -> semantically different
-	Garbage collecting, deletes stuff after run time
-	Uses references instead of pointers (C++ uses pointers) -> they work differently, but from a programmer perspective, you don’ need to know the difference
-	Run time stack consists of primitive variables and references
-	Data types are always the same size in Java from operating system to operating system
o	Part of what makes it portable
-	Compiled language -> it has a compiler
o	Lower languages tend to be compiled, other languages that focus on performance and speed tend to be interpreted languages
o	Java tries to walk the middle line, it compiles code for a virtual machine -> looks like machine code -> .class file containing Java Byte Code -> Java virtual machine compiles the code to run on whatever operating system you’re dealing with, all of these need to have a JVM on them to run the code
File Organization
C++
o	Header files -> containing class definitions
o	.cpp files -> contains implementation
o	.o object code -> compiler makes .o file, and then linker inter
Java
o	One file per class and the names have to match
o	All our code goes into a .java file inside a bin, and the compiler creates a .class file for every .java file
o	There can be multiple main methods, in this case you would specify which main method you’d like to run
How to run files
-	Command line: cd into directory -> javac (path to file ex. .\SimpleJavaClass.java) (make sure the stuff is in the bin)
-	You can also just use IntelliJ which is what we’ll usually do
o	Pass arguments by editing the run time configurations
JavaDoc
-	Java comes with documentation for all its built-in libraries
-	Go to docs.oracle.com -> learn about any class built into Java
-	*Available when you’re taking the programming exam*
-	There are also third-party libraries you can import, we’re not talking about those, just the ones that are built into Java itself.
Classes/Objects
-	Every variable is individually declared as public, private, etc. vs blocks of declaration in C++
-	Every class has a toString() method that you can override
-	Any time you declare an object it is just a pointer, it doesn’t actually point to anything until you use “new ClassName(parameters)”
Primitive Datatypes -> not objects, just raw values so they don’t have methods on them
-	byte
-	short
-	int
-	long
-	float
-	double
-	char
-	boolean
Wrapper class -> for every primitive datatype there is a wrapper class
-	Wrapper classes are built into Java
Strings
-	Immutable -> you can’t change them after you declare them -> “read only”
o	You can still operate on strings, but you have to store the operated-on string in a new string object
-	Declaration
o	You can call String s = new String(“Hello”) but no one does, String s = “Hello”; means the same thing
-	Concatenation
o	String s3 = s1 + “ ” + s2;
o	Can be pretty inefficient -> StringBuilder class and use its append method
Arrays
-	An array of objects is just an array of pointers to objects 
