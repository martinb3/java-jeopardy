package yaml.parser;


@SuppressWarnings( "all" )
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
//        BufferedReader in = new BufferedReader(new FileReader("yml/testArrayInJavaBean_minimal.yml"));
//        ScannerImpl scanner = new ScannerImpl(in);
//        ParserImpl parser = new ParserImpl(scanner);
//        
//        for(final Iterator iter = parser.eachEvent();iter.hasNext();) {
//            System.out.println(iter.next().getClass().getName());
//        }
//        while (it.hasNext()){
//            System.out.println(it.next());
//        }
        String[] lines = "one\n".split("\n");
        System.out.println(lines.length);
    }

}
