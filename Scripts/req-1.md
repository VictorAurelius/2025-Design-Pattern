# Plan Task: Tạo Bài Toán Mới Cho Adapter Design Pattern

## Mục tiêu
Tạo một bài toán MỚI, sáng tạo áp dụng Adapter Design Pattern, không copy bài toán từ lecture hay code sample. Bài toán cần có code và sơ đồ UML đầy đủ theo chuẩn.

## Yêu cầu đầu ra
Đối với Adapter Design Pattern cần có:
1. Bài toán MỚI, sáng tạo nhưng phù hợp với mẫu thiết kế
2. Yêu cầu bài toán cụ thể, rõ ràng
3. Phân tích hiệu quả khi sử dụng design pattern này
4. Code Java hoàn chỉnh theo chuẩn (học từ code sample)
5. Sơ đồ UML trong file package.bluej có đầy đủ phương thức giống như trong lecture

## Các bước thực hiện

### Bước 1: Phân tích code hiện tại (để hiểu vấn đề)
- Đọc tất cả file Java trong folder `1-Adapter-DP/`
- Đọc file `Solution/Adapter.md` để hiểu cấu trúc solution hiện tại
- Phân tích:
  - Hiểu bài toán hiện tại đang giải quyết vấn đề gì
  - Xác định điểm yếu của bài toán hiện tại (nếu có)

### Bước 2: Học cách implement Adapter Pattern từ tài liệu chuẩn
- Đọc file `Solution/Adapter-Lecture.pdf` để:
  - Hiểu BẢN CHẤT của Adapter Pattern (không phải học bài toán cụ thể)
  - Xem cách giảng viên trình bày: mô tả vấn đề → giải pháp → code
  - Xem cấu trúc UML diagram có đầy đủ phương thức
  - Hiểu mối quan hệ giữa Target, Adaptee, Adapter, Client

- Đọc code trong `Code-Sample/AdapterPattern-Project/` để:
  - Học cách đặt tên class, interface, method
  - Học coding convention và code style
  - Học cách triển khai Adapter pattern đúng chuẩn
  - Học cách viết main class để test
  - **KHÔNG COPY code, chỉ HỌC phong cách**

### Bước 3: Sáng tạo bài toán mới

#### 3.1. Brainstorm ý tưởng bài toán
Tìm một tình huống thực tế cần Adapter Pattern:
- **KHÔNG dùng**: Payment gateway (đã có trong lecture)
- **KHÔNG dùng**: Media player (đã có trong code cũ)

**Gợi ý các lĩnh vực có thể dùng**:
- Tích hợp API bên thứ ba (weather, maps, notification services)
- Chuyển đổi định dạng dữ liệu (JSON → XML, CSV → Database)
- Tích hợp thư viện cũ với code mới (legacy system)
- Adapter cho thiết bị (printer, scanner, sensor)
- Adapter cho database (MySQL → PostgreSQL, SQL → NoSQL)
- Adapter cho logging system (Log4j → SLF4J)
- Adapter cho messaging system (Email → SMS, Push notification)

**Chọn 1 lĩnh vực và tạo bài toán cụ thể**

#### 3.2. Yêu cầu bài toán phải có
1. **Ngữ cảnh rõ ràng**: Ai đang gặp vấn đề gì?
2. **Hai interface không tương thích**:
   - Interface cũ (Adaptee): Hệ thống đã tồn tại, không thể thay đổi
   - Interface mới (Target): Yêu cầu mới, cần phải sử dụng
3. **Lý do không thể thay đổi trực tiếp**:
   - Có nhiều lớp đang sử dụng interface cũ
   - Hoặc không có quyền sửa đổi API bên thứ ba
   - Hoặc rủi ro khi thay đổi lớn
4. **Giải pháp**: Dùng Adapter để kết nối 2 interface

#### 3.3. Thiết kế các thành phần
- **Target Interface**: Interface mới mà client muốn dùng
- **Adaptee Interface/Class**: Interface/Class cũ cần được adapt
- **Adapter Class**: Implements Target, wraps Adaptee
- **Concrete Adaptee**: Implementation của Adaptee (nếu cần)
- **Client/Main**: Class sử dụng để demo

**Lưu ý**:
- Phải có sự khác biệt rõ ràng giữa Target và Adaptee:
  - Tên method khác nhau
  - Số lượng parameters khác nhau
  - Hoặc kiểu dữ liệu khác nhau
  - Hoặc cách tổ chức data khác nhau

### Bước 4: Viết lại Solution/Adapter.md

Tạo lại file `Solution/Adapter.md` với cấu trúc:

#### 4.1. Mô tả mẫu Adapter
- Giữ nguyên phần mô tả tổng quan về Adapter Pattern
- Các thành phần chính: Target, Adaptee, Adapter, Client
- Khi nào sử dụng

#### 4.2. Mô tả bài toán MỚI
- Viết bài toán dựa trên ý tưởng đã brainstorm ở Bước 3
- Bài toán cần:
  - Có ngữ cảnh thực tế, dễ hiểu
  - Nêu rõ vấn đề không tương thích giữa các interface
  - Giải thích tại sao cần Adapter pattern
  - Có tình huống cụ thể (giống như Max và payment gateway trong lecture, nhưng là tình huống KHÁC)

#### 4.3. Yêu cầu bài toán
**Input**: Những gì hệ thống hiện có
- Interface cũ với các methods cụ thể
- Số lượng class đang sử dụng
- Lý do không thể thay đổi

**Problem**: Vấn đề không tương thích cần giải quyết
- Interface mới yêu cầu gì
- Tại sao không tương thích với interface cũ
- Constraints (không được thay đổi gì)

**Solution**: Cách Adapter pattern giải quyết
- Tạo Adapter class
- Adapter implements Target, wraps Adaptee
- Mapping giữa các methods

**Expected Output**: Kết quả mong đợi
- Hệ thống hoạt động với interface mới
- Không cần thay đổi code cũ
- Dễ dàng bảo trì và mở rộng

#### 4.4. Hiệu quả của việc sử dụng Adapter Pattern
- Lợi ích cụ thể trong bài toán này
- So sánh trước và sau khi dùng pattern
- Khả năng mở rộng và bảo trì
- Trade-offs (nếu có)

#### 4.5. Cài đặt
- Viết code Java cho bài toán MỚI
- Mỗi class có:
  - Comment đầy đủ (nếu cần)
  - Tên biến, method rõ ràng
  - Logic đúng với Adapter pattern
  - Coding style học từ code sample
- Code phải hoàn chỉnh và có thể compile

#### 4.6. Kết quả chạy chương trình
- Output khi chạy main class
- Giải thích cách pattern hoạt động qua output

#### 4.7. Sơ đồ UML
- Mô tả sơ đồ UML cho bài toán MỚI
- Liệt kê các class và mối quan hệ
- Nêu rõ các phương thức quan trọng

#### 4.8. Tổng kết
- Kết luận về bài toán và cách giải quyết
- Ứng dụng thực tế của pattern này

### Bước 5: Viết code Java cho bài toán mới trong 1-Adapter-DP/

#### 5.1. Xóa code cũ
- Xóa tất cả file Java cũ không còn phù hợp với bài toán mới

#### 5.2. Tạo code mới
Dựa trên bài toán đã thiết kế, tạo các file:
- **Target interface** - Interface mới mà client muốn dùng
- **Adaptee interface/class** - Interface/Class cũ cần được adapt
- **Concrete Adaptee** (nếu cần) - Implementation của Adaptee
- **Adapter class** - Implements Target, wraps Adaptee
- **Client/Main class** - Demo pattern

#### 5.3. Coding standard
- Package declaration: không cần (hoặc dùng nếu cần tổ chức)
- Import statements: chỉ khi cần
- Class/Interface đặt tên rõ ràng, theo Java convention
- Method naming: camelCase
- Code formatting đúng chuẩn Java
- Logic rõ ràng, dễ hiểu

#### 5.4. Đảm bảo
- Code compile được không lỗi
- Logic đúng với Adapter pattern
- Dễ hiểu, dễ bảo trì
- Phù hợp với bài toán trong Solution/Adapter.md

### Bước 6: Tạo file package.bluej với UML diagram

Tạo file `1-Adapter-DP/package.bluej` với:

#### 6.1. Yêu cầu UML diagram
- Hiển thị tất cả các class/interface của bài toán MỚI
- Hiển thị đầy đủ methods cho mỗi class (BlueJ sẽ tự động đọc từ source code)
- Hiển thị relationship đúng:
  - Interface implementation (mũi tên nét đứt rỗng)
  - Class composition/association (mũi tên nét liền)
  - Dependency (mũi tên nét đứt)
- Layout đẹp, dễ đọc giống như trong lecture PDF

#### 6.2. Format BlueJ file
```
#BlueJ package file
dependency1.from=<Adapter>
dependency1.to=<Target>
dependency1.type=UsesDependency

dependency2.from=<Adapter>
dependency2.to=<Adaptee>
dependency2.type=UsesDependency

dependency3.from=<ConcreteAdaptee>
dependency3.to=<Adaptee>
dependency3.type=UsesDependency

dependency4.from=<Main>
dependency4.to=<Target>
dependency4.type=UsesDependency

... (các dependencies khác)

target1.name=<ClassName>
target1.type=ClassTarget hoặc InterfaceTarget
target1.x=<tọa độ x>
target1.y=<tọa độ y>
target1.width=<chiều rộng>
target1.height=<chiều cao>

... (các targets khác)
```

#### 6.3. Layout gợi ý
```
Sắp xếp theo mô hình Adapter Pattern:
- Client/Main: góc trên bên trái
- Target Interface: trung tâm trên
- Adapter Class: trung tâm giữa
- Adaptee Interface: dưới Adapter hoặc bên phải
- Concrete Classes: dưới các Interface tương ứng
```

## Deliverables

### 1. File Solution/Adapter.md
- Hoàn chỉnh theo cấu trúc ở Bước 4
- Bài toán MỚI, sáng tạo
- Format markdown đúng chuẩn
- Code blocks có syntax highlighting

### 2. Folder 1-Adapter-DP/
Chứa các file Java cho bài toán MỚI:
- Tất cả source code cần thiết
- Code sạch, đúng chuẩn
- Compile và run được
- Không còn code cũ

### 3. File 1-Adapter-DP/package.bluej
- UML diagram đầy đủ cho bài toán MỚI
- Hiển thị methods (BlueJ tự động)
- Layout đẹp giống lecture

## Tiêu chí đánh giá bài toán mới

### Bài toán tốt cần có:
✅ **Tính thực tế**: Vấn đề có thể gặp trong thực tế
✅ **Rõ ràng**: Dễ hiểu, dễ hình dung
✅ **Đúng pattern**: Thể hiện rõ vấn đề "incompatible interfaces"
✅ **Có ý nghĩa**: Thấy được lợi ích khi dùng Adapter
✅ **Khác biệt**: Không trùng với lecture hay code sample
✅ **Đơn giản vừa đủ**: Đủ để minh họa pattern, không quá phức tạp

### Bài toán nên tránh:
❌ Copy y nguyên lecture (Xpay → PayD)
❌ Copy y nguyên code sample hoặc ví dụ phổ biến
❌ Quá đơn giản: chỉ đổi tên method
❌ Quá phức tạp: nhiều business logic không cần thiết
❌ Không rõ ràng: khó hiểu tại sao cần Adapter

## Lưu ý quan trọng

### Về bài toán mới
- **PHẢI sáng tạo**: Không copy từ lecture, code sample, hay internet
- **Học cách implement**: Từ lecture và code sample
- **Tạo ví dụ mới**: Với ngữ cảnh và tên gọi khác
- Bài toán phải thực tế và dễ hiểu
- Phải thể hiện rõ vấn đề "interface không tương thích"

### Về code
- Học coding style từ Code-Sample nhưng viết code mới
- Không cần phức tạp hóa
- Code phải chạy được và cho output có ý nghĩa
- Tên class/method phải phù hợp với bài toán mới

### Về UML
- Đây là phần quan trọng nhất cần cải thiện
- PHẢI có đầy đủ methods trong diagram (BlueJ tự động hiển thị)
- Format phải giống lecture
- Relationships phải đúng

### Về documentation
- Solution/Adapter.md là tài liệu chính
- Phải đầy đủ, rõ ràng, dễ hiểu
- Giải thích rõ ràng tại sao cần Adapter trong bài toán này

## Ví dụ hướng dẫn (chỉ để tham khảo cấu trúc, KHÔNG copy)

**Ví dụ về tình huống có thể dùng**:

### Ví dụ 1: Temperature Sensor System
- **Adaptee**: Hệ thống sensor cũ trả về Fahrenheit
- **Target**: Hệ thống hiển thị mới cần Celsius
- **Adapter**: Chuyển đổi giữa hai đơn vị

### Ví dụ 2: Logging System Migration
- **Adaptee**: Legacy logger với method `writeLog(String)`
- **Target**: Hệ thống mới cần `log(LogLevel, String, Timestamp)`
- **Adapter**: Bổ sung thêm thông tin và chuyển đổi format

### Ví dụ 3: Database Connection
- **Adaptee**: MySQL driver với connection cũ
- **Target**: Application cần generic DatabaseConnection interface
- **Adapter**: Wrap MySQL-specific methods vào interface chung

### Ví dụ 4: Notification System
- **Adaptee**: Old email service với `sendEmail(to, subject, body)`
- **Target**: New notification system cần `notify(NotificationRequest)`
- **Adapter**: Convert NotificationRequest to email parameters

**Lưu ý**: Đây chỉ là ví dụ về CẤU TRÚC, bạn cần tạo bài toán MỚI với:
- Tên class/interface khác
- Logic business cụ thể
- Ngữ cảnh và câu chuyện riêng

## Checklist cuối cùng

Trước khi hoàn thành, kiểm tra:
- [ ] Đã đọc và hiểu lecture để học CÁCH implement Adapter
- [ ] Đã đọc code sample để học coding style
- [ ] Đã sáng tạo bài toán MỚI (không copy)
- [ ] Bài toán rõ ràng, thực tế, phù hợp với Adapter Pattern
- [ ] Solution/Adapter.md có đầy đủ 8 sections
- [ ] Code trong 1-Adapter-DP/ là code MỚI cho bài toán mới
- [ ] Code compile và chạy được
- [ ] Code phù hợp với bài toán trong Solution/Adapter.md
- [ ] package.bluej hiển thị UML với đầy đủ classes/interfaces
- [ ] UML diagram format giống lecture
- [ ] Đã xóa hết code cũ không liên quan
- [ ] Tất cả files đã được format đẹp và dễ đọc
