package win95.debug;

public class ExceptionPrinter {
    public static void print(String className,int lineNumber,String message){
        System.out.println("Exception occurred in class : "+className+" at line : "+lineNumber);
        System.out.println("------------------------------------------------------------" +
                "           Message Start" +
                "------------------------------------------------------------");
        System.out.println(message);
        System.out.println("------------------------------------------------------------" +
                "           Message End" +
                "------------------------------------------------------------");

    }
}
