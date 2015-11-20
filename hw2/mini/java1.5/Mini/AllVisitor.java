package Mini;

import Mini.Absyn.*;

/** BNFC-Generated All Visitor */
public interface AllVisitor<R,A> extends
  Mini.Absyn.Program.Visitor<R,A>,
  Mini.Absyn.Stm.Visitor<R,A>,
  Mini.Absyn.Exp.Visitor<R,A>,
  Mini.Absyn.Type.Visitor<R,A>
{}
