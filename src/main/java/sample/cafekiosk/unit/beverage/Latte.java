package sample.cafekiosk.unit.beverage;

public class Latte implements Beverage{
    @Override
    public String getName() {
        return "latte";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
