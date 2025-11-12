# Chain of Responsibility Design Pattern

## 1. Mô tả mẫu Chain of Responsibility

### Định nghĩa
Chain of Responsibility là một mẫu thiết kế hành vi (Behavioral Design Pattern) cho phép một request được chuyển tiếp qua một chuỗi (chain) các handlers. Mỗi handler trong chuỗi quyết định xem nó có thể xử lý request đó hay không. Nếu không thể xử lý, request sẽ được chuyển tiếp đến handler tiếp theo trong chuỗi.

### Đặc điểm chính
- **Decoupling**: Tách rời sender (người gửi request) khỏi receiver (người nhận request)
- **Chain Structure**: Các handlers được liên kết theo dạng chuỗi
- **Dynamic Handling**: Request được xử lý bởi handler phù hợp, không cần biết trước handler nào
- **Successor Relationship**: Mỗi handler có reference đến handler tiếp theo (successor)
- **Flexible Configuration**: Có thể thêm, xóa, hoặc sắp xếp lại handlers dễ dàng

### Các thành phần chính

#### 1. Handler (Abstract/Interface)
- Định nghĩa interface cho việc xử lý requests
- Chứa reference đến successor (handler tiếp theo)
- Methods:
  - `setHandler(Handler)` hoặc `setSuccessor(Handler)`: Set handler tiếp theo
  - `handleRequest(Request)`: Xử lý request hoặc chuyển tiếp

#### 2. ConcreteHandler
- Implement logic xử lý cụ thể
- Quyết định có thể handle request hay không
- Nếu có thể handle → xử lý request
- Nếu không → chuyển tiếp cho successor

#### 3. Request/Context Object
- Chứa dữ liệu cần xử lý
- Được truyền qua chuỗi handlers
- Có thể được modify bởi handlers

#### 4. Client
- Tạo chain of handlers
- Setup successor relationships
- Gửi request đến handler đầu tiên

### Khi nào sử dụng Chain of Responsibility?

**Sử dụng khi:**
- ✅ Có nhiều handlers có thể xử lý cùng một request
- ✅ Handler cụ thể không biết trước (runtime decision)
- ✅ Muốn decouple sender và receiver
- ✅ Handlers có thể thay đổi dynamically
- ✅ Có escalation logic (tăng dần mức độ xử lý)
- ✅ Approval workflows, filtering pipelines

**Không nên sử dụng khi:**
- ❌ Handler luôn biết trước (direct call tốt hơn)
- ❌ Chỉ có 1-2 handlers (quá đơn giản, overkill)
- ❌ Thứ tự không quan trọng (dùng Command pattern)
- ❌ Performance critical (chain có overhead)
- ❌ Request bắt buộc phải được xử lý (chain không đảm bảo)

### Ưu điểm
1. **Decoupling**: Sender không cần biết handler nào sẽ xử lý
2. **Flexibility**: Dễ dàng thêm/xóa/sắp xếp lại handlers
3. **Single Responsibility**: Mỗi handler chỉ lo logic của mình
4. **Open/Closed Principle**: Thêm handlers mới không cần sửa code cũ
5. **Dynamic Chain**: Có thể thay đổi chain runtime

### Nhược điểm
1. **No Guarantee**: Request có thể không được xử lý nếu không có handler phù hợp
2. **Performance**: Chain dài có thể ảnh hưởng performance
3. **Debugging**: Khó debug khi chain phức tạp
4. **Circular Reference**: Có thể tạo vòng lặp vô hạn nếu không cẩn thận

### So sánh với các patterns khác

#### Chain of Responsibility vs Command
- **Chain**: Chuyển request qua chuỗi handlers → focus vào routing
- **Command**: Encapsulate request thành object → focus vào undo/redo

#### Chain of Responsibility vs Strategy
- **Chain**: Multiple handlers, chỉ một xử lý → focus vào escalation
- **Strategy**: One context, select algorithm → focus vào interchangeable algorithms

#### Chain of Responsibility vs Decorator
- **Chain**: Request chỉ được xử lý bởi một handler trong chain
- **Decorator**: Request được xử lý bởi tất cả decorators (wrap around)

---

## 2. Mô tả bài toán

### 🎬 Context Linking (Liên kết với patterns đã học)

**Liên kết với**: **EnterpriseSoft ERP System** (đã học trong Singleton pattern)

- **Singleton pattern**: Configuration Manager cho EnterpriseSoft ERP
- **Chain of Responsibility**: Customer Support Ticket System cho EnterpriseSoft ERP
- **Memory Anchor**: "Enterprise domain = Configuration (Singleton) + Support Tickets (Chain of Responsibility)"

### Bài toán: EnterpriseSoft Customer Support System

**Ngữ cảnh:**
EnterpriseSoft là một công ty phần mềm ERP lớn với hàng nghìn khách hàng doanh nghiệp. Mỗi ngày, hệ thống support nhận hàng trăm support tickets từ khách hàng với các mức độ phức tạp khác nhau:

- **Basic Issues**: Câu hỏi đơn giản về cách sử dụng, reset password, feature requests
- **Technical Issues**: Lỗi kỹ thuật, integration issues, configuration problems
- **Escalated Issues**: Vấn đề nghiêm trọng ảnh hưởng nhiều users, data corruption
- **Critical Issues**: System outage, security breaches, business-critical failures

**Tình huống hiện tại:**
Hệ thống support hiện tại có 4 levels:
1. **Level 1 Support** (Junior Support Engineers): Xử lý basic issues
2. **Level 2 Support** (Senior Support Engineers): Xử lý technical issues
3. **Support Manager**: Xử lý escalated issues và quản lý team
4. **Engineering Director**: Xử lý critical issues và quyết định chiến lược

**Vấn đề cụ thể:**

Khi khách hàng gửi ticket, hệ thống cũ hoạt động như sau:
```java
// Tight coupling - Hard to maintain
if (ticket.getPriority().equals("basic")) {
    level1Support.resolve(ticket);
} else if (ticket.getPriority().equals("technical")) {
    level2Support.resolve(ticket);
} else if (ticket.getPriority().equals("escalated")) {
    manager.resolve(ticket);
} else if (ticket.getPriority().equals("critical")) {
    director.resolve(ticket);
} else {
    // What to do?
}
```

**Các vấn đề:**

1. **Tight Coupling**: Client phải biết tất cả support levels và quyết định routing
2. **Inflexible**: Khó thêm support levels mới (ví dụ: Level 3 Specialist)
3. **No Escalation**: Không tự động escalate khi level thấp hơn không handle được
4. **Hard to Test**: Phải test tất cả if-else branches
5. **Violation of Open/Closed**: Thêm level mới phải sửa if-else logic
6. **Single Point of Failure**: Client logic phức tạp, dễ lỗi

**Ví dụ thực tế:**
- Ticket #1234: "How to reset password?" → Basic → Nên do L1 handle
- Ticket #5678: "API integration returning 500 error" → Technical → Nên do L2 handle
- Ticket #9012: "20 users cannot access system" → Escalated → Nên do Manager handle
- Ticket #3456: "Complete system down, revenue loss" → Critical → Nên do Director handle

Nhưng đôi khi:
- L1 không biết cách fix API issue → cần escalate to L2
- L2 không có authority để shutdown system → cần escalate to Manager
- Manager cần Director approval cho architectural changes

**Nhu cầu:**
- Tự động route tickets đến đúng support level
- Cho phép escalation khi level thấp không handle được
- Dễ dàng thêm/xóa support levels
- Decouple sender khỏi receiver
- Flexible chain configuration

---

## 3. Yêu cầu bài toán

### Input

**Hệ thống hiện có:**
- EnterpriseSoft Support System với 4 support levels
- Support tickets từ customers với different priorities
- Mỗi support level có capabilities và limitations riêng
- Quy trình escalation khi không handle được

**Support Levels:**
1. **Level 1 Support**: Handles basic issues (password, how-to questions)
2. **Level 2 Support**: Handles technical issues (bugs, API, integration)
3. **Support Manager**: Handles escalated issues (multiple users affected)
4. **Engineering Director**: Handles critical issues (system-wide failures)

**Ticket Types:**
- **Basic**: Simple questions, password resets, feature requests
- **Technical**: Bugs, errors, integration problems, configuration
- **Escalated**: Multiple users affected, data issues, serious bugs
- **Critical**: Complete outage, security breach, revenue impact

### Problem

**Vấn đề với cách tiếp cận hiện tại (if-else ladder):**

1. **Tight Coupling**:
   - Client phải biết tất cả support levels
   - Client phải quyết định routing logic
   - Thay đổi support levels → phải sửa client code

2. **Inflexibility**:
   - Thêm support level mới (L3 Specialist) → sửa nhiều nơi
   - Thay đổi escalation rules → phải refactor
   - Không thể reorder support levels dễ dàng

3. **No Automatic Escalation**:
   - Nếu L1 không handle được → ticket stuck
   - Không có mechanism để escalate tự động
   - Phải manual reassign tickets

4. **Violation of OCP**:
   - Thêm ticket type mới → sửa if-else
   - Thêm support level → sửa if-else
   - Không extend được mà không modify

5. **Complex Testing**:
   - Phải test tất cả if-else branches
   - Nhiều edge cases (unknown priority, null, etc.)
   - Hard to mock và unit test

6. **Unclear Responsibility**:
   - Client biết quá nhiều về support levels
   - Support levels không tự quyết định
   - Trộn business logic vào routing logic

### Solution

**Chain of Responsibility Pattern giải quyết:**

1. **Decoupling**:
   - Client chỉ biết handler đầu tiên
   - Gửi ticket vào chain → không care ai handle
   - Support levels tự quyết định handle hay pass

2. **Automatic Escalation**:
   - L1 không handle được → tự động pass to L2
   - L2 không handle được → tự động pass to Manager
   - Manager không handle được → escalate to Director
   - Chain tự động route đúng handler

3. **Flexibility**:
   - Thêm L3 Specialist → chỉ cần insert vào chain
   - Reorder chain → chỉ cần change successor
   - Remove level → update predecessor's successor

4. **Clear Responsibility**:
   - Mỗi handler chỉ lo logic của mình
   - Handler tự decide: "I can handle" or "Pass to next"
   - Client không biết gì về routing logic

5. **Easy to Extend**:
   - Thêm handler mới → implement interface
   - Không sửa existing handlers
   - Open/Closed Principle

**Cách hoạt động:**

```
Client creates ticket → Send to L1
  ↓
L1: Can I handle?
  → YES: Resolve ticket ✓
  → NO: Pass to L2
      ↓
    L2: Can I handle?
      → YES: Resolve ticket ✓
      → NO: Pass to Manager
          ↓
        Manager: Can I handle?
          → YES: Resolve ticket ✓
          → NO: Pass to Director
              ↓
            Director: Handle (highest level)
```

**Chain structure:**
```
Level1Support → Level2Support → Manager → Director
    ↑                                         ↓
    └─────────── Client sends here ───────────┘
              (doesn't know final handler)
```

### Expected Output

**Sau khi áp dụng Chain of Responsibility:**

1. **Automatic Routing**:
   ```
   Ticket: "How to reset password?"
   → L1 handles → Resolved ✓

   Ticket: "API returning 500 error"
   → L1 passes → L2 handles → Resolved ✓

   Ticket: "50 users cannot login"
   → L1 passes → L2 passes → Manager handles → Resolved ✓

   Ticket: "Complete system outage"
   → L1 passes → L2 passes → Manager passes → Director handles → Resolved ✓
   ```

2. **Flexible Chain**:
   - Dễ dàng thêm L3 Specialist giữa L2 và Manager
   - Có thể bypass L1 cho VIP customers
   - Reorder chain based on business rules

3. **Clean Code**:
   - Client code: `supportChain.handleTicket(ticket);`
   - Không if-else, không switch-case
   - Handlers tự quyết định

4. **Testable**:
   - Test từng handler độc lập
   - Mock successor dễ dàng
   - Test chain behavior riêng

5. **Maintainable**:
   - Thêm handler mới không sửa code cũ
   - Clear separation of concerns
   - Easy to understand flow

---

## 4. Hiệu quả của việc sử dụng Chain of Responsibility Pattern

### Lợi ích cụ thể trong bài toán Support System

#### 1. Decoupling Sender and Receiver

**Trước khi dùng Chain:**
```java
// Client phải biết tất cả support levels
class TicketRouter {
    void routeTicket(Ticket ticket) {
        if (ticket.getPriority().equals("basic")) {
            level1Support.resolve(ticket);  // Tight coupling
        } else if (ticket.getPriority().equals("technical")) {
            level2Support.resolve(ticket);  // Tight coupling
        }
        // ... more if-else
    }
}
```

**Sau khi dùng Chain:**
```java
// Client chỉ biết handler đầu tiên
class TicketRouter {
    void routeTicket(Ticket ticket) {
        supportChain.handleTicket(ticket);  // Loose coupling
        // Chain tự quyết định ai handle
    }
}
```

**Lợi ích:**
- Client không phụ thuộc vào support levels cụ thể
- Thay đổi support structure không ảnh hưởng client
- Testability tốt hơn (mock chain dễ dàng)

#### 2. Flexibility - Thêm/Xóa/Reorder Handlers

**Scenario: Thêm Level 3 Specialist**

**Trước khi dùng Chain:**
```java
// Phải sửa nhiều nơi
if (ticket.getPriority().equals("basic")) {
    level1Support.resolve(ticket);
} else if (ticket.getPriority().equals("technical")) {
    level2Support.resolve(ticket);
} else if (ticket.getPriority().equals("specialist")) {  // NEW
    level3Specialist.resolve(ticket);  // NEW - phải sửa if-else
} else if (ticket.getPriority().equals("escalated")) {
    manager.resolve(ticket);
}
// Phải update tất cả nơi có routing logic
```

**Sau khi dùng Chain:**
```java
// Chỉ cần insert vào chain
level2Support.setSuccessor(level3Specialist);  // NEW
level3Specialist.setSuccessor(manager);        // Update
// Không cần sửa client code, không cần sửa existing handlers
```

**Lợi ích:**
- Thêm handler: O(1) operation (chỉ update successor links)
- Không vi phạm Open/Closed Principle
- Không risk breaking existing code

#### 3. Automatic Escalation

**Scenario: Ticket cần escalate**

**Trước khi dùng Chain:**
```java
// Manual escalation - error prone
Ticket ticket = new Ticket("API error");
if (level1Support.canHandle(ticket)) {
    level1Support.resolve(ticket);
} else {
    // Phải explicitly escalate
    if (level2Support.canHandle(ticket)) {
        level2Support.resolve(ticket);
    } else {
        // Nếu quên check → ticket lost
        manager.resolve(ticket);
    }
}
```

**Sau khi dùng Chain:**
```java
// Automatic escalation - fool-proof
Ticket ticket = new Ticket("API error");
supportChain.handleTicket(ticket);
// Chain tự động:
// L1 check → cannot handle → pass to L2
// L2 check → can handle → resolve ✓
```

**Lợi ích:**
- Không bao giờ quên escalate
- Escalation logic trong từng handler (clear responsibility)
- Không duplicate escalation code

#### 4. Single Responsibility Principle

**Mỗi handler chỉ lo logic của mình:**

```java
// Level1Support - chỉ lo basic issues
class Level1Support extends SupportHandler {
    void handleTicket(Ticket ticket) {
        if (ticket.getPriority().equals("basic")) {
            // CHỈ xử lý basic logic
            resolveBasicIssue(ticket);
        } else {
            // Pass to successor - không biết gì về L2
            successor.handleTicket(ticket);
        }
    }
}

// Level2Support - chỉ lo technical issues
class Level2Support extends SupportHandler {
    void handleTicket(Ticket ticket) {
        if (ticket.getPriority().equals("technical")) {
            // CHỈ xử lý technical logic
            resolveTechnicalIssue(ticket);
        } else {
            // Pass to successor
            successor.handleTicket(ticket);
        }
    }
}
```

**Lợi ích:**
- Mỗi class có một responsibility rõ ràng
- Dễ maintain và test
- Dễ hiểu và document

#### 5. Performance Comparison

**Metric 1: Code Complexity**

| Aspect | Without Chain | With Chain |
|--------|--------------|------------|
| Client LOC | ~50 lines (if-else) | ~5 lines |
| Cyclomatic Complexity | O(n) - n handlers | O(1) |
| Adding Handler | Modify client + tests | Add handler only |
| Testing | Test all branches | Test each handler |

**Metric 2: Maintainability**

| Task | Without Chain | With Chain |
|------|--------------|------------|
| Add new support level | 30 min (modify 5 files) | 5 min (1 new file) |
| Reorder handlers | 15 min (refactor if-else) | 30 sec (update successor) |
| Fix bug in routing | 20 min (find all if-else) | 5 min (single handler) |

**Metric 3: Real-world Impact**

Giả sử EnterpriseSoft nhận **500 tickets/day**:

**Without Chain:**
- Average routing time: 100ms (if-else checks)
- Add new level: 2 hours (test all paths)
- Bug in routing: Affects all tickets → 500 tickets misrouted

**With Chain:**
- Average routing time: 120ms (chain traversal - slightly slower)
- Add new level: 10 minutes (just insert)
- Bug in one handler: Only affects specific priority → isolated impact

**Trade-off**: Chain có overhead nhỏ (~20ms) nhưng được bù lại bởi:
- Maintainability tốt hơn nhiều
- Fewer bugs (simpler code)
- Faster development time

### So sánh với các approaches khác

#### Approach 1: If-Else Ladder (Current)

```java
if (priority.equals("basic")) {
    l1.resolve();
} else if (priority.equals("technical")) {
    l2.resolve();
} else if (priority.equals("escalated")) {
    manager.resolve();
} else {
    director.resolve();
}
```

**Nhược điểm:**
- ❌ Tight coupling
- ❌ Hard to extend
- ❌ Violates OCP
- ❌ Complex testing

#### Approach 2: Switch-Case

```java
switch (ticket.getPriority()) {
    case "basic": l1.resolve(); break;
    case "technical": l2.resolve(); break;
    case "escalated": manager.resolve(); break;
    default: director.resolve();
}
```

**Nhược điểm:**
- ❌ Same as if-else
- ❌ Không có automatic escalation
- ❌ Rigid structure

#### Approach 3: Factory Pattern

```java
SupportLevel handler = SupportFactory.getHandler(ticket.getPriority());
handler.resolve(ticket);
```

**Nhược điểm:**
- ❌ Không có escalation (handler fixed)
- ❌ Không có chain concept
- ❌ Handler phải biết trước

#### Approach 4: Strategy Pattern

```java
SupportStrategy strategy = strategyMap.get(priority);
strategy.handle(ticket);
```

**Nhược điểm:**
- ❌ Không có escalation
- ❌ Không có fallback mechanism
- ❌ Client phải chọn strategy

#### ✅ Chain of Responsibility Pattern

```java
supportChain.handleTicket(ticket);
```

**Ưu điểm:**
- ✅ Automatic routing + escalation
- ✅ Loose coupling
- ✅ Easy to extend
- ✅ Clear responsibility
- ✅ Fallback mechanism

### Trade-offs cần lưu ý

#### Nhược điểm của Chain of Responsibility:

1. **No Guarantee of Handling**:
   - Request có thể reach end of chain mà không được handle
   - **Giải pháp**: Default handler ở cuối chain

2. **Performance Overhead**:
   - Phải traverse chain (O(n) worst case)
   - **Giải pháp**: Keep chain short (3-5 handlers ideal)

3. **Debugging Difficulty**:
   - Khó trace request qua chain
   - **Giải pháp**: Logging ở mỗi handler

4. **Runtime Configuration**:
   - Chain setup phải đúng
   - **Giải pháp**: Unit test chain configuration

### Kết luận

**Chain of Responsibility phù hợp với Support System vì:**
1. ✅ Support escalation là use case điển hình
2. ✅ Flexibility quan trọng (support levels thay đổi)
3. ✅ Decoupling cần thiết (client không biết routing)
4. ✅ Easy to extend (thêm support levels)
5. ✅ Clear separation of concerns

**Performance trade-off nhỏ (~20ms) được bù bằng:**
- Maintainability tốt hơn nhiều
- Fewer bugs (simpler code)
- Faster development và testing
- Better scalability

---

## 5. Cài đặt

### 5.1. Abstract Handler - SupportHandler.java

```java
public abstract class SupportHandler {

	protected SupportHandler successor;
	protected String handlerName;

	public SupportHandler(String handlerName) {
		this.handlerName = handlerName;
	}

	public void setSuccessor(SupportHandler successor) {
		this.successor = successor;
	}

	public abstract void handleTicket(SupportTicket ticket);

	public String getHandlerName() {
		return handlerName;
	}
}
```

**Giải thích:**
- `successor`: Reference đến handler tiếp theo trong chain
- `setSuccessor()`: Setup chain bằng cách link handlers
- `handleTicket()`: Abstract method mà mỗi concrete handler implement
- `handlerName`: Để logging và debugging

### 5.2. Request Object - SupportTicket.java

```java
public class SupportTicket {

	private final String ticketId;
	private final String priority;
	private final String description;
	private final String customerName;

	public SupportTicket(String ticketId, String priority, String description, String customerName) {
		this.ticketId = ticketId;
		this.priority = priority;
		this.description = description;
		this.customerName = customerName;
	}

	public String getTicketId() {
		return ticketId;
	}

	public String getPriority() {
		return priority;
	}

	public String getDescription() {
		return description;
	}

	public String getCustomerName() {
		return customerName;
	}
}
```

**Giải thích:**
- Immutable object chứa ticket data
- Priority determines which handler should handle
- Passed along chain until handled

### 5.3. Concrete Handler 1 - Level1Support.java

```java
public class Level1Support extends SupportHandler {

	public Level1Support(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("basic")) {
			// L1 can handle basic issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName);
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Provided step-by-step guide");
			System.out.println("│ → Password reset link sent to email");
			System.out.println("│ → Basic issue resolved within 5 minutes");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else if (successor != null) {
			// Cannot handle - escalate to next level
			System.out.println("\n[" + handlerName + "] Cannot handle '" + ticket.getPriority() + "' priority ticket");
			System.out.println("[" + handlerName + "] Escalating to: " + successor.getHandlerName());
			successor.handleTicket(ticket);

		} else {
			System.out.println("\n[ERROR] No handler available for ticket: " + ticket.getTicketId());
		}
	}
}
```

**Giải thích:**
- Handles **basic** priority tickets
- Escalates other priorities to successor
- Shows resolution details khi handle

### 5.4. Concrete Handler 2 - Level2Support.java

```java
public class Level2Support extends SupportHandler {

	public Level2Support(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("technical")) {
			// L2 can handle technical issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName);
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Analyzed error logs and stack traces");
			System.out.println("│ → Identified API configuration issue");
			System.out.println("│ → Applied hotfix and tested integration");
			System.out.println("│ → Technical issue resolved within 2 hours");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else if (successor != null) {
			// Cannot handle - escalate to manager
			System.out.println("\n[" + handlerName + "] Cannot handle '" + ticket.getPriority() + "' priority ticket");
			System.out.println("[" + handlerName + "] Escalating to: " + successor.getHandlerName());
			successor.handleTicket(ticket);

		} else {
			System.out.println("\n[ERROR] No handler available for ticket: " + ticket.getTicketId());
		}
	}
}
```

**Giải thích:**
- Handles **technical** priority tickets
- Escalates non-technical issues
- Shows technical resolution process

### 5.5. Concrete Handler 3 - SupportManager.java

```java
public class SupportManager extends SupportHandler {

	public SupportManager(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("escalated")) {
			// Manager can handle escalated issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName);
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Coordinated with engineering team");
			System.out.println("│ → Deployed database rollback script");
			System.out.println("│ → Restored access for 50 affected users");
			System.out.println("│ → Escalated issue resolved within 4 hours");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else if (successor != null) {
			// Cannot handle - escalate to director
			System.out.println("\n[" + handlerName + "] Cannot handle '" + ticket.getPriority() + "' priority ticket");
			System.out.println("[" + handlerName + "] Escalating to: " + successor.getHandlerName());
			successor.handleTicket(ticket);

		} else {
			System.out.println("\n[ERROR] No handler available for ticket: " + ticket.getTicketId());
		}
	}
}
```

**Giải thích:**
- Handles **escalated** priority tickets
- Coordinates with teams để resolve
- Escalates critical issues to director

### 5.6. Concrete Handler 4 - EngineeringDirector.java

```java
public class EngineeringDirector extends SupportHandler {

	public EngineeringDirector(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("critical")) {
			// Director handles critical issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName + " (HIGHEST LEVEL)");
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase() + " 🚨");
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Initiated emergency response protocol");
			System.out.println("│ → Assembled crisis management team");
			System.out.println("│ → Activated backup systems");
			System.out.println("│ → Restored full service within 1 hour");
			System.out.println("│ → Post-mortem analysis scheduled");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else {
			// Director is highest level - handles everything if reached
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ⚠ HANDLED BY: " + handlerName + " (FALLBACK)");
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Priority: " + ticket.getPriority());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Note: Ticket reached highest level (fallback handler)");
			System.out.println("│ Director will personally review and assign");
			System.out.println("│ Status: UNDER REVIEW");
			System.out.println("└─────────────────────────────────────────────────────┘");
		}
	}
}
```

**Giải thích:**
- Handles **critical** priority tickets (highest severity)
- Acts as **fallback handler** (end of chain)
- No successor - terminal handler
- Handles anything that reaches this level

### 5.7. Client/Demo - ChainDemo.java

```java
public class ChainDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════════════════╗");
		System.out.println("║   CHAIN OF RESPONSIBILITY PATTERN DEMO                     ║");
		System.out.println("║   EnterpriseSoft ERP - Customer Support System             ║");
		System.out.println("║   (Linked: Singleton pattern - EnterpriseSoft ERP)         ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");

		// Step 1: Create handlers (support levels)
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("STEP 1: Creating Support Chain");
		System.out.println("═══════════════════════════════════════════════════════════");

		SupportHandler level1 = new Level1Support("Level 1 Support (Junior Engineers)");
		SupportHandler level2 = new Level2Support("Level 2 Support (Senior Engineers)");
		SupportHandler manager = new SupportManager("Support Manager");
		SupportHandler director = new EngineeringDirector("Engineering Director");

		System.out.println("\n✓ Created 4 support levels:");
		System.out.println("  1. " + level1.getHandlerName());
		System.out.println("  2. " + level2.getHandlerName());
		System.out.println("  3. " + manager.getHandlerName());
		System.out.println("  4. " + director.getHandlerName());

		// Step 2: Build the chain
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("STEP 2: Building Chain of Responsibility");
		System.out.println("═══════════════════════════════════════════════════════════");

		level1.setSuccessor(level2);
		level2.setSuccessor(manager);
		manager.setSuccessor(director);
		// director has no successor (end of chain)

		System.out.println("\n✓ Chain structure:");
		System.out.println("  Level 1 → Level 2 → Manager → Director");
		System.out.println("\n💡 Tickets start at Level 1, escalate if needed");

		// Step 3: Test with different priority tickets
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 1: Basic Priority Ticket");
		System.out.println("═══════════════════════════════════════════════════════════");

		SupportTicket ticket1 = new SupportTicket(
			"#1234",
			"basic",
			"How to reset my password?",
			"Acme Corp"
		);

		System.out.println("\n→ Ticket submitted: " + ticket1.getTicketId());
		System.out.println("  Customer: " + ticket1.getCustomerName());
		System.out.println("  Issue: " + ticket1.getDescription());
		System.out.println("\n→ Sending to support chain...");

		level1.handleTicket(ticket1);

		// Test 2: Technical ticket
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 2: Technical Priority Ticket");
		System.out.println("═══════════════════════════════════════════════════════════");

		SupportTicket ticket2 = new SupportTicket(
			"#5678",
			"technical",
			"API integration returning 500 Internal Server Error",
			"TechStart Inc"
		);

		System.out.println("\n→ Ticket submitted: " + ticket2.getTicketId());
		System.out.println("  Customer: " + ticket2.getCustomerName());
		System.out.println("  Issue: " + ticket2.getDescription());
		System.out.println("\n→ Sending to support chain...");

		level1.handleTicket(ticket2);

		// Test 3: Escalated ticket
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 3: Escalated Priority Ticket");
		System.out.println("═══════════════════════════════════════════════════════════");

		SupportTicket ticket3 = new SupportTicket(
			"#9012",
			"escalated",
			"50 users cannot access their accounts - database error",
			"MegaCorp Ltd"
		);

		System.out.println("\n→ Ticket submitted: " + ticket3.getTicketId());
		System.out.println("  Customer: " + ticket3.getCustomerName());
		System.out.println("  Issue: " + ticket3.getDescription());
		System.out.println("\n→ Sending to support chain...");

		level1.handleTicket(ticket3);

		// Test 4: Critical ticket
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 4: Critical Priority Ticket");
		System.out.println("═══════════════════════════════════════════════════════════");

		SupportTicket ticket4 = new SupportTicket(
			"#3456",
			"critical",
			"Complete system outage - all customers affected - revenue loss",
			"Enterprise Global"
		);

		System.out.println("\n→ Ticket submitted: " + ticket4.getTicketId());
		System.out.println("  Customer: " + ticket4.getCustomerName());
		System.out.println("  Issue: " + ticket4.getDescription());
		System.out.println("\n→ Sending to support chain...");

		level1.handleTicket(ticket4);

		// Test 5: Unknown priority (fallback)
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 5: Unknown Priority (Fallback Test)");
		System.out.println("═══════════════════════════════════════════════════════════");

		SupportTicket ticket5 = new SupportTicket(
			"#7890",
			"unknown",
			"Some weird issue we've never seen before",
			"Mystery Customer"
		);

		System.out.println("\n→ Ticket submitted: " + ticket5.getTicketId());
		System.out.println("  Customer: " + ticket5.getCustomerName());
		System.out.println("  Issue: " + ticket5.getDescription());
		System.out.println("\n→ Sending to support chain...");

		level1.handleTicket(ticket5);

		// Summary
		System.out.println("\n\n╔════════════════════════════════════════════════════════════╗");
		System.out.println("║                      SUMMARY                               ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");
		System.out.println("\n✓ Automatic Routing:");
		System.out.println("  - Basic ticket → Level 1 handled");
		System.out.println("  - Technical ticket → Level 2 handled (L1 escalated)");
		System.out.println("  - Escalated ticket → Manager handled (L1, L2 escalated)");
		System.out.println("  - Critical ticket → Director handled (all escalated)");
		System.out.println("  - Unknown ticket → Director handled (fallback)");

		System.out.println("\n✓ Chain of Responsibility Benefits:");
		System.out.println("  - Decoupling: Client doesn't know which handler will handle");
		System.out.println("  - Automatic escalation: Tickets routed to right level");
		System.out.println("  - Flexibility: Easy to add/remove/reorder support levels");
		System.out.println("  - Fallback: Director catches all unhandled tickets");

		System.out.println("\n🎬 Context Link: EnterpriseSoft ERP (Singleton) now has");
		System.out.println("   smart support ticket routing (Chain of Responsibility)!");
		System.out.println("════════════════════════════════════════════════════════════");
	}
}
```

**Giải thích:**
- Tạo 4 support handlers
- Build chain: L1 → L2 → Manager → Director
- Test 5 scenarios với different priorities
- Demo automatic routing và escalation
- Highlight context link với Singleton pattern

---

## 6. Kết quả chạy chương trình

### 6.1. Giải thích các testcase

#### Test 1: Basic Priority Ticket
**Mục đích:**
Kiểm tra xem Level 1 Support có xử lý được ticket cơ bản (password reset, login issues) không. Test này minh họa trường hợp đơn giản nhất của chain - request được xử lý ngay tại handler đầu tiên mà không cần escalate.

**Cách triển khai:**
```java
SupportTicket ticket1 = new SupportTicket("#1234", "basic", "Password reset", "Acme Corp");
level1.handleTicket(ticket1);
```

Trong `Level1Support.handleTicket()`:
```java
if (ticket.getPriority().equals("basic")) {
    // L1 can handle basic issues
    System.out.println("HANDLED BY: " + handlerName);
    // ... resolution details
} else if (successor != null) {
    // Escalate to next level
    successor.handleTicket(ticket);
}
```

**Kết quả mong đợi:**
- Ticket được xử lý bởi Level 1 Support
- Không escalate lên Level 2
- Output: "Level 1 Support handling ticket #1234"

**Ý nghĩa:**
Testcase này demonstrate rằng Chain of Responsibility pattern cho phép handler đầu tiên xử lý request nếu nó có khả năng. Client (main) không cần biết handler nào sẽ xử lý - chỉ cần gửi vào chain. Đây là loose coupling principle - sender không phụ thuộc vào receiver cụ thể.

---

#### Test 2: Technical Priority Ticket
**Mục đích:**
Kiểm tra escalation mechanism - khi Level 1 không đủ khả năng, ticket tự động được chuyển đến Level 2. Test này minh họa cách request "travel through chain" cho đến khi tìm được handler phù hợp.

**Cách triển khai:**
```java
SupportTicket ticket2 = new SupportTicket("#5678", "technical", "API integration error", "TechStart Inc");
level1.handleTicket(ticket2);
```

Flow trong chain:
1. Level1Support nhận ticket, check priority
2. Priority = "technical" → Level 1 không handle được
3. Level 1 gọi `successor.handleTicket(ticket)` (escalate to Level 2)
4. Level2Support nhận ticket, check priority
5. Priority = "technical" → Level 2 handle thành công

**Kết quả mong đợi:**
- Level 1 nhận nhưng không xử lý
- Level 1 escalate lên Level 2 tự động
- Level 2 xử lý ticket
- Output hiển thị quá trình escalation: "Level 1 cannot handle... Escalating to Level 2"

**Ý nghĩa:**
Testcase này demonstrate successor mechanism - core của Chain of Responsibility. Handler có thể decide "I can't handle this" và pass sang successor. Client không cần biết logic này - pattern tự động route request đến đúng handler. Đây là automatic escalation - một trong những lợi ích chính của pattern.

---

#### Test 3: Escalated Priority Ticket
**Mục đích:**
Test chain với request cần đến Manager level. Minh họa request đi qua nhiều handlers (Level 1 → Level 2 → Manager) cho đến khi tìm được handler có authority phù hợp.

**Cách triển khai:**
```java
SupportTicket ticket3 = new SupportTicket("#9012", "escalated", "Database access issue", "MegaCorp Ltd");
level1.handleTicket(ticket3);
```

Chain traversal:
- Level 1: "escalated" ≠ "basic" → pass to successor
- Level 2: "escalated" ≠ "technical" → pass to successor
- Manager: "escalated" = "escalated" → handle successfully

**Kết quả mong đợi:**
- Request đi qua Level 1 (pass)
- Qua Level 2 (pass)
- Manager xử lý thành công
- Output hiển thị 2 lần escalation trước khi được handle

**Ý nghĩa:**
Testcase này demonstrate rằng chain có thể dài, request có thể đi qua nhiều handlers trước khi được xử lý. Pattern này flexible - có thể thêm/xóa handlers trong chain (ví dụ: thêm Level 3 Specialist giữa Level 2 và Manager) mà không cần sửa client code. Đây là Open/Closed Principle - open for extension, closed for modification.

---

#### Test 4: Critical Priority Ticket
**Mục đích:**
Test request đi đến cuối chain (Director). Minh họa rằng chain có thể route request đến highest authority khi cần thiết, và mỗi handler trong chain có responsibility rõ ràng.

**Cách triển khai:**
```java
SupportTicket ticket4 = new SupportTicket("#3456", "critical", "System outage", "Enterprise Global");
level1.handleTicket(ticket4);
```

Full chain traversal:
- Level 1: cannot handle → escalate
- Level 2: cannot handle → escalate
- Manager: cannot handle → escalate
- Director: "critical" priority → handle (highest authority)

**Kết quả mong đợi:**
- Request đi qua toàn bộ chain: Level 1 → Level 2 → Manager → Director
- Director xử lý (highest level)
- Output hiển thị 3 lần escalation và resolution bởi Engineering Director

**Ý nghĩa:**
Testcase này demonstrate escalation to highest level. Critical issues cần highest authority để resolve. Pattern đảm bảo rằng request luôn đến đúng level of responsibility. Không cần if-else logic - chain tự động route based on handler capabilities.

---

#### Test 5: Unknown Priority (Fallback Test)
**Mục đích:**
Test fallback mechanism - khi priority không match bất kỳ handler nào trong chain. Minh họa rằng pattern cần default handler để ensure tất cả requests đều được xử lý.

**Cách triển khai:**
```java
SupportTicket ticket5 = new SupportTicket("#7890", "unknown", "Unusual issue", "Mystery Customer");
level1.handleTicket(ticket5);
```

Fallback behavior:
- Level 1, 2, Manager: priority không match → escalate
- Director: end of chain → acts as fallback handler → handles everything that reaches

**Kết quả mong đợi:**
- Request đi qua toàn bộ chain mà không match handler nào
- Đến Director (end of chain)
- Director xử lý as fallback (không phải vì match "unknown", mà vì là terminal handler)
- Output: "HANDLED BY: Engineering Director (FALLBACK)"

**Ý nghĩa:**
Testcase này demonstrate best practice: luôn có default/fallback handler ở cuối chain. Nếu không có fallback, request với unknown priority sẽ bị lost (không được xử lý). Director trong design này serves dual purpose: (1) handle critical issues, (2) catch all unhandled requests. Đây là defensive programming - ensure no request falls through cracks.

---

### 6.2. Output thực tế

```
=== Chain of Responsibility Demo ===

Chain: Level 1 -> Level 2 -> Manager -> Director

--- Test 1: Basic Priority Ticket ---
[Level 1 Support] Handling ticket #1234: Password reset

--- Test 2: Technical Priority Ticket ---
[Level 1 Support] Cannot handle 'technical' priority ticket
[Level 1 Support] Escalating to: Level 2 Support
[Level 2 Support] Handling ticket #5678: API integration error

--- Test 3: Escalated Priority Ticket ---
[Level 1 Support] Cannot handle 'escalated' priority ticket
[Level 1 Support] Escalating to: Level 2 Support
[Level 2 Support] Cannot handle 'escalated' priority ticket
[Level 2 Support] Escalating to: Support Manager
[Support Manager] Handling ticket #9012: Database access issue

--- Test 4: Critical Priority Ticket ---
[Level 1 Support] Cannot handle 'critical' priority ticket
[Level 1 Support] Escalating to: Level 2 Support
[Level 2 Support] Cannot handle 'critical' priority ticket
[Level 2 Support] Escalating to: Support Manager
[Support Manager] Cannot handle 'critical' priority ticket
[Support Manager] Escalating to: Engineering Director
[Engineering Director] Handling ticket #3456: System outage

--- Test 5: Unknown Priority (Fallback) ---
[Level 1 Support] Cannot handle 'unknown' priority ticket
[Level 1 Support] Escalating to: Level 2 Support
[Level 2 Support] Cannot handle 'unknown' priority ticket
[Level 2 Support] Escalating to: Support Manager
[Support Manager] Cannot handle 'unknown' priority ticket
[Support Manager] Escalating to: Engineering Director
[Engineering Director] Handling ticket #7890: Unusual issue (FALLBACK)
```

---

### 6.3. Output ban đầu (verbose version)

```
╔════════════════════════════════════════════════════════════╗
║   CHAIN OF RESPONSIBILITY PATTERN DEMO                     ║
║   EnterpriseSoft ERP - Customer Support System             ║
║   (Linked: Singleton pattern - EnterpriseSoft ERP)         ║
╚════════════════════════════════════════════════════════════╝


═══════════════════════════════════════════════════════════
STEP 1: Creating Support Chain
═══════════════════════════════════════════════════════════

✓ Created 4 support levels:
  1. Level 1 Support (Junior Engineers)
  2. Level 2 Support (Senior Engineers)
  3. Support Manager
  4. Engineering Director


═══════════════════════════════════════════════════════════
STEP 2: Building Chain of Responsibility
═══════════════════════════════════════════════════════════

✓ Chain structure:
  Level 1 → Level 2 → Manager → Director

💡 Tickets start at Level 1, escalate if needed


═══════════════════════════════════════════════════════════
TEST 1: Basic Priority Ticket
═══════════════════════════════════════════════════════════

→ Ticket submitted: #1234
  Customer: Acme Corp
  Issue: How to reset my password?

→ Sending to support chain...

┌─────────────────────────────────────────────────────┐
│ ✓ HANDLED BY: Level 1 Support (Junior Engineers)
├─────────────────────────────────────────────────────┤
│ Ticket ID: #1234
│ Customer: Acme Corp
│ Priority: BASIC
│ Issue: How to reset my password?
├─────────────────────────────────────────────────────┤
│ Resolution:
│ → Provided step-by-step guide
│ → Password reset link sent to email
│ → Basic issue resolved within 5 minutes
│ Status: RESOLVED ✓
└─────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════
TEST 2: Technical Priority Ticket
═══════════════════════════════════════════════════════════

→ Ticket submitted: #5678
  Customer: TechStart Inc
  Issue: API integration returning 500 Internal Server Error

→ Sending to support chain...

[Level 1 Support (Junior Engineers)] Cannot handle 'technical' priority ticket
[Level 1 Support (Junior Engineers)] Escalating to: Level 2 Support (Senior Engineers)

┌─────────────────────────────────────────────────────┐
│ ✓ HANDLED BY: Level 2 Support (Senior Engineers)
├─────────────────────────────────────────────────────┤
│ Ticket ID: #5678
│ Customer: TechStart Inc
│ Priority: TECHNICAL
│ Issue: API integration returning 500 Internal Server Error
├─────────────────────────────────────────────────────┤
│ Resolution:
│ → Analyzed error logs and stack traces
│ → Identified API configuration issue
│ → Applied hotfix and tested integration
│ → Technical issue resolved within 2 hours
│ Status: RESOLVED ✓
└─────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════
TEST 3: Escalated Priority Ticket
═══════════════════════════════════════════════════════════

→ Ticket submitted: #9012
  Customer: MegaCorp Ltd
  Issue: 50 users cannot access their accounts - database error

→ Sending to support chain...

[Level 1 Support (Junior Engineers)] Cannot handle 'escalated' priority ticket
[Level 1 Support (Junior Engineers)] Escalating to: Level 2 Support (Senior Engineers)

[Level 2 Support (Senior Engineers)] Cannot handle 'escalated' priority ticket
[Level 2 Support (Senior Engineers)] Escalating to: Support Manager

┌─────────────────────────────────────────────────────┐
│ ✓ HANDLED BY: Support Manager
├─────────────────────────────────────────────────────┤
│ Ticket ID: #9012
│ Customer: MegaCorp Ltd
│ Priority: ESCALATED
│ Issue: 50 users cannot access their accounts - database error
├─────────────────────────────────────────────────────┤
│ Resolution:
│ → Coordinated with engineering team
│ → Deployed database rollback script
│ → Restored access for 50 affected users
│ → Escalated issue resolved within 4 hours
│ Status: RESOLVED ✓
└─────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════
TEST 4: Critical Priority Ticket
═══════════════════════════════════════════════════════════

→ Ticket submitted: #3456
  Customer: Enterprise Global
  Issue: Complete system outage - all customers affected - revenue loss

→ Sending to support chain...

[Level 1 Support (Junior Engineers)] Cannot handle 'critical' priority ticket
[Level 1 Support (Junior Engineers)] Escalating to: Level 2 Support (Senior Engineers)

[Level 2 Support (Senior Engineers)] Cannot handle 'critical' priority ticket
[Level 2 Support (Senior Engineers)] Escalating to: Support Manager

[Support Manager] Cannot handle 'critical' priority ticket
[Support Manager] Escalating to: Engineering Director

┌─────────────────────────────────────────────────────┐
│ ✓ HANDLED BY: Engineering Director (HIGHEST LEVEL)
├─────────────────────────────────────────────────────┤
│ Ticket ID: #3456
│ Customer: Enterprise Global
│ Priority: CRITICAL 🚨
│ Issue: Complete system outage - all customers affected - revenue loss
├─────────────────────────────────────────────────────┤
│ Resolution:
│ → Initiated emergency response protocol
│ → Assembled crisis management team
│ → Activated backup systems
│ → Restored full service within 1 hour
│ → Post-mortem analysis scheduled
│ Status: RESOLVED ✓
└─────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════
TEST 5: Unknown Priority (Fallback Test)
═══════════════════════════════════════════════════════════

→ Ticket submitted: #7890
  Customer: Mystery Customer
  Issue: Some weird issue we've never seen before

→ Sending to support chain...

[Level 1 Support (Junior Engineers)] Cannot handle 'unknown' priority ticket
[Level 1 Support (Junior Engineers)] Escalating to: Level 2 Support (Senior Engineers)

[Level 2 Support (Senior Engineers)] Cannot handle 'unknown' priority ticket
[Level 2 Support (Senior Engineers)] Escalating to: Support Manager

[Support Manager] Cannot handle 'unknown' priority ticket
[Support Manager] Escalating to: Engineering Director

┌─────────────────────────────────────────────────────┐
│ ⚠ HANDLED BY: Engineering Director (FALLBACK)
├─────────────────────────────────────────────────────┤
│ Ticket ID: #7890
│ Priority: unknown
│ Issue: Some weird issue we've never seen before
├─────────────────────────────────────────────────────┤
│ Note: Ticket reached highest level (fallback handler)
│ Director will personally review and assign
│ Status: UNDER REVIEW
└─────────────────────────────────────────────────────┘


╔════════════════════════════════════════════════════════════╗
║                      SUMMARY                               ║
╚════════════════════════════════════════════════════════════╝

✓ Automatic Routing:
  - Basic ticket → Level 1 handled
  - Technical ticket → Level 2 handled (L1 escalated)
  - Escalated ticket → Manager handled (L1, L2 escalated)
  - Critical ticket → Director handled (all escalated)
  - Unknown ticket → Director handled (fallback)

✓ Chain of Responsibility Benefits:
  - Decoupling: Client doesn't know which handler will handle
  - Automatic escalation: Tickets routed to right level
  - Flexibility: Easy to add/remove/reorder support levels
  - Fallback: Director catches all unhandled tickets

🎬 Context Link: EnterpriseSoft ERP (Singleton) now has
   smart support ticket routing (Chain of Responsibility)!
════════════════════════════════════════════════════════════
```

### Giải thích output

#### Test 1: Basic Ticket
- Ticket gửi vào chain tại Level 1
- Level 1 check: priority = "basic" → **CAN HANDLE**
- Level 1 resolves ngay → không escalate
- **Result**: Handled by Level 1 ✓

#### Test 2: Technical Ticket
- Ticket gửi vào chain tại Level 1
- Level 1 check: priority = "technical" → **CANNOT HANDLE**
- Level 1 escalates to Level 2
- Level 2 check: priority = "technical" → **CAN HANDLE**
- Level 2 resolves → không escalate thêm
- **Result**: Escalated once, handled by Level 2 ✓

#### Test 3: Escalated Ticket
- Ticket gửi vào chain tại Level 1
- Level 1 → cannot handle → escalate to Level 2
- Level 2 → cannot handle → escalate to Manager
- Manager check: priority = "escalated" → **CAN HANDLE**
- Manager resolves
- **Result**: Escalated twice, handled by Manager ✓

#### Test 4: Critical Ticket
- Ticket gửi vào chain tại Level 1
- Level 1 → escalate to Level 2
- Level 2 → escalate to Manager
- Manager → escalate to Director
- Director check: priority = "critical" → **CAN HANDLE**
- Director resolves (highest authority)
- **Result**: Escalated through entire chain, handled by Director ✓

#### Test 5: Unknown Priority (Fallback)
- Ticket gửi vào chain tại Level 1
- Level 1 → cannot handle → escalate
- Level 2 → cannot handle → escalate
- Manager → cannot handle → escalate
- Director: **Fallback handler** → handles everything that reaches
- **Result**: Director handles as fallback ✓

### Key Observations

1. **Automatic Routing**: Client không biết handler nào sẽ handle
2. **Escalation**: Tickets tự động escalate cho đến khi handled
3. **Fallback**: Director acts as catch-all (end of chain)
4. **Decoupling**: Client code chỉ gọi `level1.handleTicket(ticket)`
5. **Flexibility**: Dễ thêm Level 3 Specialist giữa L2 và Manager

---

## 7. Sơ đồ UML

### Cấu trúc UML cho Customer Support System

```
┌─────────────────────────────────────────────────────┐
│         <<abstract>>                                 │
│         SupportHandler                               │
├─────────────────────────────────────────────────────┤
│ # successor : SupportHandler                         │
│ # handlerName : String                               │
├─────────────────────────────────────────────────────┤
│ + SupportHandler(handlerName : String)              │
│ + setSuccessor(successor : SupportHandler) : void   │
│ + handleTicket(ticket : SupportTicket) : void       │
│ + getHandlerName() : String                         │
└─────────────────────────────────────────────────────┘
                         ▲
                         │ extends
         ┌───────────────┼───────────────┬───────────────┐
         │               │               │               │
         │               │               │               │
┌────────┴────────┐ ┌────┴─────────┐ ┌──┴──────────┐ ┌──┴─────────────┐
│ Level1Support   │ │ Level2Support│ │SupportManager│ │EngineeringDir..│
├─────────────────┤ ├──────────────┤ ├─────────────┤ ├────────────────┤
│                 │ │              │ │             │ │                │
├─────────────────┤ ├──────────────┤ ├─────────────┤ ├────────────────┤
│ + handleTicket()│ │+ handleTicket│ │+handleTicket│ │+ handleTicket()│
└─────────────────┘ └──────────────┘ └─────────────┘ └────────────────┘


┌─────────────────────────────────────────────────────┐
│         SupportTicket                                │
├─────────────────────────────────────────────────────┤
│ - ticketId : String                                  │
│ - priority : String                                  │
│ - description : String                               │
│ - customerName : String                              │
├─────────────────────────────────────────────────────┤
│ + SupportTicket(...)                                │
│ + getTicketId() : String                            │
│ + getPriority() : String                            │
│ + getDescription() : String                         │
│ + getCustomerName() : String                        │
└─────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────┐
│         ChainDemo                                    │
├─────────────────────────────────────────────────────┤
│                                                      │
├─────────────────────────────────────────────────────┤
│ + main(args : String[]) : void                      │
└─────────────────────────────────────────────────────┘
```

### Relationships

1. **Inheritance**:
   - `Level1Support extends SupportHandler`
   - `Level2Support extends SupportHandler`
   - `SupportManager extends SupportHandler`
   - `EngineeringDirector extends SupportHandler`

2. **Association (Successor)**:
   - `SupportHandler` → `SupportHandler` (self-association)
   - Represents chain: handler points to next handler

3. **Dependency (Uses)**:
   - `SupportHandler` uses `SupportTicket` (parameter in handleTicket)
   - `ChainDemo` uses all classes (creates and uses)

### Chain Structure

```
Level1Support → Level2Support → SupportManager → EngineeringDirector
      ↓              ↓                 ↓                  ↓
   handles        handles           handles          handles all
    basic        technical         escalated         critical +
                                                      fallback
```

### Key UML Elements

**SupportHandler (Abstract)**:
- `successor`: Reference to next handler (enables chain)
- `setSuccessor()`: Builds chain by linking handlers
- `handleTicket()`: Abstract method implemented by concrete handlers
- `handlerName`: For logging and identification

**ConcreteHandlers**:
- Override `handleTicket()` với specific logic
- Check if can handle → resolve or pass to successor
- Each has different handling criteria (priority)

**SupportTicket**:
- Immutable data object (final fields)
- Contains ticket information
- Passed along chain

**ChainDemo**:
- Client code
- Creates handlers
- Builds chain (setSuccessor calls)
- Sends tickets into chain

### BlueJ Visualization

Trong file `package.bluej`:
- `SupportHandler`: `type=AbstractTarget`, `showInterface=true`
- Concrete handlers: `type=ClassTarget`, `showInterface=true`
- Dependencies show:
  - Inheritance arrows (ConcreteHandlers → SupportHandler)
  - Successor association (SupportHandler → SupportHandler)
  - Uses relationships (handlers use SupportTicket)

---

## 8. Tổng kết

### Kết luận về bài toán

**EnterpriseSoft Customer Support System** là một ví dụ điển hình của **Chain of Responsibility Pattern** vì:

1. **Natural Escalation Flow**: Support tickets tự nhiên có hierarchy (L1 → L2 → Manager → Director)
2. **Unknown Handler**: Khi ticket đến, không biết trước handler nào sẽ xử lý
3. **Multiple Handlers**: Có nhiều support levels với capabilities khác nhau
4. **Decoupling Needed**: Client không nên biết chi tiết về support structure
5. **Dynamic Routing**: Tickets được route automatically based on priority

### Ứng dụng thực tế của Chain of Responsibility

#### 1. Enterprise Applications

**Customer Support Systems** (như bài toán này):
- Zendesk, Jira Service Desk, ServiceNow
- Automatic ticket routing và escalation
- Support tiers: L1 → L2 → L3 → Management

**Expense Approval Workflows**:
- Employee → Manager → Director → CFO
- Amount-based routing ($0-1k → $1k-5k → $5k-20k → $20k+)
- Used in SAP, Oracle ERP systems

**Purchase Order Approvals**:
- Buyer → Supervisor → Manager → VP
- Multi-level approval cho large purchases

#### 2. Web Development

**Middleware Chains** (Express.js, ASP.NET):
```javascript
app.use(authMiddleware);      // Check authentication
app.use(validationMiddleware); // Validate input
app.use(loggingMiddleware);    // Log request
app.use(errorMiddleware);      // Handle errors
```

**Request Filtering**:
- Authentication → Authorization → Validation → Processing
- Each middleware decides: process or pass to next

#### 3. Content Moderation

**Social Media Platforms** (Facebook, YouTube):
- Spam Filter → Profanity Filter → Copyright Filter → Manual Review
- Content passes through chain of filters
- Each filter can approve, reject, or escalate

#### 4. Error Handling

**Exception Handling Chains**:
- Specific Exception Handler → General Exception Handler → Default Handler
- Each handler checks exception type
- Falls back to more general handlers

#### 5. Event Processing

**GUI Event Handling**:
- Child Widget → Parent Widget → Container → Window
- Click events bubble up until handled
- Windows Forms, Android, iOS use this

#### 6. Security

**Authentication Chains**:
- Local Auth → OAuth → SAML → SSO
- Try each method until successful

**Firewall Rules**:
- Rule 1 → Rule 2 → Rule 3 → Default Deny
- Each rule checks và allows/blocks/passes

### Khi nào nên dùng Chain of Responsibility?

**✅ Nên dùng khi:**

1. **Multiple Handlers**: Có nhiều objects có thể xử lý request
2. **Unknown Handler**: Handler cụ thể không biết trước (runtime decision)
3. **Decoupling**: Muốn decouple sender khỏi receiver
4. **Escalation**: Có escalation/hierarchy logic
5. **Dynamic Chain**: Handlers có thể thay đổi at runtime
6. **Approval Workflows**: Multi-level approval processes
7. **Filtering Pipelines**: Sequential filtering/processing

**❌ Không nên dùng khi:**

1. **Handler Known**: Handler luôn biết trước → direct call tốt hơn
2. **Single Handler**: Chỉ có 1-2 handlers → quá đơn giản, overkill
3. **No Order**: Thứ tự không quan trọng → dùng Command/Strategy
4. **Performance Critical**: Chain traversal có overhead
5. **Must Handle**: Request bắt buộc phải được handle (chain không đảm bảo)
6. **Simple Routing**: If-else đơn giản đủ rồi

### Alternatives và khi nào dùng

#### 1. Command Pattern

**Khi nào**: Muốn encapsulate requests as objects, undo/redo

**Ví dụ**: Text editor commands (Copy, Paste, Undo)

**So sánh**:
- Chain: Focus vào routing qua handlers
- Command: Focus vào encapsulation và undo

#### 2. Strategy Pattern

**Khi nào**: Muốn switch algorithms at runtime

**Ví dụ**: Payment methods (Credit Card, PayPal, Bitcoin)

**So sánh**:
- Chain: Multiple handlers, chỉ một xử lý
- Strategy: One context, select one algorithm

#### 3. Decorator Pattern

**Khi nào**: Muốn add responsibilities dynamically

**Ví dụ**: Adding features to coffee (Milk, Sugar, Whip)

**So sánh**:
- Chain: Request handled by one handler
- Decorator: Request processed by all decorators

#### 4. Observer Pattern

**Khi nào**: Multiple objects cần notified về state changes

**Ví dụ**: YouTube subscribers notified về new videos

**So sánh**:
- Chain: One-to-one handling (sequential)
- Observer: One-to-many notification (broadcast)

### Best Practices

**1. Keep Chain Short**: 3-5 handlers ideal (performance)

**2. Default Handler**: Luôn có fallback handler ở cuối chain

**3. Logging**: Log mỗi khi pass to successor (debugging)

**4. Clear Criteria**: Mỗi handler có clear "I can handle if..." logic

**5. Avoid Circular**: Đảm bảo chain không circular (infinite loop)

**6. Test Chain**: Unit test chain configuration riêng

**7. Document Order**: Document succession order rõ ràng

### Trade-offs cần nhớ

**Advantages**:
- ✅ Decoupling sender/receiver
- ✅ Flexibility (add/remove/reorder)
- ✅ Single Responsibility
- ✅ Open/Closed Principle

**Disadvantages**:
- ❌ No guarantee request handled
- ❌ Performance overhead (O(n) worst case)
- ❌ Debugging difficulty (trace through chain)
- ❌ Runtime configuration complexity

### Context Linking Summary

**EnterpriseSoft ERP** giờ có:
1. **Singleton Pattern**: Configuration Manager (centralized config)
2. **Chain of Responsibility**: Support Ticket System (automatic routing)

**Memory Anchor**: "Enterprise domain = Configuration + Support"

Khi nhớ Singleton → nhớ Enterprise → nhớ Chain of Responsibility!

### Final Thoughts

Chain of Responsibility là pattern tuyệt vời cho **escalation workflows** và **filtering pipelines**. Trong bài toán Support System, nó giải quyết hoàn hảo vấn đề routing tickets tự động và decoupling client khỏi support structure.

**Key Takeaway**: "Pass the request along the chain until someone handles it" - simple concept nhưng powerful trong right contexts!
