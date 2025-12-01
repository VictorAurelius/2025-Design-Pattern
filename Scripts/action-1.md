tôi cần tạo:
đối với 1 design pattern:
+ nêu bài toán
+ nêu yêu cầu bài toán
+ nêu được sử dụng design pattern này sẽ có hiệu quả như thế nào
+ sơ đồ UML mô tả lại code, sao cho giống lecture

tôi đã thử làm với 3 mẫu, trong các folder 1-Adapter-DP, 2-Faceda-DP, 3-Composite-DP
nhưng còn tồn tại các khuyết điểm:
1. code không phù hợp như code-sample
2. sơ đồ UML của bulej chưa có phương thức
3. bài toán có thể chưa phù hợp

hãy giúp tôi tạo Scripts/req-1.md là plan task cho claude để:
1. đọc code hiện tại trong 1-Adapter-DP và bài toán tương ứng Solution/Adapter.md để hiểu code hiện tại
2. đọc Solution/Adapter-Lecture.pdf để hiểu bài toán Sample
3. đọc code trong Code-Sample/AdapterPattern-Project để hiểu mẫu code chuẩn sẽ cần như thế nào
4. tạo lại solution cho mẫu apdater theo yêu cầu
5. tạo lại code theo mẫu
6. tạo lại file package.bulej của mẫu apdater sao cho giống Solution/Adapter-Lecture.pdf

lưu ý: đây chỉ là plan task cho claude, chưa thực hiện ngay

hãy sửa lại req-1, tôi không cần bạn copy lại Code-Sample và bài toán sample, tôi cần 1 bài toán mới, áp dụng được mẫu Adapter

tạo req-2 cho mẫu Facade
tôi cũng đang có input Facade-Lecture.pdf

tạo req-3 cho mẫu Composite
tôi cũng đang có input Composite-Lecture.pdf

tạo req-4 cho mẫu Bridge
input lecture nằm trong Documents/Lectures, tôi chưa có code hay solution mẫu
solution giờ nằm trong Documents/Solutions

tạo req-5 cho mẫu Singletion

tạo req-6 cho mẫu Observer

tạo req-7 cho mẫu Mediator

tạo req-8 cho mẫu Proxy, tôi có yêu cầu thêm cho các req tiếp theo là hãy chọn các field và context cho bài toán giống nhau 1 xíu để tôi có thể dễ nhớ hơn, vì nếu mỗi bài toán của design pattern có 1 context khác nhau, tôi sẽ khá khó nhớ (vì có 24 mẫu design pattern phải nhớ). Nhưng bài toán vẫn phải ưu tiên phù hợp để thể hiện design pattern.

tạo req-9 cho mẫu Chain of Responsibility

tạo req-10 cho mẫu Flighweight

tạo req-11 cho mẫu Builder

tạo req-12 cho mẫu Factory Method

tạo req-13 cho mẫu Abstract Factory

tạo req-14 cho mẫu Prototype

tạo req-15 cho mẫu Memento

tạo req-16 cho mẫu Template

tạo req-17 cho mẫu State

tạo req-18 cho mẫu Strategy

tạo req-19 cho mẫu Command

tạo req-20 cho mẫu Interpreter

hãy quay lại với Apdater, tôi cần vẽ UML ở cả StarUML nữa, nên tôi đã tự vẽ và được file 1-Apdater-DP/UML.mdj

file này tôi đã cấu hình format và điều chỉnh sau cho giống lecture (thêm đường nối với client)

bây giờ bạn hãy:
1. sửa req-1 để có plan tạo được file như vậy (nhưng sẽ không thực hiện vì đã có file rồi)
2. sửa req-2 để tạo được file tương ứng để vẽ được UML cho mẫu Facede (sao cho giống Lecture và hợp lý)

do step 7 of req-2

tôi không hài lòng với starUMl của bạn tạo lắm, nên hãy bỏ qua nó
hãy tạo req-21 cho mẫu Decorator

hãy tạo req-22 cho mẫu Iterator

hãy tạo req-23 cho mẫu Visitor

tốt, đã xong tất cả các design pattern cần thiết, hãy tạo file readme cho toàn bộ project này
đặc biệt tôi muốn mỗi DP sẽ có mô tả cơ bản bài toán và mapping context giữa các bài toán

hãy đọc req-9 đến req-23 để hiểu context
sau đó hãy viết req-24 để thực hiện task:
1. hàm main (demo) của các mẫu 9 đến 23 đang quá dài và dườm rà, cần phải rút gọn, hiển thị vừa đủ như mẫu 1 đến mẫu 8
2. các lệnh in ra cũng vậy, cần ngắn gọn, dễ đọc, dễ hiểu, không có icon
3. các solution.md của các mẫu cần mô tả rõ ý nghĩa, mục đích, cách triển khai của các testcase trong hàm main (demo)

session cũ đang do req-24 đến mẫu 16 thì dừng, hãy kiểm tra mẫu 16 đúng chưa và do tiếp req-24

hãy đọc 99-B-Learing/DTPM_B-Learning.pdf và thực hiện đánh giá chi tiết về thiết kế database này 

hãy viết lại báo cáo thành file md trong 99-B-Learing/documents

bây giờ hãy tạo 99-B-Learing/req-1.md là plan task cho claude để tạo lại thiết kế db B-Learning này:
1. tôi sẽ vẽ lại sơ đồ BFD và ERD trên starUML (giống các design pattern: Scripts/req-1.md) hãy gen code hoặc mã thuận lợi reserver tốt trên starUML
2. hệ thống này không yêu cầu forum và review hoặc discussion nữa
3. có cấp chứng chỉ
4. thiết kế lại giống 1 hệ thống B-learing thông dụng (theo dạng assignment) thay vì submission như hiện tại
5. mô tả PK, FK, thuộc tính rõ ràng
6. mô tả lại chức năng mới cập nhật rõ ràng

Mục tiêu: đưa ra 1 đặc tả hệ thống B-Learing (BFD, ERD, đặc tả) hoàn thiện.

Tất cả các báo cáo ở dạng md
do req-1

hãy đọc 99-B-Learing/req-1.md để hiểu context

Hiện tại db này đang bị vấn đề là quá nhiều bảng mà liên kết quá chặt chẽ, khó nắm bắt cho người tìm hiểu. Tôi muốn tinh giảm nó:
1. bỏ các chức năng không phải core
2. giản lược bảng cho 1 số chức năng

hãy tạo 1 báo cáo md đề xuất

tôi đồng ý với phương án chỉnh sửa này, hãy tạo req-2.md là plan task để chỉnh sửa lại db:
1. hãy tạo file trong thư mực 98-B-Learing-Core
2. tài liệu đầy đủ như 99 (BFD, ERD, SQL, ...)
3. tạo thêm file giải thích ý nghĩa bảng và thuộc tính dạng tiếng việt để hiểu rõ db hơn
4. không cần trigger, view, drop ... chỉ cần schema, index, constraints, seed-data

hãy đọc 98-B-Learing-Core/req-2.md để hiểu context
hãy tạo req-3 là plan task cho claude code để:

bây giờ tôi cần 2 giao diện web để thao tác chứng minh database
hãy dùng code python và next js để tạo 2 giao diện này:
1. 2 giao diện (2 page), 1 page thao tác với 1 bảng, 1 page thao tác với nhiều bảng, giao diện dễ sử dụng
2. theo chuẩn restful, query bằng lệnh sql
3. chọn bảng là đặc trưng của B-learning
4. có tài liệu giải thích code dễ dàng

hãy tạo docker compose để build web này, tự tạo image db bằng 98-B-learning-core

hãy đọc 98-B-Learing-Core/req-2.md để hiểu context
tôi có sửa lại các file sql của 98-B-Learing-Core , nhưng vẫn còn lỗi ở 4-seed-data.sql. File gốc của seed này nằm ở B-Learning-Demo/init/04-seed-data.sql, hãy giúp tôi sửa lại toàn bộ để tránh lỗi seed-data để Demo được thuận lợi

cần sửa tất cả các file B-Learning-Demo/init/ để build được docker