public class User {

	private String name;
	private boolean hasSubscription;

	public User(String name, boolean hasSubscription) {
		this.name = name;
		this.hasSubscription = hasSubscription;
	}

	public String getName() {
		return name;
	}

	public boolean hasSubscription() {
		return hasSubscription;
	}

	public void subscribe() {
		this.hasSubscription = true;
		System.out.println(name + " subscribed to Premium!");
	}
}
