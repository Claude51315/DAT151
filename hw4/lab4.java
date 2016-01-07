import java_cup.runtime.*;
import Fun.*;
import Fun.Absyn.*;
import java.io.*;

public class lab4 {
    public static void main(String args[]) {
        // 0 : call by value  , 1 : call by name 
        int method = 0   ; 
        String filename = null;  
        if (args.length < 1) {
            System.err.println("Usage: lab4 <SourceFile>");
            System.exit(1);
        }
        else if( args.length == 1)
        {
            method  = 0;
            filename = args[0]; 
        }
        else if (args.length == 2 )
        {
            if (args[0].compareTo("-n") == 0 )
                method = 1 ; 
            else
                method = 0; 
            filename = args[1] ;
        }


        Yylex l = null;
        try {
            l = new Yylex(new FileReader(filename));
            parser p = new parser(l);
            Fun.Absyn.Program parse_tree = p.pProgram();
            //new TypeChecker().typecheck(parse_tree);
            new Interpreter().interpret(parse_tree , method  );
        }
        /* catch (TypeException e) {
            System.out.println("TYPE ERROR");
            System.err.println(e.toString());
            System.exit(1);
        } catch (RuntimeException e) {
            //            System.out.println("RUNTIME ERROR");
            System.err.println(e.toString());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }*/
        catch (Throwable e) {
            System.out.println("SYNTAX ERROR");
            System.out.println("At line " + String.valueOf(l.line_num())
                       + ", near \"" + l.buff() + "\" :");
            System.out.println("     " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

