# ÔN TẬP DESIGN PATTERNS - CÂU HỎI THI

**Dựa trên code thực tế trong project**

## 1. PROXY PATTERN

### Câu hỏi: Video thật class nào, video ảo class nào, khi nào thì tải video thật, ưu điểm của proxy?

**Trả lời:**

#### Video thật: [`RealVideo`](8-Proxy-DP/RealVideo.java:1) class
- Chứa video thực tế (~500MB)
- Tốn 2 giây để load từ disk
- Thực hiện công việc thực tế: `loadVideoFromDisk()`

#### Video ảo: [`VideoProxy`](8-Proxy-DP/VideoProxy.java:1) class  
- Chỉ chứa metadata nhẹ (title, duration)
- Không load video thật khi tạo
- Giữ reference đến `RealVideo realVideo = null`

#### Khi nào tải video thật?
**Lazy Loading** - chỉ tải khi thực sự cần:
```java
public void play() {
    if (realVideo == null) {  // ← Chưa load
        System.out.println("[VideoProxy] Lazy loading...");
        realVideo = new RealVideo(filename);  // ← Tải ngay lúc này!
    }
    realVideo.play();  // Delegate to real
}
```

#### Ưu điểm của Proxy:
1. **Lazy Loading**: Tiết kiệm memory, chỉ load khi cần
2. **Fast Initial Display**: Hiển thị danh sách video ngay lập tức
3. **Caching**: Load 1 lần, dùng nhiều lần
4. **Control Access**: Kiểm soát việc truy cập RealVideo

---

## 2. FACTORY METHOD PATTERN

### Câu hỏi: Cái nào là product, cái nào là factory, chỉ phương thức factory, ưu điểm của factory?

**Trả lời:**

#### Product: [`VideoExporter`](12-Factory-Method-DP/VideoExporter.java:1) interface
- Sản phẩm trừu tượng với method `export()`
- Concrete products: `MP4Exporter`, `AVIExporter`, `MOVExporter`, `WebMExporter`

#### Factory: [`ExporterFactory`](12-Factory-Method-DP/ExporterFactory.java:1) abstract class
- Creator trừu tượng
- Concrete factories: `MP4ExporterFactory`, `AVIExporterFactory`, etc.

#### Phương thức Factory:
```java
// ← ĐÂY LÀ FACTORY METHOD!
public abstract VideoExporter createExporter();  

// Ví dụ implement:
public class MP4ExporterFactory extends ExporterFactory {
    @Override
    public VideoExporter createExporter() {
        return new MP4Exporter();  // ← Tạo product cụ thể
    }
}
```

#### Ưu điểm của Factory Method:
1. **Loose Coupling**: Client không phụ thuộc concrete classes
2. **Easy Extension**: Thêm format mới = tạo 1 class
3. **Consistency**: Đảm bảo object được tạo đúng cách
4. **Polymorphism**: Xử lý tất cả exporter giống nhau

---

## 3. BRIDGE PATTERN

### Câu hỏi: Chỉ cái nào là cầu?

**Trả lời:**

#### Cái là cầu: [`NotificationChannel`](4-Bridge-DP/NotificationChannel.java:1) interface

**Giải thích cấu trúc Bridge:**
```java
// Abstraction (Notification types)
Notification ←——————————————————————————————→ NotificationChannel
    ↑                                              ↑
    |                                              |
AlertNotification                              EmailChannel
MarketingNotification                          SMSChannel
SystemNotification                             SlackChannel
```

#### Tại sao NotificationChannel là cầu?
- **Kết nối 2 hierarchy**: Notification types ↔ Delivery channels
- **Decoupling**: Notification không biết cách gửi, Channel không biết nội dung
- **Bridge Implementation**:
```java
public abstract class Notification {
    protected NotificationChannel channel;  // ← Đây là cầu!
    
    public void send(String content) {
        String formatted = formatMessage(content);
        channel.sendMessage(formatted);  // ← Delegate qua cầu
    }
}
```

#### Lợi ích của Bridge:
- **Independent Evolution**: Thay đổi notification type hoặc channel độc lập
- **Runtime Bridge Switching**: `notification.setChannel(newChannel)`
- **N×M → N+M**: 3 notifications × 3 channels = 6 classes thay vì 9 classes

---

## 4. COMMAND PATTERN

### Câu hỏi: Receiver và Invoker, lợi ích của việc dùng command pattern?

**Trả lời:**

#### Receiver: [`VideoClip`](19-Command-DP/VideoClip.java:1) class
- Đối tượng thực hiện công việc thật
- Biết cách thực hiện operations: `addTextOverlay()`, `setBrightness()`
- Không biết gì về Command pattern

#### Invoker: [`VideoEditor`](19-Command-DP/VideoEditor.java:1) class  
- Quản lý và thực thi commands
- Duy trì undo/redo stacks
- **Không biết gì về VideoClip!** (Perfect decoupling)

```java
// Invoker (VideoEditor) structure:
private Stack<Command> undoStack;   // Commands có thể undo
private Stack<Command> redoStack;   // Commands có thể redo
private VideoClip video;            // Receiver

public void executeCommand(Command command) {
    command.execute();              // Delegate to command
    undoStack.push(command);        // Store for undo
}
```

#### Lợi ích của Command Pattern:
1. **Undo/Redo Functionality**: 100% operations support undo
2. **Perfect Decoupling**: Invoker không biết Receiver internals
3. **Extensibility**: Thêm command mới = tạo 1 class
4. **Operation Logging**: Track tất cả operations trong history
5. **Macro Commands**: Group nhiều operations thành 1

**Ví dụ concrete**: Client → VideoEditor (invoker) → AddTextCommand → VideoClip (receiver)

---

## 5. COMPOSITE PATTERN

### Câu hỏi: Phương thức nào khiến mình phải dùng Composite? Trong nhóm có thể có 1 nhóm khác ko? Hàm nào thể hiện việc dùng composite?

**Trả lời:**

#### Phương thức khiến dùng Composite: [`add(MenuComponent)`](3-Composite-DP/MenuComponent.java:19) method
```java
public void add(MenuComponent component) {
    throw new UnsupportedOperationException();  // Base implementation
}

// Trong MenuCategory:
public void add(MenuComponent component) {
    menuComponents.add(component);  // ← Composite behavior!
}
```

#### Trong nhóm có thể có nhóm khác không? **CÓ!**
```java
public class MenuCategory extends MenuComponent {
    private ArrayList<MenuComponent> menuComponents;  // ← Chứa cả Leaf và Composite!
    
    // Có thể add MenuItem (leaf) hoặc MenuCategory khác (composite)
    menu.add(new MenuItem("Pizza", "Delicious", 15.0));          // Leaf
    menu.add(new MenuCategory("Desserts", "Sweet treats"));     // Composite!
}
```

#### Hàm thể hiện Composite (không phải pattern khác):
**[`display()`](3-Composite-DP/MenuCategory.java:49) method với recursive call:**
```java
public void display() {
    System.out.println(getName() + " - " + getDescription());
    
    for (MenuComponent component : menuComponents) {
        component.display();  // ← Recursive! Composite đặc trưng
    }
}
```

**Tại sao đây là Composite không phải pattern khác?**
- **Uniform Interface**: MenuCategory và MenuItem cùng extend MenuComponent
- **Tree Structure**: Category chứa Items hoặc Categories khác  
- **Recursive Operations**: `display()`, `getPrice()` xử lý cả tree
- **Client Transparency**: Client xử lý Leaf và Composite giống nhau

---

## 6. ƯU ĐIỂM CỦA MEDIATOR, OBSERVER, FLYWEIGHT

### A. MEDIATOR PATTERN - [`SmartHomeController`](7-Mediator-DP/SmartHomeController.java:1)

#### Ưu điểm:
1. **Loose Coupling**: Devices không biết nhau, chỉ biết hub
2. **Centralized Logic**: Tất cả coordination logic ở 1 chỗ
3. **Easy Maintenance**: Thay đổi behavior chỉ sửa mediator
4. **Reusable Components**: Device classes độc lập, tái sử dụng được

#### Ví dụ thực tế:
```java
// Without Mediator: MotionSensor phải biết tất cả devices
// With Mediator: 
public void notify(SmartDevice device, String event) {
    switch(event) {
        case "motion_detected":
            securityCamera.startRecording();  // ← Mediator coordinate
            smartLight.turnOn(100);
            break;
    }
}
```

### B. OBSERVER PATTERN - [`Channel`](6-Observer-DP/Channel.java:1) interface

#### Ưu điểm:
1. **Dynamic Relationships**: Subscribe/unsubscribe runtime
2. **Broadcast Communication**: 1 subject → nhiều observers
3. **Open/Closed Principle**: Thêm observer mới không sửa subject
4. **Loose Coupling**: Subject không biết concrete observers

#### Ví dụ thực tế:
```java
// YouTube channel upload video → notify all subscribers
channel.attach(emailSubscriber);     // Subscribe
channel.attach(mobileAppSubscriber);
channel.notifySubscribers();         // Broadcast to all!
```

### C. FLYWEIGHT PATTERN - [`IconFactory`](10-Flyweight-DP/IconFactory.java:1)

#### Ưu điểm:
1. **Memory Optimization**: Share common state giữa nhiều objects
2. **Performance**: Giảm object creation overhead
3. **Object Pool**: Reuse existing flyweights thay vì tạo mới
4. **Scalability**: Support hàng ngàn objects với ít memory

#### Ví dụ thực tế:
```java
// 1000 videos but only 4 unique icon types
VideoIcon playIcon = IconFactory.getIcon("play");  // Created once
VideoIcon sameIcon = IconFactory.getIcon("play");  // Reused!

// Statistics: 1000 requests → 4 objects created, 996 reused
// Memory saved: 99.6%
```

**Intrinsic state**: Icon shape, color (shared)
**Extrinsic state**: Position, video title (passed in render())

---

## 7. SINGLETON PATTERN

### Câu hỏi: Singleton có mấy cách implement, cách nào an toàn với thread, ưu điểm nhược điểm của singleton?

**Trả lời:**

#### Implementation trong code: Bill Pugh Singleton (Inner Static Helper Class)

**[`ConfigurationManager`](5-Singleton-DP/ConfigurationManager.java:1) sử dụng Bill Pugh method:**
```java
public class ConfigurationManager {
    // Private constructor
    private ConfigurationManager() {
        properties = new Properties();
        cache = new HashMap<>();
        loadConfiguration();  // ← Khởi tạo config
    }

    // Inner static helper class ← BILL PUGH METHOD
    private static class SingletonHelper {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }

    // Public method to get instance
    public static ConfigurationManager getInstance() {
        return SingletonHelper.INSTANCE;  // ← Thread-safe!
    }
}
```

#### Các cách implement Singleton khác:

1. **Eager Initialization** (Sớm):
```java
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();  // ← Tạo ngay
    public static Singleton getInstance() { return INSTANCE; }
}
```

2. **Lazy Initialization** (Muộn - KHÔNG thread-safe):
```java
public class Singleton {
    private static Singleton instance;
    public static Singleton getInstance() {
        if (instance == null) {          // ← Race condition!
            instance = new Singleton();
        }
        return instance;
    }
}
```

3. **Synchronized Method** (Thread-safe nhưng chậm):
```java
public static synchronized Singleton getInstance() {  // ← Synchronized
    if (instance == null) {
        instance = new Singleton();
    }
    return instance;
}
```

4. **Double-Checked Locking** (Thread-safe + performance):
```java
private static volatile Singleton instance;  // ← Volatile quan trọng!
public static Singleton getInstance() {
    if (instance == null) {                  // First check
        synchronized (Singleton.class) {
            if (instance == null) {          // Second check
                instance = new Singleton();
            }
        }
    }
    return instance;
}
```

5. **Bill Pugh (Trong code)** - **TỐT NHẤT**:
```java
private static class SingletonHelper {
    private static final Singleton INSTANCE = new Singleton();
}
public static Singleton getInstance() {
    return SingletonHelper.INSTANCE;  // JVM đảm bảo thread-safe
}
```

#### Cách nào an toàn với thread?
1. ✅ **Eager Initialization** - Thread-safe
2. ❌ **Lazy Initialization** - KHÔNG thread-safe
3. ✅ **Synchronized Method** - Thread-safe (nhưng chậm)
4. ✅ **Double-Checked Locking** - Thread-safe (nhanh)
5. ✅ **Bill Pugh (code hiện tại)** - Thread-safe (tốt nhất)

#### Ưu điểm của Singleton:
1. **Global Access**: Truy cập từ mọi nơi trong ứng dụng
2. **Single Instance**: Chỉ 1 instance trong toàn bộ app
3. **Resource Sharing**: Share tài nguyên (database, config, logger)
4. **Memory Efficient**: Tiết kiệm bộ nhớ

#### Nhược điểm của Singleton:
1. **Global State**: Khó test, khó debug
2. **Hidden Dependencies**: Classes phụ thuộc Singleton không rõ ràng
3. **Violates SRP**: Quản lý instance + business logic
4. **Difficult to Mock**: Khó mock cho unit testing

#### Ví dụ thực tế trong code:
```java
// Cả 2 modules dùng CÙNG 1 ConfigurationManager instance
ConfigurationManager config1 = ConfigurationManager.getInstance();  // InventoryModule
ConfigurationManager config2 = ConfigurationManager.getInstance();  // SalesModule

// config1.hashCode() == config2.hashCode()  ← Same object!
```

**Use cases phù hợp**: Configuration, Logger, Database Connection Pool, Cache

---

## 📝 TIPS GHI NHỚ

### Proxy: "Đại diện" - Lazy loading video
### Factory: "Nhà máy" - `createExporter()` method
### Bridge: "Cầu nối" - NotificationChannel interface
### Command: "Đóng gói lệnh" - VideoEditor (invoker), VideoClip (receiver)
### Composite: "Cây phân cấp" - `add()` method, recursive `display()`
### Singleton: "Duy nhất" - Bill Pugh method, thread-safe
### Mediator: "Trung gian" - SmartHomeController coordinate devices
### Observer: "Quan sát" - Channel notify subscribers
### Flyweight: "Chia sẻ" - IconFactory pool reuse objects

---

**Lưu ý:** Tất cả ví dụ và code snippets dựa trên implementation thực tế trong project, không phải lý thuyết!