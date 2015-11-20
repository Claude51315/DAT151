package calc;
import calc.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  calc.Absyn.Exp.Visitor<calc.Absyn.Exp,A>
{
/* Exp */
    public Exp visit(calc.Absyn.EAdd p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new calc.Absyn.EAdd(exp_1, exp_2);
    }
    public Exp visit(calc.Absyn.ESub p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new calc.Absyn.ESub(exp_1, exp_2);
    }
    public Exp visit(calc.Absyn.EMul p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new calc.Absyn.EMul(exp_1, exp_2);
    }
    public Exp visit(calc.Absyn.EDiv p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new calc.Absyn.EDiv(exp_1, exp_2);
    }
    public Exp visit(calc.Absyn.EInt p, A arg)
    {
      Integer integer_ = p.integer_;

      return new calc.Absyn.EInt(integer_);
    }

}