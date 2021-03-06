import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;  
import java.util.Scanner ; 
public class Interpreter {
    //  Scanner for reading from standard input 
    public Scanner Scan = new Scanner(System.in);
    // class for recording execution body of an function 
    public static class FunExe {
        public LinkedList<String> args ;
        public LinkedList<Stm> liststm_; 
        public Value returnValue; 
    }
    
    public static class Env {
        public HashMap<String , FunExe> signature ; 
        public LinkedList<HashMap<String , Value>> contexts ; 
        public Env() 
        {
            contexts = new LinkedList<HashMap<String , Value>>();
            signature = new HashMap<String , FunExe> () ; 
            enterScope(); 
        }
        public Value lookupVar(String id )
        {
            for (HashMap<String , Value> context : contexts)
            {
                Value t = context.get(id) ; 
                if(t != null)
                    return t ; 
            }
            throw new RuntimeException("Unknown variable");
        }
        public FunExe lookupFun (String id)
        {
            FunExe t = signature.get(id) ; 
            if (t == null)
                throw new RuntimeException("There is no [" + id + "] function");
            else
                return t ; 
        }
        // add variable to current scope  with value undefined 
        public void addVar(String id )
        {
            contexts.getFirst().put(id , new Value.Undefined());
        }
        // set a new value to a variable 
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
    // class for Value 
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
    // entry point of intepreter 
    public void interpret(Program p) 
    {
        PDefs defs = (PDefs) p ; 
        Env env = new Env() ; 
        for(Def d : defs.listdef_ )
        {
            // add function execution body except the main function
            checkDef1(d , env) ; 
        }
        
        for(Def d : defs.listdef_)
        {
            // executio main function 
            checkDef2(d , env);
        }
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
                FunExe F = new FunExe() ;     
                F.liststm_ = p.liststm_ ;
                F.args = new LinkedList<String>(); 
                for (Arg x  : p.listarg_)
                {
                    ADecl tmp = (ADecl)x; 
                    F.args.addLast(tmp.id_); 
                }
                env.signature.put(p.id_ , F) ; 
            }
            return null; 
        }                                
    }
    private class mainFunExecuter implements Def.Visitor<Object , Env>
    {
        public Object visit(CPP.Absyn.DFun p , Env env)
        {
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
    // execute statements 
    // only return statement return a Value 
    // others retur null 
    private Value exeStm (Stm s , Env env)
    {
        return  s.accept(new StmExecuter() , env);
    }
    private class StmExecuter implements Stm.Visitor<Value , Env>
    {
        public Value visit(CPP.Absyn.SExp p, Env env)
        {
            /* Code For SExp Goes Here */
            exeExp(p.exp_ , env ) ; 
            return null;
        }
        public Value visit(CPP.Absyn.SDecls p, Env env)
        {
            /* Code For SDecls Goes Here */
            for(String x :p.listid_)
            {
                env.addVar(x);
            }
            return null;
        }
        public Value visit(CPP.Absyn.SInit p, Env env)
        {
            /* Code For SInit Goes Here */
            env.addVar(p.id_);
            env.setVar(p.id_ , exeExp(p.exp_ , env));
            return null;
        }
        public Value visit(CPP.Absyn.SReturn p, Env env)
        {
            /* Code For SReturn Goes Here */
            Value t = exeExp(p.exp_ , env);
            return t ;
        }
        public Value visit(CPP.Absyn.SWhile p, Env env)
        {
            /* Code For SWhile Goes Here */
            while(true)
            {
                Value t = exeExp(p.exp_ , env);
                if(t.isInt())
                {
                    
                    Integer a = t.getInt();
                    if( a.equals(1) )
                    {
                        exeStm(p.stm_ , env);
                    }
                    else
                        break ; 
                }
            }
            return null;
        }
        public Value visit(CPP.Absyn.SBlock p, Env env)
        {
            /* Code For SBlock Goes Here */
            env.enterScope();
            for(Stm s : p.liststm_)
            {
                Value t = exeStm(s , env );
                if(!(t == null)) // encounter a return statement
                {
                    env.leaveScope();
                    return (Value)t ;
                }
            }
            env.leaveScope();
            return null ;
        }
        public Value visit(CPP.Absyn.SIfElse p, Env env)
        {
            /* Code For SIfElse Goes Here */
            Value t = exeExp(p.exp_ , env);
            if(t.isInt())
            {
                Integer a = t.getInt(); 
                if(a.equals(1) )
                {
                    Value o = exeStm(p.stm_1 , env);
                    if(!(o == null))
                    {
                       return (Value)o ;
                    
                    }
                }
                else
                {
                    Value o = exeStm(p.stm_2 , env);
                    if(!(o == null))
                    {
                        return (Value)o;
                    }
                }
             }
            return null;
        }
          
    }
   
    // execute exp
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
            // hardcode the build-in functions
            if(p.id_ == "printInt") 
            {
                Exp x =  p.listexp_.get(0);
                Value v = exeExp(x , env) ; 
                System.out.println(v.toString());
                return null; 
            }
            else if (p.id_ =="printDouble")
            {
                Exp x = p.listexp_.get(0); 
                Value v = exeExp(x , env);
                
                System.out.println(v.toString());
                return null; 
            }
            else if (p.id_ == "readInt")
            {
                Integer x = Scan.nextInt();
                return new Value.IntValue(x);
            }
            else if (p.id_ == "readDouble")
            {
                double x = Scan.nextDouble();
                return new Value.DoubleValue(x);
                //return null; 
            }
            else
            {
                //execute a function call 
                FunExe F = env.lookupFun(p.id_);
                // evaluate the args first 
                LinkedList<Value> tmpList = new LinkedList<Value>(); 
                for(Exp x : p.listexp_)
                {
                    tmpList.addLast(exeExp(x , env));
                }
                // add the arg to the function scope  
                env.enterScope();
                for(String s : F.args )
                {
                    env.addVar(s);
                    env.setVar(s , tmpList.remove());
                
                }
                // execute function 
                for(Stm s : F.liststm_)
                {
                    Value t = exeStm(s , env) ; 
                    if(! (t == null)) // encounter a return statement
                    {
                        env.leaveScope(); 
                        return t ;
                    } 
                }
                env.leaveScope();
                return null;
            }
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
                Integer a = t1.getInt();
                Integer b = t2.getInt();
                if(a.equals(b))
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                Double a = t1.getDouble();
                Double b = t2.getDouble();
                if(a.equals(b))
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }

        }
        public Value visit(CPP.Absyn.ENEq p , Env env)
        {
            //ddSystem.out.println ("ENEq");
            Value t1 = exeExp(p.exp_1 , env);
            Value t2 = exeExp(p.exp_2 , env);
            if(t1.isInt())
            {
                Integer a = t1.getInt(); 
                Integer b = t2.getInt(); 
                if( a.equals(b))
                    return new Value.IntValue(0) ;
                else
                    return new Value.IntValue(1) ;
            }
            else
            {
                Double a = t1.getDouble();
                Double b = t2.getDouble();
                if(a.equals(b))
                    return new Value.IntValue(0);
                else
                    return new Value.IntValue(1);
            }

        }
        public Value visit(CPP.Absyn.EAnd p , Env env)
        {
           // System.out.println ("EAnd");
            Value t1 = exeExp(p.exp_1 , env);
            if(t1.isInt())
            {
                Integer a = t1.getInt();
                if(a.equals(0))
                    return new Value.IntValue(0) ; 
                Value t2 = exeExp(p.exp_2 , env);
                Integer b = t2.getInt();
                if(  b.equals(1))
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                Double a = t1.getDouble();
                if(a.equals(0.0))
                    return new Value.IntValue(0) ;
                Value t2 = exeExp(p.exp_2 , env);
                Double b = t2.getDouble();
                if( b.equals(1.0))
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
                Integer a = t1.getInt();
                if(a.equals(1))
                    return new Value.IntValue(1) ;
                Value t2 = exeExp(p.exp_2 , env);
                Integer b = t2.getInt();
                if(  b.equals(1))
                    return new Value.IntValue(1) ;
                else
                    return new Value.IntValue(0) ;
            }
            else
            {
                Double a = t1.getDouble();
                if(a.equals(1.0))
                    return new Value.IntValue(1) ;
                Value t2 = exeExp(p.exp_2 , env);
                Double b = t2.getDouble();
                if( b.equals(1.0))
                    return new Value.IntValue(1);
                else
                    return new Value.IntValue(0);
            }
        }
        public Value visit(CPP.Absyn.EAss p , Env env)
        {
           // System.out.println ("EAss");
            String id = getId(p.exp_1 , env);
            Value v = exeExp(p.exp_2 , env);
            env.setVar(id , v);
            return v ;
        }
    }
    // function to get iden.  
    // used in pre/post incr/decr and assignment  
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
}
