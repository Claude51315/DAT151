package CPP;
import CPP.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* Exp */
    public R visit(CPP.Absyn.EAdd p, A arg) { return visitDefault(p, arg); }
    public R visit(CPP.Absyn.ESub p, A arg) { return visitDefault(p, arg); }

    public R visit(CPP.Absyn.EMul p, A arg) { return visitDefault(p, arg); }
    public R visit(CPP.Absyn.EDiv p, A arg) { return visitDefault(p, arg); }

    public R visit(CPP.Absyn.EInt p, A arg) { return visitDefault(p, arg); }

    public R visitDefault(CPP.Absyn.Exp p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
