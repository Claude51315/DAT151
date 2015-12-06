import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;  
import java.util.ListIterator;
import java.io.*;
public class CodeGenerator 
{
    public enum TypeCode 
    {
        INT , DOUBLE , BOOL , VOID 
    }
    public enum ReturnType
    {
        INT , BOOL , VOID , MAIN 
    }
    private class FunType {
        public LinkedList<TypeCode> args ; 
        public TypeCode returnType; 
        public String javaRef ; 
        FunType (String javaRef , LinkedList<TypeCode> args , TypeCode returnType)
        {
            this.args = args; 
            this.returnType = returnType ; 
            this.javaRef = javaRef ; 
        }
        // 
        public String getJVMFunHeader()
        {
            //System.out.println(this.javaRef); 
           // System.out.println(this.args.size()); 
            String ans = "(" ; 
            ListIterator<TypeCode> it  = this.args.listIterator();
            while(it.hasNext())
            {
               if(it.next().equals(TypeCode.INT))
                ans = ans + "I" ; 
            }
            ans = ans + ")";
            //System.out.println(ans); 
            
            if(this.returnType.equals(TypeCode.INT))
                ans = ans + "I" ; 
            if(this.returnType.equals(TypeCode.VOID))
                ans = ans + "V" ; 
            return ans ; 
        }
    }
    private class Env {
        public HashMap<String , FunType> signature ; 
        public LinkedList<HashMap<String , Integer >> vars ; 
        public LinkedList<Integer> varCount ;     
        public Integer labelCount ; 
        public TypeCode returnType ; 
        //public LinkedList<HashMap<String , TypeCode>> contexts ; 
        public int returnFlag ; 
        public Env() 
        {
            signature = new HashMap<String , FunType>() ;
            vars = new LinkedList<HashMap<String , Integer>>();
            varCount = new LinkedList<Integer>() ; 
            vars.addFirst(new HashMap<String , Integer>());
            varCount.addFirst(0);
            labelCount = 0 ; 
        }
        public Integer lookupVar (String id)
        {
            Integer ans = 0 ; 
            for(HashMap<String ,Integer> scope : this.vars)
            {
                ans = scope.get(id);
                if(ans != null)
                    break;
            }
            return ans ; 
        } 
        public Integer lookupLabelCount()
        {
            return labelCount;
        }
        public void addLabelCount()
        {
           labelCount  ++ ; 
        }
        public void addVar( String id )
        {
            HashMap<String , Integer > scope = vars.getFirst();
            Integer number = varCount.getFirst();
            scope.put(id , number);
            number = number +1 ; 
            varCount.removeFirst();
            varCount.addFirst(number); 
        }
        public boolean isFunDecl (String id )
        {
            return signature.containsKey(id); 
        }
        public void updateFun (String id , FunType ft)
        {
            signature.put(id , ft) ; 
        }
        public FunType lookupFun (String id)
        {
            FunType t = signature.get(id) ; 
            return t ; 
        }
        public void enterScope()
        {
            vars.addFirst(new HashMap<String , Integer>());
        }
        public void leaveScope()
        {
            vars.removeFirst();
        }
    }
    private Env env = new Env() ;
    private LinkedList<String> out = new LinkedList<String>();  
    private void emit(String s) 
    {
        out.add(s + "\n");
    } 
    private void outputResult( String filename)
    {
        try{
            
            FileWriter tmp = new FileWriter(filename + ".j");
            for(String s : out)
                tmp.write(s);
            tmp.close();
        }       
        catch (IOException e ){}
     

    }
    // entry point of code Generator
    public void codeGenerate(Program p , String filename) 
    {
       
        // add JVM assembly 
        emit(".class public "+ filename);
        emit(".super java/lang/Object");
        emit(".method public <init>()V");
        emit("aload_0");
        emit("invokenonvirtual java/lang/Object/<init>()V") ;
        emit("return");
        emit(".end method");
            
        emit(".method public static main([Ljava/lang/String;)V");
        emit("invokestatic "+ filename +"/main()I");
        emit("pop");
        emit("return");
        emit(".end method");

        PDefs defs = (PDefs) p ; 
        //Env env = new Env() ; 
        // add build-in functions 
        LinkedList<TypeCode> args = new LinkedList<TypeCode>(); 
        args.add( TypeCode.INT);
        env.updateFun("printInt", new FunType("Runtime/printInt", args , TypeCode.VOID));
        args = new LinkedList<TypeCode>();
        env.updateFun("readInt" , new FunType("Runtime/readInt" , args , TypeCode.INT));
        // add function declaration to env.signature 
        for(Def f : defs.listdef_) 
        {
           
            DFun df = (DFun)f ;
            args = new LinkedList<TypeCode>(); 
            TypeCode returnType = getTypeCode(df.type_);
            //System.out.println(df.id_);
            for(Arg a : df.listarg_)
            {
                ADecl tmp = (ADecl)a ; 
                    
                //System.out.println(tmp.type_);
                TypeCode tc = getTypeCode(tmp.type_);
                args.add(tc);
            }
            //System.out.println(args.size());
            env.updateFun(df.id_ , new FunType(filename + "/"+df.id_ , args , returnType));
        }
        // generate JVM assembly
        for(Def f : defs.listdef_)
        {
            DFun df = (DFun) f ; 
            FunType FT = env.lookupFun(df.id_);
            // function header 
            emit(".method public static " + df.id_ +FT.getJVMFunHeader());
            emit(".limit locals 100"); 
            emit(".limit stack 100"); 
            // args??
            env.enterScope();
            env.varCount.addFirst(0) ; 
            for(Arg a : df.listarg_)
            {
                ADecl arg = (ADecl) a ; 
                env.addVar(arg.id_);

            }    
            // stms 
            for (Stm s : df.liststm_)
            {
                generateStm(s); 
            }
            // return 
            TypeCode returnType = getTypeCode(df.type_); 
          
            if(returnType.equals(TypeCode.INT)) 
            {
                emit("iconst_0");
                emit("ireturn");
            }
            else 
            {
                emit("return");
            }
            // function end 
            emit(".end method");
            env.varCount.removeFirst();
            env.leaveScope();
        }
        outputResult(filename);
    }
    private void generateStm (Stm s )
    {
        s.accept(new stmGenerator() , null);
    }
    private class stmGenerator implements Stm.Visitor<Object , Object>
    {
        public Object visit(CPP.Absyn.SExp p, Object o)
        {
            generateExp(p.exp_); 
            emit("pop");
            return null ;
        }
        public Object visit(CPP.Absyn.SDecls p, Object o)
        {
            TypeCode tc = getTypeCode(p.type_);
            for(String id : p.listid_)
            {
                env.addVar(id);
                if(tc.equals(TypeCode.INT))
                {
                    emit("iconst_0");
                    emit("istore_" + env.lookupVar(id));
                }
            }
            return null ; 
        }
        public Object visit(CPP.Absyn.SInit p, Object o)
        {
            TypeCode tc = getTypeCode(p.type_);
            generateExp(p.exp_);
            env.addVar(p.id_);
            if(tc.equals(TypeCode.INT))
            {
                emit("istore_" + env.lookupVar(p.id_));
            }
            else if(tc.equals(TypeCode.BOOL))
            {
                emit("istore_" + env.lookupVar(p.id_));
            }
            return null;
        }
        public Object visit(CPP.Absyn.SReturn p, Object o)
        {
            generateExp(p.exp_);
            emit("ireturn");
            return null; 
        }
        public Object visit(CPP.Absyn.SWhile p, Object o)
        {
            Integer enterLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer leaveLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("L"+ enterLabel + ":");
            generateExp(p.exp_);
            emit("iconst_0");            
            emit("if_icmpeq L" + leaveLabel);
            generateStm(p.stm_);
            emit("goto L" + enterLabel);
            emit("L" + leaveLabel + ":");
            return null;
        }
        public Object visit(CPP.Absyn.SBlock p, Object o)
        {
            env.enterScope();
            for(Stm s : p.liststm_)
                generateStm(s);
            env.leaveScope();
            return null;
        } 
        public Object visit(CPP.Absyn.SIfElse p, Object o)
        {
            Integer elseLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer leaveLabel = env.lookupLabelCount();
            env.addLabelCount(); 
            //emit("L" + enterLabel + ":");
            
            generateExp(p.exp_);
            emit(";; after generating Exp ");
            emit("iconst_0");
            emit("if_icmpeq L" + elseLabel );
            generateStm(p.stm_1);
            emit("goto L" + leaveLabel);
            emit("L" + elseLabel + ":");
            generateStm(p.stm_2);
            emit("L" + leaveLabel + ":");
            
            return null;
        }
    
    }
    private Integer generateExp (Exp e)
    {
        return e.accept(new expGenerator() , null );
    }
    private class expGenerator implements Exp.Visitor<Integer , Object>
    {
        public Integer visit(CPP.Absyn.ETrue p, Object o )
        {
            emit("ldc 1");
            return null;
        }
        public Integer visit(CPP.Absyn.EFalse p, Object o )
        {
            emit("ldc 0");
            return null;
        }
        public Integer visit(CPP.Absyn.EInt p, Object o )
        {
            emit("ldc " + p.integer_);
            return null;
        }
        public Integer visit(CPP.Absyn.EDouble p, Object o )
        {
            
            return null;
        }
        public Integer visit(CPP.Absyn.EId p, Object o )
        {
            Integer number = env.lookupVar(p.id_); 
            emit("iload_" + number);
            return number;
        }
        public Integer visit(CPP.Absyn.EApp p, Object o )
        {
            for(Exp e : p.listexp_)
                generateExp(e);
            FunType ft = env.lookupFun(p.id_);
            emit("invokestatic " + ft.javaRef + ft.getJVMFunHeader());
            if(ft.returnType.equals(TypeCode.VOID))
                emit("iconst_0"); 
            return null;
        }
        public Integer visit(CPP.Absyn.EPostIncr p, Object o )
        {
            Integer number = generateExp(p.exp_);
            emit("iconst_1");
            emit("iadd");
            if(number != null )
            {
                emit("istore_" + number);
                emit("iload_" + number);
            }
            return null;
        }
        public Integer visit(CPP.Absyn.EPostDecr p, Object o )
        {
            Integer number = generateExp(p.exp_);
            emit("iconst_1");
            emit("isub");
            if(number != null )
            {
                emit("istore_" + number);
                emit("iload_" + number);
            }
            return null;
        }
        public Integer visit(CPP.Absyn.EPreIncr p, Object o )
        {
            Integer number = generateExp(p.exp_);
            emit("iconst_1");
            emit("iadd");
            if(number != null )
            {
                emit("istore_" + number);
                emit("iload_" + number) ;
            }
            return null;
        }
        public Integer visit(CPP.Absyn.EPreDecr p, Object o )
        {
            Integer number = generateExp(p.exp_);
            emit("iconst_1");
            emit("isub");
            if(number != null )
            {
                emit("istore_" + number);
                emit("iload_" + number);
            }
            return null;
        }
        public Integer visit(CPP.Absyn.ETimes p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            emit("imul"); 
            return null;
        }
        public Integer visit(CPP.Absyn.EDiv p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            emit("idiv");
            return null;
        }
        public Integer visit(CPP.Absyn.EPlus p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            emit("iadd");
            return null;
        }
        public Integer visit(CPP.Absyn.EMinus p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            emit("isub");
            return null;
        }
            
        //store the result of comparison on the top of the stack 
        public Integer visit(CPP.Absyn.ELt p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer exitLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("if_icmplt L" + trueLabel);
            emit("iconst_0");
            emit("goto L" + exitLabel);
            emit("L" + trueLabel + ":");
            emit("iconst_1"); 
            emit("L" + exitLabel + ":");
            return null;
        }
        public Integer visit(CPP.Absyn.EGt p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer exitLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("if_icmpgt L" + trueLabel);
            emit("iconst_0");
            emit("goto L" + exitLabel);
            emit("L" + trueLabel + ":");
            emit("iconst_1");
            emit("L" + exitLabel+ ":");
            return null;
        }
        public Integer visit(CPP.Absyn.ELtEq p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer exitLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("if_icmple L" + trueLabel);
            emit("iconst_0");
            emit("goto L" + exitLabel);
            emit("L" + trueLabel + ":");
            emit("iconst_1");
            emit("L" + exitLabel + ":");


            return null;
        }
        public Integer visit(CPP.Absyn.EGtEq p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer exitLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("if_icmpge L" + trueLabel);
            emit("iconst_0");
            emit("goto L" + exitLabel);
            emit("L" + trueLabel + ":");
            emit("iconst_1");
            emit("L" + exitLabel + ":");

            return null;
        }
        public Integer visit(CPP.Absyn.EEq p, Object o )
        {
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer exitLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("if_icmpeq L" + trueLabel);
            emit("iconst_0");
            emit("goto L" + exitLabel);
            emit("L" + trueLabel + ":");
            emit("iconst_1");
            emit("L" + exitLabel + ":");
 
            return null;
        } 
        public Integer visit(CPP.Absyn.ENEq p, Object o )
        {
            emit(";; not equal");
            generateExp(p.exp_1);
            generateExp(p.exp_2);
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer exitLabel = env.lookupLabelCount();
            env.addLabelCount();
            emit("if_icmpne L" + trueLabel);
            emit("iconst_0");
            emit("goto L" + exitLabel);
            emit("L" + trueLabel + ":");
            emit("iconst_1");
            emit("L" + exitLabel + ":");
            return null;
        } 
        public Integer visit(CPP.Absyn.EAnd p, Object o )
        {
            Integer falseLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer endLabel = env.lookupLabelCount();
            env.addLabelCount();
        
            generateExp(p.exp_1);
            emit("ifeq L"+ falseLabel);
            generateExp(p.exp_2);
            emit("ifeq L"+ falseLabel);
            emit("ldc 1");
            emit("goto L"+endLabel);
            emit("L" + falseLabel + ":");
            emit("ldc 0");
            emit("L" + endLabel + ":");
            return null;
        } 
        public Integer visit(CPP.Absyn.EOr p, Object o )
        {
            emit(";; EOR");
            Integer trueLabel = env.lookupLabelCount();
            env.addLabelCount();
            Integer endLabel = env.lookupLabelCount();
            env.addLabelCount();
            
            generateExp(p.exp_1);
            emit("ifne L"+ trueLabel);
            generateExp(p.exp_2);
            emit("ifne L"+ trueLabel);
            emit("ldc 0");
            emit("goto L"+endLabel);
            emit("L" + trueLabel + ":");
            emit("ldc 1");
            emit("L" + endLabel + ":");
            return null;
        } 
        public Integer visit(CPP.Absyn.EAss p, Object o )
        {
            Integer number = generateExp(p.exp_1);
            emit("pop");
            generateExp(p.exp_2);
            
            emit("istore_" + number);
            emit("iload_" + number);
            return null;
        } 
    }
    private TypeCode getTypeCode(Type t)
    {
        return t.accept(new TypeCoder() , null);
    }
    private class TypeCoder implements Type.Visitor<TypeCode , Object>
    {
        public TypeCode visit(Type_bool t, Object arg)
        {
            return TypeCode.BOOL ;
        }
        public TypeCode visit(Type_int t, Object arg)
        {
            return TypeCode.INT ;
        }
        public TypeCode visit(Type_double t, Object arg)
        {
            return TypeCode.DOUBLE ;
        }
        public TypeCode visit(Type_void t, Object arg)
        {
            return TypeCode.VOID ;
        }
    }

}
