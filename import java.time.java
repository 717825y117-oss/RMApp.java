import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 1. Order class
class Order {
    private int orderId;
    private String buyer;
    private List<String> items;
    private double totalAmount;
    private LocalDate purchaseDate;
    private static int orderCount = 0;

    public Order(int orderId, String buyer, List<String> items,
                 double totalAmount, LocalDate purchaseDate) {
        this.orderId = orderId;
        this.buyer = buyer;
        this.items = new ArrayList<String>(items);
        this.totalAmount = totalAmount;
        this.purchaseDate = purchaseDate;
        orderCount++;
    }

    public int getOrderId() { return orderId; }
    public String getBuyer() { return buyer; }

    public List<String> getItems() {
        return new ArrayList<String>(items);
    }

    public double getTotalAmount() { return totalAmount; }
    public LocalDate getPurchaseDate() { return purchaseDate; }

    public static int getOrderCount() { return orderCount; }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount >= 0) {
            this.totalAmount = totalAmount;
        }
    }

    public boolean containsItem(String item) {
        return items.contains(item);
    }

    public void displayOrder() {
        System.out.println("Order ID: " + orderId
                + ", Buyer: " + buyer
                + ", Items: " + items
                + ", Amount: Rs." + String.format("%.2f", totalAmount)
                + ", Purchase Date: " + purchaseDate);
    }
}

// 2. ReturnRequest class
class ReturnRequest {
    private int rmaId;
    private int orderId;
    private String reason;
    private String condition;
    private String status;
    private List<String> photos;
    private LocalDate requestDate;
    private static int requestCount = 0;

    public ReturnRequest(int rmaId, int orderId, String reason,
                         String condition, List<String> photos) {
        this.rmaId = rmaId;
        this.orderId = orderId;
        this.reason = reason;
        this.condition = condition;
        this.photos = new ArrayList<String>(photos);
        this.status = "REQUESTED";
        this.requestDate = LocalDate.now();
        requestCount++;
    }

    public int getRmaId() { return rmaId; }
    public int getOrderId() { return orderId; }
    public String getReason() { return reason; }
    public String getCondition() { return condition; }
    public String getStatus() { return status; }

    public List<String> getPhotos() {
        return new ArrayList<String>(photos);
    }

    public LocalDate getRequestDate() { return requestDate; }

    public static int getRequestCount() { return requestCount; }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean approve() {
        if (!status.equals("REQUESTED")) {
            return false;
        }
        status = "APPROVED";
        return true;
    }

    public boolean deny() {
        if (!status.equals("REQUESTED")) {
            return false;
        }
        status = "DENIED";
        return true;
    }

    public boolean markRefunded() {
        if (!status.equals("APPROVED")) {
            return false;
        }
        status = "REFUNDED";
        return true;
    }

    public void displayRequest() {
        System.out.println("RMA ID: " + rmaId
                + ", Order ID: " + orderId
                + ", Reason: " + reason
                + ", Condition: " + condition
                + ", Status: " + status);
    }
}

// 3. Refund base class
class Refund {
    private int refundId;
    private int rmaId;
    private String method;
    private double amount;
    private LocalDate processedDate;
    private boolean processed;
    private static int refundCount = 0;

    public Refund(int refundId, int rmaId, String method, double amount) {
        this.refundId = refundId;
        this.rmaId = rmaId;
        this.method = method;
        this.amount = amount;
        this.processedDate = null;
        this.processed = false;
        refundCount++;
    }

    public int getRefundId() { return refundId; }
    public int getRmaId() { return rmaId; }
    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public LocalDate getProcessedDate() { return processedDate; }
    public boolean isProcessed() { return processed; }

    public static int getRefundCount() {
        return refundCount;
    }

    public void setAmount(double amount) {
        if (!processed && amount >= 0) {
            this.amount = amount;
        }
    }

    protected void completeProcessing() {
        processed = true;
        processedDate = LocalDate.now();
    }

    public void processRefund() {
        if (processed) {
            System.out.println("Refund " + refundId
                    + " already processed.");
            return;
        }

        completeProcessing();

        System.out.println("Refund " + refundId
                + " processed using " + method
                + " for Rs." + String.format("%.2f", amount));
    }

    public void displayRefund() {
        System.out.println("Refund ID: " + refundId
                + ", RMA ID: " + rmaId
                + ", Method: " + method
                + ", Amount: Rs." + String.format("%.2f", amount)
                + ", Date: " + processedDate);
    }

    public boolean isValidAmount() {
        return amount > 0;
    }
}

// 4. CardRefund class
class CardRefund extends Refund {
    private String cardLast4;
    private String bankReference;
    private double processingFee;
    private String cardNetwork;
    private static int cardRefundCount = 0;

    public CardRefund(int refundId, int rmaId, double amount,
                      String cardLast4, String bankReference,
                      double processingFee, String cardNetwork) {
        super(refundId, rmaId, "CARD", amount);
        this.cardLast4 = cardLast4;
        this.bankReference = bankReference;
        this.processingFee = processingFee;
        this.cardNetwork = cardNetwork;
        cardRefundCount++;
    }

    public String getCardLast4() { return cardLast4; }
    public String getBankReference() { return bankReference; }
    public double getProcessingFee() { return processingFee; }
    public String getCardNetwork() { return cardNetwork; }

    public static int getCardRefundCount() {
        return cardRefundCount;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    @Override
    public void processRefund() {
        if (isProcessed()) {
            System.out.println("Refund " + getRefundId()
                    + " already processed.");
            return;
        }

        completeProcessing();

        System.out.println("Card refund processed: Rs."
                + String.format("%.2f", getAmount())
                + " to " + cardNetwork
                + " card ending " + cardLast4
                + ", Reference: " + bankReference);
    }
}

// 5. WalletRefund class
class WalletRefund extends Refund {
    private String walletProvider;
    private String walletAccount;
    private String transactionId;
    private boolean cashbackEligible;
    private static int walletRefundCount = 0;

    public WalletRefund(int refundId, int rmaId, double amount,
                        String walletProvider, String walletAccount,
                        String transactionId, boolean cashbackEligible) {
        super(refundId, rmaId, "WALLET", amount);
        this.walletProvider = walletProvider;
        this.walletAccount = walletAccount;
        this.transactionId = transactionId;
        this.cashbackEligible = cashbackEligible;
        walletRefundCount++;
    }

    public String getWalletProvider() { return walletProvider; }
    public String getWalletAccount() { return walletAccount; }
    public String getTransactionId() { return transactionId; }

    public boolean isCashbackEligible() {
        return cashbackEligible;
    }

    public static int getWalletRefundCount() {
        return walletRefundCount;
    }

    public void setWalletAccount(String walletAccount) {
        this.walletAccount = walletAccount;
    }

    @Override
    public void processRefund() {
        if (isProcessed()) {
            System.out.println("Refund " + getRefundId()
                    + " already processed.");
            return;
        }

        completeProcessing();

        System.out.println("Wallet refund processed: Rs."
                + String.format("%.2f", getAmount())
                + " to " + walletProvider
                + " account " + walletAccount
                + ", Transaction: " + transactionId);
    }
}

// 6. RMAService class
class RMAService {
    private List<Order> orders;
    private List<ReturnRequest> requests;
    private List<Refund> refunds;
    private int nextRmaId;
    private static int serviceCount = 0;

    public RMAService() {
        orders = new ArrayList<Order>();
        requests = new ArrayList<ReturnRequest>();
        refunds = new ArrayList<Refund>();
        nextRmaId = 1001;
        serviceCount++;
    }

    public static int getServiceCount() {
        return serviceCount;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public ReturnRequest createRMA(int orderId, String reason,
                                   String condition) {
        return createRMA(orderId, reason, condition,
                new ArrayList<String>());
    }

    public ReturnRequest createRMA(int orderId, String reason,
                                   String condition, List<String> photos) {
        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order " + orderId + " not found.");
            return null;
        }

        ReturnRequest request = new ReturnRequest(
                nextRmaId++, orderId, reason, condition, photos);

        requests.add(request);

        System.out.println("RMA created successfully: "
                + request.getRmaId());

        return request;
    }

    public ReturnRequest createRMA(int orderId, List<String> items,
                                   String reason, String condition) {
        Order order = findOrder(orderId);

        if (order == null || items == null || items.isEmpty()) {
            System.out.println("Invalid order or item list.");
            return null;
        }

        for (String item : items) {
            if (!order.containsItem(item)) {
                System.out.println("Item not found in order: " + item);
                return null;
            }
        }

        return createRMA(orderId,
                reason + " - Items: " + items, condition);
    }

    public Order findOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    public ReturnRequest findRMA(int rmaId) {
        for (ReturnRequest request : requests) {
            if (request.getRmaId() == rmaId) {
                return request;
            }
        }
        return null;
    }

    public boolean inspectItem(int rmaId) {
        ReturnRequest request = findRMA(rmaId);

        if (request == null
                || !request.getStatus().equals("REQUESTED")) {
            System.out.println("Cannot inspect RMA " + rmaId);
            return false;
        }

        System.out.println("Inspecting RMA " + rmaId
                + ": condition = " + request.getCondition());

        return true;
    }

    public boolean approve(int rmaId) {
        ReturnRequest request = findRMA(rmaId);

        if (request != null && request.approve()) {
            System.out.println("RMA " + rmaId + " approved.");
            return true;
        }

        System.out.println("RMA " + rmaId + " cannot be approved.");
        return false;
    }

    public boolean deny(int rmaId) {
        ReturnRequest request = findRMA(rmaId);

        if (request != null && request.deny()) {
            System.out.println("RMA " + rmaId + " denied.");
            return true;
        }

        System.out.println("RMA " + rmaId + " cannot be denied.");
        return false;
    }

    public boolean processRefund(int rmaId, Refund refund) {
        ReturnRequest request = findRMA(rmaId);

        if (request == null
                || !request.getStatus().equals("APPROVED")
                || refund == null
                || refund.getRmaId() != rmaId
                || !refund.isValidAmount()
                || refund.isProcessed()) {
            System.out.println(
                    "Refund cannot be processed for RMA " + rmaId);
            return false;
        }

        refund.processRefund();

        if (refund.isProcessed()) {
            request.markRefunded();
            refunds.add(refund);
            return true;
        }

        return false;
    }

    public void printDailyRMASummary() {
        LocalDate today = LocalDate.now();
        int count = 0;

        System.out.println(
                "\n===== DAILY RMA SUMMARY: " + today + " =====");

        for (ReturnRequest request : requests) {
            if (request.getRequestDate().equals(today)) {
                request.displayRequest();
                count++;
            }
        }

        System.out.println("Total RMAs today: " + count);
    }

    public void printDailyRefundSummary() {
        LocalDate today = LocalDate.now();
        int count = 0;
        double total = 0;

        System.out.println(
                "\n===== DAILY REFUND SUMMARY: " + today + " =====");

        for (Refund refund : refunds) {
            if (today.equals(refund.getProcessedDate())) {
                refund.displayRefund();
                count++;
                total += refund.getAmount();
            }
        }

        System.out.println("Total refunds today: " + count);
        System.out.println("Total refund amount: Rs."
                + String.format("%.2f", total));
    }

    public void displayAllOrders() {
        for (Order order : orders) {
            order.displayOrder();
        }
    }

    public void displayAllRMAs() {
        for (ReturnRequest request : requests) {
            request.displayRequest();
        }
    }

    public void displayAllRefunds() {
        for (Refund refund : refunds) {
            refund.displayRefund();
        }
    }

    public int getTotalRMARequests() {
        return requests.size();
    }

    public int getTotalRefunds() {
        return refunds.size();
    }
}

// 7. Main class
public class RMAppMain {
    public static void main(String[] args) {

        RMAService service = new RMAService();

        // Load sample orders
        service.addOrder(new Order(101, "Arun",
                Arrays.asList("Laptop", "Mouse"),
                50000.00, LocalDate.now().minusDays(5)));

        service.addOrder(new Order(102, "Priya",
                Arrays.asList("Headphones", "Keyboard"),
                6000.00, LocalDate.now().minusDays(3)));

        service.addOrder(new Order(103, "Kumar",
                Arrays.asList("Monitor"),
                12000.00, LocalDate.now().minusDays(2)));

        System.out.println("===== ALL ORDERS =====");
        service.displayAllOrders();

        System.out.println("\n===== CREATE RMA REQUESTS =====");

        ReturnRequest r1 = service.createRMA(
                101, "Product defective", "GOOD",
                Arrays.asList("laptop_front.jpg",
                              "laptop_back.jpg"));

        ReturnRequest r2 = service.createRMA(
                102, "Wrong item delivered", "GOOD");

        ReturnRequest r3 = service.createRMA(
                103, Arrays.asList("Monitor"),
                "Screen damaged", "DAMAGED");

        System.out.println("\n===== INSPECT AND APPROVE/DENY =====");

        if (r1 != null && service.inspectItem(r1.getRmaId())) {
            service.approve(r1.getRmaId());
        }

        if (r2 != null && service.inspectItem(r2.getRmaId())) {
            service.deny(r2.getRmaId());
        }

        if (r3 != null && service.inspectItem(r3.getRmaId())) {
            service.approve(r3.getRmaId());
        }

        System.out.println("\n===== PROCESS REFUNDS =====");

        if (r1 != null) {
            Refund refund1 = new CardRefund(
                    5001, r1.getRmaId(), 50000.00,
                    "1234", "BANKREF1001", 50.00, "VISA");

            service.processRefund(r1.getRmaId(), refund1);
        }

        if (r3 != null) {
            Refund refund2 = new WalletRefund(
                    5002, r3.getRmaId(), 12000.00,
                    "Google Pay", "user@upi",
                    "TXN2001", true);

            service.processRefund(r3.getRmaId(), refund2);
        }

        System.out.println("\n===== FINAL RMA STATUS =====");
        service.displayAllRMAs();

        System.out.println("\n===== ALL REFUNDS =====");
        service.displayAllRefunds();

        service.printDailyRMASummary();
        service.printDailyRefundSummary();

        System.out.println("\n===== SYSTEM TOTALS =====");
        System.out.println("Orders loaded: "
                + Order.getOrderCount());

        System.out.println("RMA requests: "
                + service.getTotalRMARequests());

        System.out.println("Refunds processed: "
                + service.getTotalRefunds());

        System.out.println("Card refunds created: "
                + CardRefund.getCardRefundCount());

        System.out.println("Wallet refunds created: "
                + WalletRefund.getWalletRefundCount());

        System.out.println("Total refund objects created: "
                + Refund.getRefundCount());

        System.out.println("Service instances: "
                + RMAService.getServiceCount());
    }
}