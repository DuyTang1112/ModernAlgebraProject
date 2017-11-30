import java.math.BigInteger;
import java.util.Scanner;

public class ElGamal {
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
    public static Scanner in=new Scanner(System.in);
    public static void main(String[] args) {
        int messageLength="7243469162906133262707138361729247674528418426076702186281286038623238274842547507072974617594640311".length()/2;
        System.out.println("Maximum message length = "+ messageLength);
        System.out.println("Maximum key length = "+messageLength*2);
        BigInteger prime=new BigInteger      ("7243469162906133262707138361729247674528418426076702186281286038623238274842547507072974617594640311");
        BigInteger generator=new BigInteger  ("3242736143229285405697273596419677873912657748731448981302390864459158863881443495029809033284732127");
        String message;
        PublicInfo publicInfo=new PublicInfo(prime,generator);
        try{
            System.out.println("Enter message: ");
            message=in.nextLine();
            while (message.length()>messageLength){
                System.out.println("Exceeding maximum length. Try again: ");
                message=in.nextLine();
            }
            System.out.println("Mapped message: "+mapMessage(message));
            //System.out.println("Unmapped message: "+unmapMessage(new BigInteger("7269767679")));
            System.out.println("Enter Alice's secret key:");
            BigInteger AliceSecret=new BigInteger(in.next());
            while(AliceSecret.compareTo(zero)<=0 ||AliceSecret.compareTo(prime)>=0){
                System.out.println("Enter again Alice's secret key:");
                AliceSecret=new BigInteger(in.next());
            }

            BigInteger AliceSendsBob=generator.modPow(AliceSecret,prime);
            System.out.println(ANSI_BLUE+"Alice Sends Bob: "+AliceSendsBob+ANSI_RESET);
            System.out.println("Encrypt the message? y/n: ");
            if (in.next().charAt(0)=='y'){
                CipherPair cipherPair=encrypt(publicInfo,AliceSendsBob,mapMessage(message));
                System.out.println(ANSI_GREEN+"Encrypted from Bob:\n"+ cipherPair+ANSI_RESET);
                System.out.println("Decrypt this cipher text? y/n");
                if (in.next().charAt(0)=='y'){
                    BigInteger decipher=decrypt(publicInfo,AliceSecret,cipherPair);
                    System.out.println(ANSI_RED+"Decrypted message: "+decipher+ANSI_RESET);
                    System.out.println(ANSI_RED+"Unmapped message: "+unmapMessage(decipher)+ANSI_RESET);
                }
                else{
                    System.out.println("Too bad you don't want to see....");
                }
            }
            else{
                System.out.println("Too bad you don't want to see....");
            }

        }catch (Exception e){
            System.err.println(e.getMessage());
            main(args);
        }
    }
    public static BigInteger mapMessage(String s){
        s=s.toUpperCase();
        StringBuilder res=new StringBuilder();
        for (char i:s.toCharArray()){
            res.append(String.valueOf((int)i));
        }
        return new BigInteger(res.toString());
    }

    public static String unmapMessage(BigInteger n){
        String s=n.toString();
        StringBuilder res=new StringBuilder();
        for (int i=0;i<=s.length()-2;i+=2){

            char c=(char)Integer.parseInt(s.substring(i,i+2));
            //System.out.println(c+"");
            res.append(c);
        }
        return res.toString();
    }

    public static CipherPair encrypt(PublicInfo publicInfo,BigInteger fromAlice,BigInteger message){
        System.out.println("Enter Bob's secret key:");
        BigInteger BobSecret=new BigInteger(in.next());
        while(BobSecret.compareTo(zero)<=0 ||BobSecret.compareTo(publicInfo.prime)>=0){
            System.out.println("Enter again Bob's secret key:");
            BobSecret=new BigInteger(in.next());
        }
        BigInteger BobSendsAlice=publicInfo.generator.modPow(BobSecret,publicInfo.prime);
        BigInteger sharedSecret=fromAlice.modPow(BobSecret,publicInfo.prime);
        System.out.println(ANSI_PURPLE+"Bob computes the shared secret: "+sharedSecret+ANSI_RESET);
        BigInteger ciphertext=message.multiply(sharedSecret).mod(publicInfo.prime);
        return new CipherPair(BobSendsAlice,ciphertext);
    }

    public static BigInteger decrypt(PublicInfo publicInfo,BigInteger AliceSecret,CipherPair cipherPair){
        BigInteger sharedSecret=cipherPair.c1.modPow(AliceSecret,publicInfo.prime);
        BigInteger sharedSecretInverse=sharedSecret.modInverse(publicInfo.prime);
        System.out.println(ANSI_PURPLE+"Alice computes the shared secret: "+sharedSecret+ANSI_RESET);
        BigInteger message=cipherPair.c2.multiply(sharedSecretInverse).mod(publicInfo.prime);
        return message;
    }

    public static class CipherPair{
        public BigInteger c1;
        public BigInteger c2;
        public CipherPair(BigInteger c1,BigInteger c2){
            this.c1=c1;
            this.c2=c2;
        }

        @Override
        public String toString() {
            return "("+c1.toString()+", \n"+c2.toString()+" )";
        }
    }

    public static class PublicInfo{
        public BigInteger prime;
        public BigInteger generator;
        public PublicInfo(BigInteger p, BigInteger g){
            prime=p;
            generator=g;
        }
    }
}
