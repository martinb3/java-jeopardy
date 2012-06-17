package yaml.parser;
/** Generic parser event API.

    @autor: Rolf Veen
    @date: March 2002
    @license: Open-source compatible TBD (Apache or zlib or Public Domain)
 */

@SuppressWarnings( "all" )
public interface ParserEvent
{
        void event(int i);
        void event(String s);
        void content(String a, String b);
        void property(String a, String b);
        void error(Exception e, int line);
}
