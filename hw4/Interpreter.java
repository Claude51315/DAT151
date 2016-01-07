import Fun.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;  
import java.util.Scanner ; 
import java.util.Iterator;
public class Interpreter {
    //  Scanner for reading from standard input 
    public Scanner Scan = new Scanner(System.in);
    public HashMap<String , Closure>  Funs ;
    public int flag = 0 ; 
    public class FunDef 
    {
        private LinkedList<String> args; 
        private Exp funBody ; 
        FunDef(LinkedList<String> args , Exp funBody)
        {
            this.args = args ; 
            this.funBody = funBody ; 
        }
    }
    public class Closure
    {
        private Exp exp ; 
        HashMap<String , Closure> sub;
        Closure(Exp e )
        {
            this.exp = e ;
            sub = new HashMap<String , Closure>(); 
        }
        Closure()
        {
            exp = null ;
            sub = new HashMap<String , Closure>();
        }
        public void addExp(Exp e )
        {
            this.exp = e ; 
        }
        public void addClosure(String name , Closure c)
        {
            sub.put(name  , c ) ; 
        }
        public Integer getVal()
        {
            EInt i = (EInt) exp ; 
            return i.integer_ ; 
        }
    }
    private abstract class Value
    {
        public boolean isInt()
        {
            return false ; 
        }
        public Integer getInt()
        {
            throw new RuntimeException(this + " is not an Integer");
        }
        public String print()
        {
            if(this.isInt())
                return this.toString();
            else
                return this.toString();
        }
        public class IntValue extends Value 
        {
            private Integer i ; 
            public IntValue(Integer i ){this.i = i ; }
            public boolean isInt(){return true ; }
            public Integer getInt(){ return i ;}
            public String toString(){return i.toString();}
        }
    }
    public class Env 
    {
        public HashMap<String , FunDef> signature ; 
        public LinkedList<HashMap<String , Value>> contexts ; 
        public Env() 
        {
            contexts = new LinkedList<HashMap<String ,Value>>() ; 
            signature = new HashMap<String , FunDef>() ; 
        }
    }
    // entry point of intepreter 
    // method : 0 call by value
    //          1 call by name
    
    public void interpret(Program p , int method ) 
    {
        flag = method ; 

        Prog defs = (Prog) p ; 
        Closure mainCl = new Closure();
        for( Def d : defs.listdef_)
        {
            DDef dd = (DDef) d ; 
            if( !(dd.ident_.equals("main")))
            { 
                // desugaring 
                // example : fst x y = x  ->  fst = /x -> /y -> x 
                //System.out.println(dd.ident_);
                if(dd.listident_.size() > 0 )
                {
                    LinkedList<String> argList = dd.listident_ ; 
                    Iterator<String> it  = argList.descendingIterator();
                    Exp e  = dd.exp_ ; 
                    Exp desugaring = null ; 
                    while(it.hasNext())
                    {
                        String s = it.next();
                        EAbs fabs = new EAbs(s , e );
                        if(it.hasNext())
                        {
                            e = fabs ; 
                        }
                        else
                        {
                            desugaring  = fabs ; 
                            break; 
                        }
                    }
                
                    Closure tmp = new Closure();
                    tmp.addExp(desugaring); 
                    //eval(tmp);
 
                    mainCl.addClosure(dd.ident_ , tmp );
                }
                else
                {
                    Closure tmp = new Closure();
                    tmp.addExp(dd.exp_);
                    mainCl.addClosure(dd.ident_ , tmp ); 
                }
            }
            else // add main exp 
            {
                mainCl.addExp(dd.exp_); 
            }
        }
        Integer ans = eval(mainCl).getVal();
        System.out.println(ans);
    }
    
    public Closure eval(Closure c)
    {
        return  c.exp.accept(new evalExp() , c.sub);

    }
     
    public class evalExp implements Exp.Visitor<Closure , HashMap<String , Closure>>
    {
        public Closure visit(Fun.Absyn.EVar p , HashMap<String , Closure> sub)
        {
            System.out.println("Evar");
           
            
            Closure c = sub.get(p.ident_) ; 
            if(c == null)
                System.out.println("bounding error");
            else
            {
                return c ;     
            }
            return null;
         
        }
        public Closure visit(Fun.Absyn.EInt p , HashMap<String , Closure> sub)
        {
            System.out.println("EInt");
            Closure result = new Closure() ; 
            result.addExp((Exp)p); 
            return result ;         
        
        }
        public Closure visit(Fun.Absyn.EApp p , HashMap<String , Closure> sub)
        {
            System.out.println("EApp");
            
            Closure f = p.exp_1.accept(this, sub);
            EAbs fabs = (EAbs) f.exp ; 
            Closure a = p.exp_2.accept(this, sub) ; 
            f.sub.put(fabs.ident_ , a ) ;
            Closure result = fabs.exp_.accept(this , f.sub) ; 
            f.sub.remove(fabs.ident_);
            return result ; 
            //return null;         
        }
        public Closure visit(Fun.Absyn.EAdd p , HashMap<String , Closure> sub)
        {
            
            System.out.println("EAdd");
            Closure c1 = p.exp_1.accept(this, sub);
            Closure c2 = p.exp_2.accept(this, sub);
            Integer ans = c1.getVal() + c2.getVal(); 
            EInt tmp = new EInt(ans) ; 
            Exp tmp2 = tmp; 
            Closure result = new Closure();
            result.addExp(tmp2);  
            return result;         
        }
        public Closure visit(Fun.Absyn.ESub p , HashMap<String , Closure> sub)
        {
            System.out.println("ESub");
            Closure c1 = p.exp_1.accept(this, sub);
            Closure c2 = p.exp_2.accept(this, sub);
            Integer ans = c1.getVal() - c2.getVal();
            EInt tmp = new EInt(ans) ;
            Exp tmp2 = tmp;
            Closure result = new Closure();
            result.addExp(tmp2);
            return result;

        }
        public Closure visit(Fun.Absyn.ELt p , HashMap<String , Closure> sub)
        {
            System.out.println("ELt");
            Closure c1 = p.exp_1.accept(this, sub);
            Closure c2 = p.exp_2.accept(this, sub);
            Integer ans = 0 ;
            if(c1.getVal() < c2.getVal())
                ans = 1;
            else
                ans = 0 ; 
            EInt tmp = new EInt(ans) ;
            Exp tmp2 = tmp;
            Closure result = new Closure();
            result.addExp(tmp2);
            return result;
        }
        public Closure visit(Fun.Absyn.EIf p , HashMap<String , Closure> sub)
        {
            System.out.println("EIf");
            Closure c1 = p.exp_1.accept(this , sub);
            Closure result = null; 
            if(c1.getVal().equals(1))
                 result = p.exp_2.accept(this , sub );
            else
                result = p.exp_3.accept(this , sub);
            return result;         
        }
        public Closure visit(Fun.Absyn.EAbs p , HashMap<String , Closure> sub)
        {
        
            System.out.println("EAbs");
            Exp exp  = (Exp) p ; 
            Closure tmp = new Closure();
            tmp.addExp(p.exp_);
            sub.put(p.ident_ , tmp) ; 
            Closure result = p.exp_.accept(this , sub ) ; 
            return result;
        }
    }
    
}
