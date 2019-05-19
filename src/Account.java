public class Account {
    private int id;
    private int balance;
    private int transNum;
    public Account(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public synchronized int getTransNum() {
        return transNum;
    }

    public synchronized void transfer(int money) {
        balance += money;
        transNum++;
    }

    public synchronized void withdraw(int money) {
        balance -= money;
        transNum++;
    }

    @Override
    public String toString() {
        return "acct:"+id+" bal:"+balance+" trans:"+transNum;
    }
}
