# Mẫu thiết kế Bridge (Bridge Pattern)

## 1. Mô tả mẫu Bridge

**Mẫu Bridge** (Bridge Pattern) là một mẫu thiết kế cấu trúc cho phép tách biệt abstraction (trừu tượng) khỏi implementation (triển khai), giúp cả hai có thể thay đổi độc lập mà không ảnh hưởng đến nhau. Pattern này giải quyết vấn đề "class explosion" khi có nhiều dimensions (chiều) biến đổi.

### Các thành phần chính:

- **Abstraction**: Abstract class định nghĩa high-level interface, chứa reference đến Implementation
- **RefinedAbstraction**: Concrete classes kế thừa Abstraction, mở rộng interface
- **Implementation**: Interface định nghĩa low-level operations
- **ConcreteImplementation**: Concrete classes implement Implementation
- **Bridge**: Mối quan hệ composition giữa Abstraction và Implementation

### Khi nào sử dụng:

- Khi muốn tránh binding vĩnh viễn giữa abstraction và implementation
- Khi cả abstraction và implementation đều cần mở rộng bằng subclassing
- Khi thay đổi implementation không ảnh hưởng đến client code
- Khi có nhiều dimensions biến đổi (cartesian product complexity)
- Khi muốn chia sẻ implementation giữa nhiều objects

### Đặc điểm quan trọng:

- **Decouple abstraction from implementation**: Tách 2 hierarchies độc lập
- **Composition over inheritance**: Dùng composition thay vì inheritance
- **Independent variation**: Cả 2 hierarchies có thể extend độc lập
- **Runtime flexibility**: Có thể thay đổi implementation lúc runtime
- **Reduce class explosion**: Giảm từ n×m xuống n+m classes

---

## 2. Mô tả bài toán

Sarah là CTO của công ty công nghệ **TechCorp**. Công ty cần một hệ thống thông báo (notification system) để gửi thông báo cho nhân viên và khách hàng qua nhiều kênh khác nhau.

**Yêu cầu hệ thống**:

**3 loại thông báo** (Notification types):
1. **System Notification**: Thông báo hệ thống (server down, maintenance)
2. **Marketing Notification**: Thông báo marketing (promotion, new product)
3. **Alert Notification**: Thông báo cảnh báo khẩn cấp (security breach, critical error)

**3 kênh gửi** (Channels):
1. **Email**: Gửi qua email
2. **SMS**: Gửi qua tin nhắn
3. **Slack**: Gửi qua Slack workspace

### Vấn đề phát sinh:

**Cách tiếp cận ban đầu (Inheritance)**:

Sarah nghĩ đến việc tạo class cho mỗi tổ hợp:
- SystemEmailNotification
- SystemSMSNotification
- SystemSlackNotification
- MarketingEmailNotification
- MarketingSMSNotification
- MarketingSlackNotification
- AlertEmailNotification
- AlertSMSNotification
- AlertSlackNotification

**Vấn đề**:
1. **Class explosion**: Cần 3 × 3 = **9 classes** cho chỉ 3 loại thông báo và 3 kênh
2. **Khó mở rộng**:
   - Thêm 1 loại thông báo mới → phải tạo thêm 3 classes
   - Thêm 1 kênh mới (ví dụ: WhatsApp) → phải tạo thêm 3 classes
   - Nếu có 5 loại × 5 kênh → cần 25 classes!
3. **Code trùng lặp**: Logic format thông báo lặp lại ở nhiều class
4. **Tight coupling**: Loại thông báo bị gắn chặt với kênh gửi
5. **Khó maintain**: Thay đổi logic một kênh phải sửa nhiều class

Sarah cần một giải pháp để **tách riêng** logic xử lý thông báo (abstraction) khỏi logic gửi qua kênh (implementation), cho phép cả hai thay đổi độc lập.

---

## 3. Yêu cầu bài toán

### Input (Điều kiện ban đầu):
Hệ thống cần hỗ trợ:
- **3 loại thông báo**: System, Marketing, Alert (có thể mở rộng)
- **3 kênh gửi**: Email, SMS, Slack (có thể mở rộng)
- Mỗi loại thông báo có format riêng
- Mỗi kênh có cách gửi riêng

### Problem (Vấn đề):
1. **Class explosion**: Cần n × m classes cho n loại thông báo và m kênh
2. **Tight coupling**: Loại thông báo gắn chặt với kênh gửi
3. **Khó mở rộng**: Thêm loại hoặc kênh mới phải tạo nhiều classes
4. **Code trùng lặp**: Logic format và gửi lặp lại
5. **Không linh hoạt**: Không thể thay đổi kênh gửi lúc runtime

### Solution (Giải pháp):
Sử dụng **Bridge Pattern** để:
- Tạo **Notification hierarchy** (abstraction) cho các loại thông báo
- Tạo **NotificationChannel hierarchy** (implementation) cho các kênh gửi
- **Bridge**: Notification chứa reference đến NotificationChannel
- Client có thể kết hợp bất kỳ loại thông báo với bất kỳ kênh nào

### Expected Output (Kết quả mong đợi):
- Giảm số class từ 9 (3×3) xuống **8 classes** (3+3+2 base)
- Dễ thêm loại thông báo mới mà không ảnh hưởng kênh
- Dễ thêm kênh mới mà không ảnh hưởng loại thông báo
- Runtime flexibility: Thay đổi kênh gửi trong quá trình chạy
- Loose coupling giữa 2 hierarchies

---

## 4. Hiệu quả của việc sử dụng Bridge Pattern

### Lợi ích cụ thể:

1. **Giảm class explosion**:
   - Không dùng Bridge: 3 loại × 3 kênh = **9 classes**
   - Dùng Bridge: 3 + 3 + 2 base = **8 classes** (Notification abstract + 3 refined, NotificationChannel interface + 3 concrete)
   - Nếu mở rộng lên 5 loại × 5 kênh:
     - Không Bridge: 25 classes
     - Có Bridge: 10 + 2 base = 12 classes

2. **Independent variation**:
   - Thêm loại thông báo mới: chỉ tạo 1 class extends Notification
   - Thêm kênh mới: chỉ tạo 1 class implements NotificationChannel
   - Không cần sửa code hiện có

3. **Runtime flexibility**:
   - Có thể thay đổi kênh gửi lúc runtime
   - Ví dụ: Send qua Email, nếu fail thì retry qua SMS

4. **Loose coupling**:
   - Notification không biết chi tiết về kênh gửi
   - NotificationChannel không biết chi tiết về loại thông báo

### So sánh trước và sau khi dùng Bridge:

| Tiêu chí | Không dùng Bridge (Inheritance) | Có dùng Bridge |
|----------|--------------------------------|----------------|
| Số classes (3×3) | 9 classes | 8 classes (3+3+2) |
| Số classes (5×5) | 25 classes | 12 classes (5+5+2) |
| Thêm loại mới | Tạo m classes (m kênh) | Tạo 1 class |
| Thêm kênh mới | Tạo n classes (n loại) | Tạo 1 class |
| Runtime flexibility | Không | Có |
| Coupling | Tight | Loose |
| Code reuse | Thấp (trùng lặp) | Cao |

### Trade-offs:

**Ưu điểm**: Giảm class explosion, independent variation, runtime flexibility, loose coupling

**Nhược điểm**: Thêm độ phức tạp (2 hierarchies), client phải hiểu cách kết hợp abstraction và implementation

---

## 5. Cài đặt

### 5.1. Interface NotificationChannel (Implementation)

```java
public interface NotificationChannel {

	void sendMessage(String formattedMessage);

	String getChannelName();
}
```

### 5.2. Class EmailChannel (ConcreteImplementation)

```java
public class EmailChannel implements NotificationChannel {

	@Override
	public void sendMessage(String formattedMessage) {
		System.out.println("  [Email] Sending via Email...");
		System.out.println("  To: recipient@company.com");
		System.out.println("  Subject: Notification from TechCorp");
		System.out.println("  Body:");
		System.out.println(formattedMessage);
		System.out.println("  [Email] Message sent successfully!");
	}

	@Override
	public String getChannelName() {
		return "Email";
	}
}
```

### 5.3. Class SMSChannel (ConcreteImplementation)

```java
public class SMSChannel implements NotificationChannel {

	@Override
	public void sendMessage(String formattedMessage) {
		System.out.println("  [SMS] Sending via SMS...");
		System.out.println("  To: +84-123-456-789");
		System.out.println("  Message:");
		System.out.println(formattedMessage);
		System.out.println("  [SMS] Message sent successfully!");
	}

	@Override
	public String getChannelName() {
		return "SMS";
	}
}
```

### 5.4. Class SlackChannel (ConcreteImplementation)

```java
public class SlackChannel implements NotificationChannel {

	@Override
	public void sendMessage(String formattedMessage) {
		System.out.println("  [Slack] Posting to #general channel...");
		System.out.println("  Workspace: techcorp.slack.com");
		System.out.println("  Message:");
		System.out.println(formattedMessage);
		System.out.println("  [Slack] Message posted successfully!");
	}

	@Override
	public String getChannelName() {
		return "Slack";
	}
}
```

### 5.5. Abstract Class Notification (Abstraction)

```java
public abstract class Notification {

	protected NotificationChannel channel;

	public Notification(NotificationChannel channel) {
		this.channel = channel;
	}

	public abstract String formatMessage(String content);

	public void send(String content) {
		String formattedMessage = formatMessage(content);
		System.out.println("\n=== Sending Notification ===");
		System.out.println("Type: " + getNotificationType());
		System.out.println("Channel: " + channel.getChannelName());
		channel.sendMessage(formattedMessage);
	}

	public abstract String getNotificationType();

	public void setChannel(NotificationChannel channel) {
		this.channel = channel;
	}
}
```

### 5.6. Class SystemNotification (RefinedAbstraction)

```java
public class SystemNotification extends Notification {

	public SystemNotification(NotificationChannel channel) {
		super(channel);
	}

	@Override
	public String formatMessage(String content) {
		return "[SYSTEM] " + content + "\n" +
		       "Priority: Normal\n" +
		       "Action: Please acknowledge";
	}

	@Override
	public String getNotificationType() {
		return "System Notification";
	}
}
```

### 5.7. Class MarketingNotification (RefinedAbstraction)

```java
public class MarketingNotification extends Notification {

	public MarketingNotification(NotificationChannel channel) {
		super(channel);
	}

	@Override
	public String formatMessage(String content) {
		return "🎉 SPECIAL OFFER 🎉\n" +
		       content + "\n" +
		       "Don't miss out! Limited time only.\n" +
		       "Unsubscribe: techcorp.com/unsubscribe";
	}

	@Override
	public String getNotificationType() {
		return "Marketing Notification";
	}
}
```

### 5.8. Class AlertNotification (RefinedAbstraction)

```java
public class AlertNotification extends Notification {

	public AlertNotification(NotificationChannel channel) {
		super(channel);
	}

	@Override
	public String formatMessage(String content) {
		return "⚠️ CRITICAL ALERT ⚠️\n" +
		       content + "\n" +
		       "Priority: URGENT\n" +
		       "Action: IMMEDIATE ATTENTION REQUIRED!";
	}

	@Override
	public String getNotificationType() {
		return "Alert Notification";
	}
}
```

### 5.9. Class NotificationDemo (Client)

```java
public class NotificationDemo {

	public static void main(String[] args) {

		System.out.println("========================================");
		System.out.println("    TECHCORP NOTIFICATION SYSTEM");
		System.out.println("========================================");

		// Create channels (implementations)
		NotificationChannel emailChannel = new EmailChannel();
		NotificationChannel smsChannel = new SMSChannel();
		NotificationChannel slackChannel = new SlackChannel();

		// Test 1: System notification via Email
		Notification notification = new SystemNotification(emailChannel);
		notification.send("Server maintenance scheduled for tonight at 11 PM");

		// Test 2: Marketing notification via SMS
		notification = new MarketingNotification(smsChannel);
		notification.send("Get 50% off on all products this weekend!");

		// Test 3: Alert notification via Slack
		notification = new AlertNotification(slackChannel);
		notification.send("Security breach detected on production server!");

		// Test 4: Runtime flexibility - change channel dynamically
		System.out.println("\n\n--- RUNTIME FLEXIBILITY DEMO ---");
		notification = new SystemNotification(emailChannel);
		notification.send("Database backup completed");

		System.out.println("\n--- Switching channel from Email to SMS ---");
		notification.setChannel(smsChannel);
		notification.send("Database backup completed");

		// Test 5: All combinations
		System.out.println("\n\n--- ALL COMBINATIONS DEMO ---");

		System.out.println("\n[System + Email]");
		new SystemNotification(emailChannel).send("Test message 1");

		System.out.println("\n[System + SMS]");
		new SystemNotification(smsChannel).send("Test message 2");

		System.out.println("\n[Marketing + Slack]");
		new MarketingNotification(slackChannel).send("Test message 3");

		System.out.println("\n========================================");
		System.out.println("Total classes created: 8");
		System.out.println("(3 notification types + 3 channels + 2 base)");
		System.out.println("Without Bridge would need: 9 classes (3×3)");
		System.out.println("========================================");
	}
}
```

---

## 6. Kết quả chạy chương trình

```
========================================
    TECHCORP NOTIFICATION SYSTEM
========================================

=== Sending Notification ===
Type: System Notification
Channel: Email
  [Email] Sending via Email...
  To: recipient@company.com
  Subject: Notification from TechCorp
  Body:
[SYSTEM] Server maintenance scheduled for tonight at 11 PM
Priority: Normal
Action: Please acknowledge
  [Email] Message sent successfully!

=== Sending Notification ===
Type: Marketing Notification
Channel: SMS
  [SMS] Sending via SMS...
  To: +84-123-456-789
  Message:
🎉 SPECIAL OFFER 🎉
Get 50% off on all products this weekend!
Don't miss out! Limited time only.
Unsubscribe: techcorp.com/unsubscribe
  [SMS] Message sent successfully!

=== Sending Notification ===
Type: Alert Notification
Channel: Slack
  [Slack] Posting to #general channel...
  Workspace: techcorp.slack.com
  Message:
⚠️ CRITICAL ALERT ⚠️
Security breach detected on production server!
Priority: URGENT
Action: IMMEDIATE ATTENTION REQUIRED!
  [Slack] Message posted successfully!


--- RUNTIME FLEXIBILITY DEMO ---

=== Sending Notification ===
Type: System Notification
Channel: Email
  [Email] Sending via Email...
  To: recipient@company.com
  Subject: Notification from TechCorp
  Body:
[SYSTEM] Database backup completed
Priority: Normal
Action: Please acknowledge
  [Email] Message sent successfully!

--- Switching channel from Email to SMS ---

=== Sending Notification ===
Type: System Notification
Channel: SMS
  [SMS] Sending via SMS...
  To: +84-123-456-789
  Message:
[SYSTEM] Database backup completed
Priority: Normal
Action: Please acknowledge
  [SMS] Message sent successfully!


--- ALL COMBINATIONS DEMO ---

[System + Email]

=== Sending Notification ===
Type: System Notification
Channel: Email
  [Email] Sending via Email...
  To: recipient@company.com
  Subject: Notification from TechCorp
  Body:
[SYSTEM] Test message 1
Priority: Normal
Action: Please acknowledge
  [Email] Message sent successfully!

[System + SMS]

=== Sending Notification ===
Type: System Notification
Channel: SMS
  [SMS] Sending via SMS...
  To: +84-123-456-789
  Message:
[SYSTEM] Test message 2
Priority: Normal
Action: Please acknowledge
  [SMS] Message sent successfully!

[Marketing + Slack]

=== Sending Notification ===
Type: Marketing Notification
Channel: Slack
  [Slack] Posting to #general channel...
  Workspace: techcorp.slack.com
  Message:
🎉 SPECIAL OFFER 🎉
Test message 3
Don't miss out! Limited time only.
Unsubscribe: techcorp.com/unsubscribe
  [Slack] Message posted successfully!

========================================
Total classes created: 8
(3 notification types + 3 channels + 2 base)
Without Bridge would need: 9 classes (3×3)
========================================
```

**Giải thích**:
- Client tạo Notification với NotificationChannel cụ thể
- Notification.send() format message theo loại, sau đó delegate đến channel.sendMessage()
- Runtime flexibility: Có thể thay đổi channel qua setChannel()
- Mỗi loại notification kết hợp được với mọi channel

---

## 7. Sơ đồ UML

### 7.1. Class Diagram

```
   [NotificationDemo]
      (Client)
          |
          | uses
          ↓
   [Notification]──────────────────○ [NotificationChannel]
   (Abstraction)         bridge         (Implementation)
   - channel
   + send(content)                    + sendMessage(message)
   + formatMessage()                  + getChannelName()
   + setChannel()
          △                                    △
          |                                    |
    ┌─────┼─────┐                    ┌────────┼────────┐
    |     |     |                    |        |        |
[System] [Marketing] [Alert]    [Email]   [SMS]   [Slack]
(Refined Abstractions)           (Concrete Implementations)
```

**Abstraction Side** (trái):
- **Notification**: Abstract class với reference đến NotificationChannel
- **SystemNotification, MarketingNotification, AlertNotification**: RefinedAbstraction classes

**Implementation Side** (phải):
- **NotificationChannel**: Interface với methods: sendMessage(), getChannelName()
- **EmailChannel, SMSChannel, SlackChannel**: ConcreteImplementation classes

**Bridge**: Notification → NotificationChannel (composition)

**Client**: NotificationDemo sử dụng Notification

---

## 8. Tổng kết

Bridge Pattern giải quyết bài toán notification system của TechCorp:

1. **Vấn đề**: 3 loại thông báo × 3 kênh → class explosion (9 classes)
2. **Giải pháp**: Tách 2 hierarchies - Notification và NotificationChannel
3. **Kết quả**: Chỉ cần 8 classes (3+3+2), dễ mở rộng, runtime flexibility

**Lợi ích chính**:
- **Giảm class explosion**: từ n×m xuống n+m
- **Independent variation**: Thêm loại hoặc kênh mới không ảnh hưởng nhau
- **Runtime flexibility**: Thay đổi implementation lúc runtime
- **Loose coupling**: Abstraction và Implementation tách biệt

Pattern này hữu ích cho: Notification systems, Document rendering, Payment processing, Media players, UI theming, Database drivers, Logging systems.

Bridge Pattern thể hiện nguyên lý **"Decouple abstraction from implementation"** - tách abstraction khỏi implementation để cả hai có thể thay đổi độc lập.
