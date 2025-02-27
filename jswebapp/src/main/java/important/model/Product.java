package important.model;

public class Product {

    private int prodId;
    private String prodName;
    private double price;

    public Product(int i, String s, double v) {
        this.prodId = i;
        this.prodName = s;
        this.price = v;
    }

    public double getPrice() {
        return price;
    }

    public String getProdName() {
        return prodName;
    }

    public int getProdId() {
        return prodId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "prodId=" + prodId +
                ", prodName='" + prodName + '\'' +
                ", price=" + price +
                '}';
    }
}
