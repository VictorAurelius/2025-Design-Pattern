public class EngineeringDirector extends SupportHandler {

	public EngineeringDirector(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("critical")) {
			// Director handles critical issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName + " (HIGHEST LEVEL)");
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase() + " 🚨");
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Initiated emergency response protocol");
			System.out.println("│ → Assembled crisis management team");
			System.out.println("│ → Activated backup systems");
			System.out.println("│ → Restored full service within 1 hour");
			System.out.println("│ → Post-mortem analysis scheduled");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else {
			// Director is highest level - handles everything if reached
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ⚠ HANDLED BY: " + handlerName + " (FALLBACK)");
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Priority: " + ticket.getPriority());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Note: Ticket reached highest level (fallback handler)");
			System.out.println("│ Director will personally review and assign");
			System.out.println("│ Status: UNDER REVIEW");
			System.out.println("└─────────────────────────────────────────────────────┘");
		}
	}
}
