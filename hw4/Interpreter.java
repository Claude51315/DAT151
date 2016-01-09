import Fun.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;  
import java.util.Scanner ; 
import java.util.Iterator;
import java.util.Set;
public class Interpreter {
    //  Scanner for reading from standard input 
    public int flag = 0 ; 
    public class Closure
    {
        public Exp exp ; 
        public HashMap<String , Closure> sub;
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
        Closure(Exp e , HashMap<String , Closure > sub)
        {
            this.exp = e ; 
            this.sub = new HashMap<String , Closure>(sub); 
        }
        public void replaceClosure( HashMap<String , Closure> sub)
        {
            this.sub = new HashMap<String , Closure>(sub)  ;
        }
        public void replaceExp(Exp e )
        {
            this.exp = e ; 
        }
        public boolean isSubEmpty()
        {
            return sub.isEmpty(); 
        }
        public void addClosure(String name , Closure c)
        {
            sub.put(name  , c ) ; 
        }
        public Integer getVal()
        {
            if(exp instanceof EInt)
            {
                EInt i = (EInt) exp ; 
                return i.integer_ ; 
            }
            else
                throw new RuntimeException("the exp is not an integer");
        }
        public void printSub()
        {
            Iterator<String> it = sub.keySet().iterator();
            while(it.hasNext())
            {
                System.out.print(it.next() + " ") ; 
            }
            System.out.print("\n");
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
                
                    Closure tmp = new Closure(desugaring);
                    mainCl.addClosure(dd.ident_ , tmp );
                }
                else
                {
                    Closure tmp = new Closure(dd.exp_);
                    mainCl.addClosure(dd.ident_ , tmp ); 
                }
            }
            else // add main exp 
            {
                mainCl.replaceExp(dd.exp_); 
                mainCl.addClosure(dd.ident_ , new Closure(dd.exp_));
            }
        }
        if(mainCl.exp == null)
            throw new RuntimeException("no main function ");
        Closure ansCl  = eval(mainCl);
        Integer ans = eval(ansCl).getVal(); 
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
            
            //System.out.println("Evar ");     
            Closure c = sub.get(p.ident_) ;
            if(c == null)
            {
                throw new RuntimeException("bounding error");
            }
            else
            {
                if(c.isSubEmpty())
                    c.replaceClosure(sub);
                return c ;     
            }
         
        }
        public Closure visit(Fun.Absyn.EInt p , HashMap<String , Closure> sub)
        {
            //System.out.println("EInt");
            Closure result = new Closure((Exp) p ) ; 
            return result ;         
        }
        public Closure visit(Fun.Absyn.EApp p , HashMap<String , Closure> sub)
        {
            //System.out.println("EApp");
            if(flag == 0 ) // call by value
            {
                Closure f = p.exp_1.accept(this, sub);
                EAbs fabs = (EAbs) f.exp ; 
                Closure a = p.exp_2.accept(this, sub) ; 
                f.sub.put(fabs.ident_ , a ) ;
                Closure result = fabs.exp_.accept(this , f.sub) ; 
                f.sub.remove(fabs.ident_);
                return result ; 
            }
            else  // call by name
            {
                Closure f = p.exp_1.accept(this,  sub);
                EAbs fabs = (EAbs) f.exp ; 
                f.sub.put(fabs.ident_ , new Closure(p.exp_2 , sub)); 
                Closure result = fabs.exp_.accept(this, f.sub);
                f.sub.remove(fabs.ident_);
                return result; 
            }
        }
        public Closure visit(Fun.Absyn.EAdd p , HashMap<String , Closure> sub)
        {
            //System.out.println("EAdd");
            Closure c1 = p.exp_1.accept(this, sub);
            Closure c2 = p.exp_2.accept(this, sub);
            if( c1.exp instanceof EInt  && c2.exp instanceof EInt)
            {    
                Integer ans = c1.getVal() + c2.getVal(); 
                EInt tmp = new EInt(ans) ; 
                Exp tmp2 = tmp; 
                Closure result = new Closure(tmp2);
                return result;         
            }
            else
            {
                throw new RuntimeException("cannot apply +");
            }
        }
        public Closure visit(Fun.Absyn.ESub p , HashMap<String , Closure> sub)
        {
            //System.out.println("ESub");
            Closure c1 = p.exp_1.accept(this, sub);
            Closure c2 = p.exp_2.accept(this, sub);
            if( c1.exp instanceof EInt  && c2.exp instanceof EInt)
            {
                Integer ans = c1.getVal() - c2.getVal();
                EInt tmp = new EInt(ans) ;
                Exp tmp2 = tmp;
                Closure result = new Closure(tmp2);
                return result;
            }
            else
            {
                throw new RuntimeException("cannot apply -");
            }
        }
        public Closure visit(Fun.Absyn.ELt p , HashMap<String , Closure> sub)
        {
            //System.out.println("ELt");
            Closure c1 = p.exp_1.accept(this, sub);
            Closure c2 = p.exp_2.accept(this, sub);
            
            Integer ans = 0 ;
            if(c1.getVal() < c2.getVal())
                ans = 1;
            else
                ans = 0 ; 
            EInt tmp = new EInt(ans) ;
            Exp tmp2 = tmp;
            Closure result = new Closure(tmp2);
            return result;
        }
        public Closure visit(Fun.Absyn.EIf p , HashMap<String , Closure> sub)
        {
            //System.out.println("EIf");
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
            
            //System.out.println("EAbs");
            Exp exp = (Exp) p ; 
            Closure result = new Closure(exp);
            result.replaceClosure(sub) ; 
            return result ; 
        }
    }
    
}
