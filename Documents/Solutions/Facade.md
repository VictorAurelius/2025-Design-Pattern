# Mẫu thiết kế Facade (Facade Pattern)

## 1. Mô tả mẫu Facade

**Mẫu Facade** (Facade Pattern) là một mẫu thiết kế cấu trúc cung cấp một giao diện đơn giản cho một hệ thống con phức tạp. Facade đóng vai trò là một "mặt tiền" che giấu sự phức tạp của nhiều subsystems bên trong, giúp client dễ dàng sử dụng hệ thống mà không cần biết chi tiết bên trong.

### Các thành phần chính:

- **Facade**: Class cung cấp interface đơn giản, high-level cho client
- **Subsystems**: Các class phức tạp thực hiện công việc thực sự
- **Client**: Đối tượng sử dụng Facade thay vì tương tác trực tiếp với subsystems

### Khi nào sử dụng:

- Khi muốn cung cấp interface đơn giản cho hệ thống phức tạp
- Khi muốn giảm dependencies giữa client và subsystems
- Khi muốn tổ chức các subsystems thành layers
- Khi muốn cung cấp entry point đơn giản cho các tác vụ phổ biến
- Khi client chỉ cần một tập con nhỏ các chức năng của hệ thống phức tạp

### Đặc điểm quan trọng:

- Facade **không** ẩn hoàn toàn subsystems (client vẫn có thể truy cập trực tiếp nếu cần)
- Facade **không** thêm functionality mới, chỉ orchestrate (điều phối) subsystems
- Subsystems **không** biết về sự tồn tại của Facade
- Giảm coupling nhưng không loại bỏ hoàn toàn

---

## 2. Mô tả bài toán

Lập trình viên Linda làm việc cho công ty thương mại điện tử **ShopEasy**. Công ty có một nền tảng e-commerce với hệ thống backend phức tạp gồm nhiều services độc lập:

**Các subsystems hiện có**:
1. **InventorySystem**: Quản lý kho hàng, kiểm tra tồn kho, reserve/release sản phẩm
2. **PaymentProcessor**: Xử lý thanh toán qua nhiều phương thức (credit card, e-wallet, COD)
3. **ShippingService**: Tính phí ship, sắp xếp vận chuyển, tracking
4. **NotificationService**: Gửi email/SMS xác nhận, cập nhật trạng thái cho khách hàng

**Quy trình xử lý đơn hàng hiện tại**:
Khi khách hàng đặt hàng, client code (web frontend, mobile app) phải thực hiện 8-10 bước:
1. Kiểm tra từng sản phẩm có còn hàng không (InventorySystem)
2. Reserve sản phẩm trong kho (InventorySystem)
3. Validate thông tin thanh toán (PaymentProcessor)
4. Charge payment (PaymentProcessor)
5. Tính phí shipping (ShippingService)
6. Tạo shipping order (ShippingService)
7. Gửi email xác nhận đơn hàng (NotificationService)
8. Gửi SMS thông báo (NotificationService)

### Vấn đề phát sinh:

1. **Code phức tạp và dễ sai**:
   - Developer phải nhớ đúng thứ tự 8-10 bước
   - Nếu quên một bước, đơn hàng có thể bị lỗi
   - Logic phức tạp lặp lại ở nhiều nơi (web, mobile, API)

2. **Tight coupling**:
   - Client code phụ thuộc vào 4 subsystems
   - Khi một subsystem thay đổi API, phải sửa nhiều nơi
   - Khó test vì phụ thuộc nhiều dependencies

3. **Khó maintain và mở rộng**:
   - Thêm một bước mới (ví dụ: loyalty points) phải sửa tất cả clients
   - Code trùng lặp ở web frontend, mobile app, admin panel
   - Khó onboard developer mới vì quá phức tạp

4. **Error handling phân tán**:
   - Mỗi client tự handle errors khác nhau
   - Không consistent trong việc rollback khi có lỗi
   - Ví dụ: Nếu payment fail sau khi đã reserve inventory, phải release lại

Linda cần một giải pháp để **đơn giản hóa** việc xử lý đơn hàng cho tất cả clients mà không cần sửa đổi các subsystems hiện có.

---

## 3. Yêu cầu bài toán

### Input (Điều kiện ban đầu):
Hệ thống có 4 subsystems phức tạp:
- **InventorySystem**: `checkStock()`, `reserveProduct()`, `releaseProduct()`
- **PaymentProcessor**: `validatePaymentInfo()`, `chargePayment()`, `refundPayment()`
- **ShippingService**: `calculateShippingCost()`, `createShipment()`, `cancelShipment()`
- **NotificationService**: `sendEmailConfirmation()`, `sendSMSNotification()`

Client phải gọi 8-10 methods theo đúng thứ tự để hoàn thành một đơn hàng.

### Problem (Vấn đề):
1. **Phức tạp**: Client phải biết chi tiết implementation của 4 subsystems
2. **Khó sử dụng**: Phải nhớ đúng thứ tự các bước
3. **Code trùng lặp**: Logic giống nhau ở web, mobile, API
4. **Tight coupling**: Client phụ thuộc vào 4 subsystems
5. **Khó maintain**: Thay đổi một subsystem ảnh hưởng nhiều nơi
6. **Inconsistent error handling**: Mỗi client handle errors khác nhau

### Solution (Giải pháp):
Sử dụng **Facade Pattern** để:
- Tạo class **OrderFacade** cung cấp interface đơn giản
- Facade orchestrate (điều phối) các subsystems
- Client chỉ cần gọi **một method** của Facade: `placeOrder()`, `cancelOrder()`

### Expected Output (Kết quả mong đợi):
- Client code đơn giản: 1 dòng thay vì 8-10 dòng
- Giảm coupling: Client chỉ phụ thuộc Facade
- Code reusable: Logic tập trung ở một nơi
- Error handling consistent: Facade handle tất cả errors
- Dễ maintain: Thay đổi subsystem chỉ sửa Facade

---

## 4. Hiệu quả của việc sử dụng Facade Pattern

### Lợi ích cụ thể:

1. **Đơn giản hóa client code đáng kể**:
   - Trước: 8-10 dòng code phức tạp
   - Sau: 1 dòng gọi `facade.placeOrder(...)`
   - Giảm 80-90% lines of code ở client side

2. **Giảm coupling**: Client chỉ phụ thuộc vào Facade (1 dependency) thay vì 4 subsystems

3. **Code reusability cao**: Logic xử lý đơn hàng tập trung ở Facade, web/mobile/API đều dùng chung

4. **Error handling tập trung**: Facade handle errors và rollback consistent

5. **Dễ maintain và mở rộng**: Thêm bước mới chỉ sửa trong Facade

### So sánh trước và sau khi dùng Facade:

| Tiêu chí | Không dùng Facade | Có dùng Facade |
|----------|-------------------|-----------------|
| Lines of code (client) | 8-10 dòng/order | 1 dòng/order |
| Dependencies | 4 subsystems | 1 Facade |
| Code trùng lặp | Nhiều | Không |
| Error handling | Phân tán | Tập trung |
| Testing complexity | Cao (4 mocks) | Thấp (1 mock) |

### Trade-offs:

**Ưu điểm**: Interface đơn giản, giảm coupling, tăng reusability, centralized error handling

**Nhược điểm**: Thêm một lớp trung gian, Facade có thể trở thành "God Object" nếu không cẩn thận

---

## 5. Cài đặt

### 5.1. Class InventorySystem (Subsystem)

```java
public class InventorySystem {

	public boolean checkStock(String productId, int quantity) {
		System.out.println("  [Inventory] Checking stock for product: " + productId);
		return true;
	}

	public boolean reserveProduct(String productId, int quantity) {
		System.out.println("  [Inventory] Reserving " + quantity + " units of product: " + productId);
		return true;
	}

	public void releaseProduct(String productId, int quantity) {
		System.out.println("  [Inventory] Releasing " + quantity + " units of product: " + productId);
	}
}
```

### 5.2. Class PaymentProcessor (Subsystem)

```java
public class PaymentProcessor {

	public boolean validatePaymentInfo(String cardNumber, String cvv, double amount) {
		System.out.println("  [Payment] Validating payment info for amount: $" + amount);
		return cardNumber != null && !cardNumber.isEmpty();
	}

	public String chargePayment(String cardNumber, double amount) {
		System.out.println("  [Payment] Charging $" + amount + " to card: " + cardNumber);
		return "TXN" + System.currentTimeMillis();
	}

	public boolean refundPayment(String transactionId) {
		System.out.println("  [Payment] Refunding transaction: " + transactionId);
		return true;
	}
}
```

### 5.3. Class ShippingService (Subsystem)

```java
public class ShippingService {

	public double calculateShippingCost(String address, double weight) {
		System.out.println("  [Shipping] Calculating shipping cost to: " + address);
		return 5.99;
	}

	public String createShipment(String address, String productId) {
		System.out.println("  [Shipping] Creating shipment to: " + address);
		return "SHIP" + System.currentTimeMillis();
	}

	public void cancelShipment(String trackingNumber) {
		System.out.println("  [Shipping] Cancelling shipment: " + trackingNumber);
	}
}
```

### 5.4. Class NotificationService (Subsystem)

```java
public class NotificationService {

	public void sendEmailConfirmation(String email, String orderDetails) {
		System.out.println("  [Notification] Sending email to: " + email);
		System.out.println("    Order details: " + orderDetails);
	}

	public void sendSMSNotification(String phone, String message) {
		System.out.println("  [Notification] Sending SMS to: " + phone);
		System.out.println("    Message: " + message);
	}
}
```

### 5.5. Class OrderFacade (Facade)

```java
public class OrderFacade {

	private InventorySystem inventory;
	private PaymentProcessor payment;
	private ShippingService shipping;
	private NotificationService notification;

	public OrderFacade() {
		this.inventory = new InventorySystem();
		this.payment = new PaymentProcessor();
		this.shipping = new ShippingService();
		this.notification = new NotificationService();
	}

	public String placeOrder(String customerId, String productId, int quantity,
	                         String cardNumber, String cvv, String address,
	                         String email, String phone) {

		System.out.println("=== Order Facade: Processing Order ===");

		String orderId = "ORD" + System.currentTimeMillis();
		String transactionId = null;
		String trackingNumber = null;

		try {
			if (!inventory.checkStock(productId, quantity)) {
				System.out.println("  [Facade] ERROR: Product out of stock");
				return null;
			}

			if (!inventory.reserveProduct(productId, quantity)) {
				System.out.println("  [Facade] ERROR: Cannot reserve product");
				return null;
			}

			double amount = quantity * 29.99;
			if (!payment.validatePaymentInfo(cardNumber, cvv, amount)) {
				System.out.println("  [Facade] ERROR: Invalid payment info");
				inventory.releaseProduct(productId, quantity);
				return null;
			}

			transactionId = payment.chargePayment(cardNumber, amount);
			if (transactionId == null) {
				System.out.println("  [Facade] ERROR: Payment failed");
				inventory.releaseProduct(productId, quantity);
				return null;
			}

			double shippingCost = shipping.calculateShippingCost(address, quantity * 0.5);
			trackingNumber = shipping.createShipment(address, productId);

			String orderDetails = "Order ID: " + orderId + ", Product: " + productId +
			                     ", Quantity: " + quantity + ", Total: $" + (amount + shippingCost);
			notification.sendEmailConfirmation(email, orderDetails);
			notification.sendSMSNotification(phone, "Your order " + orderId + " is confirmed!");

			System.out.println("=== Order Facade: Order Completed Successfully ===");
			System.out.println("  Order ID: " + orderId);
			System.out.println("  Tracking: " + trackingNumber);

			return orderId;

		} catch (Exception e) {
			System.out.println("  [Facade] ERROR: " + e.getMessage());
			if (transactionId != null) {
				payment.refundPayment(transactionId);
			}
			if (trackingNumber != null) {
				shipping.cancelShipment(trackingNumber);
			}
			inventory.releaseProduct(productId, quantity);
			return null;
		}
	}

	public boolean cancelOrder(String orderId, String productId, int quantity, String transactionId, String trackingNumber) {
		System.out.println("=== Order Facade: Cancelling Order " + orderId + " ===");

		payment.refundPayment(transactionId);
		shipping.cancelShipment(trackingNumber);
		inventory.releaseProduct(productId, quantity);

		System.out.println("=== Order Facade: Order Cancelled Successfully ===");
		return true;
	}
}
```

### 5.6. Class ECommerceDemo (Client)

```java
public class ECommerceDemo {

	public static void main(String[] args) {

		System.out.println("========================================");
		System.out.println("   E-COMMERCE ORDER PROCESSING DEMO    ");
		System.out.println("========================================\n");

		OrderFacade orderFacade = new OrderFacade();

		System.out.println("\n--- CASE 1: Place Order (Success) ---\n");
		String orderId = orderFacade.placeOrder(
			"CUST001", "LAPTOP-X1", 2,
			"4532-1234-5678", "123",
			"123 Main St, Hanoi",
			"customer@email.com",
			"+84-123-456-789"
		);

		if (orderId != null) {
			System.out.println("\n✓ Order placed successfully: " + orderId);
		}

		System.out.println("\n\n--- CASE 2: Cancel Order ---\n");
		boolean cancelled = orderFacade.cancelOrder(
			orderId, "LAPTOP-X1", 2, "TXN123456", "SHIP789"
		);

		if (cancelled) {
			System.out.println("\n✓ Order cancelled successfully");
		}

		System.out.println("\n========================================");
	}
}
```

---

## 6. Kết quả chạy chương trình

```
========================================
   E-COMMERCE ORDER PROCESSING DEMO
========================================


--- CASE 1: Place Order (Success) ---

=== Order Facade: Processing Order ===
  [Inventory] Checking stock for product: LAPTOP-X1
  [Inventory] Reserving 2 units of product: LAPTOP-X1
  [Payment] Validating payment info for amount: $59.98
  [Payment] Charging $59.98 to card: 4532-1234-5678
  [Shipping] Calculating shipping cost to: 123 Main St, Hanoi
  [Shipping] Creating shipment to: 123 Main St, Hanoi
  [Notification] Sending email to: customer@email.com
    Order details: Order ID: ORD1730700000123, Product: LAPTOP-X1, Quantity: 2, Total: $65.97
  [Notification] Sending SMS to: +84-123-456-789
    Message: Your order ORD1730700000123 is confirmed!
=== Order Facade: Order Completed Successfully ===
  Order ID: ORD1730700000123
  Tracking: SHIP1730700000456

✓ Order placed successfully: ORD1730700000123


--- CASE 2: Cancel Order ---

=== Order Facade: Cancelling Order ORD1730700000123 ===
  [Payment] Refunding transaction: TXN123456
  [Shipping] Cancelling shipment: SHIP789
  [Inventory] Releasing 2 units of product: LAPTOP-X1
=== Order Facade: Order Cancelled Successfully ===

✓ Order cancelled successfully

========================================
```

**Giải thích**:
- Client chỉ gọi 1 method `placeOrder()` thay vì 8 methods
- Facade orchestrate tất cả subsystems
- Error handling và rollback tự động

---

## 7. Sơ đồ UML

### 7.1. Class Diagram

```
[Client] --> [OrderFacade] --> [InventorySystem]
                         |--> [PaymentProcessor]
                         |--> [ShippingService]
                         |--> [NotificationService]
```

**Facade**: OrderFacade - Cung cấp `placeOrder()`, `cancelOrder()`

**Subsystems**: 4 classes độc lập (Inventory, Payment, Shipping, Notification)

**Client**: ECommerceDemo - Chỉ sử dụng Facade

---

## 8. Tổng kết

Facade Pattern đơn giản hóa hệ thống e-commerce phức tạp:

1. **Vấn đề**: 4 subsystems, client phải gọi 8-10 bước
2. **Giải pháp**: OrderFacade với 1-2 methods đơn giản
3. **Kết quả**: Client code đơn giản, dễ maintain, giảm coupling

Pattern này hữu ích cho: E-commerce platforms, Banking systems, Booking systems, Smart home, Microservices API Gateway.
