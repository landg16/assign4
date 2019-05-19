public class Transaction {

    private int fromId;
    private int toId;
    private int money;

    public Transaction(int fromId, int toId, int money){
            this.fromId = fromId;
            this.toId = toId;
            this.money = money;
    }

    public int getFromId(){
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public int getMoney() {
        return money;
    }
}
