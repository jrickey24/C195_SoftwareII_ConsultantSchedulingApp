package Model;


public class Customer {

    private int customerId;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private String divisionName;
    private String countryName;

    public Customer (int customerId, String name, String address, String postalCode, String phone,
                 int divisionId, String divisionName, String countryName) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryName = countryName;
    }

    public Customer(){}

    public void setCustomerId(int customerId){this.customerId = customerId;}
    public void setName(String name){this.name = name;}
    public void setAddress(String address){this.address = address;}
    public void setPostalCode(String postalCode){this.postalCode = postalCode;}
    public void setPhone(String phone){this.phone = phone;}
    public void setDivisionId(int divisionId){this.divisionId = divisionId;}
    public void setDivisionName(String divisionName){this.divisionName = divisionName;}
    public void setCountryName(String countryName){this.countryName = countryName;}
    public int getCustomerId(){return customerId;}
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getPostalCode(){return postalCode;}
    public String getPhone(){return phone;}
    public int getDivisionId(){return divisionId;}
    public String getDivisionName(){return divisionName;}
    public String getCountryName(){return countryName;}
}
