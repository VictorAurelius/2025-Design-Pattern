# Plan Task: Tạo Bài Toán Mới Cho Mediator Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Mediator Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Mediator Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu

- Đọc file `Documents/Lectures/Mediator.pdf` để:
  - Hiểu BẢN CHẤT của Mediator Pattern (không phải học bài toán cụ thể)
  - Hiểu centralized communication giữa nhiều objects
  - Hiểu cách Mediator giảm coupling giữa các Colleagues
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram với Mediator, ConcreteMediator, Colleague, ConcreteColleagues
  - Hiểu phương thức notify() và mediate() operations

- Đọc code trong `Code-Sample/MediatorPattern-Project/` để:
  - Học cách đặt tên class, method, interface
  - Học coding convention và code style
  - Học cách implement Mediator và Colleague
  - Học cách colleagues communicate qua mediator
  - Học cách mediator coordinate interactions
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế có nhiều objects cần communicate với nhau (many-to-many):
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: Washing Machine (có trong code sample)
- **KHÔNG dùng**: Chat Room (quá phổ biến)
- **KHÔNG dùng**: Air Traffic Control (ví dụ phổ biến nhất)

**Điểm khác biệt với Observer Pattern**:
- **Observer**: One-to-many (1 subject → nhiều observers, broadcast)
- **Mediator**: Many-to-many (nhiều colleagues ↔ nhiều colleagues, qua central mediator)
- Observer: passive notification (observers chỉ nhận)
- Mediator: active coordination (colleagues có thể gửi và nhận qua mediator)

**Gợi ý các lĩnh vực có thể dùng (nhiều objects communicate qua mediator)**:

1. **Smart Home Control System**:
   - Mediator: Smart Home Hub
   - Colleagues: Lights, Thermostat, Security System, Blinds, Sensors
   - Interaction: Sensor phát hiện → Hub điều phối → nhiều devices phản ứng
   - Ví dụ: SmartHomeHub, Light, Thermostat, SecurityCamera

2. **Airport Gate Management**:
   - Mediator: Gate Coordinator
   - Colleagues: Gates, Flights, Baggage Systems, Boarding Staff
   - Interaction: Flight request gate → Coordinator assign → các systems cập nhật
   - Ví dụ: GateCoordinator, Gate, Flight, BaggageHandler

3. **Restaurant Kitchen System**:
   - Mediator: Kitchen Manager / Expeditor
   - Colleagues: Chefs, Waiters, Prep Stations, Inventory
   - Interaction: Order → Manager coordinates → chefs/prep stations work together
   - Ví dụ: KitchenManager, Chef, Waiter, PrepStation

4. **Stock Trading Platform**:
   - Mediator: Trading System / Exchange
   - Colleagues: Buyers, Sellers, Order Book, Pricing Engine
   - Interaction: Orders → System matches → nhiều parties thông báo
   - Ví dụ: TradingSystem, Buyer, Seller, OrderBook

5. **Smart Office Conference Room**:
   - Mediator: Room Controller
   - Colleagues: Projector, Lights, AC, Blinds, Door Lock, Calendar
   - Interaction: Meeting starts → Controller coordinates all devices
   - Ví dụ: RoomController, Projector, Light, AC

6. **Hospital Emergency System**:
   - Mediator: Emergency Coordinator
   - Colleagues: Doctors, Nurses, ER Rooms, Labs, Pharmacy
   - Interaction: Patient arrives → Coordinator assigns resources
   - Ví dụ: EmergencyCoordinator, Doctor, Nurse, ERRoom

7. **Parking Lot Management**:
   - Mediator: Parking Controller
   - Colleagues: Entry Gates, Exit Gates, Payment Kiosks, Display Boards
   - Interaction: Car enters → Controller updates → all displays/gates sync
   - Ví dụ: ParkingController, EntryGate, ExitGate, DisplayBoard

8. **Online Auction System**:
   - Mediator: Auction House
   - Colleagues: Bidders, Sellers, Payment System, Notification System
   - Interaction: Bids → Auction mediates → notify parties and process
   - Ví dụ: AuctionHouse, Bidder, Seller, PaymentProcessor

9. **Game Multiplayer Lobby**:
   - Mediator: Game Server / Lobby Manager
   - Colleagues: Players, Chat, Scoreboard, Match Maker
   - Interaction: Player actions → Server coordinates → update all
   - Ví dụ: GameServer, Player, ChatSystem, Scoreboard

10. **Call Center Routing**:
    - Mediator: Call Router
    - Colleagues: Agents, Customers, Queue System, Skill Matcher
    - Interaction: Call arrives → Router assigns → agent và systems update
    - Ví dụ: CallRouter, Agent, Customer, QueueSystem

11. **Smart Traffic Intersection**:
    - Mediator: Intersection Controller
    - Colleagues: Traffic Lights (4 directions), Pedestrian Signals, Sensors
    - Interaction: Sensors detect → Controller coordinates → lights change
    - Ví dụ: IntersectionController, TrafficLight, PedestrianSignal

12. **Library Management**:
    - Mediator: Library System
    - Colleagues: Books, Members, Checkout, Returns, Notifications
    - Interaction: Borrow request → System coordinates → multiple operations
    - Ví dụ: LibrarySystem, Book, Member, CheckoutDesk

13. **Team Collaboration Tool**:
    - Mediator: Collaboration Platform
    - Colleagues: Users, Chat, Files, Calendar, Tasks
    - Interaction: User shares → Platform notifies → multiple features update
    - Ví dụ: CollaborationPlatform, User, ChatModule, FileModule

14. **Supply Chain Coordination**:
    - Mediator: Supply Chain Manager
    - Colleagues: Suppliers, Warehouses, Transporters, Retailers
    - Interaction: Order → Manager coordinates → all parties work together
    - Ví dụ: SupplyChainManager, Supplier, Warehouse, Transporter

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có
1. **Multiple Colleagues**: Ít nhất 3-4 colleague types cần communicate
2. **Central Mediator**: Một mediator điều phối tất cả communications
3. **Complex Interactions**: Colleagues interact với nhau qua mediator, không directly
4. **Bidirectional**: Colleagues có thể send và receive messages
5. **Coordination Logic**: Mediator có business logic để coordinate interactions

**Đặc điểm của Mediator Pattern**:
- **Mediator Interface**: Định nghĩa communication methods
- **ConcreteMediator**: Implement coordination logic, biết tất cả colleagues
- **Colleague (abstract/interface)**: Reference đến Mediator
- **ConcreteColleagues**: Communicate qua mediator thay vì directly
- **Decoupling**: Colleagues không biết nhau, chỉ biết mediator
- **Centralized Control**: Tất cả interaction logic ở một chỗ

**Quan trọng**:
- Colleagues KHÔNG được communicate directly với nhau
- Colleagues phải có reference đến Mediator
- Mediator phải có references đến tất cả Colleagues
- Communication flow: Colleague → Mediator → Other Colleagues
- Mediator contains coordination/orchestration logic

#### 2.3. Thiết kế các thành phần

**Mediator Interface**:
```java
public interface Mediator {
    void notify(Colleague sender, String event);
    // Or specific methods for different operations
}
```

**ConcreteMediator**:
```java
public class ConcreteMediator implements Mediator {
    private ColleagueA colleagueA;
    private ColleagueB colleagueB;
    private ColleagueC colleagueC;

    // Setters to register colleagues
    public void setColleagueA(ColleagueA a) {
        this.colleagueA = a;
    }

    @Override
    public void notify(Colleague sender, String event) {
        // Coordination logic here
        if (sender == colleagueA && event.equals("action1")) {
            // Coordinate: tell B and C to do something
            colleagueB.doSomething();
            colleagueC.doSomethingElse();
        }
        // More coordination logic...
    }
}
```

**Colleague Abstract Class or Interface**:
```java
public abstract class Colleague {
    protected Mediator mediator;

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    // Colleagues call mediator when something happens
    public abstract void doAction();
}
```

**ConcreteColleagues**:
```java
public class ColleagueA extends Colleague {
    public ColleagueA(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void doAction() {
        System.out.println("ColleagueA doing action");
        // Notify mediator about this action
        mediator.notify(this, "action1");
    }

    // Mediator calls this when needed
    public void reactToSomething() {
        System.out.println("ColleagueA reacting");
    }
}
```

#### 2.4. Communication Flow
1. ColleagueA performs action
2. ColleagueA calls `mediator.notify(this, "event")`
3. Mediator receives notification
4. Mediator runs coordination logic
5. Mediator calls methods on other colleagues (ColleagueB, ColleagueC)
6. Other colleagues perform their actions

### Bước 3: Viết Documents/Solutions/Mediator.md

Tạo file `Documents/Solutions/Mediator.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Mediator
- Giữ phần mô tả tổng quan về Mediator Pattern
- Các thành phần chính: Mediator, ConcreteMediator, Colleague, ConcreteColleagues
- Khi nào sử dụng: many-to-many communication, complex interactions
- Đặc điểm quan trọng: centralized control, loose coupling, single point of coordination

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- Bài toán cần:
  - Có nhiều colleagues cần interact với nhau
  - Nêu rõ vấn đề nếu không dùng Mediator (tight coupling, complex dependencies)
  - Giải thích tại sao cần central coordination
  - Có tình huống cụ thể minh họa với ít nhất 3-4 colleagues

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Có nhiều objects cần communicate và coordinate với nhau
- Interactions phức tạp với nhiều rules
- Colleagues phụ thuộc lẫn nhau
- Cần centralized coordination logic

**Problem**: Vấn đề phức tạp cần giải quyết
- Direct coupling giữa colleagues → spaghetti code
- Thay đổi một colleague ảnh hưởng nhiều colleagues khác
- Logic interaction scattered across nhiều classes
- Khó maintain và extend
- Many-to-many relationships → O(n²) connections

**Solution**: Cách Mediator pattern giải quyết
- Mediator làm central hub cho communications
- Colleagues chỉ biết Mediator, không biết nhau
- Coordination logic centralized trong Mediator
- Dễ dàng modify interactions không ảnh hưởng colleagues
- Reduce từ O(n²) xuống O(n) connections

**Expected Output**: Kết quả mong đợi
- Colleagues communicate qua mediator
- Loose coupling giữa colleagues
- Centralized coordination logic dễ maintain
- Dễ dàng thêm colleagues mới
- Complex interactions được manage tốt

#### 3.4. Hiệu quả của việc sử dụng Mediator Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh với cách không dùng Mediator (direct coupling)
- Reduced coupling giữa colleagues
- Centralized control vs distributed control
- Easier to maintain và extend
- Trade-offs: Mediator có thể trở nên complex (God Object)

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Phải có:
  - 1 Mediator interface (hoặc abstract class)
  - 1 ConcreteMediator class
  - 1 Colleague abstract class (hoặc interface)
  - Ít nhất 3-4 ConcreteColleague classes
  - 1 Demo/Main class
- Code phải hoàn chỉnh và có thể compile
- Coding style học từ code sample

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Demo colleagues communicating qua mediator
- Demo coordination logic của mediator
- Giải thích cách pattern hoạt động qua output
- Show multiple interactions và how mediator coordinates

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Phải có:
  - Mediator interface
  - ConcreteMediator class
  - Colleague abstract class/interface
  - Multiple ConcreteColleagues
  - Associations: Mediator ↔ Colleagues (bidirectional)
- Thể hiện methods và relationships

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Mediator
- So sánh với Observer pattern

### Bước 4: Viết code Java cho bài toán mới trong 7-Mediator-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `7-Mediator-DP/`:

**Interfaces/Abstract Classes**:
- Mediator.java (interface or abstract class)
- Colleague.java (abstract class hoặc interface)

**Concrete Classes**:
- ConcreteMediator.java (1 class)
- ConcreteColleague1.java, ConcreteColleague2.java, ConcreteColleague3.java, ... (3-4 classes)

**Demo**:
- MediatorDemo.java (Main class)

**Ví dụ cấu trúc**:
```
7-Mediator-DP/
├── Mediator.java              (Interface)
├── Colleague.java             (Abstract class)
├── ConcreteMediator.java      (Mediator implementation)
├── ConcreteColleague1.java    (Colleague 1)
├── ConcreteColleague2.java    (Colleague 2)
├── ConcreteColleague3.java    (Colleague 3)
├── ConcreteColleague4.java    (Colleague 4 - optional)
└── MediatorDemo.java          (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: nếu cần
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Mediator có references đến ALL colleagues
- Colleagues có reference đến Mediator

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Mediator pattern
- Demo rõ ràng multiple colleagues interacting
- Demo coordination logic của mediator
- Colleagues KHÔNG communicate directly
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Mediator.md

#### 4.4. Demo Requirements
Demo phải thể hiện:
1. Tạo Mediator
2. Tạo multiple Colleagues (ít nhất 3-4)
3. Register colleagues với mediator
4. Colleague1 thực hiện action → gọi mediator
5. Mediator coordinate → gọi methods của other colleagues
6. Show multiple scenarios với different interactions
7. Demonstrate centralized coordination logic

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `7-Mediator-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Mediator interface
- Hiển thị ConcreteMediator và Colleagues
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationships:
  - ConcreteMediator implements Mediator
  - ConcreteColleagues extend/implement Colleague
  - Bidirectional associations: Mediator ↔ Colleagues
  - Mediator has associations to all ConcreteColleagues
  - Colleagues have association to Mediator
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=ConcreteMediator
dependency1.to=Mediator
dependency1.type=ImplementsDependency

dependency2.from=ConcreteMediator
dependency2.to=ConcreteColleague1
dependency2.type=UsesDependency

dependency3.from=ConcreteColleague1
dependency3.to=Colleague
dependency3.type=ImplementsDependency

dependency4.from=ConcreteColleague1
dependency4.to=Mediator
dependency4.type=UsesDependency

# Similar for other colleagues...

# Targets với showInterface=true
target1.name=Mediator
target1.type=InterfaceTarget
target1.showInterface=true
...

target2.name=Colleague
target2.type=AbstractTarget
target2.showInterface=true
...

target3.name=ConcreteMediator
target3.type=ClassTarget
target3.showInterface=true
...
```

#### 5.3. Layout gợi ý
```
Top center:
- Mediator interface

Middle left:
- ConcreteMediator (dưới Mediator)

Top right:
- Colleague abstract class

Middle right:
- ConcreteColleague1, ConcreteColleague2, ConcreteColleague3, ... (dưới Colleague)

Bottom center:
- MediatorDemo
```

## Deliverables

### 1. File Documents/Solutions/Mediator.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ centralized coordination
- Minh họa many-to-many interactions qua mediator

### 2. Folder 7-Mediator-DP/
Chứa các file Java cho bài toán MỚI:
- Mediator interface
- Colleague abstract class
- 1 ConcreteMediator
- 3-4 ConcreteColleagues
- 1 Demo/Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Demo rõ coordination qua mediator

### 3. File 7-Mediator-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ interfaces/abstract classes và implementations
- Thể hiện rõ bidirectional relationships

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Many-to-many interactions**: Nhiều colleagues cần communicate với nhau
✅ **Complex coordination**: Logic phức tạp cần centralized control
✅ **Multiple colleagues**: Ít nhất 3-4 colleagues khác nhau
✅ **Bidirectional**: Colleagues gửi messages VÀ nhận responses
✅ **Centralized logic**: Mediator chứa coordination logic
✅ **Loose coupling**: Colleagues không biết nhau, chỉ biết mediator
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Đầy đủ Mediator, Colleagues, coordination logic
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Mediator
✅ **Khác biệt**: Không trùng với lecture hay code sample

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy Washing Machine (có trong code sample)
❌ Copy Chat Room (quá phổ biến)
❌ Copy Air Traffic Control (ví dụ phổ biến nhất)
❌ Chỉ có 2 colleagues (không thể hiện complexity)
❌ Colleagues communicate directly (không đúng pattern)
❌ Mediator chỉ forward messages (không có coordination logic)
❌ One-way communication (giống Observer hơn)
❌ Quá đơn giản: không có complex interactions
❌ Quá phức tạp: quá nhiều business logic không liên quan

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "many-to-many communication qua mediator"
- Phải có ít nhất 3-4 colleagues khác nhau

### Về Mediator Pattern
- Mediator **PHẢI** có references đến tất cả colleagues
- Colleagues **PHẢI** có reference đến Mediator
- Colleagues **KHÔNG ĐƯỢC** communicate directly với nhau
- **PHẢI** có coordination/orchestration logic trong Mediator
- Mediator interface định nghĩa communication methods
- ConcreteMediator implement logic để coordinate

### Về Implementation
- **Recommended**: Mediator interface + ConcreteMediator class
- **Colleague**: Abstract class hoặc interface với reference to Mediator
- **Communication**: Colleague → Mediator.notify() → Mediator calls other colleagues
- Mediator biết tất cả colleagues (registered hoặc passed in constructor)
- Colleagues chỉ biết Mediator, không biết colleagues khác

### Về Coordination Logic
- Mediator chứa business rules cho interactions
- Mediator quyết định colleague nào cần được notify/called
- Logic có thể phức tạp: if-else, switch-case, state-based
- Mediator có thể transform/process messages trước khi forward

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Mediator có methods: notify(), register(), hoặc specific operations
- Colleagues gọi mediator.notify(this, event) hoặc specific methods
- Mediator gọi methods trên colleagues để coordinate
- Demo rõ ràng: action → mediator → coordination → multiple colleagues react

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Interfaces/Abstract: Mediator và Colleague
- Implementations: ConcreteMediator và ConcreteColleagues
- Relationships:
  - Implements: ConcreteMediator → Mediator, ConcreteColleagues → Colleague
  - Association (bidirectional): Mediator ↔ Colleagues
  - Mediator has associations to ALL colleagues
- Format phải giống lecture

### Về documentation
- Documents/Solutions/Mediator.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- Giải thích rõ ràng centralized coordination
- So sánh với cách không dùng Mediator (direct coupling)
- Nêu rõ trade-offs (Mediator có thể trở thành God Object)
- So sánh với Observer pattern (Mediator vs Observer)

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Smart Home System
- Mediator: SmartHomeHub
- Colleagues: Light, Thermostat, SecurityCamera, MotionSensor
- Interaction: MotionSensor detects → Hub coordinates → Light on, Camera record, Thermostat adjust
- Methods: notify(colleague, event), coordinateSecurity(), adjustEnvironment()

### Ví dụ 2: Airport Gate Management
- Mediator: GateCoordinator
- Colleagues: Gate, Flight, BaggageSystem, BoardingStaff
- Interaction: Flight ready → Coordinator assigns gate → update baggage, staff, displays
- Methods: assignGate(flight), notifyBaggageSystem(gate), notifyStaff()

### Ví dụ 3: Restaurant Kitchen
- Mediator: KitchenManager (Expeditor)
- Colleagues: Waiter, Chef, PrepStation, Plating
- Interaction: Order → Manager coordinates → chef cooks, prep prepares, plating assembles
- Methods: receiveOrder(waiter), coordinateCooking(), platingReady()

### Ví dụ 4: Smart Traffic Intersection
- Mediator: IntersectionController
- Colleagues: TrafficLight (4 directions), PedestrianSignal, EmergencyVehicleSensor
- Interaction: Emergency → Controller coordinates → lights change, pedestrians stop
- Methods: handleEmergency(), normalOperation(), adjustTiming()

### Ví dụ 5: Call Center
- Mediator: CallRouter
- Colleagues: Agent, Customer, QueueSystem, SkillMatcher
- Interaction: Call → Router checks skills → assign agent → update queue
- Methods: routeCall(customer), findAvailableAgent(), updateQueue()

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic coordination cụ thể
- Ngữ cảnh và câu chuyện riêng
- Ít nhất 3-4 concrete colleagues
- Demo rõ centralized coordination

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Mediator
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán có yêu cầu rõ ràng về many-to-many communication
- [ ] Có Mediator interface với communication methods
- [ ] Có Colleague abstract class với reference to Mediator
- [ ] Có ConcreteMediator với coordination logic
- [ ] Có ít nhất 3-4 ConcreteColleagues
- [ ] Colleagues KHÔNG communicate directly
- [ ] Documents/Solutions/Mediator.md có đầy đủ 8 sections
- [ ] Code trong 7-Mediator-DP/ là code MỚI cho bài toán mới
- [ ] Demo thể hiện multiple colleagues interacting
- [ ] Demo thể hiện coordination logic của mediator
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong Documents/Solutions/Mediator.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram có Mediator và Colleague interfaces/abstract
- [ ] UML diagram có bidirectional associations
- [ ] UML diagram format giống lecture
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng centralized coordination
- [ ] Đã giải thích trade-offs của Mediator (God Object risk)
- [ ] Đã so sánh với Observer pattern

## Common Pitfalls cần tránh

### Mediator Pattern có thể gây vấn đề nếu:
❌ Mediator trở thành God Object (quá nhiều responsibility)
❌ Logic quá phức tạp trong Mediator (hard to maintain)
❌ Colleagues biết nhau (không đúng pattern)
❌ Mediator chỉ forward messages (không có coordination logic)
❌ Too many colleagues → Mediator quá complex
❌ Tight coupling giữa Mediator và Colleagues

### Best Practices:
✅ Keep Mediator focused (Single Responsibility)
✅ Use Strategy or State pattern nếu coordination logic phức tạp
✅ Colleagues nên autonomous, chỉ notify mediator khi cần
✅ Document coordination logic rõ ràng
✅ Consider splitting mediator nếu quá lớn
✅ Use interfaces để giảm coupling

### When to use Mediator:
✅ Khi có many-to-many interactions giữa objects
✅ Khi interactions phức tạp và thay đổi thường xuyên
✅ Khi muốn reuse objects trong contexts khác
✅ Khi communication logic cần centralized
✅ Khi có nhiều conditional logic về interactions

### When NOT to use Mediator:
❌ Khi chỉ có simple interactions (overkill)
❌ Khi one-to-many broadcast (dùng Observer)
❌ Khi objects độc lập, không cần coordinate
❌ Khi Mediator sẽ trở nên quá phức tạp
❌ Khi communication flow đơn giản và cố định

## Mediator vs Observer

### Key Differences:

**Observer Pattern**:
- **Relationship**: One-to-many
- **Communication**: One-way broadcast (Subject → Observers)
- **Purpose**: Notify nhiều observers về state changes
- **Observers**: Passive (chỉ nhận notification)
- **Example**: YouTube channel → subscribers

**Mediator Pattern**:
- **Relationship**: Many-to-many (qua central mediator)
- **Communication**: Bidirectional (Colleagues ↔ Mediator ↔ Colleagues)
- **Purpose**: Coordinate complex interactions giữa nhiều objects
- **Colleagues**: Active (gửi và nhận qua mediator)
- **Example**: Smart home hub coordinating devices

### When to use which:

**Use Observer when**:
- Cần broadcast state changes đến nhiều listeners
- One-to-many relationship
- Observers không cần communicate với nhau
- Passive notification

**Use Mediator when**:
- Nhiều objects cần interact với nhau
- Many-to-many relationships
- Complex coordination logic
- Active bidirectional communication

### Can be combined:
- Mediator có thể implement Observer pattern
- Subject có thể là Mediator cho observers
- Observers có thể là Colleagues trong Mediator
