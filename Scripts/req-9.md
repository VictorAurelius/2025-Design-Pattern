# Plan Task: Tạo Bài Toán Mới Cho Chain of Responsibility Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Chain of Responsibility Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## 🎯 YÊU CẦU ĐẶC BIỆT: Context Liên Kết (Tiếp tục)

Để dễ nhớ (vì có 24 design patterns), bài toán nên chọn **context liên kết** với các patterns đã làm:

### Contexts đã sử dụng:
1. **Adapter**: Media Player (Audio/Video players)
2. **Facade**: Home Theater System (Movie watching)
3. **Composite**: Restaurant Menu System (Golden Fork Restaurant)
4. **Bridge**: Notification System (Messages)
5. **Singleton**: Configuration Manager (EnterpriseSoft ERP)
6. **Observer**: YouTube Channel (TechReview Pro - Emma)
7. **Mediator**: Smart Home Automation (James's apartment)
8. **Proxy**: Video Streaming Platform (StreamFlix - Lisa)

### 🌟 RECOMMENDED Contexts cho Chain of Responsibility (có liên kết):

#### Option 1: **Customer Support System** ⭐ BEST (liên kết với Enterprise)
- **Liên kết**: EnterpriseSoft ERP (Singleton), Business domain
- **Use case**:
  - Support tickets với different levels: L1 Support → L2 Support → Manager → Director
  - Each handler decides if can handle hoặc pass to next
  - Real-world: Zendesk, Jira Service Desk
- **Ví dụ**: SupportHandler chain, Ticket class
- **Ưu điểm**: Rất phù hợp với Chain of Responsibility, realistic, business context

#### Option 2: **Content Moderation System** (liên kết với Video/Social Media)
- **Liên kết**: YouTube Channel (Observer), StreamFlix (Proxy)
- **Use case**:
  - Content filtering chain: Spam Filter → Profanity Filter → Copyright Filter → Manual Review
  - Each filter checks content và decides
- **Ví dụ**: ContentFilter chain, Content class

#### Option 3: **Restaurant Order Processing** (liên kết với Restaurant)
- **Liên kết**: Golden Fork Restaurant (Composite)
- **Use case**:
  - Order approval chain: Waiter → Chef → Manager (for special requests)
  - Discount approval: Sales → Manager → Owner
- **Ví dụ**: OrderHandler chain, Order class

#### Option 4: **Smart Home Security Alert** (liên kết với Smart Home)
- **Liên kết**: Smart Home Automation (Mediator)
- **Use case**:
  - Alert processing: Low Priority Handler → Medium → High → Emergency
  - Each handler filters và escalates
- **Ví dụ**: AlertHandler chain, SecurityAlert class

#### Option 5: **Expense Approval Workflow** (liên kết với Enterprise)
- **Liên kết**: EnterpriseSoft ERP (Singleton)
- **Use case**:
  - Expense approval: TeamLead ($0-$1000) → Manager ($1000-$5000) → Director ($5000-$20000) → CFO ($20000+)
  - Amount-based routing
- **Ví dụ**: ApprovalHandler chain, ExpenseRequest class

### 💡 Recommendation:
**Chọn Option 1 (Customer Support System)** vì:
- ✅ Liên kết mạnh với EnterpriseSoft ERP (Singleton pattern)
- ✅ Chain of Responsibility rất phù hợp với support ticket escalation
- ✅ Realistic - mọi người đều biết customer support
- ✅ Clear hierarchy: L1 → L2 → Manager → Director
- ✅ Easy to understand và demonstrate pattern

**Alternative**: Option 2 (Content Moderation) nếu muốn liên kết với Video domain

## Yêu cầu đầu ra
Đối với Chain of Responsibility Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích yêu cầu

- Đọc file `Documents/Lectures/Chain of Responsibility.pdf` để:
  - Hiểu BẢN CHẤT của Chain of Responsibility Pattern
  - Hiểu chain of handlers concept
  - Hiểu cách request passed along chain until handled
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram với Handler, ConcreteHandlers
  - Hiểu successor relationship

- Đọc code trong `Code-Sample/ChainofResponsibility-Project/` để:
  - Học cách đặt tên class, method, interface
  - Học coding convention và code style
  - Học cách implement Handler abstract class
  - Học cách set successor (next handler)
  - Học cách pass request along chain
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 2: Sáng tạo bài toán mới

#### 2.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Chain of Responsibility:
- **KHÔNG dùng**: Các ví dụ có thể có trong lecture/code sample
- **KHÔNG dùng**: ATM (quá phổ biến)
- **KHÔNG dùng**: File Handler (có trong code sample)
- **KHÔNG dùng**: Logger (ví dụ phổ biến)

**⭐ RECOMMENDED: Chọn context có liên kết với patterns đã học**

**Gợi ý các lĩnh vực có thể dùng (với liên kết)**:

### 💼 Enterprise/Business Domain (liên kết Singleton):

1. **Customer Support Ticket System** ⭐ BEST
   - Handler chain: L1Support → L2Support → Manager → Director
   - Tickets với different complexity levels
   - Each handler can resolve hoặc escalate
   - Real-world: Help desk systems

2. **Expense Approval Workflow**
   - Handler chain: TeamLead → Manager → Director → CFO
   - Based on expense amount
   - Clear authority limits

3. **Purchase Order Approval**
   - Handler chain: Buyer → Supervisor → Manager → VP
   - Based on order value
   - Multi-level approval

### 🎬 Video/Content Domain (liên kết Observer + Proxy):

4. **Content Moderation Pipeline**
   - Handler chain: SpamFilter → ProfanityFilter → CopyrightFilter → ManualReview
   - Each filter checks và passes/rejects
   - For YouTube-like platforms

5. **Video Upload Processing**
   - Handler chain: FormatValidator → SizeValidator → ContentValidator → Publisher
   - Each step validates aspect of video

### 🍽️ Restaurant Domain (liên kết Composite):

6. **Order Special Request Handling**
   - Handler chain: Waiter → Chef → Manager
   - Special dietary requests, customizations
   - Complex requests need escalation

7. **Discount Approval Chain**
   - Handler chain: Cashier → Supervisor → Manager → Owner
   - Discount % determines handler

### 🏠 Smart Home Domain (liên kết Mediator):

8. **Security Alert Processing**
   - Handler chain: LowPriorityHandler → MediumPriorityHandler → HighPriorityHandler → EmergencyHandler
   - Alert severity determines handler
   - Escalation based on urgency

9. **Smart Home Command Processing**
   - Handler chain: BasicCommandHandler → ComplexCommandHandler → SystemCommandHandler
   - Command complexity determines handler

### 📱 General Options:

10. **Email Spam Filter**
    - Handler chain: WhitelistFilter → BlacklistFilter → ContentFilter → BayesianFilter
    - Each filter can mark/pass email

11. **Game Damage Calculation**
    - Handler chain: ArmorHandler → ShieldHandler → BuffHandler → DamageApplier
    - Each handler modifies/absorbs damage

12. **Payment Processing**
    - Handler chain: CardValidator → FraudChecker → BankAuthenticator → PaymentProcessor
    - Each step validates payment

13. **Document Approval**
    - Handler chain: Editor → Reviewer → Approver → Publisher
    - Documents pass through review stages

14. **Error Handling Pipeline**
    - Handler chain: ValidationErrorHandler → DatabaseErrorHandler → NetworkErrorHandler → GeneralErrorHandler
    - Each handles specific error types

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 2.2. Yêu cầu bài toán phải có

**Đặc điểm của Chain of Responsibility Pattern**:

1. **Handler (Abstract Class/Interface)**:
   - Defines interface for handling requests
   - Has reference to **successor** (next handler)
   - Method: `handleRequest(Request)`
   - Method: `setSuccessor(Handler)`

2. **ConcreteHandlers**:
   - Implement handling logic
   - Decide if can handle hoặc pass to successor
   - May modify request before passing

3. **Request/Context Object**:
   - Contains data for handlers
   - Passed along chain
   - May be modified by handlers

4. **Chain Setup**:
   - Client creates chain: handler1 → handler2 → handler3
   - Request starts at first handler
   - Passes along until handled hoặc reaches end

**Bài toán phải demonstrate**:
- ✅ Multiple handlers in chain (ít nhất 3-4 handlers)
- ✅ Request passed along chain
- ✅ Some handlers handle, some pass to next
- ✅ Clear criteria for handling vs passing
- ✅ Dynamic chain configuration (có thể change order)

**Quan trọng**:
- Handler PHẢI có reference đến successor
- Handler PHẢI decide: handle hoặc pass to successor
- Request PHẢI pass along chain
- Chain có thể stop bất kỳ đâu (khi handled)
- Decoupling: sender không biết which handler will handle

#### 2.3. Thiết kế các thành phần

**Handler Abstract Class**:
```java
public abstract class Handler {
    protected Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract void handleRequest(Request request);
}
```

**ConcreteHandler**:
```java
public class ConcreteHandler1 extends Handler {
    @Override
    public void handleRequest(Request request) {
        if (canHandle(request)) {
            // Handle the request
            System.out.println("Handler1 handling request");
        } else if (successor != null) {
            // Pass to next handler
            successor.handleRequest(request);
        }
    }

    private boolean canHandle(Request request) {
        // Decision logic
        return request.getLevel() == 1;
    }
}
```

**Request Class**:
```java
public class Request {
    private String type;
    private int priority;

    // Getters and setters
}
```

**Client Setup**:
```java
// Build chain
Handler handler1 = new ConcreteHandler1();
Handler handler2 = new ConcreteHandler2();
Handler handler3 = new ConcreteHandler3();

handler1.setSuccessor(handler2);
handler2.setSuccessor(handler3);

// Send request
Request request = new Request("urgent", 3);
handler1.handleRequest(request);  // Starts chain
```

### Bước 3: Viết Documents/Solutions/ChainOfResponsibility.md

Tạo file `Documents/Solutions/ChainOfResponsibility.md` với cấu trúc 8 sections:

#### 3.1. Mô tả mẫu Chain of Responsibility
- Giữ phần mô tả tổng quan về Chain of Responsibility Pattern
- Các thành phần chính: Handler, ConcreteHandlers, Request
- Khi nào sử dụng: multiple handlers, request routing
- Đặc điểm quan trọng: chain of handlers, successor, decoupling

#### 3.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 2
- **Nhấn mạnh liên kết với context đã học** (nếu có)
- Bài toán cần:
  - Có multiple handlers cần xử lý requests
  - Nêu rõ vấn đề nếu không dùng Chain of Responsibility
  - Giải thích tại sao cần chain
  - Có tình huống cụ thể minh họa với different request types

#### 3.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Có requests cần processing
- Multiple handlers với different capabilities
- Cần route requests to appropriate handler
- Handler hierarchy or precedence

**Problem**: Vấn đề phức tạp cần giải quyết
- Tight coupling between sender và handler
- Sender must know which handler to use
- Hard to add new handlers
- Rigid request routing logic
- Cannot change handler order easily

**Solution**: Cách Chain of Responsibility giải quyết
- Chain of handlers, each checks if can handle
- Request passes along chain until handled
- Sender doesn't know which handler will handle
- Easy to add/remove/reorder handlers
- Loose coupling

**Expected Output**: Kết quả mong đợi
- Requests routed automatically
- Appropriate handler handles request
- Flexible chain configuration
- Easy to extend

#### 3.4. Hiệu quả của việc sử dụng Chain of Responsibility Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh với cách không dùng Chain (if-else ladder, switch-case)
- Decoupling sender and receiver
- Flexibility in assigning responsibilities
- Trade-offs: no guarantee of handling, performance

#### 3.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Phải có:
  - 1 Handler abstract class (hoặc interface)
  - 3-4 ConcreteHandler classes
  - 1 Request class
  - 1 Demo/Main class
- Code phải hoàn chỉnh và có thể compile
- Coding style học từ code sample

#### 3.6. Kết quả chạy chương trình
- Output khi chạy main class
- Demo different requests routed to different handlers
- Demo request passed along chain
- Demo some requests handled, some escalated
- Giải thích cách pattern hoạt động qua output

#### 3.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Phải có:
  - Handler abstract class
  - Multiple ConcreteHandlers extending Handler
  - Request class
  - Successor relationships (Handler → Handler)
- Thể hiện chain structure

#### 3.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này
- Khi nào nên và không nên dùng Chain of Responsibility
- Alternatives: Command pattern, Strategy pattern

### Bước 4: Viết code Java cho bài toán mới trong 9-ChainOfResponsibility-DP/

#### 4.1. Tạo folder và code mới
Dựa trên bài toán đã thiết kế, tạo các file trong folder `9-ChainOfResponsibility-DP/`:

**Abstract/Interface**:
- Handler.java (abstract class)

**Concrete Classes**:
- ConcreteHandler1.java
- ConcreteHandler2.java
- ConcreteHandler3.java
- ConcreteHandler4.java (optional)

**Request**:
- Request.java (contains data)

**Demo**:
- ChainDemo.java (Main class)

**Ví dụ cấu trúc** (Customer Support):
```
9-ChainOfResponsibility-DP/
├── SupportHandler.java           (Abstract Handler)
├── Level1Support.java             (ConcreteHandler)
├── Level2Support.java             (ConcreteHandler)
├── Manager.java                   (ConcreteHandler)
├── Director.java                  (ConcreteHandler)
├── SupportTicket.java             (Request)
└── ChainDemo.java                 (Client/Main)
```

#### 4.2. Coding standard
- Package declaration: không cần
- Import statements: nếu cần
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu
- Handler có successor reference
- ConcreteHandlers decide handle hoặc pass

#### 4.3. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Chain of Responsibility pattern
- Demo rõ ràng chain behavior
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Documents/Solutions/ChainOfResponsibility.md

#### 4.4. Demo Requirements
Demo phải thể hiện:
1. Create chain of handlers
2. Setup successor links
3. Send different types of requests
4. Show requests handled at different levels
5. Show requests passed along chain
6. Show some requests reaching end (optional)
7. Demonstrate flexibility (reorder chain)

### Bước 5: Tạo file package.bluej với UML diagram

Tạo file `9-ChainOfResponsibility-DP/package.bluej` với:

#### 5.1. Yêu cầu UML diagram
- Hiển thị Handler abstract class
- Hiển thị ConcreteHandlers
- Hiển thị Request class
- Hiển thị đầy đủ methods cho mỗi class
- Hiển thị relationships:
  - ConcreteHandlers extend Handler
  - Handler has successor reference (self-association)
  - Handlers use Request
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 5.2. Format BlueJ file
```
#BlueJ package file

# Dependencies
dependency1.from=ConcreteHandler1
dependency1.to=Handler
dependency1.type=UsesDependency

dependency2.from=Handler
dependency2.to=Handler
dependency2.type=UsesDependency  # Successor relationship

dependency3.from=Handler
dependency3.to=Request
dependency3.type=UsesDependency

# Similar for other handlers...

# Targets với showInterface=true
target1.name=Handler
target1.type=AbstractTarget
target1.showInterface=true
...
```

#### 5.3. Layout gợi ý
```
Top:
- Handler (abstract class) with self-association arrow (successor)

Middle (horizontal chain):
- ConcreteHandler1 → ConcreteHandler2 → ConcreteHandler3 → ConcreteHandler4

Bottom left:
- Request class

Bottom right:
- ChainDemo
```

## Deliverables

### 1. File Documents/Solutions/ChainOfResponsibility.md
- Hoàn chỉnh theo cấu trúc 8 sections
- Bài toán MỚI, sáng tạo
- **Nhấn mạnh liên kết với context đã học** (nếu chọn Customer Support/Enterprise)
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting
- Giải thích rõ chain concept
- Minh họa request passing along chain

### 2. Folder 9-ChainOfResponsibility-DP/
Chứa các file Java cho bài toán MỚI:
- Handler abstract class
- 3-4 ConcreteHandlers
- Request class
- 1 Demo/Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Demo rõ chain behavior

### 3. File 9-ChainOfResponsibility-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture
- Thể hiện rõ Handler abstract class
- Thể hiện rõ successor relationships
- Thể hiện chain structure

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Có liên kết với context đã học**: Enterprise/Video/Restaurant/Smart Home (preferred)
✅ **Chain of handlers**: Ít nhất 3-4 handlers with clear hierarchy
✅ **Request routing**: Requests passed along chain
✅ **Decision logic**: Each handler decides handle or pass
✅ **Successor setup**: Clear successor relationships
✅ **Flexible chain**: Can reorder/modify handlers
✅ **Decoupling**: Sender doesn't know which handler handles
✅ **Real escalation**: Some requests handled early, some escalated
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Handler, ConcreteHandlers, successor chain
✅ **Khác biệt**: Không trùng với lecture hay code sample

### Bài toán nên tránh:
❌ Copy từ lecture/code sample
❌ Copy ATM (quá phổ biến)
❌ Copy File Handler (có trong code sample)
❌ Copy Logger (ví dụ phổ biến)
❌ Context hoàn toàn mới không liên kết (khó nhớ)
❌ Chỉ có 2 handlers (không thể hiện chain complexity)
❌ No clear escalation logic
❌ All requests go to last handler (not real chain)
❌ Quá đơn giản: chỉ if-else
❌ Quá phức tạp: quá nhiều business logic không liên quan

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **NÊN có liên kết**: Chọn context liên kết với patterns đã học để dễ nhớ
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "chain of handlers" concept

### Về Chain of Responsibility Pattern
- Handler **PHẢI** có successor reference
- Handler **PHẢI** có `setSuccessor()` method
- Handler **PHẢI** có `handleRequest()` method
- ConcreteHandlers **PHẢI** decide: handle or pass to successor
- Request **PHẢI** pass along chain
- Chain có thể stop anywhere khi handled

### Về Implementation
- **Recommended**: Abstract Handler class với successor field
- **Alternative**: Handler interface (nhưng successor cần implement in each class)
- Handler decides based on request properties (type, priority, amount, etc.)
- May modify request before passing
- Last handler may have no successor (end of chain)

### Về Decision Logic
- Each handler has clear criteria: "I can handle if..."
- Example: Amount-based ($0-$1000 vs $1000-$5000 vs $5000+)
- Example: Priority-based (Low vs Medium vs High vs Critical)
- Example: Type-based (Type A vs Type B vs Type C)
- Example: Complexity-based (Simple vs Complex vs Expert)

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Handler abstract class với successor field
- setSuccessor() method to build chain
- handleRequest() method với if-else logic
- Demo rõ ràng: build chain → send requests → show handling

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Handler: AbstractTarget
- ConcreteHandlers: ClassTarget extending Handler
- Self-association: Handler → Handler (successor)
- Request: ClassTarget
- Format phải giống lecture

### Về documentation
- Documents/Solutions/ChainOfResponsibility.md là tài liệu chính
- Phải đầy đủ 8 sections, rõ ràng, dễ hiểu
- **Highlight liên kết với context đã học** trong phần mô tả bài toán
- Giải thích rõ ràng chain concept
- So sánh với cách không dùng Chain (if-else ladder)
- Nêu rõ trade-offs (no guarantee request will be handled)

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

### ⭐ RECOMMENDED: Customer Support System (liên kết Enterprise)

**Context**: Liên kết với EnterpriseSoft ERP (Singleton pattern)

**Problem**:
- Support tickets với different complexity levels
- L1 support handles basic issues
- L2 support handles technical issues
- Manager handles escalated issues
- Director handles critical issues
- Currently: tight coupling, sender must know which level

**Solution**:
- Chain: L1Support → L2Support → Manager → Director
- Each handler checks ticket complexity
- Can handle → resolve ticket
- Cannot handle → pass to successor
- Sender doesn't know who will handle

**Classes**:
```java
abstract class SupportHandler {
    protected SupportHandler successor;
    public void setSuccessor(SupportHandler s);
    public abstract void handleTicket(SupportTicket ticket);
}

class Level1Support extends SupportHandler { /* Handles basic */ }
class Level2Support extends SupportHandler { /* Handles technical */ }
class Manager extends SupportHandler { /* Handles escalated */ }
class Director extends SupportHandler { /* Handles critical */ }

class SupportTicket {
    String issueType;  // "basic", "technical", "escalated", "critical"
    String description;
}
```

**Demo**:
1. Build chain: L1 → L2 → Manager → Director
2. Send basic ticket → L1 handles
3. Send technical ticket → L2 handles (L1 passes)
4. Send escalated ticket → Manager handles (L1, L2 pass)
5. Send critical ticket → Director handles (all pass)

### Other Examples (chỉ structure, KHÔNG copy):

**Content Moderation** (liên kết Video):
- Problem: YouTube content needs filtering
- Chain: SpamFilter → ProfanityFilter → CopyrightFilter → ManualReview
- Each filter checks và passes/rejects

**Restaurant Discount** (liên kết Composite):
- Problem: Discount approval based on percentage
- Chain: Cashier (0-5%) → Supervisor (5-10%) → Manager (10-20%) → Owner (20%+)

**Smart Home Alert** (liên kết Mediator):
- Problem: Security alerts with different priorities
- Chain: LowPriorityHandler → MediumPriorityHandler → HighPriorityHandler → EmergencyHandler

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/method khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết handling criteria
- Demo rõ chain behavior

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Chain of Responsibility
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Đã chọn context có liên kết với patterns đã học (nếu có thể)
- [ ] Bài toán có chain of handlers (ít nhất 3-4)
- [ ] Có Handler abstract class với successor
- [ ] ConcreteHandlers extend Handler
- [ ] Handlers decide handle or pass
- [ ] Demo multiple request types
- [ ] Documents/Solutions/ChainOfResponsibility.md có đầy đủ 8 sections
- [ ] Code trong 9-ChainOfResponsibility-DP/ là code MỚI
- [ ] Demo thể hiện chain behavior rõ ràng
- [ ] Demo thể hiện requests handled at different levels
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong markdown
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram có Handler abstract class
- [ ] UML diagram có successor relationships
- [ ] UML diagram có ConcreteHandlers
- [ ] UML diagram format giống lecture
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng chain behavior
- [ ] Đã giải thích trade-offs (no guarantee handling)

## Common Pitfalls cần tránh

### Chain of Responsibility có thể gây vấn đề nếu:
❌ Chain too long (performance issue)
❌ No handler can handle request (request lost)
❌ Circular chain (infinite loop)
❌ Handler forgot to call successor
❌ Unclear handling criteria
❌ All requests go to last handler (chain useless)

### Best Practices:
✅ Keep chain short (3-5 handlers ideal)
✅ Clear handling criteria for each handler
✅ Default handler at end (catch-all)
✅ Log when request passed/handled
✅ Consider performance (chain overhead)
✅ Document successor order

### When to use Chain of Responsibility:
✅ Khi có multiple handlers for requests
✅ Khi handler not known in advance
✅ Khi want decouple sender and receiver
✅ Khi handlers can be added/removed dynamically
✅ Khi escalation logic exists
✅ Approval workflows, filtering pipelines

### When NOT to use Chain of Responsibility:
❌ Khi handler luôn known (direct call better)
❌ Khi chỉ có 1-2 handlers (overkill)
❌ Khi order không quan trọng (use Command pattern)
❌ Khi performance critical (chain overhead)
❌ Khi request must be handled (no guarantee in chain)

## Chain of Responsibility vs Command vs Strategy

### Key Differences:

**Chain of Responsibility**:
- **Purpose**: Pass request along chain until handled
- **Focus**: Dynamic handler selection based on chain
- **Structure**: Linked list of handlers
- **Example**: Support escalation, approval workflow

**Command Pattern**:
- **Purpose**: Encapsulate request as object
- **Focus**: Decouple invoker from executor
- **Structure**: Command objects with execute()
- **Example**: Undo/redo, macro commands

**Strategy Pattern**:
- **Purpose**: Select algorithm at runtime
- **Focus**: Interchangeable algorithms
- **Structure**: Strategy interface with implementations
- **Example**: Sorting algorithms, payment methods

### When to use which:

**Use Chain of Responsibility when**:
- Multiple handlers may handle request
- Handler not known in advance
- Want pass request along chain

**Use Command when**:
- Want encapsulate requests as objects
- Want undo/redo functionality
- Want queue or log requests

**Use Strategy when**:
- Want switch algorithms at runtime
- Have multiple ways to do same thing
- Want avoid if-else chains
