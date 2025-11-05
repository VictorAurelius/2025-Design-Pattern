# Plan Task: Tạo Bài Toán Mới Cho Bridge Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Bridge Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Bridge Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu (vì chưa có code hiện tại)
- Đọc file `Documents/Lectures/Bridge.pdf` để:
  - Hiểu BẢN CHẤT của Bridge Pattern (không phải học bài toán cụ thể)
  - Hiểu vấn đề "cartesian product complexity"
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram có đầy đủ phương thức
  - Hiểu mối quan hệ giữa Abstraction và Implementation
  - Hiểu cách 2 hierarchies có thể vary independently

- Đọc code trong `Code-Sample/BridgePattern-Project/` để:
  - Học cách đặt tên class, interface, method
  - Học coding convention và code style
  - Học cách triển khai Bridge pattern đúng chuẩn
  - Học cách Abstraction chứa reference đến Implementation
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Bridge Pattern - có 2 dimensions độc lập:
- **KHÔNG dùng**: Car/Product example (có thể có trong code sample)
- **KHÔNG dùng**: Shape + Color (ví dụ phổ biến nhất)
- **KHÔNG dùng**: Remote Control + Device (ví dụ phổ biến)

**Gợi ý các lĩnh vực có thể dùng (2 dimensions độc lập)**:

1. **Message System**:
   - Abstraction: TextMessage, EmailMessage, UrgentMessage
   - Implementation: SMSSender, EmailSender, PushNotificationSender

2. **Document Rendering**:
   - Abstraction: Report, Letter, Invoice
   - Implementation: PDFRenderer, HTMLRenderer, MarkdownRenderer

3. **Payment Processing**:
   - Abstraction: OnlinePayment, RecurringPayment, RefundPayment
   - Implementation: PayPalGateway, StripeGateway, BankTransferGateway

4. **Media Player**:
   - Abstraction: VideoPlayer, AudioPlayer, StreamingPlayer
   - Implementation: WindowsOS, MacOS, LinuxOS

5. **Database Query**:
   - Abstraction: SimpleQuery, AggregateQuery, JoinQuery
   - Implementation: MySQLDriver, PostgreSQLDriver, MongoDBDriver

6. **Logging System**:
   - Abstraction: DebugLogger, ErrorLogger, AuditLogger
   - Implementation: FileHandler, DatabaseHandler, CloudHandler

7. **UI Components**:
   - Abstraction: Button, TextField, Slider
   - Implementation: WindowsTheme, MacTheme, DarkTheme

8. **Notification System**:
   - Abstraction: SystemNotification, MarketingNotification, AlertNotification
   - Implementation: EmailChannel, SMSChannel, SlackChannel

9. **Drawing Application**:
   - Abstraction: PencilTool, BrushTool, EraserTool
   - Implementation: VectorAPI, RasterAPI, 3DAPI

10. **Streaming Service**:
    - Abstraction: LiveStream, OnDemand, RecordedStream
    - Implementation: HighQuality, MediumQuality, LowQuality

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có
1. **2 dimensions độc lập**: Abstraction hierarchy và Implementation hierarchy
2. **Cartesian product problem**: Nếu không dùng Bridge, cần n×m classes
3. **Composition over inheritance**: Abstraction chứa reference đến Implementation
4. **Independent variation**: Cả 2 hierarchies có thể thay đổi độc lập

**Đặc điểm của Bridge Pattern**:
- **Abstraction**: Abstract class/interface định nghĩa high-level operations
- **RefinedAbstraction**: Concrete classes kế thừa Abstraction
- **Implementation**: Interface định nghĩa low-level operations
- **ConcreteImplementation**: Concrete classes implement Implementation
- **Bridge**: Abstraction chứa reference đến Implementation (composition)
- Client tạo Abstraction với Implementation cụ thể

**Quan trọng**:
- 2 hierarchies có thể extend độc lập
- Giảm số class từ n×m xuống n+m
- Runtime flexibility: có thể thay đổi implementation

#### 2.3. Thiết kế các thành phần

**Abstraction Side**:
- Abstract class với reference đến Implementation
- Có constructor nhận Implementation
- Có high-level methods delegate đến Implementation
- RefinedAbstraction extends Abstraction

**Implementation Side**:
- Interface với low-level methods
- ConcreteImplementation implements Interface
- Không biết về Abstraction

**Ví dụ cấu trúc**:
```
Abstraction (abstract class)
  - implementation: Implementation
  - operation()

RefinedAbstractionA extends Abstraction
RefinedAbstractionB extends Abstraction

Implementation (interface)
  - operationImpl()

ConcreteImplementationX implements Implementation
ConcreteImplementationY implements Implementation
```

### Bước 3: Viết Documents/Solutions/Bridge.md

Tạo file `Documents/Solutions/Bridge.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Bridge
- Giữ phần mô tả tổng quan về Bridge Pattern
- Các thành phần chính: Abstraction, RefinedAbstraction, Implementation, ConcreteImplementation
- Khi nào sử dụng
- Đặc điểm quan trọng: tách 2 hierarchies, composition, independent variation

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- Bài toán cần:
  - Có 2 dimensions độc lập cần vary
  - Nêu rõ vấn đề cartesian product complexity
  - Giải thích tại sao inheritance không tốt
  - Có tình huống cụ thể minh họa

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- 2 dimensions cần kết hợp
- Nếu dùng inheritance: cần n×m classes
- Khó mở rộng khi thêm abstraction hoặc implementation mới

**Problem**: Vấn đề phức tạp cần giải quyết
- Class explosion: quá nhiều subclasses
- Tight coupling giữa abstraction và implementation
- Khó thêm abstraction hoặc implementation mới
- Vi phạm Single Responsibility Principle

**Solution**: Cách Bridge pattern giải quyết
- Tạo 2 hierarchies riêng biệt
- Abstraction chứa reference đến Implementation
- Composition thay vì inheritance
- Cả 2 hierarchies có thể extend độc lập

**Expected Output**: Kết quả mong đợi
- Giảm số class từ n×m xuống n+m
- Dễ thêm abstraction hoặc implementation mới
- Runtime flexibility
- Loose coupling giữa 2 hierarchies

#### 3.4. Hiệu quả của việc sử dụng Bridge Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh số lượng classes trước và sau
- Khả năng mở rộng
- Runtime flexibility
- Trade-offs (nếu có)

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Mỗi class có:
  - Comment đầy đủ (nếu cần)
  - Tên biến, method rõ ràng
  - Logic đúng với Bridge pattern
  - Coding style học từ code sample
- Code phải hoàn chỉnh và có thể compile

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Giải thích cách pattern hoạt động qua output
- Demo runtime flexibility (thay đổi implementation)

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Liệt kê các class và mối quan hệ
- Nêu rõ Abstraction, RefinedAbstraction, Implementation, ConcreteImplementation
- Thể hiện composition relationship (Bridge)

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này

### Bước 4: Viết code Java cho bài toán mới trong 4-Bridge-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `4-Bridge-DP/`:

**Abstraction hierarchy**:
- 1 abstract class (Abstraction)
- 2-3 concrete classes (RefinedAbstraction)

**Implementation hierarchy**:
- 1 interface (Implementation)
- 2-3 concrete classes (ConcreteImplementation)

**Client**:
- 1 Main/Demo class

**Ví dụ cấu trúc**:
```
4-Bridge-DP/
├── Message.java              (Abstraction)
├── TextMessage.java          (RefinedAbstraction)
├── EmailMessage.java         (RefinedAbstraction)
├── UrgentMessage.java        (RefinedAbstraction)
├── MessageSender.java        (Implementation interface)
├── SMSSender.java            (ConcreteImplementation)
├── EmailSender.java          (ConcreteImplementation)
├── PushNotificationSender.java (ConcreteImplementation)
└── MessagingDemo.java        (Client)
```

#### 4.2. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: chỉ khi cần
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Abstraction có protected/private reference đến Implementation

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Bridge pattern
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/Bridge.md
- Demo rõ ràng independent variation và runtime flexibility

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `4-Bridge-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị tất cả các class của bài toán MỚI
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationship đúng:
  - RefinedAbstraction → Abstraction (inheritance)
  - ConcreteImplementation → Implementation (inheritance)
  - Abstraction → Implementation (composition/aggregation - BRIDGE)
  - Main → Abstraction, Implementation (dependency)
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=RefinedAbstractionA
dependency1.to=Abstraction
dependency1.type=UsesDependency

dependency2.from=Abstraction
dependency2.to=Implementation
dependency2.type=UsesDependency    # BRIDGE - composition

dependency3.from=ConcreteImplementationX
dependency3.to=Implementation
dependency3.type=UsesDependency

... (các dependencies khác)

# Targets với showInterface=true
target1.name=Abstraction
target1.type=AbstractTarget
target1.showInterface=true    # QUAN TRỌNG
target1.width=200
target1.height=140
target1.x=...
target1.y=...

... (các targets khác)
```

#### 5.3. Layout gợi ý
```
Sắp xếp theo mô hình Bridge Pattern:
- Abstraction side (trái):
  - Abstraction: trên
  - RefinedAbstraction classes: dưới, xếp dọc

- Implementation side (phải):
  - Implementation: trên
  - ConcreteImplementation classes: dưới, xếp dọc

- Bridge: Arrow từ Abstraction → Implementation (composition)

- Main/Demo: góc trên bên trái hoặc trung tâm trên
```

## Deliverables

### 1. File Documents/Solutions/Bridge.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting

### 2. Folder 4-Bridge-DP/
Chứa các file Java cho bài toán MỚI:
- 1 Abstraction abstract class
- 2-3 RefinedAbstraction classes
- 1 Implementation interface
- 2-3 ConcreteImplementation classes
- 1 Main/Demo class
- Code sạch, đúng chuẩn
- Compile và run được

### 3. File 4-Bridge-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ inheritance và composition (bridge)

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **2 dimensions độc lập**: Rõ ràng là 2 hierarchies riêng biệt
✅ **Cartesian product problem**: Thấy rõ nếu dùng inheritance sẽ có n×m classes
✅ **Independent variation**: Demo được cả 2 hierarchies có thể extend độc lập
✅ **Runtime flexibility**: Có thể thay đổi implementation lúc runtime
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung 2 dimensions
✅ **Đúng pattern**: Thể hiện rõ "decouple abstraction from implementation"
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Bridge
✅ **Khác biệt**: Không trùng với lecture hay code sample
✅ **Đơn giản vừa đủ**: Đủ để minh họa pattern, không quá phức tạp

### Bài toán nên tránh:
❌ Copy Car/Product từ code sample
❌ Copy Shape + Color (ví dụ quá phổ biến)
❌ Copy Remote + Device (ví dụ phổ biến)
❌ Chỉ có 1 dimension: không thấy được 2 hierarchies
❌ Không rõ ràng tại sao cần tách abstraction và implementation
❌ Quá đơn giản: chỉ 1-2 classes mỗi bên
❌ Quá phức tạp: quá nhiều business logic

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "2 independent hierarchies"

### Về Bridge Pattern
- Abstraction **PHẢI** chứa reference đến Implementation (composition)
- Abstraction **KHÔNG** implement chi tiết, delegate đến Implementation
- Implementation **KHÔNG** biết về Abstraction
- RefinedAbstraction có thể override/extend Abstraction
- ConcreteImplementation implement low-level operations
- Client có thể thay đổi Implementation lúc runtime

### Về số lượng classes
- **Ví dụ**: 3 abstractions × 3 implementations
- **Không dùng Bridge**: cần 9 classes (3×3)
- **Dùng Bridge**: cần 6 classes (3+3) + 2 base (Abstraction + Implementation) = 8 classes total
- Càng nhiều variations, lợi ích Bridge càng rõ

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Abstraction là abstract class với reference đến Implementation
- Implementation là interface (hoặc abstract class)
- Abstraction methods delegate đến Implementation methods
- Code phải demo được runtime flexibility

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Thể hiện rõ 2 hierarchies: Abstraction side và Implementation side
- Thể hiện rõ Bridge: Abstraction → Implementation (composition)
- Format phải giống lecture
- Kích thước boxes đủ lớn để chứa methods

### Về documentation
- Documents/Solutions/Bridge.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- Giải thích rõ ràng vấn đề class explosion
- So sánh số lượng classes có vs không có Bridge
- Có bảng so sánh rõ ràng

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Message System
- **Abstraction**: Message (abstract), TextMessage, EmailMessage, UrgentMessage
- **Implementation**: MessageSender (interface), SMSSender, EmailSender, PushSender
- **Bridge**: Message has-a MessageSender
- **Benefit**: 3 message types × 3 senders = 6 classes thay vì 9

### Ví dụ 2: Document Rendering
- **Abstraction**: Document (abstract), Report, Letter, Invoice
- **Implementation**: Renderer (interface), PDFRenderer, HTMLRenderer, MarkdownRenderer
- **Bridge**: Document has-a Renderer
- **Benefit**: 3 doc types × 3 renderers = 6 classes thay vì 9

### Ví dụ 3: Payment Processing
- **Abstraction**: Payment (abstract), OnlinePayment, RecurringPayment, RefundPayment
- **Implementation**: PaymentGateway (interface), PayPalGateway, StripeGateway, BankGateway
- **Bridge**: Payment has-a PaymentGateway
- **Benefit**: 3 payment types × 3 gateways = 6 classes thay vì 9

### Ví dụ 4: Media Player
- **Abstraction**: Player (abstract), VideoPlayer, AudioPlayer, StreamingPlayer
- **Implementation**: Platform (interface), WindowsPlatform, MacPlatform, LinuxPlatform
- **Bridge**: Player has-a Platform
- **Benefit**: 3 players × 3 platforms = 6 classes thay vì 9

### Ví dụ 5: Logging System
- **Abstraction**: Logger (abstract), DebugLogger, ErrorLogger, AuditLogger
- **Implementation**: LogHandler (interface), FileHandler, DatabaseHandler, CloudHandler
- **Bridge**: Logger has-a LogHandler
- **Benefit**: 3 loggers × 3 handlers = 6 classes thay vì 9

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/interface khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết methods phù hợp
- Demo rõ runtime flexibility

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Bridge
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán có 2 dimensions độc lập rõ ràng
- [ ] Abstraction chứa reference đến Implementation
- [ ] RefinedAbstraction extends Abstraction
- [ ] ConcreteImplementation implements Implementation
- [ ] Documents/Solutions/Bridge.md có đầy đủ 8 sections
- [ ] Code trong 4-Bridge-DP/ là code MỚI cho bài toán mới
- [ ] Có Abstraction + RefinedAbstraction + Implementation + ConcreteImplementation + Main
- [ ] Code compile và chạy được
- [ ] Demo được runtime flexibility
- [ ] So sánh rõ số lượng classes (n×m vs n+m)
- [ ] Code phù hợp với bài toán trong Documents/Solutions/Bridge.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram format giống lecture
- [ ] UML thể hiện rõ Bridge (composition từ Abstraction → Implementation)
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng 2 independent hierarchies và runtime flexibility
