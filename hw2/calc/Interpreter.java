package calc;
import java_cup.runtime.*;
import calc.*;
import calc.Absyn.*;
import java.io.*;


public class Interpreter {
  public Integer eval(Exp e ) {
    return e.accept(new Value() , null) ; 
  }
  private class Value implements Exp. Visitor<Integer , Object>{
    public Integer visit (EAdd p , Object arg){
      return eval(p.exp_1) + eval(p.exp_2) ; 
    }
    public Integer visit (ESub p , Object arg){
      return eval(p.exp_1) - eval(p.exp_2) ;
    }
    public Integer visit (EMul p , Object arg){
      return eval(p.exp_1) * eval(p.exp_2) ;
    }
    public Integer visit (EDiv p , Object arg){
      return eval(p.exp_1) / eval(p.exp_2) ;
    }
    public Integer visit (EInt p , Object arg){
      return p.integer_ ;
    }

  }
}
