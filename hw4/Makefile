JAVAC = javac
JAVAC_FLAGS = -sourcepath .  

JAVA = java

.PHONY: bnfc lab4 clean distclean vclean

all: bnfc lab4

lab4:
	${JAVAC} ${JAVAC_FLAGS} lab4.java
	chmod a+x lab4

bnfc:
	bnfc -java1.5 Fun.cf
	${JAVA} ${JAVA_FLAGS} JLex.Main Fun/Yylex
	${JAVA} ${JAVA_FLAGS} java_cup.Main -nopositions -expect 100 Fun/Fun.cup
	mv sym.java parser.java Fun

clean:
	 -rm -f Fun/Absyn/*.class Fun/*.class
	 -rm -f .dvi Fun.aux Fun.log Fun.ps  *.class

distclean: vclean

vclean: clean
	 -rm -f Fun/Absyn/*.java
	 -rmdir Fun/Absyn/
	 -rm -f Fun.tex Fun.dvi Fun.aux Fun.log Fun.ps 
	 -rm -f Fun/Yylex Fun/Fun.cup Fun/Yylex.java Fun/VisitSkel.java Fun/ComposVisitor.java Fun/AbstractVisitor.java Fun/FoldVisitor.java Fun/AllVisitor.java Fun/PrettyPrinter.java Fun/Skeleton.java Fun/Test.java Fun/sym.java Fun/parser.java Fun/*.class
	 -rmdir -p Fun/

