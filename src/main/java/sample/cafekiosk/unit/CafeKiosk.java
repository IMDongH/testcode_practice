package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    private static final LocalTime ShopOpenTime = LocalTime.of(10, 0);
    private static final LocalTime ShopCloseTime = LocalTime.of(22, 0);
    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(Beverage beverage, int count) {
        if (count <= 0)
            throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");

        for (int i = 0; i < count; i++)
            beverages.add(beverage);
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void removeAll() {
        beverages.clear();
    }

    //    public int calculateTotalPrice() {
//        return beverages.stream().mapToInt(Beverage::getPrice).sum();
//    }
    public int calculateTotalPrice() {
        return beverages.stream().mapToInt(Beverage::getPrice).sum();
    }

    public Order createOrder() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (currentTime.isBefore(ShopOpenTime) || currentTime.isAfter(ShopCloseTime))
            throw new IllegalArgumentException("주문시간이 아닙니다. 관리자에게 문의하세요");

        return new Order(currentDateTime, beverages);
    }

    public Order createOrder(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (currentTime.isBefore(ShopOpenTime) || currentTime.isAfter(ShopCloseTime))
            throw new IllegalArgumentException("주문시간이 아닙니다. 관리자에게 문의하세요");

        return new Order(currentDateTime, beverages);
    }


}
