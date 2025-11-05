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
