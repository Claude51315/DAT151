package Mini;
import Mini.Absyn.*;
/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class VisitSkel
{
  public class ProgramVisitor<R,A> implements Program.Visitor<R,A>
  {
    public R visit(Mini.Absyn.Prog p, A arg)
    {
      /* Code For Prog Goes Here */

      for (Stm x : p.liststm_) {
      }

      return null;
    }

  }
  public class StmVisitor<R,A> implements Stm.Visitor<R,A>
  {
    public R visit(Mini.Absyn.SDecl p, A arg)
    {
      /* Code For SDecl Goes Here */

      p.type_.accept(new TypeVisitor<R,A>(), arg);
      //p.ident_;

      return null;
    }
    public R visit(Mini.Absyn.SAss p, A arg)
    {
      /* Code For SAss Goes Here */

      //p.ident_;
      p.exp_.accept(new ExpVisitor<R,A>(), arg);

      return null;
    }
    public R visit(Mini.Absyn.SBlock p, A arg)
    {
      /* Code For SBlock Goes Here */

      for (Stm x : p.liststm_) {
      }

      return null;
    }
    public R visit(Mini.Absyn.SPrint p, A arg)
    {
      /* Code For SPrint Goes Here */

      p.exp_.accept(new ExpVisitor<R,A>(), arg);

      return null;
    }

  }
  public class ExpVisitor<R,A> implements Exp.Visitor<R,A>
  {
    public R visit(Mini.Absyn.EVar p, A arg)
    {
      /* Code For EVar Goes Here */

      //p.ident_;

      return null;
    }
    public R visit(Mini.Absyn.EInt p, A arg)
    {
      /* Code For EInt Goes Here */

      //p.integer_;

      return null;
    }
    public R visit(Mini.Absyn.EDouble p, A arg)
    {
      /* Code For EDouble Goes Here */

      //p.double_;

      return null;
    }
    public R visit(Mini.Absyn.EAdd p, A arg)
    {
      /* Code For EAdd Goes Here */

      p.exp_1.accept(new ExpVisitor<R,A>(), arg);
      p.exp_2.accept(new ExpVisitor<R,A>(), arg);

      return null;
    }

  }
  public class TypeVisitor<R,A> implements Type.Visitor<R,A>
  {
    public R visit(Mini.Absyn.TInt p, A arg)
    {
      /* Code For TInt Goes Here */


      return null;
    }
    public R visit(Mini.Absyn.TDouble p, A arg)
    {
      /* Code For TDouble Goes Here */


      return null;
    }

  }
}