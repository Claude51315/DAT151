
//----------------------------------------------------
// The following code was generated by CUP v0.10k
// Wed Nov 18 22:07:42 CET 2015
//----------------------------------------------------

package calc;


/** CUP v0.10k generated parser.
  * @version Wed Nov 18 22:07:42 CET 2015
  */
public class parser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public parser() {super();}

  /** Constructor which sets the default scanner. */
  public parser(java_cup.runtime.Scanner s) {super(s);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\011\000\002\002\004\000\002\003\005\000\002\003" +
    "\005\000\002\003\003\000\002\004\005\000\002\004\005" +
    "\000\002\004\003\000\002\005\003\000\002\005\005" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\017\000\006\007\004\011\007\001\002\000\006\007" +
    "\004\011\007\001\002\000\014\002\ufffb\004\ufffb\005\ufffb" +
    "\006\ufffb\010\ufffb\001\002\000\014\002\ufffe\004\ufffe\005" +
    "\015\006\014\010\ufffe\001\002\000\014\002\ufffa\004\ufffa" +
    "\005\ufffa\006\ufffa\010\ufffa\001\002\000\006\002\012\004" +
    "\011\001\002\000\006\007\004\011\007\001\002\000\004" +
    "\002\001\001\002\000\014\002\000\004\000\005\015\006" +
    "\014\010\000\001\002\000\006\007\004\011\007\001\002" +
    "\000\006\007\004\011\007\001\002\000\014\002\ufffd\004" +
    "\ufffd\005\ufffd\006\ufffd\010\ufffd\001\002\000\014\002\ufffc" +
    "\004\ufffc\005\ufffc\006\ufffc\010\ufffc\001\002\000\006\004" +
    "\011\010\021\001\002\000\014\002\ufff9\004\ufff9\005\ufff9" +
    "\006\ufff9\010\ufff9\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\017\000\010\003\007\004\005\005\004\001\001\000" +
    "\010\003\017\004\005\005\004\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\006\004\012\005\004\001\001\000\002\001\001\000\002" +
    "\001\001\000\004\005\016\001\001\000\004\005\015\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}



  public calc.Absyn.Exp pExp() throws Exception
  {
	java_cup.runtime.Symbol res = parse();
	return (calc.Absyn.Exp) res.value;
  }

public <B,A extends java.util.LinkedList<? super B>> A cons_(B x, A xs) { xs.addFirst(x); return xs; }

public void syntax_error(java_cup.runtime.Symbol cur_token)
{
	report_error("Syntax Error, trying to recover and continue parse...", cur_token);
}

public void unrecovered_syntax_error(java_cup.runtime.Symbol cur_token) throws java.lang.Exception
{
	throw new Exception("Unrecoverable Syntax Error");
}


}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$parser$actions {
  private final parser parser;

  /** Constructor */
  CUP$parser$actions(parser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$parser$do_action(
    int                        CUP$parser$act_num,
    java_cup.runtime.lr_parser CUP$parser$parser,
    java.util.Stack            CUP$parser$stack,
    int                        CUP$parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$parser$result;

      /* select the action based on the action number */
      switch (CUP$parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // Exp2 ::= _SYMB_3 Exp _SYMB_4 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_2 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT = p_2; 
              CUP$parser$result = new java_cup.runtime.Symbol(3/*Exp2*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // Exp2 ::= _INTEGER_ 
            {
              calc.Absyn.Exp RESULT = null;
		Integer p_1 = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = new calc.Absyn.EInt(p_1); 
              CUP$parser$result = new java_cup.runtime.Symbol(3/*Exp2*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // Exp1 ::= Exp2 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_1 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = p_1; 
              CUP$parser$result = new java_cup.runtime.Symbol(2/*Exp1*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // Exp1 ::= Exp1 _SYMB_2 Exp2 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_1 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		calc.Absyn.Exp p_3 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = new calc.Absyn.EDiv(p_1,p_3); 
              CUP$parser$result = new java_cup.runtime.Symbol(2/*Exp1*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // Exp1 ::= Exp1 _SYMB_1 Exp2 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_1 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		calc.Absyn.Exp p_3 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = new calc.Absyn.EMul(p_1,p_3); 
              CUP$parser$result = new java_cup.runtime.Symbol(2/*Exp1*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // Exp ::= Exp1 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_1 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = p_1; 
              CUP$parser$result = new java_cup.runtime.Symbol(1/*Exp*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // Exp ::= Exp _SYMB_0 Exp1 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_1 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		calc.Absyn.Exp p_3 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = new calc.Absyn.ESub(p_1,p_3); 
              CUP$parser$result = new java_cup.runtime.Symbol(1/*Exp*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // Exp ::= Exp _SYMB_0 Exp1 
            {
              calc.Absyn.Exp RESULT = null;
		calc.Absyn.Exp p_1 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		calc.Absyn.Exp p_3 = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT = new calc.Absyn.EAdd(p_1,p_3); 
              CUP$parser$result = new java_cup.runtime.Symbol(1/*Exp*/, RESULT);
            }
          return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= Exp EOF 
            {
              Object RESULT = null;
		calc.Absyn.Exp start_val = (calc.Absyn.Exp)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		RESULT = start_val;
              CUP$parser$result = new java_cup.runtime.Symbol(0/*$START*/, RESULT);
            }
          /* ACCEPT */
          CUP$parser$parser.done_parsing();
          return CUP$parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

