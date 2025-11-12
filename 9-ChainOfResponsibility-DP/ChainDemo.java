public class ChainDemo {

	public static void main(String[] args) {

		System.out.println("=== Chain of Responsibility Demo ===\n");

		// Setup support chain
		SupportHandler level1 = new Level1Support("Level 1 Support");
		SupportHandler level2 = new Level2Support("Level 2 Support");
		SupportHandler manager = new SupportManager("Support Manager");
		SupportHandler director = new EngineeringDirector("Engineering Director");

		level1.setSuccessor(level2);
		level2.setSuccessor(manager);
		manager.setSuccessor(director);

		System.out.println("Chain: Level 1 -> Level 2 -> Manager -> Director\n");

		// Test 1: Basic ticket
		System.out.println("--- Test 1: Basic Priority Ticket ---");
		SupportTicket ticket1 = new SupportTicket("#1234", "basic", "Password reset", "Acme Corp");
		level1.handleTicket(ticket1);

		// Test 2: Technical ticket
		System.out.println("\n--- Test 2: Technical Priority Ticket ---");
		SupportTicket ticket2 = new SupportTicket("#5678", "technical", "API integration error", "TechStart Inc");
		level1.handleTicket(ticket2);

		// Test 3: Escalated ticket
		System.out.println("\n--- Test 3: Escalated Priority Ticket ---");
		SupportTicket ticket3 = new SupportTicket("#9012", "escalated", "Database access issue", "MegaCorp Ltd");
		level1.handleTicket(ticket3);

		// Test 4: Critical ticket
		System.out.println("\n--- Test 4: Critical Priority Ticket ---");
		SupportTicket ticket4 = new SupportTicket("#3456", "critical", "System outage", "Enterprise Global");
		level1.handleTicket(ticket4);

		// Test 5: Unknown priority (fallback)
		System.out.println("\n--- Test 5: Unknown Priority (Fallback) ---");
		SupportTicket ticket5 = new SupportTicket("#7890", "unknown", "Unusual issue", "Mystery Customer");
		level1.handleTicket(ticket5);
	}
}
