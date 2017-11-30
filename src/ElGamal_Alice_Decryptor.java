import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElGamal_Alice_Decryptor {
    static BigInteger prime=new BigInteger      ("7243469162906133262707138361729247674528418426076702186281286038623238274842547507072974617594640311");
    static BigInteger generator=new BigInteger  ("3242736143229285405697273596419677873912657748731448981302390864459158863881443495029809033284732127");
    public static BigInteger zero=new BigInteger("0");
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static PublicInfo publicInfo=new PublicInfo(prime,generator);
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.println("Enter your secret key:");
        BigInteger AliceSecret=new BigInteger(in.next());
        while(AliceSecret.compareTo(zero)<=0 ||AliceSecret.compareTo(publicInfo.prime)>=0){
            System.out.println("Enter again your secret key:");
            AliceSecret=new BigInteger(in.next());
        }
        in.nextLine();
        BigInteger AliceSendsBob=publicInfo.generator.modPow(AliceSecret,prime);
        System.out.println(ANSI_BLUE+"Please send to someone this public key to encrypt their messages: \n"+AliceSendsBob+ANSI_RESET);
        System.out.println("Post the cipher pair here: ");
        String cipher=in.nextLine();
        String[] cipherList=new String[2];
        int k=0;
        Pattern pattern = Pattern.compile("\\w+([0-9]+)\\w+([0-9]+)");
        Matcher matcher = pattern.matcher(cipher);
        for(int i = 0 ; i < matcher.groupCount(); i++) {
            matcher.find();
            //System.out.println(matcher.group());
            if (k<2)
                cipherList[k++]=matcher.group();
        }

        CipherPair cipherPair=new CipherPair(new BigInteger(cipherList[0]),new BigInteger(cipherList[1]));
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
    public static BigInteger decrypt(PublicInfo publicInfo, BigInteger AliceSecret, CipherPair cipherPair){
        BigInteger sharedSecret=cipherPair.c1.modPow(AliceSecret,publicInfo.prime);
        BigInteger sharedSecretInverse=sharedSecret.modInverse(publicInfo.prime);
        System.out.println(ANSI_PURPLE+"Computing the shared secret.....\n: "+sharedSecret+ANSI_RESET);
        BigInteger message=cipherPair.c2.multiply(sharedSecretInverse).mod(publicInfo.prime);
        return message;
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
