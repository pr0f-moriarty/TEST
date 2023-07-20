package com.example;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class MainApp implements InitializingBean {
    private static Scanner scanner = new Scanner(System.in);
    private SessionFactory sessionFactory;

    @Override
    public void afterPropertiesSet() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Connected to the database.");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        mainApp.start();
    }

    private void start() {
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

    private void placeOrder() {
        System.out.println("Available Stocks:");
        List<Stock> stocks = getStockList();
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
        insertOrder(order);

        System.out.println("Your order no." + orderNumber + ", thanks for placing the order.");
    }

    private void displayOrderSummary() {
        System.out.print("Enter order number: ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine();

        Order order = findOrderById(orderNumber);
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

    private List<Stock> getStockList() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Stock", Stock.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching stocks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private int generateOrderNumber() {
        try (Session session = sessionFactory.openSession()) {
            Integer maxOrderId = session.createQuery("SELECT MAX(orderId) FROM Order", Integer.class).uniqueResult();
            return maxOrderId != null ? maxOrderId + 1 : 1;
        } catch (Exception e) {
            System.err.println("Error fetching maximum order ID: " + e.getMessage());
            return 1;
        }
    }

    private void insertOrder(Order order) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Error inserting order: " + e.getMessage());
        }
    }

    private Order findOrderById(int orderId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Order.class, orderId);
        } catch (Exception e) {
            System.err.println("Error finding order: " + e.getMessage());
            return null;
        }
    }

    @Component
    private static class Stock {
        private int stockId;
        private String name;
        private String symbol;
        private double currentPrice;

        public Stock() {
        }

        public Stock(int stockId, String name, String symbol, double currentPrice) {
            this.stockId = stockId;
            this.name = name;
            this.symbol = symbol;
            this.currentPrice = currentPrice;
        }

        public int getStockId() {
            return stockId;
        }

        public void setStockId(int stockId) {
            this.stockId = stockId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(double currentPrice) {
            this.currentPrice = currentPrice;
        }

        @Override
        public String toString() {
            return name + " (" + symbol + ") - " + currentPrice;
        }
    }

    @Component
    private static class Order {
        private int orderId;
        private Stock stock;
        private String action;
        private int quantity;
        private double orderAmount;
        private double brokerCommission;

        public Order() {
        }

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
