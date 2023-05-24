package models;

import java.util.ArrayList;

public class Business extends Customer{
    private String name;
    private String CUI;
    private String companyNr;

    public Business(String id,
                    ArrayList<Account> accounts,
                    String name,
                    String CUI,
                    String companyNr) {
        super(id, accounts);
        this.name = name;
        this.CUI = CUI;
        this.companyNr = companyNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCUI() {
        return CUI;
    }

    public void setCUI(String CUI) {
        this.CUI = CUI;
    }

    public String getCompanyNr() {
        return companyNr;
    }

    public void setCompanyNr(String companyNr) {
        this.companyNr = companyNr;
    }

    @Override
    public String toString() {
        return "\nBusiness{" +
                "\n    name=" + name +
                "\n    CUI=" + CUI +
                "\n    companyNr=" + companyNr +
                "\n}";
    }
}
