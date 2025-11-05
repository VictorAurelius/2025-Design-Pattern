# Plan Task: Tạo Bài Toán Mới Cho Facade Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Facade Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Facade Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích code hiện tại (để hiểu vấn đề)
- Đọc tất cả file Java trong folder `2-Facade-DP/`
- Đọc file `Solution/Facade.md` để hiểu cấu trúc solution hiện tại
- Phân tích:
  - Hiểu bài toán hiện tại đang giải quyết vấn đề gì
  - Xác định điểm yếu của bài toán hiện tại (nếu có)

### Bước 2: Học cách implement Facade Pattern từ tài liệu chuẩn
- Đọc file `Solution/Facade-Lecture.pdf` để:
  - Hiểu BẢN CHẤT của Facade Pattern (không phải học bài toán cụ thể)
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram có đầy đủ phương thức
  - Hiểu mối quan hệ giữa Facade và các Subsystems

- Đọc code trong `Code-Sample/FacadePattern-Project/` để:
  - Học cách đặt tên class, interface, method
  - Học coding convention và code style
  - Học cách triển khai Facade pattern đúng chuẩn
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 3: Sáng tạo bài toán mới

#### 3.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Facade Pattern:
- **KHÔNG dùng**: Home Theater System (đã có trong lecture/code sample)
- **KHÔNG dùng**: Computer Startup (ví dụ phổ biến)

**Gợi ý các lĩnh vực có thể dùng**:
- **Banking/Finance System**: Account management, transaction processing
- **E-commerce**: Order processing, inventory, shipping, payment
- **Hotel Management**: Room booking, restaurant, housekeeping
- **Restaurant System**: Kitchen, waiter, cashier, inventory
- **Smart Home**: Lights, thermostat, security, appliances
- **Car System**: Engine, transmission, fuel, electronics
- **Airport System**: Check-in, security, boarding, baggage
- **Library System**: Books, membership, borrowing, fines
- **Hospital System**: Appointment, medical records, billing, pharmacy
- **Gaming Console**: Graphics, audio, input, network

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 3.2. Yêu cầu bài toán phải có
1. **Hệ thống phức tạp**: Nhiều subsystems phải phối hợp với nhau
2. **Client đơn giản hóa**: Client không muốn biết chi tiết bên trong
3. **Facade đơn giản**: Cung cấp interface đơn giản cho các tác vụ phổ biến
4. **High-level operations**: Các method của Facade thực hiện nhiều bước

**Đặc điểm của Facade Pattern**:
- Facade không ẩn hoàn toàn subsystems (client vẫn có thể truy cập trực tiếp nếu cần)
- Facade đơn giản hóa interface nhưng không thêm functionality mới
- Facade orchestrate (điều phối) các subsystems để thực hiện tác vụ phức tạp
- Giảm coupling giữa client và subsystems

#### 3.3. Thiết kế các thành phần
- **Facade**: Class chính cung cấp interface đơn giản
- **Subsystem Classes**: Các class phức tạp bên trong (3-6 classes)
- **Client/Main**: Class sử dụng Facade

**Lưu ý**:
- Facade có references đến tất cả subsystem classes
- Mỗi method của Facade thực hiện nhiều bước trên các subsystems
- Subsystems không biết về sự tồn tại của Facade
- Client chỉ tương tác với Facade (không trực tiếp với subsystems)

### Bước 4: Viết lại Solution/Facade.md

Tạo lại file `Solution/Facade.md` với cấu trúc:

#### 4.1. Mô tả mẫu Facade
- Giữ nguyên phần mô tả tổng quan về Facade Pattern
- Các thành phần chính: Facade, Subsystems, Client
- Khi nào sử dụng

#### 4.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 3
- Bài toán cần:
  - Có hệ thống phức tạp với nhiều subsystems
  - Nêu rõ vấn đề: client phải biết quá nhiều chi tiết
  - Giải thích tại sao cần Facade pattern
  - Có tình huống cụ thể (giống như Home Theater trong lecture, nhưng là tình huống KHÁC)

#### 4.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Các subsystems với APIs phức tạp
- Client phải gọi nhiều methods theo đúng thứ tự
- Khó sử dụng và dễ sai

**Problem**: Vấn đề phức tạp cần giải quyết
- Client phải biết quá nhiều chi tiết implementation
- Client phải gọi nhiều methods theo workflow cụ thể
- Tight coupling giữa client và subsystems
- Khó maintain khi subsystems thay đổi

**Solution**: Cách Facade pattern giải quyết
- Tạo Facade class
- Facade cung cấp high-level methods đơn giản
- Facade orchestrate các subsystems
- Client chỉ tương tác với Facade

**Expected Output**: Kết quả mong đợi
- Client code đơn giản, dễ hiểu
- Giảm coupling giữa client và subsystems
- Dễ dàng bảo trì và mở rộng
- Client có thể bypass Facade nếu cần (optional)

#### 4.4. Hiệu quả của việc sử dụng Facade Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh trước và sau khi dùng pattern
- Khả năng mở rộng và bảo trì
- Trade-offs (nếu có)

#### 4.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Mỗi class có:
  - Comment đầy đủ (nếu cần)
  - Tên biến, method rõ ràng
  - Logic đúng với Facade pattern
  - Coding style học từ code sample
- Code phải hoàn chỉnh và có thể compile

#### 4.6. Kết quả chạy chương trình
- Output khi chạy main class
- Giải thích cách pattern hoạt động qua output
- So sánh code có Facade vs không có Facade

#### 4.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Liệt kê các class và mối quan hệ
- Nêu rõ các phương thức quan trọng

#### 4.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này

### Bước 5: Viết code Java cho bài toán mới trong 2-Facade-DP/

#### 5.1. Xóa code cũ
- Xóa tất cả file Java cũ không còn phù hợp với bài toán mới

#### 5.2. Tạo code mới
Dựa trên bài toán đã thiết kế, tạo các file:
- **Subsystem classes** (3-6 classes) - Các class phức tạp
- **Facade class** - Class chính cung cấp interface đơn giản
- **Client/Main class** - Demo pattern

**Ví dụ cấu trúc**:
```
2-Facade-DP/
├── SubsystemA.java
├── SubsystemB.java
├── SubsystemC.java
├── SubsystemD.java
├── SystemFacade.java    (Facade)
└── Main.java            (Client)
```

#### 5.3. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: chỉ khi cần
- Class đặt tên rõ ràng, theo Java convention
- Method naming: camelCase, descriptive
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu

#### 5.4. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Facade pattern
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Solution/Facade.md

### Bước 6: Tạo file package.bluej với UML diagram

Tạo file `2-Facade-DP/package.bluej` với:

#### 6.1. Yêu cầu UML diagram
- Hiển thị tất cả các class của bài toán MỚI
- Hiển thị đầy đủ methods cho mỗi class (BlueJ sẽ tự động đọc từ source code)
- Hiển thị relationship đúng:
  - Facade → Subsystems (composition/association)
  - Main → Facade (dependency)
- Layout đẹp, dễ đọc giống như trong lecture PDF
- **Quan trọng**: `showInterface=true` cho TẤT CẢ targets

#### 6.2. Format BlueJ file
```
#BlueJ package file

# Dependencies (Facade uses Subsystems)
dependency1.from=SystemFacade
dependency1.to=SubsystemA
dependency1.type=UsesDependency

dependency2.from=SystemFacade
dependency2.to=SubsystemB
dependency2.type=UsesDependency

dependency3.from=Main
dependency3.to=SystemFacade
dependency3.type=UsesDependency

... (các dependencies khác)

# Targets với showInterface=true
target1.name=SubsystemA
target1.type=ClassTarget
target1.showInterface=true    # QUAN TRỌNG
target1.width=160
target1.height=100
target1.x=...
target1.y=...

... (các targets khác)
```

#### 6.3. Layout gợi ý
```
Sắp xếp theo mô hình Facade Pattern:
- Client/Main: góc trên bên trái
- Facade: trung tâm trái
- Subsystems: bên phải, xếp dọc hoặc grid
  - SubsystemA: trên cùng
  - SubsystemB: giữa trên
  - SubsystemC: giữa dưới
  - SubsystemD: dưới cùng
```

## Deliverables

### 1. File Solution/Facade.md
- Hoàn chỉnh theo cấu trúc ở Bước 4
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting

### 2. Folder 2-Facade-DP/
Chứa các file Java cho bài toán MỚI:
- 3-6 Subsystem classes
- 1 Facade class
- 1 Main class
- Code sạch, đúng chuẩn
- Compile và run được
- Không còn code cũ

### 3. File 2-Facade-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (showInterface=true)
- Layout đẹp giống lecture

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Hệ thống phức tạp**: Nhiều subsystems phải làm việc cùng nhau
✅ **Interface đơn giản**: Facade cung cấp high-level methods dễ dùng
✅ **Thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Thể hiện rõ "simplifying complex subsystems"
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Facade
✅ **Khác biệt**: Không trùng với lecture hay code sample
✅ **Đơn giản vừa đủ**: Đủ để minh họa pattern, không quá phức tạp

### Bài toán nên tránh:
❌ Copy y nguyên lecture (Home Theater System)
❌ Copy Computer Startup (ví dụ phổ biến)
❌ Quá đơn giản: chỉ có 1-2 subsystems
❌ Quá phức tạp: quá nhiều business logic
❌ Không rõ ràng: khó hiểu tại sao cần Facade
❌ Facade thêm functionality mới (sai pattern - Facade chỉ orchestrate, không thêm logic)

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ "simplifying complex interface"

### Về Facade Pattern
- Facade **KHÔNG** ẩn hoàn toàn subsystems
- Facade **KHÔNG** thêm functionality mới
- Facade **CHỈ** orchestrate (điều phối) subsystems
- Subsystems **KHÔNG** biết về Facade
- Client **CÓ THỂ** bypass Facade nếu cần advanced features

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Không cần phức tạp hóa
- Code phải chạy được và cho output có ý nghĩa
- Tên class/method phải phù hợp với bài toán mới
- Facade method names nên là high-level action verbs

### Về UML
- **Quan trọng**: `showInterface=true` cho tất cả targets
- Facade ở trung tâm, subsystems ở bên phải
- Dependencies từ Facade → Subsystems rõ ràng
- Format phải giống lecture
- Kích thước boxes đủ lớn để chứa methods

### Về documentation
- Solution/Facade.md là tài liệu chính
- Phải đầy đủ, rõ ràng, dễ hiểu
- Giải thích rõ ràng tại sao cần Facade trong bài toán này
- So sánh code có vs không có Facade

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Restaurant Order System
- **Subsystems**: Kitchen, Waiter, Cashier, Inventory
- **Facade**: RestaurantFacade
- **High-level method**: `placeOrder(customerName, items)`
- **Orchestration**: Check inventory → Send to kitchen → Notify waiter → Calculate bill

### Ví dụ 2: Hotel Booking System
- **Subsystems**: RoomService, Restaurant, Housekeeping, Billing
- **Facade**: HotelFacade
- **High-level method**: `checkIn(guest, roomType, days)`
- **Orchestration**: Reserve room → Prepare room → Setup billing → Welcome package

### Ví dụ 3: Smart Home System
- **Subsystems**: Lighting, Thermostat, SecuritySystem, MediaPlayer
- **Facade**: SmartHomeFacade
- **High-level method**: `activateNightMode()`
- **Orchestration**: Dim lights → Lower temp → Arm security → Stop media

### Ví dụ 4: Car Starting System
- **Subsystems**: Engine, FuelSystem, ElectricalSystem, Transmission
- **Facade**: CarFacade
- **High-level method**: `startCar()`
- **Orchestration**: Check fuel → Start electrical → Ignite engine → Engage transmission

### Ví dụ 5: E-commerce Order Processing
- **Subsystems**: Inventory, Payment, Shipping, Notification
- **Facade**: OrderFacade
- **High-level method**: `processOrder(order)`
- **Orchestration**: Check stock → Process payment → Arrange shipping → Send confirmation

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/interface khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng
- Chi tiết subsystems phù hợp

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Facade
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán có nhiều subsystems phức tạp
- [ ] Facade cung cấp interface đơn giản, high-level
- [ ] Solution/Facade.md có đầy đủ 8 sections
- [ ] Code trong 2-Facade-DP/ là code MỚI cho bài toán mới
- [ ] Có 3-6 subsystem classes + 1 Facade + 1 Main
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong Solution/Facade.md
- [ ] package.bluej có `showInterface=true` cho tất cả targets
- [ ] UML diagram format giống lecture
- [ ] Đã xóa hết code cũ không liên quan
- [ ] Tất cả files đã được format đẹp và dễ đọc
- [ ] Output demo rõ ràng sự khác biệt khi dùng Facade
