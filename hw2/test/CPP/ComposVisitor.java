package CPP;
import CPP.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  CPP.Absyn.Exp.Visitor<CPP.Absyn.Exp,A>
{
/* Exp */
    public Exp visit(CPP.Absyn.EAdd p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new CPP.Absyn.EAdd(exp_1, exp_2);
    }
    public Exp visit(CPP.Absyn.ESub p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new CPP.Absyn.ESub(exp_1, exp_2);
    }
    public Exp visit(CPP.Absyn.EMul p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new CPP.Absyn.EMul(exp_1, exp_2);
    }
    public Exp visit(CPP.Absyn.EDiv p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new CPP.Absyn.EDiv(exp_1, exp_2);
    }
    public Exp visit(CPP.Absyn.EInt p, A arg)
    {
      Integer integer_ = p.integer_;

      return new CPP.Absyn.EInt(integer_);
    }

}