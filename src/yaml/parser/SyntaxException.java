package yaml.parser;
@SuppressWarnings( "all" )
public class SyntaxException extends Exception
{
        public int line;

        public SyntaxException() { super(); }
        public SyntaxException(String s) { super(s); }

        public SyntaxException(String s, int line)
        {
            super(s);
            this.line = line;
        }
}
