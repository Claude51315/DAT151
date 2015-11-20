package Mini;
import Mini.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  Mini.Absyn.Program.Visitor<Mini.Absyn.Program,A>,
  Mini.Absyn.Stm.Visitor<Mini.Absyn.Stm,A>,
  Mini.Absyn.Exp.Visitor<Mini.Absyn.Exp,A>,
  Mini.Absyn.Type.Visitor<Mini.Absyn.Type,A>
{
/* Program */
    public Program visit(Mini.Absyn.Prog p, A arg)
    {
      ListStm liststm_ = new ListStm();
      for (Stm x : p.liststm_) {
        liststm_.add(x.accept(this,arg));
      }

      return new Mini.Absyn.Prog(liststm_);
    }

/* Stm */
    public Stm visit(Mini.Absyn.SDecl p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      String ident_ = p.ident_;

      return new Mini.Absyn.SDecl(type_, ident_);
    }
    public Stm visit(Mini.Absyn.SAss p, A arg)
    {
      String ident_ = p.ident_;
      Exp exp_ = p.exp_.accept(this, arg);

      return new Mini.Absyn.SAss(ident_, exp_);
    }
    public Stm visit(Mini.Absyn.SBlock p, A arg)
    {
      ListStm liststm_ = new ListStm();
      for (Stm x : p.liststm_) {
        liststm_.add(x.accept(this,arg));
      }

      return new Mini.Absyn.SBlock(liststm_);
    }
    public Stm visit(Mini.Absyn.SPrint p, A arg)
    {
      Exp exp_ = p.exp_.accept(this, arg);

      return new Mini.Absyn.SPrint(exp_);
    }

/* Exp */
    public Exp visit(Mini.Absyn.EVar p, A arg)
    {
      String ident_ = p.ident_;

      return new Mini.Absyn.EVar(ident_);
    }
    public Exp visit(Mini.Absyn.EInt p, A arg)
    {
      Integer integer_ = p.integer_;

      return new Mini.Absyn.EInt(integer_);
    }
    public Exp visit(Mini.Absyn.EDouble p, A arg)
    {
      Double double_ = p.double_;

      return new Mini.Absyn.EDouble(double_);
    }
    public Exp visit(Mini.Absyn.ETyped p, A arg)
    {
      Type type_ = p.type_.accept(this, arg);
      Exp exp_ = p.exp_.accept(this, arg);

      return new Mini.Absyn.ETyped(type_, exp_);
    }
    public Exp visit(Mini.Absyn.EAdd p, A arg)
    {
      Exp exp_1 = p.exp_1.accept(this, arg);
      Exp exp_2 = p.exp_2.accept(this, arg);

      return new Mini.Absyn.EAdd(exp_1, exp_2);
    }

/* Type */
    public Type visit(Mini.Absyn.TInt p, A arg)
    {

      return new Mini.Absyn.TInt();
    }
    public Type visit(Mini.Absyn.TDouble p, A arg)
    {

      return new Mini.Absyn.TDouble();
    }

}