package s4.B193373; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
 package s4.specification;
 public interface InformationEstimatorInterface{
 void setTarget(byte target[]); // set the data for computing the information quantities
 void setSpace(byte space[]); // set data for sample space to computer probability
 double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
 // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
 // The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
 // Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
 // Otherwise, estimation of information quantity,
 }
 */

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency
    
    byte [] subBytes(byte [] x, int start, int end) {
        // corresponding to substring of String for  byte[] ,
        // It is not implement in class library because internal structure of byte[] requires copy.
        byte [] result = new byte[end - start];
        for(int i = 0; i<end-start; i++) { result[i] = x[start + i]; };
        return result;
    }
    
    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }
    
    public void setTarget(byte [] target) { myTarget = target;}
    public void setSpace(byte []space) {
        myFrequencer = new Frequencer();
        mySpace = space;
        myFrequencer.setSpace(space);
    }
    
    
    // 改良前
    /*
     public double estimation(){
     boolean [] partition = new boolean[myTarget.length+1];
     int np;
     np = 1<<(myTarget.length-1);
     // System.out.println("np="+np+" length="+myTarget.length);
     double value = Double.MAX_VALUE; // value = mininimum of each "value1".
     
     for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
     // binary representation of p forms partition.
     // for partition {"ab" "cde" "fg"}
     // a b c d e f g   : myTarget
     // T F T F F T F T : partition:
     partition[0] = true; // I know that this is not needed, but..
     for(int i=0; i<myTarget.length -1;i++) {
     partition[i+1] = (0 !=((1<<i) & p));
     }
     partition[myTarget.length] = true;
     
     // Compute Information Quantity for the partition, in "value1"
     // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
     double value1 = (double) 0.0;
     int end = 0;
     int start = end;
     while(start<myTarget.length) {
     // System.out.write(myTarget[end]);
     end++;;
     while(partition[end] == false) {
     // System.out.write(myTarget[end]);
     end++;
     }
     // System.out.print("("+start+","+end+")");
     myFrequencer.setTarget(subBytes(myTarget, start, end));
     value1 = value1 + iq(myFrequencer.frequency());
     start = end;
     }
     // System.out.println(" "+ value1);
     
     // Get the minimal value in "value"
     if(value1 < value) value = value1;
     }
     return value;
     }
     */
    
    // 改良後
    /*
     public double estimation(){
     double value = Double.MAX_VALUE; // value = mininimum of each "value1".
     // 先頭から、文字列の最後まで for文で回す
     for(int p = 0; p < myTarget.length; p++){
     double value_min = (double) 0.0;
     if(myTarget.length > 1){   // 文字列が2文字以上からなるとき
     myFrequencer.setTarget(subBytes(myTarget, 0, p)); // 0番地からp番地まで
     value_min += iq(myFrequencer.frequency());  // 情報量の算出
     }
     myFrequencer.setTarget(subBytes(myTarget, p, myTarget.length)); // p番地から最後まで
     value_min += iq(myFrequencer.frequency());  // 情報量の算出
     if(value_min < value) value = value_min;    // 最小値の判定
     }
     return value;
     }
     */
    
    // ペアプログラミング
    
    public double estimation(){
        double value = Double.MAX_VALUE; // value = mininimum of each "value1".
        
        // Compute Information Quantity for the partition, in "value1"
        // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
        int end = 0;;
        int start = end;
        end = myTarget.length;
        
        double[] list = new double[myTarget.length];
        if(myTarget.length == 0)    return 0.0;
        else{
            for(int i = 0; i < myTarget.length; i++){
                if(i == 0){
                    myFrequencer.setTarget(subBytes(myTarget, 0, 1));
                    list[i] = iq(myFrequencer.frequency());
                }else{
                    double tmp = Double.MAX_VALUE;
                    myFrequencer.setTarget(subBytes(myTarget, start, i+1));
                    list[i] = iq(myFrequencer.frequency());
                    
                    for(int j=i;j>0;j--){
                        myFrequencer.setTarget(subBytes(myTarget, j, i+1));
                        tmp = list[j-1] + iq(myFrequencer.frequency());
                        if(list[i]>tmp)list[i] = tmp;
                    }
                }
            }
            // Get the minimal value in "value"
            if(list[myTarget.length - 1] < value) value = list[myTarget.length - 1];
        }
        return value;
    }
    
    
    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        myObject = new InformationEstimator();
        myObject.setSpace("dkfngv.jjfsvghsdlchgjnvmc.kdnghmvscjknghfsvghsdlchmc.kdnghmvscjkgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmcf.nvg.hsmfdgnfvmfngv.jvdgfchs,mcdn.fngv.jvhsfmc.kdnghmvscjkcmnjdfngv.jamfngv.jcfdnsvm,,,fk.xmcffsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vnghmvscjknghfsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdfngv.jn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmcf.nvg.hsmfdgnfvmvdgfchs,mcdn.vhsffngv.jcmnjdfngv.jamcfdnsmc.kdnghmvscjkvm,,,fk.xmhsfcmnjdfngv.nvg.hsmfdgnfvfchs,mcdn.vhsfngv.jfcmnjdfngv.jamcfdnsvm,,,fk.xmcf.nvg.hsfngv.jmfdgnfvmvdgfchsfngv.j,mcdn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmcffsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vnghmvscjknghfsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vhsfcmnjdfngv.jamcfdnsfngv.jvm,mc.kdnghmvscjk,,fngv.jfk.xmcf.nvg.hsmfdgnfvmvdgfchs,mcdn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmhsfcmnjdfngmc.kdnghmvscjkv.nvg.h".getBytes());
        myObject.setTarget("dkfngv.jjfsvghsdlchgjnvmc.kdnghmvscjknghfsvghsdlchmc.kdnghmvscjkgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmcf.nvg.hsmfdgnfvmfngv.jvdgfchs,mcdn.fngv.jvhsfmc.kdnghmvscjkcmnjdfngv.jamfngv.jcfdnsvm,,,fk.xmcffsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vnghmvscjknghfsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdfngv.jn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmcf.nvg.hsmfdgnfvmvdgfchs,mcdn.vhsffngv.jcmnjdfngv.jamcfdnsmc.kdnghmvscjkvm,,,fk.xmhsfcmnjdfngv.nvg.hsmfdgnfvfchs,mcdn.vhsfngv.jfcmnjdfngv.jamcfdnsvm,,,fk.xmcf.nvg.hsfngv.jmfdgnfvmvdgfchsfngv.j,mcdn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmcffsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vnghmvscjknghfsvghsdlchgjnvmc.kdnghmvscjknghmvdgfchs,mcdn.vhsfcmnjdfngv.jamcfdnsfngv.jvm,mc.kdnghmvscjk,,fngv.jfk.xmcf.nvg.hsmfdgnfvmvdgfchs,mcdn.vhsfcmnjdfngv.jamcfdnsvm,,,fk.xmhsfcmnjdfngmc.kdnghmvscjkv.nvg.".getBytes());
        value = myObject.estimation();
        System.out.println(">長文 "+value);
        
        
        /*myObject.setTarget("01".getBytes());
         value = myObject.estimation();
         System.out.println(">01 "+value);
         myObject.setTarget("0123".getBytes());
         value = myObject.estimation();
         System.out.println(">0123 "+value);
         myObject.setTarget("00".getBytes());
         value = myObject.estimation();
         System.out.println(">00 "+value);
         */
        
        /*
         int c;
         c = 0;
         try {
         FrequencerInterface myObject;
         int freq;
         c = 0;
         System.out.println("checking Frequencer");
         myObject = new Frequencer();
         freq = myObject.frequency();
         if(-1 != freq) { System.out.println("frequency() should return -1, when target is not set, but returns"+freq); c++; }
         myObject = new Frequencer();
         myObject.setTarget("".getBytes());
         freq = myObject.frequency();
         if(-1 != freq) { System.out.println("frequency() should return -1, when target is empty, but return"+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAA".getBytes());
         if(-1 != freq) { System.out.println("frequency() for AAA should return -1, when target is not set.But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAA".getBytes());
         myObject.setTarget("".getBytes());
         freq = myObject.frequency();
         if(-1 != freq) { System.out.println("frequency() for AAA should return -1, when taget empty string.But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setTarget("A".getBytes());
         freq = myObject.frequency();
         if(0 != freq) { System.out.println("frequency() for not set, should return 0, when taget is not empty.But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("".getBytes());
         myObject.setTarget("A".getBytes());
         freq = myObject.frequency();
         if(0 != freq) { System.out.println("frequency() for empty space, should return 0, when taget is not empty. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAA".getBytes());
         myObject.setTarget("A".getBytes());
         freq = myObject.frequency();
         if(3 != freq) { System.out.println("frequency() for AAA, should return 3, when taget is A. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAA".getBytes());
         myObject.setTarget("AA".getBytes());
         freq = myObject.frequency();
         if(2 != freq) { System.out.println("frequency() for AAA, should return 2, when taget is AA. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAA".getBytes());
         myObject.setTarget("AAA".getBytes());
         freq = myObject.frequency();
         if(1 != freq) { System.out.println("frequency() for AAA, should return 1, when taget is AAA. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAA".getBytes());
         myObject.setTarget("AAAA".getBytes());
         freq = myObject.frequency();
         if(0 != freq) { System.out.println("frequency() for AAA, should return 0, when taget is AAAA. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("Hi Ho Hi Ho".getBytes());
         myObject.setTarget("H".getBytes());
         freq = myObject.frequency();
         if(4 != freq) {System.out.println("frequency() for Hi_Ho_Hi_Ho, should return 4, when taget is H. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("Hi Ho Hi Ho".getBytes());
         myObject.setTarget("Ho".getBytes());
         freq = myObject.frequency();
         if(2 != freq) {System.out.println("frequency() for Hi_Ho_Hi_Ho, should return 2, when taget is Ho. But it returns "+freq); c++; }
         /* please note subByteFreqency(0,0) is considered illeagal specification, and you should not include
         this case */
        /*
         myObject = new Frequencer();
         myObject.setSpace("AAAB".getBytes());
         myObject.setTarget("AAAAB".getBytes());
         freq = myObject.subByteFrequency(0,1);
         if(3 != freq) { System.out.println("SubBytefrequency() for AAAB, should return 3, when taget is AAAAB[0:1]. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAAB".getBytes());
         myObject.setTarget("AAAAB".getBytes());
         freq = myObject.subByteFrequency(1,2);
         if(3 != freq) { System.out.println("SubBytefrequency() for AAAB, should return 2, when taget is AAAAB[1:2]. But it returns "+freq); c++; }
         if(2 == freq) { System.out.println("You might be confused by the intentional error in sample code."); }
         myObject = new Frequencer();
         myObject.setSpace("AAAB".getBytes());
         myObject.setTarget("AAAAB".getBytes());
         freq = myObject.subByteFrequency(1,3);
         if(2 != freq) { System.out.println("SubBytefrequency() for AAAB, should return 2, when taget is AAAAB[1:3]. But it returns "+freq); c++; }
         myObject = new Frequencer();
         myObject.setSpace("AAAB".getBytes());
         myObject.setTarget("AAAAB".getBytes());
         freq = myObject.subByteFrequency(4,5);
         if(1 != freq) {
         System.out.println("SubBytefrequency() for AAAB, should return 1, when taget is AAAAB[4:5]. But it returns "+freq); c++;
         }
         }
         catch(Exception e) {
         System.out.println("Exception occurred in Frequencer Object: STOP");
         c++;
         }
         try {
         InformationEstimatorInterface myObject;
         double value;
         System.out.println("checking InformationEstimator");
         myObject = new InformationEstimator();
         myObject.setSpace("3210321001230123".getBytes());
         myObject.setTarget("0".getBytes());
         value = myObject.estimation();
         if((value < 1.9999) || (2.0001 <value)) { System.out.println("IQ for 0 in 3210321001230123 should be 2.0. But it returns "+value); c++; }
         myObject.setTarget("01".getBytes());
         value = myObject.estimation();
         if((value < 2.9999) || (3.0001 <value)) { System.out.println("IQ for 01 in 3210321001230123 should be 3.0. But it returns "+value); c++; }
         myObject.setTarget("0123".getBytes());
         value = myObject.estimation();
         if((value < 2.9999) || (3.0001 <value)) { System.out.println("IQ for 0123 in 3210321001230123 should be 3.0. But it returns "+value); c++; }
         myObject.setTarget("00".getBytes());
         value = myObject.estimation();
         if((value < 3.9999) || (4.0001 <value)) { System.out.println("IQ for 00 in 3210321001230123 should be 4.0. But it returns "+value); c++; }
         }
         catch(Exception e) {
         System.out.println("Exception occurred: STOP");
         c++;
         }
         if(c == 0) { System.out.println("TestCase OK"); }
         */
    }
    
}





