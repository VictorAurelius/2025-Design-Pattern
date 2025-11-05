public class SupportManager extends SupportHandler {

	public SupportManager(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("escalated")) {
			// Manager can handle escalated issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName);
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Coordinated with engineering team");
			System.out.println("│ → Deployed database rollback script");
			System.out.println("│ → Restored access for 50 affected users");
			System.out.println("│ → Escalated issue resolved within 4 hours");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else if (successor != null) {
			// Cannot handle - escalate to director
			System.out.println("\n[" + handlerName + "] Cannot handle '" + ticket.getPriority() + "' priority ticket");
			System.out.println("[" + handlerName + "] Escalating to: " + successor.getHandlerName());
			successor.handleTicket(ticket);

		} else {
			System.out.println("\n[ERROR] No handler available for ticket: " + ticket.getTicketId());
		}
	}
}
