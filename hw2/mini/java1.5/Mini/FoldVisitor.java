package Mini;

import Mini.Absyn.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* Program */
    public R visit(Mini.Absyn.Prog p, A arg) {
      R r = leaf(arg);
      for (Stm x : p.liststm_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }

/* Stm */
    public R visit(Mini.Absyn.SDecl p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Mini.Absyn.SAss p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Mini.Absyn.SBlock p, A arg) {
      R r = leaf(arg);
      for (Stm x : p.liststm_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }
    public R visit(Mini.Absyn.SPrint p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }

/* Exp */
    public R visit(Mini.Absyn.EVar p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Mini.Absyn.EInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Mini.Absyn.EDouble p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Mini.Absyn.ETyped p, A arg) {
      R r = leaf(arg);
      r = combine(p.type_.accept(this, arg), r, arg);
      r = combine(p.exp_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(Mini.Absyn.EAdd p, A arg) {
      R r = leaf(arg);
      r = combine(p.exp_1.accept(this, arg), r, arg);
      r = combine(p.exp_2.accept(this, arg), r, arg);
      return r;
    }

/* Type */
    public R visit(Mini.Absyn.TInt p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(Mini.Absyn.TDouble p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
