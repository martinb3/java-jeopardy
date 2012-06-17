package yaml.parser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

@SuppressWarnings( "all" )
public class YamlParserRefEvent implements ParserEvent
{
        PrintStream out;

        public YamlParserRefEvent()
        {
                out = System.out;
        }

        public YamlParserRefEvent(File file) throws Exception
        {
                out = new PrintStream(new FileOutputStream(file));
        }

        public void event(String a)
        {
        }

        public void error(Exception e, int line)
        {
                out.println("error:  "+line);
        }

        public void event(int c)
        {
                switch (c)
                {
                        case YamlParser.MAP_CLOSE:
                            out.println("}"); break;
                        case YamlParser.LIST_CLOSE:
                            out.println("]"); break;
                        case YamlParser.MAP_NO_OPEN:
                            out.println("}-"); break;
                        case YamlParser.LIST_NO_OPEN:
                            out.println("]-"); break;
                        case YamlParser.LIST_OPEN:
                            out.println("["); break;
                        case YamlParser.MAP_OPEN:
                            out.println("{"); break;
                }
        }

        public void content(String a, String b)
        {
                out.println(a + " : " + b);
        }

        public void property(String a, String b)
        {
                out.println(a + " : " + b);
        }
}
