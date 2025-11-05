# Plan Task: Tạo Bài Toán Mới Cho Observer Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Observer Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Observer Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu

- Đọc file `Documents/Lectures/Observer.pdf` để:
  - Hiểu BẢN CHẤT của Observer Pattern (không phải học bài toán cụ thể)
  - Hiểu quan hệ one-to-many dependency giữa Subject và Observers
  - Hiểu cơ chế notify tự động khi state thay đổi
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram với Subject, Observer, ConcreteSubject, ConcreteObserver
  - Hiểu các phương thức chính: attach(), detach(), notify(), update()

- Đọc code trong `Code-Sample/ObserverPattern-Lesson-7/` để:
  - Học cách đặt tên class, method, interface
  - Học coding convention và code style
  - Học cách implement Subject và Observer interfaces
  - Học cách maintain danh sách observers
  - Học cách notify tất cả observers
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế có quan hệ one-to-many (1 Subject → nhiều Observers):
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: Weather Station (quá phổ biến)
- **KHÔNG dùng**: Stock Market/Stock Ticker (có trong code sample)
- **KHÔNG dùng**: News Agency (có trong code sample)

**Gợi ý các lĩnh vực có thể dùng (1 subject, nhiều observers)**:

1. **Social Media Platform**:
   - Subject: User/Influencer posting updates
   - Observers: Followers receiving notifications
   - Ví dụ: SocialMediaUser, Follower

2. **Job Board System**:
   - Subject: Job Board với job postings
   - Observers: Job seekers với different criteria
   - Ví dụ: JobBoard, JobSeeker

3. **Auction System**:
   - Subject: Auction Item với price updates
   - Observers: Bidders receiving price changes
   - Ví dụ: AuctionItem, Bidder

4. **Traffic Light System**:
   - Subject: Traffic Light thay đổi màu
   - Observers: Vehicles, Pedestrians responding
   - Ví dụ: TrafficLight, Vehicle, Pedestrian

5. **E-commerce Product**:
   - Subject: Product với stock/price updates
   - Observers: Customers wanting notifications
   - Ví dụ: Product, Customer

6. **Event Management**:
   - Subject: Event với updates (time, venue, etc.)
   - Observers: Attendees receiving notifications
   - Ví dụ: Event, Attendee

7. **Smart Home System**:
   - Subject: Sensor (temperature, motion, etc.)
   - Observers: Devices responding to sensor changes
   - Ví dụ: TemperatureSensor, Thermostat, Display, AlertSystem

8. **Delivery Tracking**:
   - Subject: Package/Order status
   - Observers: Customer, Warehouse, Delivery service
   - Ví dụ: DeliveryOrder, Customer, Warehouse

9. **Game Score System**:
   - Subject: Game score/state
   - Observers: Scoreboard, Achievement system, Leaderboard
   - Ví dụ: GameState, Scoreboard, AchievementTracker

10. **Restaurant Order System**:
    - Subject: Order status updates
    - Observers: Customer, Kitchen, Waiter, Delivery
    - Ví dụ: Order, Customer, KitchenDisplay, Waiter

11. **Classroom Notification**:
    - Subject: Teacher announces homework/grades
    - Observers: Students receiving different notifications
    - Ví dụ: Teacher, Student, ParentPortal

12. **Server Monitoring**:
    - Subject: Server status/metrics
    - Observers: Admin, Logger, AlertSystem, Dashboard
    - Ví dụ: Server, AdminPanel, Logger, AlertSystem

13. **Youtube Channel**:
    - Subject: Channel posts new video
    - Observers: Subscribers with different notification preferences
    - Ví dụ: YouTubeChannel, Subscriber

14. **Bank Account**:
    - Subject: Account balance changes
    - Observers: Mobile app, Email service, SMS service, Fraud detection
    - Ví dụ: BankAccount, MobileApp, EmailNotifier

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có
1. **One Subject**: Một đối tượng làm nguồn thông tin
2. **Multiple Observers**: Nhiều observers quan tâm đến subject
3. **Dynamic attach/detach**: Có thể thêm/bớt observers lúc runtime
4. **Automatic notification**: Subject tự động notify khi state thay đổi
5. **Loose coupling**: Subject không cần biết chi tiết về observers

**Đặc điểm của Observer Pattern**:
- **Subject (Observable)**: Maintains list of observers, có attach/detach/notify
- **Observer**: Interface với update() method
- **ConcreteSubject**: Implements Subject, có state và notifyObservers()
- **ConcreteObserver**: Implements Observer, có update() nhận notification
- **Pull model**: Observer lấy data từ Subject khi cần
- **Push model**: Subject gửi data trong notification

**Quan trọng**:
- Subject phải có list/collection chứa observers
- Phải có attach(Observer) và detach(Observer) methods
- Phải có notifyObservers() để gọi update() của tất cả observers
- Observer interface phải có update() method
- ConcreteObserver implement update() để xử lý notification
- Demo phải cho thấy multiple observers receiving notifications

#### 2.3. Thiết kế các thành phần

**Subject Interface or Abstract Class**:
```java
public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}
```

**Observer Interface**:
```java
public interface Observer {
    void update();
    // hoặc update(Object data) nếu dùng push model
}
```

**ConcreteSubject**:
```java
public class ConcreteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String state;  // hoặc các state fields khác

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void setState(String state) {
        this.state = state;
        notifyObservers();  // Auto notify when state changes
    }

    public String getState() {
        return state;
    }
}
```

**ConcreteObserver**:
```java
public class ConcreteObserver implements Observer {
    private String name;
    private ConcreteSubject subject;

    public ConcreteObserver(String name, ConcreteSubject subject) {
        this.name = name;
        this.subject = subject;
        subject.attach(this);  // Auto register
    }

    @Override
    public void update() {
        String state = subject.getState();  // Pull model
        System.out.println(name + " received update: " + state);
    }
}
```

#### 2.4. Push vs Pull Model
**Pull Model** (recommended):
- Observer gọi subject.getState() để lấy data khi cần
- Linh hoạt hơn, observer chọn data cần lấy
- Subject không cần biết observer cần data gì

**Push Model**:
- Subject truyền data vào update(data)
- Ít linh hoạt hơn
- Subject phải biết data nào cần gửi

### Bước 3: Viết Documents/Solutions/Observer.md

Tạo file `Documents/Solutions/Observer.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Observer
- Giữ phần mô tả tổng quan về Observer Pattern
- Các thành phần chính: Subject, Observer, ConcreteSubject, ConcreteObserver
- Khi nào sử dụng: one-to-many dependency
- Đặc điểm quan trọng: automatic notification, loose coupling, dynamic subscription

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- Bài toán cần:
  - Có 1 Subject phát ra thông tin/state changes
  - Có nhiều Observers quan tâm đến Subject
  - Nêu rõ vấn đề nếu không dùng Observer pattern (tight coupling, manual notification)
  - Giải thích tại sao cần automatic notification
  - Có tình huống cụ thể minh họa với ít nhất 2-3 observers

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Có một đối tượng (Subject) thay đổi state thường xuyên
- Có nhiều đối tượng khác (Observers) cần biết khi state thay đổi
- Observers có thể thêm/bớt dynamically
- Cần loose coupling giữa Subject và Observers

**Problem**: Vấn đề phức tạp cần giải quyết
- Tight coupling nếu Subject gọi trực tiếp từng Observer
- Khó maintain khi thêm/bớt observers
- Subject phải biết tất cả observers và cách notify chúng
- Code duplication trong notification logic
- Khó test và extend

**Solution**: Cách Observer pattern giải quyết
- Subject chỉ biết Observer interface, không biết concrete observers
- Observers tự đăng ký (attach) và hủy đăng ký (detach)
- Subject tự động notify tất cả observers khi state thay đổi
- Dễ dàng thêm observers mới không cần sửa Subject
- Loose coupling, dễ test, dễ maintain

**Expected Output**: Kết quả mong đợi
- Subject notify được tất cả registered observers
- Observers nhận notification và xử lý theo cách riêng
- Có thể attach/detach observers lúc runtime
- Subject không cần biết logic của từng observer

#### 3.4. Hiệu quả của việc sử dụng Observer Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh với cách không dùng Observer (tight coupling)
- Loose coupling giữa Subject và Observers
- Open/Closed Principle: thêm observers không sửa Subject
- Reusability và maintainability
- Trade-offs: memory overhead, notification order, cascading updates

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Phải có:
  - 1 Subject interface/abstract class
  - 1 Observer interface
  - 1 ConcreteSubject class
  - Ít nhất 2-3 ConcreteObserver classes (để thể hiện multiple observers)
  - 1 Demo/Main class
- Code phải hoàn chỉnh và có thể compile
- Coding style học từ code sample

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Demo subject thay đổi state nhiều lần
- Demo multiple observers nhận notifications
- Demo attach và detach observers
- Giải thích cách pattern hoạt động qua output

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Phải có:
  - Subject interface/abstract class
  - Observer interface
  - ConcreteSubject implementing Subject
  - Multiple ConcreteObservers implementing Observer
  - Association từ Subject đến Observer (maintains list)
  - Dependencies từ ConcreteObservers đến ConcreteSubject (nếu dùng pull model)
- Thể hiện methods: attach(), detach(), notify(), update()

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Observer
- Alternatives: Event listeners, Pub/Sub systems

### Bước 4: Viết code Java cho bài toán mới trong 6-Observer-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `6-Observer-DP/`:

**Interfaces**:
- Subject.java (interface or abstract class)
- Observer.java (interface)

**Concrete Classes**:
- ConcreteSubject.java (1 class)
- ConcreteObserver1.java, ConcreteObserver2.java, ConcreteObserver3.java (2-3 classes)

**Demo**:
- ObserverDemo.java (Main class)

**Ví dụ cấu trúc**:
```
6-Observer-DP/
├── Subject.java              (Interface)
├── Observer.java             (Interface)
├── ConcreteSubject.java      (Subject implementation)
├── ConcreteObserver1.java    (Observer implementation 1)
├── ConcreteObserver2.java    (Observer implementation 2)
├── ConcreteObserver3.java    (Observer implementation 3)
└── ObserverDemo.java         (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: ArrayList, List
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Subject phải có List<Observer>
- notify() phải loop qua tất cả observers

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Observer pattern
- Demo rõ ràng multiple observers
- Demo attach/detach functionality
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Observer.md

#### 4.4. Demo Requirements
Demo phải thể hiện:
1. Tạo Subject
2. Tạo multiple Observers (ít nhất 2-3)
3. Attach observers vào subject
4. Subject thay đổi state → tất cả observers nhận notification
5. Detach một observer
6. Subject thay đổi state lại → chỉ remaining observers nhận notification
7. Có thể attach lại observer đã detach

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `6-Observer-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Subject và Observer interfaces
- Hiển thị ConcreteSubject và các ConcreteObservers
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationships:
  - ConcreteSubject implements Subject
  - ConcreteObservers implement Observer
  - Subject has association to Observer (maintains list)
  - ConcreteObservers may have association to ConcreteSubject (pull model)
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=ConcreteSubject
dependency1.to=Subject
dependency1.type=ImplementsDependency

dependency2.from=ConcreteSubject
dependency2.to=Observer
dependency2.type=UsesDependency

dependency3.from=ConcreteObserver1
dependency3.to=Observer
dependency3.type=ImplementsDependency

dependency4.from=ConcreteObserver1
dependency4.to=ConcreteSubject
dependency4.type=UsesDependency

# Similar for other observers...

# Targets với showInterface=true
target1.name=Subject
target1.type=InterfaceTarget
target1.showInterface=true
target1.width=...
target1.height=...
target1.x=...
target1.y=...

target2.name=Observer
target2.type=InterfaceTarget
target2.showInterface=true
...

target3.name=ConcreteSubject
target3.type=ClassTarget
target3.showInterface=true
...

target4.name=ConcreteObserver1
target4.type=ClassTarget
target4.showInterface=true
...
```

#### 5.3. Layout gợi ý
```
Interfaces ở trên:
- Subject (left top)
- Observer (right top)

Concrete classes ở giữa:
- ConcreteSubject (left middle, dưới Subject)
- ConcreteObserver1, ConcreteObserver2, ConcreteObserver3 (right middle, dưới Observer)

Demo class ở dưới:
- ObserverDemo (bottom center)
```

## Deliverables

### 1. File Documents/Solutions/Observer.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ one-to-many dependency
- Minh họa automatic notification

### 2. Folder 6-Observer-DP/
Chứa các file Java cho bài toán MỚI:
- Subject interface/abstract class
- Observer interface
- 1 ConcreteSubject
- 2-3 ConcreteObservers (để thể hiện multiple observers)
- 1 Demo/Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Demo rõ attach/detach/notify

### 3. File 6-Observer-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ interfaces và implementations
- Thể hiện rõ relationships (implements, uses)

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **One-to-many dependency**: Rõ ràng 1 Subject → nhiều Observers
✅ **State changes**: Subject có state thay đổi và notify observers
✅ **Multiple observers**: Ít nhất 2-3 observers với behaviors khác nhau
✅ **Dynamic subscription**: Demo attach/detach observers
✅ **Automatic notification**: Khi state thay đổi, tự động notify
✅ **Loose coupling**: Subject không phụ thuộc vào concrete observers
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Đầy đủ Subject, Observer, attach, detach, notify
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Observer
✅ **Khác biệt**: Không trùng với lecture hay code sample

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy Weather Station (quá phổ biến)
❌ Copy Stock Market (có trong code sample)
❌ Copy News Agency (có trong code sample)
❌ Chỉ có 1 observer (không thể hiện one-to-many)
❌ Không có attach/detach functionality
❌ Notification không automatic (manual calling)
❌ Tight coupling (Subject biết concrete observers)
❌ Quá đơn giản: chỉ print message
❌ Quá phức tạp: quá nhiều business logic không liên quan

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "one-to-many dependency"
- Phải có ít nhất 2-3 observers khác nhau

### Về Observer Pattern
- Subject **PHẢI** có List<Observer> hoặc ArrayList<Observer>
- **PHẢI** có attach(Observer) method
- **PHẢI** có detach(Observer) method
- **PHẢI** có notifyObservers() method
- Observer interface **PHẢI** có update() method
- ConcreteObservers **PHẢI** implement Observer
- Demo **PHẢI** thể hiện multiple observers

### Về Implementation
- **Recommended**: Pull model (observer calls subject.getState())
- **Alternative**: Push model (subject passes data to update(data))
- Subject không nên biết concrete observer classes
- Observers tự register với Subject
- notifyObservers() loop qua tất cả observers và gọi update()

### Về Notification
- Automatic: gọi notifyObservers() trong setState() hoặc state-changing methods
- Update all observers: loop qua list và gọi observer.update()
- Order không quan trọng (hoặc có thể priority-based nếu advanced)
- Consider exception handling (nếu một observer throw exception)

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Subject có List<Observer>, methods add/remove/notify
- Observer interface đơn giản với update()
- ConcreteObservers implement update() với logic riêng
- Demo rõ ràng: create → attach → change state → notify → detach → change again

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Interfaces: Subject và Observer (InterfaceTarget)
- Implementations: ConcreteSubject và ConcreteObservers (ClassTarget)
- Relationships:
  - Implements: ConcreteSubject → Subject, ConcreteObserver → Observer
  - Uses: ConcreteSubject → Observer (maintains list)
  - Uses: ConcreteObserver → ConcreteSubject (if pull model)
- Format phải giống lecture

### Về documentation
- Documents/Solutions/Observer.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- Giải thích rõ ràng one-to-many dependency
- So sánh với cách không dùng Observer (tight coupling)
- Nêu rõ trade-offs (memory, complexity, cascading updates)

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Smart Home Temperature Sensor
- Subject: TemperatureSensor (nhiệt độ thay đổi)
- Observers: Thermostat (điều chỉnh nhiệt), Display (hiển thị), AlertSystem (cảnh báo)
- State: current temperature
- Methods: attach(), detach(), notifyObservers(), setTemperature(), getTemperature()

### Ví dụ 2: Job Board System
- Subject: JobBoard (đăng job mới)
- Observers: JobSeekers với different criteria (Java dev, Python dev, Manager)
- State: list of job postings
- Methods: attach(), detach(), notifyObservers(), addJob(), getJobs()

### Ví dụ 3: Auction System
- Subject: AuctionItem (giá bid thay đổi)
- Observers: Bidders quan tâm item
- State: current highest bid
- Methods: attach(), detach(), notifyObservers(), placeBid(), getCurrentBid()

### Ví dụ 4: Social Media Post
- Subject: SocialMediaUser (đăng bài mới)
- Observers: Followers nhận notification
- State: latest post
- Methods: attach(), detach(), notifyObservers(), post(), getLatestPost()

### Ví dụ 5: Package Delivery Tracking
- Subject: DeliveryOrder (status thay đổi)
- Observers: Customer, Warehouse, DeliveryDriver
- State: delivery status
- Methods: attach(), detach(), notifyObservers(), updateStatus(), getStatus()

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Ít nhất 2-3 concrete observers
- Demo rõ attach/detach/notify

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Observer
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán có yêu cầu rõ ràng về one-to-many dependency
- [ ] Có Subject interface/abstract class với attach/detach/notify
- [ ] Có Observer interface với update()
- [ ] Có ConcreteSubject implement Subject
- [ ] Có ít nhất 2-3 ConcreteObservers implement Observer
- [ ] Documents/Solutions/Observer.md có đầy đủ 8 sections
- [ ] Code trong 6-Observer-DP/ là code MỚI cho bài toán mới
- [ ] Demo thể hiện multiple observers
- [ ] Demo thể hiện attach/detach functionality
- [ ] Demo thể hiện automatic notification khi state thay đổi
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong Documents/Solutions/Observer.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram có Subject và Observer interfaces
- [ ] UML diagram có ConcreteSubject và ConcreteObservers
- [ ] UML diagram format giống lecture
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng one-to-many notification
- [ ] Đã giải thích trade-offs của Observer

## Common Pitfalls cần tránh

### Observer Pattern có thể gây vấn đề nếu:
❌ Memory leaks (observers không detach)
❌ Unexpected cascading updates (observer thay đổi subject trong update())
❌ Notification order matters (nhưng không được guarantee)
❌ Cyclic dependencies giữa observers
❌ Too many observers → performance issue
❌ Observer throws exception → break notification chain

### Best Practices:
✅ Observers nên detach khi không cần nữa
✅ Update() không nên thay đổi subject state (tránh infinite loop)
✅ Consider weak references để tránh memory leaks
✅ Xử lý exceptions trong notification loop
✅ Document notification order nếu quan trọng
✅ Consider async notification nếu có nhiều observers

### When to use Observer:
✅ Khi có one-to-many dependency
✅ Khi nhiều objects cần phản ứng với state changes
✅ Khi muốn loose coupling giữa subject và observers
✅ Khi số lượng observers có thể thay đổi dynamically
✅ Event-driven systems

### When NOT to use Observer:
❌ Khi chỉ có 1 observer (dùng direct reference)
❌ Khi relationship phức tạp hơn one-to-many
❌ Khi cần synchronous response (dùng callbacks)
❌ Khi performance critical và có quá nhiều observers
❌ Khi debugging khó (hard to track notification flow)
