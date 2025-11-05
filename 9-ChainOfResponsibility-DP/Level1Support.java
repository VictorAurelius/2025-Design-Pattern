public class Level1Support extends SupportHandler {

	public Level1Support(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("basic")) {
			// L1 can handle basic issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName);
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Provided step-by-step guide");
			System.out.println("│ → Password reset link sent to email");
			System.out.println("│ → Basic issue resolved within 5 minutes");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else if (successor != null) {
			// Cannot handle - escalate to next level
			System.out.println("\n[" + handlerName + "] Cannot handle '" + ticket.getPriority() + "' priority ticket");
			System.out.println("[" + handlerName + "] Escalating to: " + successor.getHandlerName());
			successor.handleTicket(ticket);

		} else {
			System.out.println("\n[ERROR] No handler available for ticket: " + ticket.getTicketId());
		}
	}
}
