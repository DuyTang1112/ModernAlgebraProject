import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Diffie_Hellman_KeyExchange {
    public static BigInteger two=new BigInteger("2");
    public static BigInteger zero=new BigInteger("0");
    public static BigInteger one=new BigInteger("1");
    public static BigInteger three=new BigInteger("3");

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        BigInteger prime=new BigInteger      ("7243469162906133262707138361729247674528418426076702186281286038623238274842547507072974617594640311");
        BigInteger generator=new BigInteger  ("3242736143229285405697273596419677873912657748731448981302390864459158863881443495029809033284732127");
        try{
            System.out.println("Enter Alice's secret key:");
            BigInteger AliceSecret=new BigInteger(in.next());
            while(AliceSecret.compareTo(zero)<=0 ||AliceSecret.compareTo(prime)>=0){
                System.out.println("Enter again Alice's secret key:");
                AliceSecret=new BigInteger(in.next());
            }
            System.out.println("Enter Bob's secret key:");
            BigInteger BobSecret=new BigInteger(in.next());
            while(BobSecret.compareTo(zero)<=0 ||BobSecret.compareTo(prime)>=0){
                System.out.println("Enter again Bob's secret key:");
                BobSecret=new BigInteger(in.next());
            }

            BigInteger AliceSendsBob=generator.modPow(AliceSecret,prime);
            BigInteger BobSendsAlice=generator.modPow(BobSecret,prime);
            BigInteger AliceCompute=BobSendsAlice.modPow(AliceSecret,prime);
            BigInteger BobCompute=AliceSendsBob.modPow(BobSecret,prime);
            System.out.println("Prime: "+prime);
            System.out.println("Generator: "+generator);
            System.out.println("Alice's secret: "+AliceSecret);
            System.out.println("Bob's secret: "+BobSecret);
            System.out.println(ANSI_RED+"Alice computes: "+AliceCompute+ANSI_RESET);
            System.out.println(ANSI_RED+"Bob computes:   "+BobCompute+ANSI_RESET);
        }catch (Exception e){
            System.out.println(e.getMessage());
            main(args);
        }

    }


}

