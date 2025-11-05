public class Level2Support extends SupportHandler {

	public Level2Support(String handlerName) {
		super(handlerName);
	}

	@Override
	public void handleTicket(SupportTicket ticket) {

		if (ticket.getPriority().equals("technical")) {
			// L2 can handle technical issues
			System.out.println("\n┌─────────────────────────────────────────────────────┐");
			System.out.println("│ ✓ HANDLED BY: " + handlerName);
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Ticket ID: " + ticket.getTicketId());
			System.out.println("│ Customer: " + ticket.getCustomerName());
			System.out.println("│ Priority: " + ticket.getPriority().toUpperCase());
			System.out.println("│ Issue: " + ticket.getDescription());
			System.out.println("├─────────────────────────────────────────────────────┤");
			System.out.println("│ Resolution:");
			System.out.println("│ → Analyzed error logs and stack traces");
			System.out.println("│ → Identified API configuration issue");
			System.out.println("│ → Applied hotfix and tested integration");
			System.out.println("│ → Technical issue resolved within 2 hours");
			System.out.println("│ Status: RESOLVED ✓");
			System.out.println("└─────────────────────────────────────────────────────┘");

		} else if (successor != null) {
			// Cannot handle - escalate to manager
			System.out.println("\n[" + handlerName + "] Cannot handle '" + ticket.getPriority() + "' priority ticket");
			System.out.println("[" + handlerName + "] Escalating to: " + successor.getHandlerName());
			successor.handleTicket(ticket);

		} else {
			System.out.println("\n[ERROR] No handler available for ticket: " + ticket.getTicketId());
		}
	}
}
