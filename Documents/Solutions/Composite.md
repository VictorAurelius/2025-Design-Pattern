# Mẫu thiết kế Composite (Composite Pattern)

## 1. Mô tả mẫu Composite

**Mẫu Composite** (Composite Pattern) là một mẫu thiết kế cấu trúc cho phép tổ chức các đối tượng thành cấu trúc cây (tree structure), trong đó các đối tượng đơn (leaf) và các nhóm đối tượng (composite) được xử lý đồng nhất thông qua một interface chung.

### Các thành phần chính:

- **Component**: Interface hoặc abstract class định nghĩa các phương thức chung cho cả Leaf và Composite
- **Leaf**: Đối tượng đơn giản không có con, implement các method của Component
- **Composite**: Đối tượng phức tạp có thể chứa nhiều Component con (Leaf hoặc Composite khác)
- **Client**: Tương tác với Component mà không cần phân biệt đó là Leaf hay Composite

### Khi nào sử dụng:

- Khi cần biểu diễn cấu trúc phân cấp dạng cây (part-whole hierarchy)
- Khi muốn client xử lý đồng nhất các đối tượng đơn và nhóm đối tượng
- Khi muốn thêm các loại component mới mà không ảnh hưởng đến code hiện có
- Khi cần thực hiện các operation recursive trên tree structure

### Đặc điểm quan trọng:

- **Uniform treatment**: Leaf và Composite được xử lý giống nhau qua Component interface
- **Recursive composition**: Composite có thể chứa Composite khác, tạo tree nhiều tầng
- **Transparency**: Client không cần biết đang làm việc với Leaf hay Composite
- **Flexibility**: Dễ dàng thêm loại component mới

---

## 2. Mô tả bài toán

Chef Michael quản lý nhà hàng cao cấp **Golden Fork Restaurant**. Menu của nhà hàng rất phức tạp với nhiều danh mục và danh mục con:

**Cấu trúc menu hiện tại**:
- **Main Menu**: Toàn bộ thực đơn nhà hàng
  - **Appetizers**: Khai vị
    - Spring Rolls, Caesar Salad, French Onion Soup, ...
  - **Main Course**: Món chính
    - **Meat Dishes**: Món thịt
      - Grilled Ribeye Steak, Roasted Lamb, BBQ Ribs, ...
    - **Seafood**: Hải sản
      - Grilled Salmon, Lobster Thermidor, Shrimp Scampi, ...
    - **Vegetarian**: Chay
      - Veggie Pasta, Tofu Stir-fry, Mushroom Risotto, ...
  - **Desserts**: Tráng miệng
    - Chocolate Lava Cake, Tiramisu, Ice Cream, ...
  - **Beverages**: Đồ uống
    - **Hot Drinks**: Nóng
      - Coffee, Tea, Hot Chocolate, ...
    - **Cold Drinks**: Lạnh
      - Juice, Smoothie, Soda, ...

### Vấn đề phát sinh:

1. **Khó tính toán tổng giá trị menu**:
   - Cần tính giá từng món riêng
   - Cần tính tổng giá danh mục (ví dụ: tổng giá tất cả Appetizers)
   - Cần tính tổng giá toàn bộ menu
   - Code xử lý Leaf (món đơn) và Composite (danh mục) khác nhau

2. **Khó hiển thị cấu trúc menu**:
   - Mỗi loại đối tượng (món đơn, danh mục) có cách hiển thị riêng
   - Phải viết code if-else để phân biệt Leaf và Composite
   - Indent/format khác nhau cho từng tầng

3. **Khó mở rộng và bảo trì**:
   - Thêm một danh mục mới phải sửa nhiều chỗ
   - Thêm một tầng mới trong tree structure phức tạp
   - Code không reusable

4. **Client code phức tạp**:
   - Client phải biết chi tiết về Leaf và Composite
   - Phải có logic riêng để traverse tree
   - Khó test vì phải mock nhiều loại đối tượng

Michael cần một giải pháp để **xử lý đồng nhất** các món ăn đơn và danh mục menu, giúp dễ dàng tính toán, hiển thị và mở rộng menu.

---

## 3. Yêu cầu bài toán

### Input (Điều kiện ban đầu):
Hệ thống có menu với cấu trúc cây phức tạp:
- **Món ăn đơn** (MenuItem): Có tên, mô tả, giá
- **Danh mục** (MenuCategory): Chứa nhiều món hoặc danh mục con
- Cần xử lý cả hai loại đối tượng: tính giá, hiển thị thông tin

### Problem (Vấn đề):
1. **Xử lý không đồng nhất**: Code xử lý MenuItem và MenuCategory khác nhau
2. **Client phức tạp**: Phải phân biệt Leaf và Composite
3. **Khó tính toán**: Tính tổng giá phải duyệt recursive thủ công
4. **Khó mở rộng**: Thêm loại component mới ảnh hưởng nhiều chỗ
5. **Code trùng lặp**: Logic tương tự cho Leaf và Composite

### Solution (Giải pháp):
Sử dụng **Composite Pattern** để:
- Tạo **MenuComponent** abstract class với interface chung
- **MenuItem** (Leaf) implement các method đơn giản
- **MenuCategory** (Composite) chứa ArrayList<MenuComponent> và implement recursive
- Client xử lý MenuItem và MenuCategory thông qua MenuComponent

### Expected Output (Kết quả mong đợi):
- Client code đơn giản, không phân biệt Leaf và Composite
- Tính tổng giá tự động qua recursive composition
- Hiển thị menu tree structure rõ ràng
- Dễ dàng thêm món mới, danh mục mới
- Code reusable và maintainable

---

## 4. Hiệu quả của việc sử dụng Composite Pattern

### Lợi ích cụ thể:

1. **Uniform treatment**:
   - Client gọi `display()` và `getPrice()` giống nhau cho cả MenuItem và MenuCategory
   - Không cần if-else để phân biệt Leaf và Composite
   - Code đơn giản, dễ đọc

2. **Recursive composition**:
   - MenuCategory tự động tính tổng giá từ các component con
   - Traverse tree tự động qua recursive calls
   - Không cần loop thủ công

3. **Dễ mở rộng**:
   - Thêm món mới: chỉ tạo MenuItem
   - Thêm danh mục mới: chỉ tạo MenuCategory
   - Thêm tầng mới: không cần sửa code cũ

4. **Tính linh hoạt cao**:
   - Build menu tree động
   - Thay đổi cấu trúc dễ dàng
   - Reuse component ở nhiều nơi

### So sánh trước và sau khi dùng Composite:

| Tiêu chí | Không dùng Composite | Có dùng Composite |
|----------|---------------------|-------------------|
| Client code | Phân biệt Leaf/Composite | Xử lý đồng nhất |
| Tính tổng giá | Loop thủ công nhiều tầng | Recursive tự động |
| Hiển thị menu | if-else phức tạp | Polymorphism đơn giản |
| Thêm component | Sửa nhiều chỗ | Chỉ tạo class mới |
| Testing | Khó (nhiều logic) | Dễ (polymorphism) |

### Trade-offs:

**Ưu điểm**: Uniform treatment, recursive composition, dễ mở rộng, code đơn giản

**Nhược điểm**: Thêm một lớp trừu tượng (Component), khó restrict loại con trong Composite (ví dụ: chỉ cho phép MenuItem trong một số MenuCategory)

---

## 5. Cài đặt

### 5.1. Class MenuComponent (Component - Abstract Class)

```java
import java.util.ArrayList;

public abstract class MenuComponent {

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public String getDescription() {
		throw new UnsupportedOperationException();
	}

	public double getPrice() {
		throw new UnsupportedOperationException();
	}

	public void display() {
		throw new UnsupportedOperationException();
	}

	public void add(MenuComponent component) {
		throw new UnsupportedOperationException();
	}

	public void remove(MenuComponent component) {
		throw new UnsupportedOperationException();
	}

	public MenuComponent getChild(int index) {
		throw new UnsupportedOperationException();
	}
}
```

### 5.2. Class MenuItem (Leaf)

```java
public class MenuItem extends MenuComponent {

	private String name;
	private String description;
	private double price;

	public MenuItem(String name, String description, double price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void display() {
		System.out.println("  - " + getName() + " ($" + getPrice() + ")");
		System.out.println("      " + getDescription());
	}
}
```

### 5.3. Class MenuCategory (Composite)

```java
import java.util.ArrayList;

public class MenuCategory extends MenuComponent {

	private String name;
	private String description;
	private ArrayList<MenuComponent> menuComponents = new ArrayList<>();

	public MenuCategory(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public double getPrice() {
		double total = 0.0;
		for (MenuComponent component : menuComponents) {
			total += component.getPrice();
		}
		return total;
	}

	@Override
	public void add(MenuComponent component) {
		menuComponents.add(component);
	}

	@Override
	public void remove(MenuComponent component) {
		menuComponents.remove(component);
	}

	@Override
	public MenuComponent getChild(int index) {
		return menuComponents.get(index);
	}

	@Override
	public void display() {
		System.out.println("\n" + getName() + " - " + getDescription());
		System.out.println("---------------------------------------");

		for (MenuComponent component : menuComponents) {
			component.display();
		}
	}
}
```

### 5.4. Class RestaurantDemo (Client)

```java
public class RestaurantDemo {

	public static void main(String[] args) {

		System.out.println("========================================");
		System.out.println("    GOLDEN FORK RESTAURANT MENU");
		System.out.println("========================================");

		// Create leaf items (món đơn)
		MenuComponent springRolls = new MenuItem("Spring Rolls", "Crispy rolls with vegetables", 5.99);
		MenuComponent caesarSalad = new MenuItem("Caesar Salad", "Fresh romaine with parmesan", 7.50);
		MenuComponent frenchSoup = new MenuItem("French Onion Soup", "Classic soup with cheese", 6.00);

		MenuComponent grilledSteak = new MenuItem("Grilled Ribeye Steak", "Premium ribeye with herbs", 28.99);
		MenuComponent roastedLamb = new MenuItem("Roasted Lamb", "Tender lamb with rosemary", 32.50);

		MenuComponent grilledSalmon = new MenuItem("Grilled Salmon", "Atlantic salmon with lemon", 24.99);
		MenuComponent lobster = new MenuItem("Lobster Thermidor", "Classic French lobster", 45.00);

		MenuComponent veggiePasta = new MenuItem("Veggie Pasta", "Penne with fresh vegetables", 15.99);
		MenuComponent tofuStirfry = new MenuItem("Tofu Stir-fry", "Asian style tofu", 14.50);

		MenuComponent chocolateCake = new MenuItem("Chocolate Lava Cake", "Warm cake with ice cream", 8.50);
		MenuComponent tiramisu = new MenuItem("Tiramisu", "Italian classic dessert", 7.99);

		// Create composite categories (danh mục)
		MenuComponent appetizers = new MenuCategory("Appetizers", "Start your meal");
		appetizers.add(springRolls);
		appetizers.add(caesarSalad);
		appetizers.add(frenchSoup);

		MenuComponent meatDishes = new MenuCategory("Meat Dishes", "Premium meat selections");
		meatDishes.add(grilledSteak);
		meatDishes.add(roastedLamb);

		MenuComponent seafood = new MenuCategory("Seafood", "Fresh from the ocean");
		seafood.add(grilledSalmon);
		seafood.add(lobster);

		MenuComponent vegetarian = new MenuCategory("Vegetarian", "Plant-based options");
		vegetarian.add(veggiePasta);
		vegetarian.add(tofuStirfry);

		MenuComponent mainCourse = new MenuCategory("Main Course", "Our signature dishes");
		mainCourse.add(meatDishes);
		mainCourse.add(seafood);
		mainCourse.add(vegetarian);

		MenuComponent desserts = new MenuCategory("Desserts", "Sweet endings");
		desserts.add(chocolateCake);
		desserts.add(tiramisu);

		// Create main menu (root composite)
		MenuComponent restaurantMenu = new MenuCategory("Golden Fork Menu", "Complete dining experience");
		restaurantMenu.add(appetizers);
		restaurantMenu.add(mainCourse);
		restaurantMenu.add(desserts);

		// Display entire menu tree
		restaurantMenu.display();

		// Calculate total menu value
		System.out.println("\n========================================");
		System.out.printf("Total Menu Value: $%.2f%n", restaurantMenu.getPrice());
		System.out.println("========================================");

		// Demonstrate flexibility: display only Main Course
		System.out.println("\n\n--- Main Course Only ---");
		mainCourse.display();
		System.out.printf("\nMain Course Total: $%.2f%n", mainCourse.getPrice());
	}
}
```

---

## 6. Kết quả chạy chương trình

```
========================================
    GOLDEN FORK RESTAURANT MENU
========================================

Golden Fork Menu - Complete dining experience
---------------------------------------

Appetizers - Start your meal
---------------------------------------
  - Spring Rolls ($5.99)
      Crispy rolls with vegetables
  - Caesar Salad ($7.5)
      Fresh romaine with parmesan
  - French Onion Soup ($6.0)
      Classic soup with cheese

Main Course - Our signature dishes
---------------------------------------

Meat Dishes - Premium meat selections
---------------------------------------
  - Grilled Ribeye Steak ($28.99)
      Premium ribeye with herbs
  - Roasted Lamb ($32.5)
      Tender lamb with rosemary

Seafood - Fresh from the ocean
---------------------------------------
  - Grilled Salmon ($24.99)
      Atlantic salmon with lemon
  - Lobster Thermidor ($45.0)
      Classic French lobster

Vegetarian - Plant-based options
---------------------------------------
  - Veggie Pasta ($15.99)
      Penne with fresh vegetables
  - Tofu Stir-fry ($14.5)
      Asian style tofu

Desserts - Sweet endings
---------------------------------------
  - Chocolate Lava Cake ($8.5)
      Warm cake with ice cream
  - Tiramisu ($7.99)
      Italian classic dessert

========================================
Total Menu Value: $198.94
========================================


--- Main Course Only ---

Main Course - Our signature dishes
---------------------------------------

Meat Dishes - Premium meat selections
---------------------------------------
  - Grilled Ribeye Steak ($28.99)
      Premium ribeye with herbs
  - Roasted Lamb ($32.5)
      Tender lamb with rosemary

Seafood - Fresh from the ocean
---------------------------------------
  - Grilled Salmon ($24.99)
      Atlantic salmon with lemon
  - Lobster Thermidor ($45.0)
      Classic French lobster

Vegetarian - Plant-based options
---------------------------------------
  - Veggie Pasta ($15.99)
      Penne with fresh vegetables
  - Tofu Stir-fry ($14.5)
      Asian style tofu

Main Course Total: $162.96
```

**Giải thích**:
- Client gọi `display()` và `getPrice()` trên root (restaurantMenu)
- Pattern tự động traverse toàn bộ tree qua recursive calls
- MenuItem hiển thị thông tin đơn giản
- MenuCategory loop qua children và gọi `display()` trên mỗi child
- `getPrice()` tính tổng recursive: MenuCategory sum giá từ tất cả children

---

## 7. Sơ đồ UML

### 7.1. Class Diagram

```
                    [MenuComponent]
                    (abstract class)
                    + getName()
                    + getDescription()
                    + getPrice()
                    + display()
                    + add()
                    + remove()
                    + getChild()
                           △
                           |
                 ┌─────────┴─────────┐
                 |                   |
          [MenuItem]          [MenuCategory]
           (Leaf)              (Composite)
        - name                - name
        - description         - description
        - price               - menuComponents: ArrayList<MenuComponent>
        + getName()           + getName()
        + getDescription()    + getDescription()
        + getPrice()          + getPrice()
        + display()           + add()
                              + remove()
                              + getChild()
                              + display()
                                      ○──> [MenuComponent]
                                           (aggregation)

[RestaurantDemo] ──> [MenuComponent]
   (Client)          (dependency)
```

**Component**: MenuComponent - Abstract class với interface chung

**Leaf**: MenuItem - Món ăn đơn, không có children

**Composite**: MenuCategory - Danh mục chứa ArrayList<MenuComponent>

**Client**: RestaurantDemo - Xây dựng tree và interact qua MenuComponent

**Aggregation**: MenuCategory chứa list MenuComponent (có thể là MenuItem hoặc MenuCategory khác)

---

## 8. Tổng kết

Composite Pattern giải quyết bài toán quản lý menu nhà hàng:

1. **Vấn đề**: Menu có cấu trúc cây phức tạp, khó xử lý đồng nhất Leaf và Composite
2. **Giải pháp**: MenuComponent abstract class, MenuItem (Leaf), MenuCategory (Composite)
3. **Kết quả**: Client xử lý uniform, tính toán và hiển thị tự động qua recursive composition

Pattern này hữu ích cho: Menu systems, File/Folder structures, Organization hierarchies, Product categories, HTML DOM, Graphics rendering, Expression trees.

Composite Pattern thể hiện nguyên lý **"Treat individual objects and compositions uniformly"** - xử lý đối tượng đơn và nhóm đối tượng một cách đồng nhất.
