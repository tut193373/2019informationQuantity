package s4.B193373;
import java.lang.*;
import s4.specification.*;


/*package s4.specification;
 ここは、１回、２回と変更のない外部仕様である。
 public interface FrequencerInterface {     // This interface provides the design for frequency counter.
 void setTarget(byte  target[]); // set the data to search.
 void setSpace(byte  space[]);  // set the data to be searched target from.
 int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
 //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
 //Otherwise, get the frequency of TAGET in SPACE
 int subByteFrequency(int start, int end);
 // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
 // For the incorrect value of START or END, the behavior is undefined.
 }
 */



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    
    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。
    
    
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.
    
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.
    
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }
    
    private int suffixCompare(int i, int j) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // Each i and j denote suffix_i, and suffix_j.
        // Example of dictionary order
        // "i"      <  "o"        : compare by code
        // "Hi"     <  "Ho"       ; if head is same, compare the next element
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
        //
        //The return value of "int suffixCompare" is as follows.
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;
        
        // ここにコードを記述せよ
        int count = 0;
        if(i < j)   count = mySpace.length - j;     // iとjの内、大きい方を開始位置とする
        else if(i >= j) count = mySpace.length - i; // i=jならばiを参照する
        for(int k=0; k<count; k++){
            // mySpaceに格納されている文字バイトを比較する
            if(mySpace[i+k] > mySpace[j+k]) return 1;       // i+k番地の要素の方が大きければ1を返す
            else if(mySpace[i+k] < mySpace[j+k]) return -1; // j+k番地の方が大きければ-1
        }
        // i<jならば1、i>jならば-1、i=jならば0を返す
        return i < j ? 1 : i > j ? -1 : 0; // この行は変更しなければいけない。
    }
    
    public void setSpace(byte []space) {
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
        }
        //
        // ここに、int suffixArrayをソートするコードを書け。
        // 順番はsuffixCompareで定義されるものとする。
        
        // バブルソート
        for (int i = 0; i < mySpace.length - 1; i++) {
            for (int j = (mySpace.length - 1); j > i; j--) {
                if (suffixCompare(suffixArray[j-1], suffixArray[j]) == 1) {
                    int temp = suffixArray[j-1];
                    suffixArray[j-1] = suffixArray[j];
                    suffixArray[j] = temp;
                }
            }
        }
        
        /*
        // ヒープソート
        int n = mySpace.length-1;   // 全長
        int parent = n/2-1;       // 親要素(parent以上のインデックスは葉を持たない)
        for(int i = parent; i >= 0; i--){
            // iは現在の参照位置、後々変更するのでposに保持
            int pos = i;
            // 注目要素
            int v = suffixArray[pos];
            while(true){    // 無限ループ
                // 子要素(jが左子分木、j+1が右子分木)
                int j = 2*pos+1;
                // 子要素のインデックスが配列サイズよりも大きい場合、ループを抜け出す
                if (j > n)  break;
                // 子要素が配列の最後尾でない時
                if (j != n) {
                    // 左子分木と右子分木を大小比較する
                    if (suffixCompare(suffixArray[j+1], suffixArray[j]) == 1) {
                        // 常にjに大きい値が入っているようにする
                        j = j+1;
                    }
                }
                // posの方がjよりも大きい(もしくは同じ)とき、ループから抜ける
                if (suffixCompare(suffixArray[pos], suffixArray[j]) != 1)   break;
                // 要素の入れ替え
                int temp = suffixArray[pos];
                suffixArray[pos] = suffixArray[j];
                suffixArray[j] = temp;
                // 現在の参照位置を更新
                pos = j;
            }
            // 現在の参照位置にもともとの参照位置にあった要素を代入
            suffixArray[pos] = v;
        }
         */
    }
    
    // Suffix Arrayを用いて、文字列の頻度を求めるコード
    // ここから、指定する範囲のコードは変更してはならない。
    
    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }
    
    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }
    
    public int subByteFrequency(int start, int end) {
        /* This method be work as follows, but much more efficient
         int spaceLength = mySpace.length;
         int count = 0;
         for(int offset = 0; offset< spaceLength - (end - start); offset++) {
         boolean abort = false;
         for(int i = 0; i< (end - start); i++) {
         if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
         }
         if(abort == false) { count++; }
         }
         */
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。
    
    private int targetCompare(int i, int j, int k) {
        // suffixArrayを探索するときに使う比較関数。
        // 次のように定義せよ
        // suffix_i is a string in mySpace starting at i-th position.
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // comparing suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_j_k, it return 0.
        // The behavior is different from suffixCompare on this case.
        // if suffix_i > target_j_k it return 1;
        // if suffix_i < target_j_k it return -1;
        // It should be used to search the appropriate index of some suffix.
        // Example of search
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        //
        // ここに比較のコードを書け
        for(int pos=0; pos<k-j; pos++){
            // i+posがmySpaceの配列外に出る場合は終了しなければならない
            if(i+pos >= mySpace.length || mySpace[i+pos] < myTarget[j+pos])   return -1;
            else if(mySpace[i+pos] > myTarget[j+pos]) return 1;
        }
        //
        return 0; // この行は変更しなければならない。
    }
    
    
    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
        
        // It returns the index of the first suffix
        // which is equal or greater than target_start_end.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 5.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho ", it will return 6.
        //
        // ここにコードを記述せよ。
        for(int i=0; i<mySpace.length; i++){
            if(targetCompare(suffixArray[i], start, end) == 0)  return i;
        }
        //
        return -1; //このコードは変更しなければならない。
    }
    
    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
        // It returns the index of the first suffix
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".
        //
        //　ここにコードを記述せよ
        for(int i=mySpace.length-1; i>=0; i--){
            if(targetCompare(suffixArray[i], start, end) == 0)  return i+1;
        }
        //
        return -1; // この行は変更しなければならない、
    }
    
    
    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray(); // you may use this line for DEBUG
            /* Example from "Hi Ho Hi Ho"
             0: Hi Ho
             1: Ho
             2: Ho Hi Ho
             3:Hi Ho
             4:Hi Ho Hi Ho
             5:Ho
             6:Ho Hi Ho
             7:i Ho
             8:i Ho Hi Ho
             9:o
             A:o Hi Ho
             */
            
            frequencerObject.setTarget("Hi Ho".getBytes());
            //
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //
            
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

