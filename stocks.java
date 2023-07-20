package com.example;

import java.util.*;

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<Integer, Order> orders = new HashMap<>();
    private static int orderIdCounter = 1;

    private static List<Stock> stocks = new ArrayList<>();

    static {
    
        stocks.add(new Stock("Reliance", "RELIANCE", 1451.50));
        stocks.add(new Stock("LNT", "LARSEN", 923.90));
        stocks.add(new Stock("Tata Steel", "TATASTEEL", 435.60));
    }

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("Main Menu:");
            System.out.println("1. Place order");
            System.out.println("2. Order summary");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    placeOrder();
                    break;
                case 2:
                    displayOrderSummary();
                    break;
                case 3:
                    System.out.println("Exiting the application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    private static void placeOrder() {
        System.out.println("Available Stocks:");
        for (int i = 0; i < stocks.size(); i++) {
            System.out.println((i + 1) + ". " + stocks.get(i));
        }

        System.out.print("Select stock (enter number): ");
        int stockChoice = scanner.nextInt();
        scanner.nextLine();

        if (stockChoice < 1 || stockChoice > stocks.size()) {
            System.out.println("Invalid stock selection. Please try again.");
            return;
        }

        Stock selectedStock = stocks.get(stockChoice - 1);

        System.out.println("Selected stock: " + selectedStock.getName() + " - " + selectedStock.getCurrentPrice());

        System.out.print("Select action (buy/sell): ");
        String action = scanner.nextLine();

        if (!action.equalsIgnoreCase("buy") && !action.equalsIgnoreCase("sell")) {
            System.out.println("Invalid action. Please enter 'buy' or 'sell'.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity <= 0) {
            System.out.println("Quantity must be a positive integer.");
            return;
        }

        double orderAmount = selectedStock.getCurrentPrice() * quantity;
        double brokerCommission = action.equalsIgnoreCase("buy") ? orderAmount * 0.005 : orderAmount * 0.01;

        int orderNumber = generateOrderNumber();

        Order order = new Order(orderNumber, selectedStock, action, quantity, orderAmount, brokerCommission);
        orders.put(orderNumber, order);

        System.out.println("Your order no." + orderNumber + ", thanks for placing the order.");
    }

    private static int generateOrderNumber() {
    
        return orderIdCounter++;
    }

    private static void displayOrderSummary() {
        System.out.print("Enter order number: ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine();

        Order order = orders.get(orderNumber);
        if (order == null) {
            System.out.println("Order not found.");
        } else {
            System.out.println("Order Summary:");
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Stock Name: " + order.getStock().getName());
            System.out.println("Action: " + order.getAction());
            System.out.println("Quantity: " + order.getQuantity());
            System.out.println("Order Amount: " + order.getOrderAmount());
            System.out.println("Broker Commission: " + order.getBrokerCommission());
        }
    }

    private static class Stock {
        private String name;
        private String symbol;
        private double currentPrice;

        public Stock(String name, String symbol, double currentPrice) {
            this.name = name;
            this.symbol = symbol;
            this.currentPrice = currentPrice;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }

        @Override
        public String toString() {
            return name + " (" + symbol + ") - " + currentPrice;
        }
    }

    private static class Order {
        private int orderId;
        private Stock stock;
        private String action;
        private int quantity;
        private double orderAmount;
        private double brokerCommission;

        public Order(int orderId, Stock stock, String action, int quantity, double orderAmount, double brokerCommission) {
            this.orderId = orderId;
            this.stock = stock;
            this.action = action;
            this.quantity = quantity;
            this.orderAmount = orderAmount;
            this.brokerCommission = brokerCommission;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public Stock getStock() {
            return stock;
        }

        public void setStock(Stock stock) {
            this.stock = stock;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }

        public double getBrokerCommission() {
            return brokerCommission;
        }

        public void setBrokerCommission(double brokerCommission) {
            this.brokerCommission = brokerCommission;
        }
    }
}
