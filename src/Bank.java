import java.io.*;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
    private final Transaction FINAL_TRANS = new Transaction(-1,0,0);
    private static int START_MONEY = 1000;
    private static int ACCOUNT_NUM = 20;
    private final int THREAD_NUM;

    private ArrayBlockingQueue<Transaction> transactions;
    private Account accs[];
    private CountDownLatch cdLatch;

    public Bank(int threads) {
        THREAD_NUM = threads;
        accs = new Account[ACCOUNT_NUM];
        for(int i = 0; i<accs.length; i++){
            accs[i] = new Account(i, START_MONEY);
        }
        transactions = new ArrayBlockingQueue<>(THREAD_NUM);
        cdLatch = new CountDownLatch(threads);
        //Initialize Workers
        for(int i = 0; i < THREAD_NUM; i++){
            Worker wrkr = new Worker();
            wrkr.start();
        }
    }

    public static void main(String[] args){
        BufferedReader reader = fileReader(args[0]);
        int workers = Integer.parseInt(args[1]);
        Bank bank = new Bank(workers);
        bank.readFile(reader);
        System.out.println(bank.result());
    }

    private String result(){
        String res = "";
        try {
            cdLatch.await();
            for(int i = 0; i<accs.length; i++){
                res += accs[i].toString() + "\n";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static BufferedReader fileReader(String file) {
        File fl = new File(file);
        try {
            FileReader fr = new FileReader(fl);
            return new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readFile(BufferedReader reader){
        while(true){
            String readLine = null;
            try {
                readLine = reader.readLine();
                if(readLine == null) break;
                StringTokenizer tk = new StringTokenizer(readLine);
                int arr[] = new int[3];
                int i = 0;
                while(tk.hasMoreTokens()){
                    String tmp = tk.nextToken();
                    String[] split = tmp.split(":");
                    arr[i] = Integer.parseInt(split[0]);
                    i++;
                }
                Transaction trans = new Transaction(arr[0], arr[1], arr[2]);
                transactions.put(trans);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i<THREAD_NUM; i++){
            try {
                transactions.put(FINAL_TRANS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* Worker inner class which extends Thread
     * We just make transactions untill we reach FINAL_TRANS
     * and then we countDown the latch.
     */
    public class Worker extends Thread {
        @Override
        public void run() {
            try {
                while (true){
                    Transaction trans = transactions.take();
                    if(!trans.equals(FINAL_TRANS)){
                        accs[trans.getFromId()].withdraw(trans.getMoney());
                        accs[trans.getToId()].transfer(trans.getMoney());
                    } else {
                        cdLatch.countDown();
                        break;
                    }
                }
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
        }
    }
}
