import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;  

public class Interpreter {
    public static enum TypeCode 
    {
        Type_int , Type_double , Type_bool , Type_void 
    }
    public static class FunType {
        public LinkedList<TypeCode> args ; 
        public TypeCode returnType; 
    }
    public static class Env {
        public HashMap<String , FunType> signature ; 
        public LinkedList<HashMap<String , Value>> contexts ; 
        public Env() 
        {
            contexts = new LinkedList<HashMap<String , Value>>();
            signature = new HashMap<String , FunType> () ; 
            enterScope(); 
        }
        public Value lookupVar(String id )
        {
            // java iterate a linked list 
            for (HashMap<String , Value> context : contexts)
            {
                Value t = context.get(id) ; 
                if(t != null)
                    return t ; 
            }
            throw new RuntimeException("Unknown variable");
        }
        public boolean isFunDecl (String id )
        {
            return signature.containsKey(id); 
        }
        public boolean isVarDecl (String id )
        {
            Value t ; 
            HashMap<String , Value> context =  contexts.getFirst(); 
            t = context.get(id);
            if(t != null)
                return true ; 
            return false ;
        }
        public FunType lookupFun (String id)
        {
            FunType t = signature.get(id) ; 
            if (t == null)
                throw new TypeException("There is no [" + id + "] function");
            else
                return t ; 
        }
        public void addVar(String id )
        {
            contexts.getFirst().put(id , new Value.Undefined());
        }
        public void setVar(String id , Value V )
        {
            for( HashMap<String , Value> cur_scope : contexts)
            {
                if(cur_scope.containsKey(id))
                {
                    cur_scope.put(id , V);
                    return ; 
                }
            }
        }
        public void enterScope()
        {
            contexts.addFirst(new HashMap<String , Value>());
        }
        public void leaveScope()
        {
            contexts.removeFirst(); 
        }
    }
    private static abstract class Value
    {
        public boolean isInt()
        {
            return false ;
        }
        public Integer getInt()
        {
            throw new RuntimeException(this + "is not an integer");
        }
        public Double getDouble()
        {
            throw new RuntimeException(this + "is not an double");
        }
        public String print()
        {
            if(this.isInt())
                return this.toString();
            else
                return this.toString();
        }
        public static class Undefined extends Value
        {
            public Undefined (){}
            public String toString (){  return "undefined" ; }
        
        }
        public static class IntValue extends Value
        {
            private Integer i ;  
            public IntValue(Integer i ) {this.i = i ;}
            public boolean isInt(){ return true;  }        
            public Integer getInt(){return i ;}
            public String toString(){return i.toString() ; }
        }
        public static class DoubleValue extends Value
        {
            private Double d ;
            public DoubleValue(Double d) {this.d = d ;}
            public Double getDouble(){return d ;}
            public String toString(){return d.toString() ; }
        }
    }   
    // entry point 
    public void interpret(Program p) 
    {
        PDefs defs = (PDefs) p ; 
        Env env = new Env() ; 
        // add build-in function def 
        FunType readInt = new FunType(); 
        FunType readDouble = new FunType(); 
        FunType printInt = new FunType(); 
        FunType printDouble = new FunType(); 

        readInt.returnType = TypeCode.Type_int ; 
        readInt.args = new LinkedList<TypeCode>() ;
        env.signature.put("readInt", readInt) ;

        readDouble.returnType = TypeCode.Type_double ;
        readDouble.args = new LinkedList<TypeCode>() ;
        env.signature.put("readDouble", readDouble) ;

        printInt.returnType = TypeCode.Type_void ;
        printInt.args = new LinkedList<TypeCode>() ;
        printInt.args.addLast(TypeCode.Type_int);
        env.signature.put("printInt", printInt) ;

        printDouble.returnType = TypeCode.Type_void ;
        printDouble.args = new LinkedList<TypeCode>() ;
        printDouble.args.addLast(TypeCode.Type_double);
        env.signature.put("printDouble", printDouble) ;

        for(Def d : defs.listdef_ )
        {
            // add function execution body
            checkDef1(d , env) ; 
        }
        
        for(Def d : defs.listdef_)
        {
            // executio main function 
            checkDef2(d , env);
        }
        //System.out.println(env.signature.size());
        //throw new TypeException("Not yetQQQQ a typechecker");
    }
    // check for Defs 
    private void checkDef1(Def d , Env env)
    {
        d.accept(new defExeAdder() , env);
    }
    private void checkDef2(Def d , Env env)
    {
        d.accept(new mainFunExecuter() , env);
    }
    private class defExeAdder implements Def.Visitor<Object , Env> 
    {
        public Object visit(CPP.Absyn.DFun p, Env env)
        {
            /* Code For DFun Goes Here */
            if(p.id_ != "main")
            {
                //add function execution body here
            
            }
            return null; 
        }                                
    }
    private class mainFunExecuter implements Def.Visitor<Object , Env>
    {
        public Object visit(CPP.Absyn.DFun p , Env env)
        {
            // add function args to current scope 
            if(p.id_ != "main")
                return null; 
            else
            {
                for (Stm s : p.liststm_)
                {
                    exeStm(s , env );
                }
            }
            return null ; 
        }
    }
    // check for statements 
    private void exeStm (Stm s , Env env)
    {
        s.accept(new StmExecuter() , env);
    }
    private class StmExecuter implements Stm.Visitor<Object , Env>
    {
        public Object visit(CPP.Absyn.SExp p, Env env)
        {
            /* Code For SExp Goes Here */
            Value v = exeExp(p.exp_ , env ) ; 
            String s = v.print();
            System.out.println(s);
            return null;
        }
        public Object visit(CPP.Absyn.SDecls p, Env env)
        {
            /* Code For SDecls Goes Here */
            for(String x :p.listid_)
            {
                env.addVar(x);
            }
            //System.out.println("an Decl");
            return null;
        }
        public Object visit(CPP.Absyn.SInit p, Env env)
        {
            /* Code For SInit Goes Here */
            //System.out.println("decalation with initialization");
            env.addVar(p.id_);
            env.setVar(p.id_ , exeExp(p.exp_ , env));
            return null;
        }
        public Object visit(CPP.Absyn.SReturn p, Env env)
        {
            /* Code For SReturn Goes Here */
             // check the returnType equal to function declaration 
            //System.out.println("return" );
            return null;
        }
        public Object visit(CPP.Absyn.SWhile p, Env env)
        {
            /* Code For SWhile Goes Here */
            //System.out.println("an while");
            while(true)
            {
                Value t = exeExp(p.exp_ , env);
                if(t.isInt())
                {
                    if(t.getInt() == 1 )
                    {
                        exeStm(p.stm_ , env);
                    }
                    else
                        break ; 
                }
            }
            return null;
        }
        public Object visit(CPP.Absyn.SBlock p, Env env)
        {
            /* Code For SBlock Goes Here */
            //System.out.println("an block");
            env.enterScope();
            for(Stm s : p.liststm_)
            {
                exeStm(s , env );
            }
            env.leaveScope();
            return null;
        }
        public Object visit(CPP.Absyn.SIfElse p, Env env)
        {
            /* Code For SIfElse Goes Here */
            Value t = exeExp(p.exp_ , env);
            if(t.isInt())
            {
                if(t.getInt() == 1 )
                    exeStm(p.stm_1 , env);
                else
                    exeStm(p.stm_2 , env);
            }
            //System.out.println("an if else");
            return null;
        }
          
    }
    
    public String getId (Exp exp , Env env)
    {
        return exp.accept(new getIdMethod() , env);
    } 
    private class getIdMethod implements Exp.Visitor<String , Env>
    {
        public String visit(CPP.Absyn.EId p , Env env )
        {
            return p.id_ ; 
        }
        public String visit(CPP.Absyn.ETrue p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EFalse p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EInt p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EDouble p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EApp p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EPostIncr p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EPostDecr p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EPreIncr p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EPreDecr p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.ETimes p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EDiv p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EPlus p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EMinus p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.ELt p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EGt p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.ELtEq p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EGtEq p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EEq p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.ENEq p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EAnd p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EOr p , Env env )
        {
            return null ;
        }
        public String visit(CPP.Absyn.EAss p , Env env )
        {
            return null ;
        }

    }
    public Value exeExp(Exp exp , Env env)
    {
        return exp.accept(new expExecuter() , env );
    }
    private class expExecuter implements Exp.Visitor<Value , Env>   
    {
        public Value visit(CPP.Absyn.ETrue p , Env env) 
        {
            //System.out.println ("ETrue");
            return new Value.IntValue(1);
        }
        public Value visit(CPP.Absyn.EFalse p , Env env)
        {
            //System.out.println ("EFalse");
            return new Value.IntValue(0); 
        }
        public Value visit(CPP.Absyn.EInt p , Env env)
        {
            //System.out.println ("EInt");
            return new Value.IntValue(p.integer_) ;
        }
        public Value visit(CPP.Absyn.EApp p , Env env)
        {
            //System.out.println ("EApp");
            
            return null ;
        }
        public Value visit(CPP.Absyn.EDouble p , Env env)
        {
            //System.out.println ("EDouble");
            return new Value.DoubleValue(p.double_);
        }
        public Value visit(CPP.Absyn.EId p , Env env)
        {
            //System.out.println ("EId");
            return env.lookupVar(p.id_) ;
        }
        public Value visit(CPP.Absyn.EPostIncr p , Env env)
        {
            //System.out.println ("EPostIncr");
            String id = getId(p.exp_ , env);
            Value v = env.lookupVar(id) ; 
            if(v.isInt())
            {
                env.setVar(id , new Value.IntValue(v.getInt() +1));
                return v  ; 
            }
            else
            {
                env.setVar(id , new Value.DoubleValue(v.getDouble() +1.0));
                return v  ;
            }
        }
        public Value visit(CPP.Absyn.EPostDecr p , Env env)
        {
            //System.out.println ("EPostDecr");
            String id = getId(p.exp_ , env);
            Value v = env.lookupVar(id) ;
            if(v.isInt())
            {
                env.setVar(id , new Value.IntValue(v.getInt() - 1));
                return v  ;
            }
            else
            {
                env.setVar(id , new Value.DoubleValue(v.getDouble() - 1.0));
                return v  ;
            }
        }
        public Value visit(CPP.Absyn.EPreIncr p , Env env)
        {
            //System.out.println ("EPreIncr");
            String id = getId(p.exp_ , env);
            Value v = env.lookupVar(id) ;
            if(v.isInt())
            {
                env.setVar(id , new Value.IntValue(v.getInt() +1));
                return env.lookupVar(id)  ;
            }
            else
            {
                env.setVar(id , new Value.DoubleValue(v.getDouble() +1.0));
                return env.lookupVar(id)  ;
            }
        }
        public Value visit(CPP.Absyn.EPreDecr p , Env env)
        {
            //System.out.println ("EPreDecr");
            String id = getId(p.exp_ , env);
            Value v = env.lookupVar(id) ;
            if(v.isInt())
            {
                env.setVar(id , new Value.IntValue(v.getInt() - 1));
                return env.lookupVar(id)  ;
            }
            else
            {
                env.setVar(id , new Value.DoubleValue(v.getDouble() - 1.0));
                return env.lookupVar(id)  ;
            }

        }
        public Value visit(CPP.Absyn.ETimes p , Env env)
        {
           // System.out.println ("ETimes");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
                return new Value.IntValue( t1.getInt() * t2.getInt()) ; 
            else
                return new Value.DoubleValue( t1.getDouble() * t2.getDouble()); 
        }
        public Value visit(CPP.Absyn.EDiv p , Env env)
        {
           // System.out.println ("EDiv");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
                return new Value.IntValue( t1.getInt() / t2.getInt()) ;
            else
                return new Value.DoubleValue( t1.getDouble() / t2.getDouble());
        }
        public Value visit(CPP.Absyn.EPlus p , Env env)
        {
            //System.out.println ("EPlus");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
                return new Value.IntValue( t1.getInt() + t2.getInt()) ;
            else
                return new Value.DoubleValue( t1.getDouble() + t2.getDouble());
        }
        public Value visit(CPP.Absyn.EMinus p , Env env)
        {
            //System.out.println ("EMinus");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
                return new Value.IntValue( t1.getInt() - t2.getInt()) ;
            else
                return new Value.DoubleValue( t1.getDouble() - t2.getDouble());
        }
        public Value visit(CPP.Absyn.ELt p , Env env)
        {
            //System.out.println ("ELt");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                if(t1.getInt() < t2.getInt())
                    return new Value.IntValue(1) ; 
                else
                    return new Value.IntValue(0) ; 
            }
            else
            {
                if(t1.getDouble() < t2.getDouble())
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }                
        }
        public Value visit(CPP.Absyn.EGt p , Env env)
        {
            //System.out.println ("EGt");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                if(t1.getInt() > t2.getInt())
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() > t2.getDouble())
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }


        }
        public Value visit(CPP.Absyn.ELtEq p , Env env)
        {
            //System.out.println ("ELtEq");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                if(t1.getInt() <= t2.getInt())
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() <= t2.getDouble())
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

        }
        public Value visit(CPP.Absyn.EGtEq p , Env env)
        {
            //System.out.println ("EGtEq");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                if(t1.getInt() >= t2.getInt())
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() >= t2.getDouble())
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

        }
        public Value visit(CPP.Absyn.EEq p , Env env)
        {
            //System.out.println ("EEq");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                if(t1.getInt() == t2.getInt())
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() == t2.getDouble())
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

        }
        public Value visit(CPP.Absyn.ENEq p , Env env)
        {
           // System.out.println ("ENEq");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                if(t1.getInt() != t2.getInt())
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() != t2.getDouble())
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

        }
        public Value visit(CPP.Absyn.EAnd p , Env env)
        {
           // System.out.println ("EAnd");
            Value t1 = exeExp(p.exp_1 , env);
            if(t1.isInt())
            {
                if(t1.getInt() == 0)
                    return new Value.IntValue(0) ; 
                Value t2 = exeExp(p.exp_2 , env);
                if(t1.getInt() == 1  && t2.getInt() == 1)
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() == 0.0)
                    return new Value.IntValue(0) ;
                Value t2 = exeExp(p.exp_2 , env);
                if(t1.getDouble() == 1.0 && t2.getDouble() == 1.0)
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

            
        }
        public Value visit(CPP.Absyn.EOr p , Env env)
        {
           // System.out.println ("EOr");
            Value t1 = exeExp(p.exp_1 , env);
            if(t1.isInt())
            {
                if(t1.getInt() == 1)
                    return new Value.IntValue(1) ;
                Value t2 = exeExp(p.exp_2 , env);

                if(t1.getInt() == 1 || t2.getInt() == 1)
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                if(t1.getDouble() == 1.0)
                    return new Value.IntValue(1) ;
                Value t2 = exeExp(p.exp_2 , env);
                if(t1.getDouble() == 1.0 || t2.getDouble() == 1.0)
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

        }
        public Value visit(CPP.Absyn.EAss p , Env env)
        {
           // System.out.println ("EAss");
            String id = getId(p.exp_1 , env);
            System.out.println(id);
            Value v = exeExp(p.exp_2 , env);
            env.setVar(id , v);
            return v ;
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
            return TypeCode.Type_bool ; 
        }
        public TypeCode visit(Type_int t, Object arg)
        {
            return TypeCode.Type_int ;
        }
        public TypeCode visit(Type_double t, Object arg)
        {
            return TypeCode.Type_double ;
        }
        public TypeCode visit(Type_void t, Object arg)
        {
            return TypeCode.Type_void ;
        }

    }
   
}
