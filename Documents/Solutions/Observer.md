# Observer Design Pattern - YouTube Channel Notification System

## 1. Mô tả mẫu Observer

**Observer Pattern** là một mẫu thiết kế hành vi (Behavioral Design Pattern) định nghĩa một mối quan hệ phụ thuộc **one-to-many** giữa các đối tượng sao cho khi một đối tượng (Subject) thay đổi trạng thái, tất cả các đối tượng phụ thuộc (Observers) của nó sẽ được **tự động thông báo** và **cập nhật**.

### Các thành phần chính:

1. **Subject (Publisher)**:
   - Đối tượng giữ trạng thái quan trọng
   - Duy trì danh sách các Observers
   - Cung cấp interface để attach và detach observers
   - Notify tất cả observers khi state thay đổi

2. **Observer (Subscriber)**:
   - Interface định nghĩa phương thức update() nhận notification
   - Được gọi bởi Subject khi có thay đổi

3. **ConcreteSubject**:
   - Implement Subject interface
   - Lưu trữ state quan trọng
   - Gửi notification đến observers khi state thay đổi

4. **ConcreteObserver**:
   - Implement Observer interface
   - Duy trì reference đến ConcreteSubject (để lấy state - pull model)
   - Implement update() để xử lý notification theo cách riêng

### Khi nào sử dụng:
- ✅ Khi có mối quan hệ one-to-many giữa objects
- ✅ Khi nhiều objects cần phản ứng với state changes của một object khác
- ✅ Khi muốn loose coupling giữa Subject và Observers
- ✅ Khi số lượng observers có thể thay đổi dynamically
- ✅ Trong event-driven systems
- ✅ Khi cần automatic notification mechanism

### Đặc điểm quan trọng:
- **Loose Coupling**: Subject không biết chi tiết về observers, chỉ biết Observer interface
- **Dynamic Subscription**: Observers có thể subscribe/unsubscribe bất kỳ lúc nào
- **Automatic Notification**: Subject tự động notify khi state thay đổi
- **Open/Closed Principle**: Có thể thêm observers mới không cần sửa Subject
- **Broadcast Communication**: Một notification gửi đến nhiều observers

---

## 2. Mô tả bài toán

### Bối cảnh:
**Emma** là một content creator trên YouTube với kênh "TechReview Pro". Kênh của cô có **hàng nghìn subscribers** quan tâm đến các video review công nghệ. Mỗi khi Emma đăng video mới, cô muốn thông báo đến tất cả subscribers, nhưng mỗi subscriber có **sở thích nhận thông báo khác nhau**:

- Một số subscriber muốn nhận **email notification** với link video và mô tả chi tiết
- Một số muốn nhận **push notification trên mobile app** để xem ngay
- Một số muốn nhận **SMS text message** ngắn gọn

### Vấn đề hiện tại:

**Cách tiếp cận không dùng Observer Pattern** (Tight Coupling):

```java
public class YouTubeChannel {
    private List<EmailSubscriber> emailSubscribers = new ArrayList<>();
    private List<MobileSubscriber> mobileSubscribers = new ArrayList<>();
    private List<SMSSubscriber> smsSubscribers = new ArrayList<>();

    public void uploadVideo(String title) {
        // Phải gọi từng loại subscriber riêng biệt
        for (EmailSubscriber sub : emailSubscribers) {
            sub.sendEmail(title);
        }
        for (MobileSubscriber sub : mobileSubscribers) {
            sub.sendPushNotification(title);
        }
        for (SMSSubscriber sub : smsSubscribers) {
            sub.sendSMS(title);
        }
    }

    // Phải có separate methods cho mỗi loại
    public void addEmailSubscriber(EmailSubscriber sub) { ... }
    public void addMobileSubscriber(MobileSubscriber sub) { ... }
    public void addSMSSubscriber(SMSSubscriber sub) { ... }
}
```

**Vấn đề**:
- ❌ **Tight Coupling**: Channel phải biết tất cả loại subscribers cụ thể
- ❌ **Khó mở rộng**: Thêm loại notification mới phải sửa YouTubeChannel class
- ❌ **Duplicate Code**: Logic notification lặp lại cho mỗi loại
- ❌ **Vi phạm Open/Closed Principle**: Không thể extend mà không modify
- ❌ **Khó maintain**: Quá nhiều dependencies và methods

### Tình huống cụ thể:

Emma có 5 subscribers:
1. **Alice** - muốn nhận email notifications
2. **Bob** - muốn nhận mobile push notifications
3. **Charlie** - muốn nhận SMS notifications
4. **Diana** - muốn nhận cả email VÀ mobile notifications
5. **Ethan** - muốn nhận cả 3 loại notifications

Khi Emma upload video "iPhone 16 Pro Review", tất cả subscribers phải được thông báo **TỰ ĐỘNG**, mỗi người nhận theo **sở thích riêng**, và họ có thể **subscribe/unsubscribe** bất kỳ lúc nào.

---

## 3. Yêu cầu bài toán

### Input:
- Một YouTubeChannel object (Subject) đăng videos
- Nhiều Subscriber objects (Observers) với notification preferences khác nhau
- Thông tin video: title, description, video URL
- Khả năng subscribe/unsubscribe dynamically

### Problem - Vấn đề cần giải quyết:

**1. Tight Coupling Issue**:
- Channel phải biết tất cả concrete subscriber types
- Không thể thêm subscriber type mới mà không sửa Channel class
- Channel phải maintain nhiều lists riêng biệt

**2. Manual Notification**:
- Phải manually gọi notification method cho mỗi loại subscriber
- Code duplication trong notification logic
- Dễ quên notify một số subscribers

**3. Lack of Flexibility**:
- Không thể dynamically thêm/bớt subscribers
- Không thể có subscribers với multiple notification methods
- Khó test individual components

**4. Violation of Principles**:
- Vi phạm Open/Closed Principle
- Vi phạm Single Responsibility Principle
- High coupling, low cohesion

### Solution - Observer Pattern giải quyết:

**1. Define Observer Interface**:
```java
public interface Subscriber {
    void update(String videoTitle);
    void subscribe();
    void unsubscribe();
}
```

**2. Define Subject Interface**:
```java
public interface Channel {
    void attach(Subscriber subscriber);
    void detach(Subscriber subscriber);
    void notifySubscribers();
}
```

**3. ConcreteSubject - YouTubeChannel**:
- Maintains `List<Subscriber>` (không biết concrete types)
- Implement attach/detach/notify methods
- Tự động gọi `notifySubscribers()` khi upload video mới

**4. ConcreteObservers - Different Subscriber Types**:
- `EmailSubscriber`: implement update() để send email
- `MobileAppSubscriber`: implement update() để send push notification
- `SMSSubscriber`: implement update() để send SMS
- Mỗi loại xử lý notification theo cách riêng

### Expected Output:

**Khi Emma upload video mới**:
1. ✅ YouTubeChannel tự động notify TẤT CẢ subscribers
2. ✅ Mỗi subscriber nhận notification theo preference riêng
3. ✅ Channel không cần biết concrete subscriber types
4. ✅ Dễ dàng thêm subscriber types mới (VD: WebPushSubscriber, TelegramSubscriber)
5. ✅ Subscribers có thể subscribe/unsubscribe bất kỳ lúc nào
6. ✅ Loose coupling giữa Channel và Subscribers

**Output mẫu**:
```
Alice subscribing to TechReview Pro...
Bob subscribing to TechReview Pro...
Charlie subscribing to TechReview Pro...

=== New Video Uploaded ===
Video: iPhone 16 Pro Review
Description: Detailed review of the new iPhone...

[Email] Sending to Alice...
[Mobile] Push notification to Bob...
[SMS] Text message to Charlie...

Charlie unsubscribing from TechReview Pro...

=== New Video Uploaded ===
Video: MacBook Pro M4 Unboxing

[Email] Sending to Alice...
[Mobile] Push notification to Bob...
(Charlie không còn nhận thông báo)
```

---

## 4. Hiệu quả của việc sử dụng Observer Pattern

### Lợi ích trong bài toán này:

#### 1. Loose Coupling 🔓
**Trước (Tight Coupling)**:
```java
// Channel biết tất cả concrete types
YouTubeChannel channel = new YouTubeChannel();
EmailSubscriber alice = new EmailSubscriber(...);
MobileSubscriber bob = new MobileSubscriber(...);
channel.addEmailSubscriber(alice);    // Specific method
channel.addMobileSubscriber(bob);     // Different method
```

**Sau (Loose Coupling)**:
```java
// Channel chỉ biết Subscriber interface
Channel channel = new YouTubeChannel(...);
Subscriber alice = new EmailSubscriber(...);
Subscriber bob = new MobileAppSubscriber(...);
alice.subscribe();    // Same interface
bob.subscribe();      // Same interface
```

#### 2. Open/Closed Principle ✅
- **Open for Extension**: Thêm subscriber type mới không cần sửa Channel
  ```java
  // Thêm type mới: chỉ cần implement Subscriber
  public class TelegramSubscriber implements Subscriber {
      // No changes to YouTubeChannel needed!
  }
  ```
- **Closed for Modification**: Channel class không cần sửa khi thêm observers

#### 3. Automatic Notification 🔔
**Trước**:
```java
// Manual notification - dễ quên
channel.uploadVideo("New Video");
channel.notifyEmailSubscribers();
channel.notifyMobileSubscribers();
// Quên notify SMS subscribers!
```

**Sau**:
```java
// Automatic notification - đảm bảo tất cả được notify
channel.uploadVideo("New Video");
// notifySubscribers() được gọi tự động!
```

#### 4. Dynamic Subscription 🔄
- Subscribers có thể subscribe/unsubscribe lúc runtime
- Không cần restart hoặc reconfigure
- Flexible và user-friendly

#### 5. Reusability & Maintainability 🔧
- Subject và Observer có thể reuse trong contexts khác
- Dễ test: mock observers để test subject
- Dễ maintain: thay đổi một observer không ảnh hưởng others

### So sánh Before vs After:

| Aspect | Without Observer | With Observer |
|--------|------------------|---------------|
| Coupling | Tight - Channel biết concrete types | Loose - Channel chỉ biết interface |
| Extensibility | Hard - phải sửa Channel | Easy - chỉ cần implement interface |
| Notification | Manual - dễ quên | Automatic - guaranteed |
| Code Duplication | High - duplicate loops | Low - single notification loop |
| Testability | Hard - nhiều dependencies | Easy - mock observers |
| Flexibility | Low - fixed notification types | High - dynamic subscription |

### Trade-offs và Nhược điểm:

#### ⚠️ Nhược điểm cần lưu ý:

1. **Memory Overhead**:
   - Maintain list of observers tốn memory
   - Observers giữ reference đến subject (potential memory leaks)
   - **Giải pháp**: Implement proper unsubscribe, consider weak references

2. **Notification Order**:
   - Thứ tự observers được notify không được guarantee
   - Có thể gây vấn đề nếu observers phụ thuộc vào nhau
   - **Giải pháp**: Document rõ order không quan trọng, hoặc implement priority

3. **Cascading Updates**:
   - Observer có thể trigger state change trong update() → infinite loop
   - Subject thay đổi → Observer update → Subject thay đổi lại...
   - **Giải pháp**: Observers không nên modify subject state trong update()

4. **Debugging Difficulty**:
   - Khó trace notification flow khi có nhiều observers
   - Observer exceptions có thể break notification chain
   - **Giải pháp**: Logging, exception handling trong notification loop

5. **Memory Leaks**:
   - Observers không unsubscribe → không thể garbage collect
   - Long-lived subjects với short-lived observers
   - **Giải pháp**: Always unsubscribe when done, use weak references

### Khi nào KHÔNG nên dùng Observer:

❌ **Khi chỉ có 1 observer**: Dùng direct reference thay vì Observer pattern
❌ **Khi cần synchronous response**: Callbacks hoặc direct method calls tốt hơn
❌ **Khi notification order quan trọng**: Observer không guarantee order
❌ **Khi có complex dependencies giữa observers**: Consider Mediator pattern
❌ **Khi performance critical**: Overhead của pattern có thể không chấp nhận được

### Best Practices:

✅ **Always unsubscribe**: Implement proper cleanup để tránh memory leaks
✅ **Handle exceptions**: Catch exceptions trong notification loop
✅ **Document order independence**: Make clear order doesn't matter
✅ **Consider async notifications**: Nếu có nhiều observers hoặc slow operations
✅ **Use weak references**: Nếu lo ngại memory leaks

---

## 5. Cài đặt

### Channel Interface (Subject):

```java
import java.util.ArrayList;

public interface Channel {
    void attach(Subscriber subscriber);
    void detach(Subscriber subscriber);
    void notifySubscribers();
    String getChannelName();
}
```

### Subscriber Interface (Observer):

```java
public interface Subscriber {
    void update(String videoTitle);
    void subscribe();
    void unsubscribe();
}
```

### YouTubeChannel (ConcreteSubject):

```java
import java.util.ArrayList;
import java.util.List;

public class YouTubeChannel implements Channel {

    private List<Subscriber> subscribers;
    private String channelName;
    private String latestVideo;
    private String videoDescription;

    public YouTubeChannel(String channelName) {
        this.channelName = channelName;
        this.subscribers = new ArrayList<>();
        System.out.println("YouTube Channel '" + channelName + "' created!");
    }

    @Override
    public void attach(Subscriber subscriber) {
        subscribers.add(subscriber);
        System.out.println("  ✓ New subscriber added. Total: " + subscribers.size());
    }

    @Override
    public void detach(Subscriber subscriber) {
        int index = subscribers.indexOf(subscriber);
        if (index >= 0) {
            subscribers.remove(index);
            System.out.println("  ✓ Subscriber removed. Total: " + subscribers.size());
        }
    }

    @Override
    public void notifySubscribers() {
        System.out.println("\n📢 Notifying " + subscribers.size() + " subscribers...");
        System.out.println("---------------------------------------");
        for (Subscriber subscriber : subscribers) {
            subscriber.update(latestVideo);
        }
        System.out.println("---------------------------------------");
    }

    @Override
    public String getChannelName() {
        return channelName;
    }

    public String getLatestVideo() {
        return latestVideo;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    // Hàm upload video - tự động notify
    public void uploadVideo(String title, String description) {
        System.out.println("\n🎥 [" + channelName + "] Uploading new video...");
        System.out.println("   Title: " + title);
        System.out.println("   Description: " + description);

        this.latestVideo = title;
        this.videoDescription = description;

        // Automatic notification!
        notifySubscribers();
    }
}
```

### EmailSubscriber (ConcreteObserver):

```java
public class EmailSubscriber implements Subscriber {

    private String name;
    private String email;
    private YouTubeChannel channel;

    public EmailSubscriber(String name, String email, YouTubeChannel channel) {
        this.name = name;
        this.email = email;
        this.channel = channel;
    }

    @Override
    public void update(String videoTitle) {
        // Pull model: lấy thêm thông tin từ channel
        String description = channel.getVideoDescription();
        String channelName = channel.getChannelName();

        System.out.println("\n📧 [Email Notification]");
        System.out.println("   To: " + name + " <" + email + ">");
        System.out.println("   Subject: New video from " + channelName);
        System.out.println("   Body:");
        System.out.println("   -------------");
        System.out.println("   Hi " + name + ",");
        System.out.println("   " + channelName + " just uploaded: " + videoTitle);
        System.out.println("   " + description);
        System.out.println("   Watch now: https://youtube.com/watch?v=xyz123");
        System.out.println("   -------------");
    }

    @Override
    public void subscribe() {
        System.out.println("\n" + name + " subscribing via Email...");
        channel.attach(this);
        System.out.println("  → Subscribed successfully!");
    }

    @Override
    public void unsubscribe() {
        System.out.println("\n" + name + " unsubscribing via Email...");
        channel.detach(this);
        System.out.println("  → Unsubscribed successfully!");
    }
}
```

### MobileAppSubscriber (ConcreteObserver):

```java
public class MobileAppSubscriber implements Subscriber {

    private String name;
    private String deviceId;
    private YouTubeChannel channel;

    public MobileAppSubscriber(String name, String deviceId, YouTubeChannel channel) {
        this.name = name;
        this.deviceId = deviceId;
        this.channel = channel;
    }

    @Override
    public void update(String videoTitle) {
        String channelName = channel.getChannelName();

        System.out.println("\n📱 [Mobile Push Notification]");
        System.out.println("   Device: " + deviceId);
        System.out.println("   User: " + name);
        System.out.println("   🔔 " + channelName + " uploaded:");
        System.out.println("   \"" + videoTitle + "\"");
        System.out.println("   Tap to watch now!");
    }

    @Override
    public void subscribe() {
        System.out.println("\n" + name + " subscribing via Mobile App...");
        channel.attach(this);
        System.out.println("  → Subscribed successfully!");
    }

    @Override
    public void unsubscribe() {
        System.out.println("\n" + name + " unsubscribing via Mobile App...");
        channel.detach(this);
        System.out.println("  → Unsubscribed successfully!");
    }
}
```

### SMSSubscriber (ConcreteObserver):

```java
public class SMSSubscriber implements Subscriber {

    private String name;
    private String phoneNumber;
    private YouTubeChannel channel;

    public SMSSubscriber(String name, String phoneNumber, YouTubeChannel channel) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.channel = channel;
    }

    @Override
    public void update(String videoTitle) {
        String channelName = channel.getChannelName();

        System.out.println("\n💬 [SMS Notification]");
        System.out.println("   To: " + phoneNumber + " (" + name + ")");
        System.out.println("   Message:");
        System.out.println("   New video from " + channelName + ": " + videoTitle);
    }

    @Override
    public void subscribe() {
        System.out.println("\n" + name + " subscribing via SMS...");
        channel.attach(this);
        System.out.println("  → Subscribed successfully!");
    }

    @Override
    public void unsubscribe() {
        System.out.println("\n" + name + " unsubscribing via SMS...");
        channel.detach(this);
        System.out.println("  → Unsubscribed successfully!");
    }
}
```

### ObserverDemo (Client):

```java
public class ObserverDemo {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("    OBSERVER PATTERN DEMO");
        System.out.println("    YouTube Channel Notification");
        System.out.println("========================================");

        // Create YouTube Channel (Subject)
        YouTubeChannel channel = new YouTubeChannel("TechReview Pro");

        // Create Subscribers (Observers)
        Subscriber alice = new EmailSubscriber("Alice Johnson", "alice@email.com", channel);
        Subscriber bob = new MobileAppSubscriber("Bob Smith", "iPhone-12345", channel);
        Subscriber charlie = new SMSSubscriber("Charlie Brown", "+1-555-0123", channel);

        // Test 1: Subscribe multiple observers
        System.out.println("\n\n=== TEST 1: Multiple Subscriptions ===");
        alice.subscribe();
        bob.subscribe();
        charlie.subscribe();

        // Test 2: Upload first video - all receive notifications
        System.out.println("\n\n=== TEST 2: First Video Upload ===");
        channel.uploadVideo(
            "iPhone 16 Pro Review",
            "Complete review of Apple's latest flagship phone with camera tests and performance benchmarks."
        );

        // Test 3: One subscriber unsubscribes
        System.out.println("\n\n=== TEST 3: Unsubscribe ===");
        charlie.unsubscribe();

        // Test 4: Upload second video - only remaining subscribers notified
        System.out.println("\n\n=== TEST 4: Second Video Upload (After Unsubscribe) ===");
        channel.uploadVideo(
            "MacBook Pro M4 Unboxing",
            "First look at the new MacBook Pro with M4 chip. Unboxing and initial impressions."
        );

        // Test 5: New subscriber joins
        System.out.println("\n\n=== TEST 5: New Subscriber Joins ===");
        Subscriber diana = new MobileAppSubscriber("Diana Prince", "Android-98765", channel);
        diana.subscribe();

        // Test 6: Upload third video - including new subscriber
        System.out.println("\n\n=== TEST 6: Third Video Upload (With New Subscriber) ===");
        channel.uploadVideo(
            "Top 5 Gadgets of 2024",
            "My favorite tech gadgets this year including smartphones, laptops, and accessories."
        );

        // Test 7: Re-subscribe previous subscriber
        System.out.println("\n\n=== TEST 7: Re-subscribe ===");
        charlie.subscribe();

        // Test 8: Final video upload - all subscribers notified
        System.out.println("\n\n=== TEST 8: Final Video Upload ===");
        channel.uploadVideo(
            "AirPods Pro 3 vs Sony WF-1000XM5",
            "Head-to-head comparison of the best wireless earbuds. Sound quality, ANC, and battery life tested."
        );

        System.out.println("\n\n========================================");
        System.out.println("Summary:");
        System.out.println("- Channel uploaded 4 videos");
        System.out.println("- Multiple subscribers with different notification types");
        System.out.println("- Dynamic subscribe/unsubscribe demonstrated");
        System.out.println("- Automatic notification on each upload");
        System.out.println("- Loose coupling: Channel doesn't know concrete subscriber types");
        System.out.println("========================================");
    }
}
```

---

## 6. Kết quả chạy chương trình

```
========================================
    OBSERVER PATTERN DEMO
    YouTube Channel Notification
========================================
YouTube Channel 'TechReview Pro' created!


=== TEST 1: Multiple Subscriptions ===

Alice Johnson subscribing via Email...
  ✓ New subscriber added. Total: 1
  → Subscribed successfully!

Bob Smith subscribing via Mobile App...
  ✓ New subscriber added. Total: 2
  → Subscribed successfully!

Charlie Brown subscribing via SMS...
  ✓ New subscriber added. Total: 3
  → Subscribed successfully!


=== TEST 2: First Video Upload ===

🎥 [TechReview Pro] Uploading new video...
   Title: iPhone 16 Pro Review
   Description: Complete review of Apple's latest flagship phone with camera tests and performance benchmarks.

📢 Notifying 3 subscribers...
---------------------------------------

📧 [Email Notification]
   To: Alice Johnson <alice@email.com>
   Subject: New video from TechReview Pro
   Body:
   -------------
   Hi Alice Johnson,
   TechReview Pro just uploaded: iPhone 16 Pro Review
   Complete review of Apple's latest flagship phone with camera tests and performance benchmarks.
   Watch now: https://youtube.com/watch?v=xyz123
   -------------

📱 [Mobile Push Notification]
   Device: iPhone-12345
   User: Bob Smith
   🔔 TechReview Pro uploaded:
   "iPhone 16 Pro Review"
   Tap to watch now!

💬 [SMS Notification]
   To: +1-555-0123 (Charlie Brown)
   Message:
   New video from TechReview Pro: iPhone 16 Pro Review
---------------------------------------


=== TEST 3: Unsubscribe ===

Charlie Brown unsubscribing via SMS...
  ✓ Subscriber removed. Total: 2
  → Unsubscribed successfully!


=== TEST 4: Second Video Upload (After Unsubscribe) ===

🎥 [TechReview Pro] Uploading new video...
   Title: MacBook Pro M4 Unboxing
   Description: First look at the new MacBook Pro with M4 chip. Unboxing and initial impressions.

📢 Notifying 2 subscribers...
---------------------------------------

📧 [Email Notification]
   To: Alice Johnson <alice@email.com>
   Subject: New video from TechReview Pro
   Body:
   -------------
   Hi Alice Johnson,
   TechReview Pro just uploaded: MacBook Pro M4 Unboxing
   First look at the new MacBook Pro with M4 chip. Unboxing and initial impressions.
   Watch now: https://youtube.com/watch?v=xyz123
   -------------

📱 [Mobile Push Notification]
   Device: iPhone-12345
   User: Bob Smith
   🔔 TechReview Pro uploaded:
   "MacBook Pro M4 Unboxing"
   Tap to watch now!
---------------------------------------
(Note: Charlie không còn nhận notification vì đã unsubscribe)


=== TEST 5: New Subscriber Joins ===

Diana Prince subscribing via Mobile App...
  ✓ New subscriber added. Total: 3
  → Subscribed successfully!


=== TEST 6: Third Video Upload (With New Subscriber) ===

🎥 [TechReview Pro] Uploading new video...
   Title: Top 5 Gadgets of 2024
   Description: My favorite tech gadgets this year including smartphones, laptops, and accessories.

📢 Notifying 3 subscribers...
---------------------------------------

📧 [Email Notification]
   To: Alice Johnson <alice@email.com>
   Subject: New video from TechReview Pro
   Body:
   -------------
   Hi Alice Johnson,
   TechReview Pro just uploaded: Top 5 Gadgets of 2024
   My favorite tech gadgets this year including smartphones, laptops, and accessories.
   Watch now: https://youtube.com/watch?v=xyz123
   -------------

📱 [Mobile Push Notification]
   Device: iPhone-12345
   User: Bob Smith
   🔔 TechReview Pro uploaded:
   "Top 5 Gadgets of 2024"
   Tap to watch now!

📱 [Mobile Push Notification]
   Device: Android-98765
   User: Diana Prince
   🔔 TechReview Pro uploaded:
   "Top 5 Gadgets of 2024"
   Tap to watch now!
---------------------------------------


=== TEST 7: Re-subscribe ===

Charlie Brown subscribing via SMS...
  ✓ New subscriber added. Total: 4
  → Subscribed successfully!


=== TEST 8: Final Video Upload ===

🎥 [TechReview Pro] Uploading new video...
   Title: AirPods Pro 3 vs Sony WF-1000XM5
   Description: Head-to-head comparison of the best wireless earbuds. Sound quality, ANC, and battery life tested.

📢 Notifying 4 subscribers...
---------------------------------------

📧 [Email Notification]
   To: Alice Johnson <alice@email.com>
   Subject: New video from TechReview Pro
   Body:
   -------------
   Hi Alice Johnson,
   TechReview Pro just uploaded: AirPods Pro 3 vs Sony WF-1000XM5
   Head-to-head comparison of the best wireless earbuds. Sound quality, ANC, and battery life tested.
   Watch now: https://youtube.com/watch?v=xyz123
   -------------

📱 [Mobile Push Notification]
   Device: iPhone-12345
   User: Bob Smith
   🔔 TechReview Pro uploaded:
   "AirPods Pro 3 vs Sony WF-1000XM5"
   Tap to watch now!

📱 [Mobile Push Notification]
   Device: Android-98765
   User: Diana Prince
   🔔 TechReview Pro uploaded:
   "AirPods Pro 3 vs Sony WF-1000XM5"
   Tap to watch now!

💬 [SMS Notification]
   To: +1-555-0123 (Charlie Brown)
   Message:
   New video from TechReview Pro: AirPods Pro 3 vs Sony WF-1000XM5
---------------------------------------


========================================
Summary:
- Channel uploaded 4 videos
- Multiple subscribers with different notification types
- Dynamic subscribe/unsubscribe demonstrated
- Automatic notification on each upload
- Loose coupling: Channel doesn't know concrete subscriber types
========================================
```

### Giải thích cách pattern hoạt động:

**1. Khởi tạo** (TEST 1):
- Tạo YouTubeChannel (Subject)
- Tạo 3 subscribers: Email, Mobile, SMS
- Mỗi subscriber tự đăng ký với channel qua `subscribe()`
- Channel add observers vào list

**2. First Upload** (TEST 2):
- Channel upload video → gọi `notifySubscribers()`
- Loop qua tất cả subscribers, gọi `update()` của từng observer
- Mỗi observer xử lý notification theo cách riêng:
  - Email: gửi email đầy đủ với description
  - Mobile: gửi push notification ngắn gọn
  - SMS: gửi text message

**3. Unsubscribe** (TEST 3):
- Charlie unsubscribe → remove khỏi list
- Next upload → Charlie không còn nhận notification

**4. Dynamic Subscription** (TEST 5):
- Diana subscribe sau → được add vào list
- Ngay lập tức nhận notifications cho videos tiếp theo

**5. Re-subscribe** (TEST 7):
- Charlie subscribe lại → add vào list again
- Nhận notifications như bình thường

**Key Points**:
- ✅ **Automatic**: `uploadVideo()` tự động notify, không cần manual calling
- ✅ **Loose Coupling**: Channel chỉ gọi `subscriber.update()`, không biết concrete type
- ✅ **Dynamic**: Subscribe/unsubscribe bất kỳ lúc nào
- ✅ **Extensible**: Có thể add `TelegramSubscriber`, `WebPushSubscriber` không cần sửa Channel

---

## 7. Sơ đồ UML

### Class Diagram:

```
┌─────────────────────────┐
│    <<interface>>        │
│       Channel           │
├─────────────────────────┤
│ + attach(Subscriber)    │
│ + detach(Subscriber)    │
│ + notifySubscribers()   │
│ + getChannelName()      │
└─────────────────────────┘
            △
            │ implements
            │
┌─────────────────────────────────────────┐
│         YouTubeChannel                  │
├─────────────────────────────────────────┤
│ - subscribers: List<Subscriber>         │
│ - channelName: String                   │
│ - latestVideo: String                   │
│ - videoDescription: String              │
├─────────────────────────────────────────┤
│ + YouTubeChannel(channelName: String)   │
│ + attach(subscriber: Subscriber): void  │
│ + detach(subscriber: Subscriber): void  │
│ + notifySubscribers(): void             │
│ + getChannelName(): String              │
│ + getLatestVideo(): String              │
│ + getVideoDescription(): String         │
│ + uploadVideo(title, desc): void        │
└─────────────────────────────────────────┘
            │
            │ maintains
            │ 1      *
            ▼
┌─────────────────────────┐
│    <<interface>>        │
│      Subscriber         │
├─────────────────────────┤
│ + update(videoTitle)    │
│ + subscribe()           │
│ + unsubscribe()         │
└─────────────────────────┘
            △
            │ implements
    ┌───────┼───────┐
    │       │       │
┌───────┐ ┌──────┐ ┌────────┐
│Email  │ │Mobile│ │  SMS   │
│Sub    │ │AppSub│ │  Sub   │
└───────┘ └──────┘ └────────┘
```

### Component Description:

**Interfaces**:
1. **Channel** (Subject Interface)
   - `attach(Subscriber)`: Add observer to list
   - `detach(Subscriber)`: Remove observer from list
   - `notifySubscribers()`: Notify all observers
   - `getChannelName()`: Return channel name

2. **Subscriber** (Observer Interface)
   - `update(String videoTitle)`: Receive notification from channel
   - `subscribe()`: Register self with channel
   - `unsubscribe()`: Unregister from channel

**Concrete Classes**:

3. **YouTubeChannel** (ConcreteSubject)
   - Implements: `Channel`
   - Fields:
     - `subscribers: List<Subscriber>` - maintains list of observers
     - `channelName: String` - channel name
     - `latestVideo: String` - current state
     - `videoDescription: String` - video details
   - Methods:
     - `uploadVideo(title, desc)` - changes state and triggers notification

4. **EmailSubscriber** (ConcreteObserver)
   - Implements: `Subscriber`
   - Fields:
     - `name: String`
     - `email: String`
     - `channel: YouTubeChannel` - reference to subject (pull model)
   - Behavior: Sends detailed email notification with description

5. **MobileAppSubscriber** (ConcreteObserver)
   - Implements: `Subscriber`
   - Fields:
     - `name: String`
     - `deviceId: String`
     - `channel: YouTubeChannel`
   - Behavior: Sends brief push notification

6. **SMSSubscriber** (ConcreteObserver)
   - Implements: `Subscriber`
   - Fields:
     - `name: String`
     - `phoneNumber: String`
     - `channel: YouTubeChannel`
   - Behavior: Sends short text message

7. **ObserverDemo** (Client)
   - Creates channel (subject)
   - Creates subscribers (observers)
   - Demonstrates subscribe/unsubscribe/upload scenarios

### Relationships:

- **YouTubeChannel** `implements` **Channel**
- **EmailSubscriber**, **MobileAppSubscriber**, **SMSSubscriber** `implement` **Subscriber**
- **YouTubeChannel** `maintains` **List<Subscriber>** (one-to-many aggregation)
- **ConcreteSubscribers** `use` **YouTubeChannel** (dependency for pull model)
- **ObserverDemo** `uses` all classes

### Interaction Flow:

```
1. Client creates YouTubeChannel
2. Client creates Subscribers (pass channel reference)
3. Subscribers call subscribe() → channel.attach(this)
4. Channel adds subscriber to list
5. Client calls channel.uploadVideo()
6. Channel updates state (latestVideo, description)
7. Channel calls notifySubscribers()
8. Channel loops through subscribers list
9. Channel calls subscriber.update() for each
10. Each subscriber handles notification differently
```

### UML Notes:
- Multiplicity: YouTubeChannel `1` → `*` Subscriber (one-to-many)
- Interface realization shown with dotted line and hollow triangle
- Aggregation shown with hollow diamond (Channel aggregates Subscribers)
- Dependency shown with dotted arrow (Subscribers depend on Channel for data)

---

## 8. Tổng kết

### Kết luận về bài toán:

**YouTube Channel Notification System** là một ví dụ điển hình của **Observer Pattern** trong thực tế, giải quyết vấn đề:

✅ **One-to-many dependency**: 1 channel → nhiều subscribers
✅ **Automatic notification**: Upload video → tự động notify all
✅ **Loose coupling**: Channel không biết concrete subscriber types
✅ **Dynamic subscription**: Subscribe/unsubscribe anytime
✅ **Different behaviors**: Mỗi subscriber xử lý notification theo cách riêng

Observer Pattern biến một hệ thống **tight coupling** (channel phải biết tất cả subscriber types) thành **loose coupling** (channel chỉ biết Subscriber interface), giúp code dễ maintain, extend, và test hơn.

### Ứng dụng thực tế của Observer Pattern:

**1. Event-Driven Systems**:
- GUI frameworks (button click → multiple listeners)
- JavaScript event listeners
- Android/iOS notification systems

**2. Pub/Sub Systems**:
- Message queues (RabbitMQ, Kafka)
- Social media notifications (Twitter, Facebook)
- Newsletter subscriptions

**3. MVC Architecture**:
- Model thay đổi → Views tự động update
- Data binding frameworks (Angular, React, Vue)

**4. Real-time Updates**:
- Stock market tickers
- Sports score updates
- Live chat applications
- IoT sensor networks

**5. Monitoring Systems**:
- Server monitoring (CPU, memory alerts)
- Application logs
- Performance metrics dashboards

### Khi nào nên dùng Observer Pattern:

✅ **Nên dùng khi**:
- Có one-to-many dependency giữa objects
- Nhiều objects cần react to state changes của một object
- Muốn loose coupling giữa subject và observers
- Số lượng observers thay đổi dynamically
- Cần broadcast notifications
- Event-driven systems

❌ **KHÔNG nên dùng khi**:
- Chỉ có 1 observer (dùng direct reference)
- Cần synchronous response với return value
- Notification order quan trọng (Observer không guarantee)
- Complex dependencies giữa observers (dùng Mediator)
- Performance critical (overhead của pattern)
- Observer phụ thuộc vào notification order

### Alternatives và Related Patterns:

**1. Mediator Pattern**:
- Khi observers cần communicate với nhau
- Complex interactions giữa multiple objects
- Centralized control hơn Observer

**2. Event Bus**:
- Decoupled hơn Observer
- Publishers và subscribers không biết nhau
- Event-based communication

**3. Pub/Sub (Publish-Subscribe)**:
- Similar to Observer nhưng thông qua message broker
- Asynchronous, distributed systems
- Scalable hơn cho large systems

**4. Reactive Programming**:
- RxJava, RxJS, Project Reactor
- Functional approach to Observer pattern
- Stream-based, composable operations

### Key Takeaways:

🎯 **Observer Pattern giải quyết**:
- One-to-many dependency với loose coupling
- Automatic notification mechanism
- Dynamic subscription management

⚠️ **Cần lưu ý**:
- Memory leaks (observers không unsubscribe)
- Cascading updates (observer modify subject)
- Notification order không được guarantee
- Exception handling trong notification loop

💡 **Best Practices**:
- Always implement unsubscribe/cleanup
- Handle exceptions trong notify loop
- Document that order doesn't matter
- Consider async notifications nếu có nhiều observers
- Use weak references nếu lo memory leaks

Observer Pattern là một trong những patterns phổ biến và hữu ích nhất, đặc biệt trong event-driven programming. Hiểu rõ pattern này giúp bạn thiết kế systems linh hoạt, maintainable, và scalable hơn.
