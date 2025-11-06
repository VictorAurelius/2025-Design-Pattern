public class RestaurantDemo {

	public static void main(String[] args) {
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
		System.out.printf("Total Menu Value: $%.2f%n", restaurantMenu.getPrice());
	}
}
