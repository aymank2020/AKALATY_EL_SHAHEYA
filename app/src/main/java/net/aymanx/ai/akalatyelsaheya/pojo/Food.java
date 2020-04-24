package net.aymanx.ai.akalatyelsaheya.pojo;

public class Food {

    private String Name;
    private String Image;
    private String Price;
    private String MenuID;
    private String Discount;
    private String Descrpition;

    public Food() {
    }

    public Food(String name, String image, String price, String menuID, String discount, String descrpition) {
        Name = name;
        Image = image;
        Price = price;
        MenuID = menuID;
        Discount = discount;
        Descrpition = descrpition;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDescrpition() {
        return Descrpition;
    }

    public void setDescrpition(String descrpition) {
        Descrpition = descrpition;
    }
}
