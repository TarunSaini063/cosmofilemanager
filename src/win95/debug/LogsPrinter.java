package win95.debug;

public class LogsPrinter {
    public static void printError(String className,int lineNumber,String message){
        System.out.println("Error occurred in class : "+className+" at line : "+lineNumber);
        System.out.println("------------------------------------------------------------" +
                "           Error Message Start" +
                "------------------------------------------------------------");
        System.out.println(message);
        System.out.println("------------------------------------------------------------" +
                "           Error Message End" +
                "------------------------------------------------------------");
    }
    public static void printLogic(String className,int lineNumber,String message){
        System.out.println("Printing from class : "+className+" at line : "+lineNumber);
        System.out.println("------------------------------------------------------------" +
                "           Message Start" +
                "------------------------------------------------------------");
        System.out.println(message);
        System.out.println("------------------------------------------------------------" +
                "           Message End" +
                "------------------------------------------------------------");
    }
}
