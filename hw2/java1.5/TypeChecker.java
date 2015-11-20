import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;  

public class TypeChecker {
    public static enum TypeCode 
    {
        Type_int , Type_double , Type_bool , Type_void 
    }
    public static class FunType {
        public LinkedList<TypeCode> args ; 
        public TypeCode returnType; 
    }
    public static class Env {
        //             FuncId  
        public HashMap<String , FunType> signature ; 
        public LinkedList<HashMap<String , TypeCode>> contexts ; 
        public Env() 
        {
            contexts = new LinkedList<HashMap<String , TypeCode>>();
            signature = new HashMap<String , FunType> () ; 
            enterScope(); 
        }
        public TypeCode lookupVar(String id )
        {
            // java iterate a linked list 
            for (HashMap<String , TypeCode> context : contexts)
            {
                TypeCode t = context.get(id) ; 
                if(t != null)
                    return t ; 
            }
            throw new TypeException("There is no variable called [ " + id + "] ");
        }
        public FunType lookupFun (String id)
        {
            FunType t = signature.get(id) ; 
            if (t == null)
                throw new TypeException("There is no [" + id + "] function");
            else
                return t ; 
        }
        public void updateVar (String id , TypeCode Ty )
        {
            // get the current scope of variable , i.e. the first element of the linkedlist contexts
            HashMap<String , TypeCode> cur_scope = contexts.getFirst() ;
            if(cur_scope.containsKey(id))
            {
                TypeCode tmp = cur_scope.get(id) ; 
                if(tmp == Ty )
                {
                    System.out.println("foo");
                }    
                else
                    throw new TypeException("The variable [ " + id + "] has the Type" + Ty + ", not "+ tmp );
            }
            else
                throw new TypeException("There is no variable called [ " + id + "] ");
        }
        public void enterScope()
        {
            contexts.addFirst(new HashMap<String , TypeCode>());
        }
        public void leaveScope()
        {
            contexts.removeFirst(); 
        }
    }

    public void typecheck(Program p) 
    {
        PDefs defs = (PDefs) p ; 
        Env env = new Env() ; 
        for(Def d : defs.listdef_ )
        {
            checkDef(d , env) ; 
        }
        throw new TypeException("Not yetQQQQ a typechecker");
    }
    // check for Defs 
    private void checkDef(Def d , Env env)
    {
        d.accept(new defVisitor() , env);
    }
    private class defVisitor implements Def.Visitor<Object , Env> 
    {
        public Object visit(CPP.Absyn.DFun p, Env env)
        {
            /* Code For DFun Goes Here */
            FunType FT = new FunType() ; 
            TypeCode returnType = getTypeCode(p.type_) ;
            FT.returnType = returnType; 
            FT.args = new LinkedList<TypeCode>() ; 
            //env.signature.put(p.id_ ,)
            //p.type_.accept(new TypeVisitor(), env);
            //p.id_;
            for (Arg x : p.listarg_) 
            {
             
                FT.args.addLast(getTypeCode(((ADecl)x).type_)) ; 
            }
            env.signature.put(p.id_ , FT) ; 
            for (Stm x : p.liststm_) 
            {
                checkStm(x , env);
            }
            return null;
        }                                
    }
    // check for statements 
    private void checkStm (Stm x , Env env)
    {
        x.accept(new StmChecker() , env);
    }
    private class StmChecker implements Stm.Visitor<Object , Env>
    {
        public Object visit(CPP.Absyn.SExp p, Env env)
        {
            /* Code For SExp Goes Here */
            System.out.println("an Exp");
            //p.exp_.accept(new ExpVisitor<R,A>(), arg);

            return null;
        }
        public Object visit(CPP.Absyn.SDecls p, Env env)
        {
            /* Code For SDecls Goes Here */

            System.out.println("an Decl");
            //p.type_.accept(new TypeVisitor<R,A>(), arg);
            //for (String x : p.listid_) {
            //}

            return null;
        }
        public Object visit(CPP.Absyn.SInit p, Env env)
        {
            /* Code For SInit Goes Here */
            System.out.println("an Init");

            //p.type_.accept(new TypeVisitor<R,A>(), arg);
            //p.id_;
            //p.exp_.accept(new ExpVisitor<R,A>(), arg);
            return null;
        }
        public Object visit(CPP.Absyn.SReturn p, Env env)
        {
            /* Code For SReturn Goes Here */

            //p.exp_.accept(new ExpVisitor<R,A>(), arg);

            System.out.println("an return ");
            return null;
        }
        public Object visit(CPP.Absyn.SWhile p, Env env)
        {
            /* Code For SWhile Goes Here */

            System.out.println("an while");
            //p.exp_.accept(new ExpVisitor<R,A>(), arg);
            //p.stm_.accept(new StmVisitor<R,A>(), arg);

            return null;
        }
        public Object visit(CPP.Absyn.SBlock p, Env env)
        {
            /* Code For SBlock Goes Here */
            System.out.println("an block");

            //for (Stm x : p.liststm_) {
            //} 

            return null;
        }
        public Object visit(CPP.Absyn.SIfElse p, Env env)
        {
            /* Code For SIfElse Goes Here */

            //p.exp_.accept(new ExpVisitor<R,A>(), arg);
            //p.stm_1.accept(new StmVisitor<R,A>(), arg);
            //p.stm_2.accept(new StmVisitor<R,A>(), arg);

            System.out.println("an if else");
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






























