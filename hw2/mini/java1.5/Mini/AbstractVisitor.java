package Mini;
import Mini.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* Program */
    public R visit(Mini.Absyn.Prog p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Mini.Absyn.Program p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Stm */
    public R visit(Mini.Absyn.SDecl p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.SAss p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.SBlock p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.SPrint p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Mini.Absyn.Stm p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Exp */
    public R visit(Mini.Absyn.EVar p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.EInt p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.EDouble p, A arg) { return visitDefault(p, arg); }

    public R visit(Mini.Absyn.ETyped p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.EAdd p, A arg) { return visitDefault(p, arg); }

    public R visitDefault(Mini.Absyn.Exp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* Type */
    public R visit(Mini.Absyn.TInt p, A arg) { return visitDefault(p, arg); }
    public R visit(Mini.Absyn.TDouble p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(Mini.Absyn.Type p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
