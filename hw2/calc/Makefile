JAVAC = javac
JAVAC_FLAGS = -sourcepath .

JAVA = java
JAVA_FLAGS =

CUP = java_cup.Main
CUPFLAGS = -nopositions -expect 100

JLEX = JLex.Main

all: test

test: absyn calc/Yylex.class calc/PrettyPrinter.class calc/Test.class calc/ComposVisitor.class calc/AbstractVisitor.class calc/FoldVisitor.class calc/AllVisitor.class calc/parser.class calc/sym.class calc/Test.class calc/Interpreter.class

.PHONY: absyn

%.class: %.java
	${JAVAC} ${JAVAC_FLAGS} $^

absyn: calc/Absyn/Exp.java calc/Absyn/EAdd.java calc/Absyn/ESub.java calc/Absyn/EMul.java calc/Absyn/EDiv.java calc/Absyn/EInt.java
	${JAVAC} ${JAVAC_FLAGS} $^

calc/Yylex.java: calc/Yylex
	${JAVA} ${JAVA_FLAGS} ${JLEX} calc/Yylex

calc/sym.java calc/parser.java: calc/calc.cup
	${JAVA} ${JAVA_FLAGS} ${CUP} ${CUPFLAGS} calc/calc.cup
	mv sym.java parser.java calc/

calc/Yylex.class: calc/Yylex.java calc/sym.java

calc/sym.class: calc/sym.java

calc/parser.class: calc/parser.java calc/sym.java

calc/PrettyPrinter.class: calc/PrettyPrinter.java

calc/Interpreter.class: calc/Interpreter.java

clean:
	rm -f calc/Absyn/*.class calc/*.class
	rm -f .dvi calc.aux calc.log calc.ps  *.class

distclean: vclean

vclean:
	 rm -f calc/Absyn/Exp.java calc/Absyn/EAdd.java calc/Absyn/ESub.java calc/Absyn/EMul.java calc/Absyn/EDiv.java calc/Absyn/EInt.java calc/Absyn/Exp.class calc/Absyn/EAdd.class calc/Absyn/ESub.class calc/Absyn/EMul.class calc/Absyn/EDiv.class calc/Absyn/EInt.class
	 rm -f calc/Absyn/*.class
	 rmdir calc/Absyn/
	 rm -f calc/Yylex calc/calc.cup calc/Yylex.java calc/VisitSkel.java calc/ComposVisitor.java calc/AbstractVisitor.java calc/FoldVisitor.java calc/AllVisitor.java calc/PrettyPrinter.java calc/Skeleton.java calc/Test.java calc/sym.java calc/parser.java calc/*.class
	rm -f Makefile
	rmdir -p calc/

