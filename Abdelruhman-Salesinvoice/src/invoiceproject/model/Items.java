
package invoiceproject.model;


public class Items {
    private String item;
    private double price;
    private int count;
    private Invoice invoice;

    public Items() {
    }

        
    public Items(String item, double price, int count, Invoice invoice) {        
        this.item = item;
        this.price = price;
        this.count = count;
        this.invoice = invoice;
    }
        
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItems() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Invoice getInvoice() {
        return invoice;
    }
    
    public double getItemTotal(){
    return price*count;
    }

    @Override
    public String toString() {
        return "Items{" + "num=" + invoice.getNum() + ", item=" + item + ", price=" + price + ", count=" + count + '}';
    }
    
    public String getAsCSV() {
        return invoice.getNum() + "," + item + "," + price + "," + count;
    }
}
