package models;

import java.util.ArrayList;

public class Customer {
    private final String id;
    private ArrayList<Account> accounts = new ArrayList<>();

    public Customer(String id, ArrayList<Account> accounts) {
        this.id = id;
        if(accounts != null)
            this.accounts = accounts;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(Account account : accounts){
            sb.append("Account");
            sb.append(1);
            sb.append(account.toString());
            sb.append("\n");
        }
        return
                "id=" + id +
                "\naccounts=" + sb.toString() + ", ";
    }
}
