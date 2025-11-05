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
