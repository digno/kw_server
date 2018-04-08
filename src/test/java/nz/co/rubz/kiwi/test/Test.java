package nz.co.rubz.kiwi.test;

//public class Test
//{
//    static boolean foo(char c)
//    {
//        System.out.print(c);
//        return true;
//    }
//    public static void main( String[] argv )
//    {
//        int i = 0;
//        for ( foo('A'); foo('B') && (i < 2); foo('C'))
//        {
//            i++ ;
//            foo('D');
//        }
//    }
//}
class A{
    public A(String str){
         
    }
}
public class Test{
    public static void main(String[] args) {
        A classa=new A("he");
        A classb=new A("he");
        System.out.println(classa==classb);
        
        byte b1=1,b2=2,b3,b6; 
        final byte b4=4,b5=6; 
        b6=b4+b5; 
//        b3=(b1+b2); 
//        System.out.println(b3+b6);
    }
}