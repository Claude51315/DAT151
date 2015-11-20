-- AR 18/2/2014 for the PLT course

=Compiler=

To compile the compiler:

  make compilemini

Make sure you have bnfc, JLex, and Cup available.

To run the compiler:

  ./compilemini ../ex.mini

Make sure you have jasmin.jar available.

To run the generated JVM code:

  java Foo

Make sure you have Runtime.class available. You can produce it by

  javac ../Runtime.java


=Files=

  compilermini       -- shell script that runs compilermini.java
  compilermini.java  -- top level compiler
  Compiler.java      -- code generator
  TypeChecker.java   -- type checker
  Interpreter.java   -- interpreter

  annotcompilermini.java      -- type annotated top-level compiler (works for doubles as well)
  AnnotCompiler.java          -- code generator using type annotations
  AnnotatingTypeChecker.java  -- type checker that produces annotations; not quite complete

  ../Mini.cf       -- grammar of Mini, source file for bnfc
  ../ex.mini       -- example Mini program
  ../Runtime.java  -- runtime system for the compiler


