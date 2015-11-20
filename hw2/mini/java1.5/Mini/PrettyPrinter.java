package Mini;
import Mini.Absyn.*;

public class PrettyPrinter
{
  //For certain applications increasing the initial size of the buffer may improve performance.
  private static final int INITIAL_BUFFER_SIZE = 128;
  //You may wish to change the parentheses used in precedence.
  private static final String _L_PAREN = new String("(");
  private static final String _R_PAREN = new String(")");
  //You may wish to change render
  private static void render(String s)
  {
    if (s.equals("{"))
    {
       buf_.append("\n");
       indent();
       buf_.append(s);
       _n_ = _n_ + 2;
       buf_.append("\n");
       indent();
    }
    else if (s.equals("(") || s.equals("["))
       buf_.append(s);
    else if (s.equals(")") || s.equals("]"))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals("}"))
    {
       _n_ = _n_ - 2;
       backup();
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals(","))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals(";"))
    {
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals("")) return;
    else
    {
       buf_.append(s);
       buf_.append(" ");
    }
  }


  //  print and show methods are defined for each category.
  public static String print(Mini.Absyn.Program foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Mini.Absyn.Program foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Mini.Absyn.ListStm foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Mini.Absyn.ListStm foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Mini.Absyn.Stm foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Mini.Absyn.Stm foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Mini.Absyn.Exp foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Mini.Absyn.Exp foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(Mini.Absyn.Type foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(Mini.Absyn.Type foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  /***   You shouldn't need to change anything beyond this point.   ***/

  private static void pp(Mini.Absyn.Program foo, int _i_)
  {
    if (foo instanceof Mini.Absyn.Prog)
    {
       Mini.Absyn.Prog _prog = (Mini.Absyn.Prog) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_prog.liststm_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Mini.Absyn.ListStm foo, int _i_)
  {
     for (java.util.Iterator<Stm> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), 0);
       if (it.hasNext()) {
         render("");
       } else {
         render("");
       }
     }
  }

  private static void pp(Mini.Absyn.Stm foo, int _i_)
  {
    if (foo instanceof Mini.Absyn.SDecl)
    {
       Mini.Absyn.SDecl _sdecl = (Mini.Absyn.SDecl) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sdecl.type_, 0);
       pp(_sdecl.ident_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.SAss)
    {
       Mini.Absyn.SAss _sass = (Mini.Absyn.SAss) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_sass.ident_, 0);
       render("=");
       pp(_sass.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.SBlock)
    {
       Mini.Absyn.SBlock _sblock = (Mini.Absyn.SBlock) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_sblock.liststm_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.SPrint)
    {
       Mini.Absyn.SPrint _sprint = (Mini.Absyn.SPrint) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("print");
       pp(_sprint.exp_, 0);
       render(";");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Mini.Absyn.Exp foo, int _i_)
  {
    if (foo instanceof Mini.Absyn.EVar)
    {
       Mini.Absyn.EVar _evar = (Mini.Absyn.EVar) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_evar.ident_, 0);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.EInt)
    {
       Mini.Absyn.EInt _eint = (Mini.Absyn.EInt) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_eint.integer_, 0);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.EDouble)
    {
       Mini.Absyn.EDouble _edouble = (Mini.Absyn.EDouble) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_edouble.double_, 0);
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.ETyped)
    {
       Mini.Absyn.ETyped _etyped = (Mini.Absyn.ETyped) foo;
       if (_i_ > 1) render(_L_PAREN);
       pp(_etyped.type_, 0);
       render("(");
       pp(_etyped.exp_, 0);
       render(")");
       if (_i_ > 1) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.EAdd)
    {
       Mini.Absyn.EAdd _eadd = (Mini.Absyn.EAdd) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_eadd.exp_1, 0);
       render("+");
       pp(_eadd.exp_2, 1);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(Mini.Absyn.Type foo, int _i_)
  {
    if (foo instanceof Mini.Absyn.TInt)
    {
       Mini.Absyn.TInt _tint = (Mini.Absyn.TInt) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("int");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof Mini.Absyn.TDouble)
    {
       Mini.Absyn.TDouble _tdouble = (Mini.Absyn.TDouble) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("double");
       if (_i_ > 0) render(_R_PAREN);
    }
  }


  private static void sh(Mini.Absyn.Program foo)
  {
    if (foo instanceof Mini.Absyn.Prog)
    {
       Mini.Absyn.Prog _prog = (Mini.Absyn.Prog) foo;
       render("(");
       render("Prog");
       render("[");
       sh(_prog.liststm_);
       render("]");
       render(")");
    }
  }

  private static void sh(Mini.Absyn.ListStm foo)
  {
     for (java.util.Iterator<Stm> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(Mini.Absyn.Stm foo)
  {
    if (foo instanceof Mini.Absyn.SDecl)
    {
       Mini.Absyn.SDecl _sdecl = (Mini.Absyn.SDecl) foo;
       render("(");
       render("SDecl");
       sh(_sdecl.type_);
       sh(_sdecl.ident_);
       render(")");
    }
    if (foo instanceof Mini.Absyn.SAss)
    {
       Mini.Absyn.SAss _sass = (Mini.Absyn.SAss) foo;
       render("(");
       render("SAss");
       sh(_sass.ident_);
       sh(_sass.exp_);
       render(")");
    }
    if (foo instanceof Mini.Absyn.SBlock)
    {
       Mini.Absyn.SBlock _sblock = (Mini.Absyn.SBlock) foo;
       render("(");
       render("SBlock");
       render("[");
       sh(_sblock.liststm_);
       render("]");
       render(")");
    }
    if (foo instanceof Mini.Absyn.SPrint)
    {
       Mini.Absyn.SPrint _sprint = (Mini.Absyn.SPrint) foo;
       render("(");
       render("SPrint");
       sh(_sprint.exp_);
       render(")");
    }
  }

  private static void sh(Mini.Absyn.Exp foo)
  {
    if (foo instanceof Mini.Absyn.EVar)
    {
       Mini.Absyn.EVar _evar = (Mini.Absyn.EVar) foo;
       render("(");
       render("EVar");
       sh(_evar.ident_);
       render(")");
    }
    if (foo instanceof Mini.Absyn.EInt)
    {
       Mini.Absyn.EInt _eint = (Mini.Absyn.EInt) foo;
       render("(");
       render("EInt");
       sh(_eint.integer_);
       render(")");
    }
    if (foo instanceof Mini.Absyn.EDouble)
    {
       Mini.Absyn.EDouble _edouble = (Mini.Absyn.EDouble) foo;
       render("(");
       render("EDouble");
       sh(_edouble.double_);
       render(")");
    }
    if (foo instanceof Mini.Absyn.ETyped)
    {
       Mini.Absyn.ETyped _etyped = (Mini.Absyn.ETyped) foo;
       render("(");
       render("ETyped");
       sh(_etyped.type_);
       sh(_etyped.exp_);
       render(")");
    }
    if (foo instanceof Mini.Absyn.EAdd)
    {
       Mini.Absyn.EAdd _eadd = (Mini.Absyn.EAdd) foo;
       render("(");
       render("EAdd");
       sh(_eadd.exp_1);
       sh(_eadd.exp_2);
       render(")");
    }
  }

  private static void sh(Mini.Absyn.Type foo)
  {
    if (foo instanceof Mini.Absyn.TInt)
    {
       Mini.Absyn.TInt _tint = (Mini.Absyn.TInt) foo;
       render("TInt");
    }
    if (foo instanceof Mini.Absyn.TDouble)
    {
       Mini.Absyn.TDouble _tdouble = (Mini.Absyn.TDouble) foo;
       render("TDouble");
    }
  }


  private static void pp(Integer n, int _i_) { buf_.append(n); buf_.append(" "); }
  private static void pp(Double d, int _i_) { buf_.append(d); buf_.append(" "); }
  private static void pp(String s, int _i_) { buf_.append(s); buf_.append(" "); }
  private static void pp(Character c, int _i_) { buf_.append("'" + c.toString() + "'"); buf_.append(" "); }
  private static void sh(Integer n) { render(n.toString()); }
  private static void sh(Double d) { render(d.toString()); }
  private static void sh(Character c) { render(c.toString()); }
  private static void sh(String s) { printQuoted(s); }
  private static void printQuoted(String s) { render("\"" + s + "\""); }
  private static void indent()
  {
    int n = _n_;
    while (n > 0)
    {
      buf_.append(" ");
      n--;
    }
  }
  private static void backup()
  {
     if (buf_.charAt(buf_.length() - 1) == ' ') {
      buf_.setLength(buf_.length() - 1);
    }
  }
  private static void trim()
  {
     while (buf_.length() > 0 && buf_.charAt(0) == ' ')
        buf_.deleteCharAt(0); 
    while (buf_.length() > 0 && buf_.charAt(buf_.length()-1) == ' ')
        buf_.deleteCharAt(buf_.length()-1);
  }
  private static int _n_ = 0;
  private static StringBuilder buf_ = new StringBuilder(INITIAL_BUFFER_SIZE);
}

