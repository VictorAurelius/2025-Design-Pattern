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
		System.out.println(getName() + " - " + getDescription());

		for (MenuComponent component : menuComponents) {
			component.display();
		}
	}
}
