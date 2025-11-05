public class SupportTicket {

	private final String ticketId;
	private final String priority;
	private final String description;
	private final String customerName;

	public SupportTicket(String ticketId, String priority, String description, String customerName) {
		this.ticketId = ticketId;
		this.priority = priority;
		this.description = description;
		this.customerName = customerName;
	}

	public String getTicketId() {
		return ticketId;
	}

	public String getPriority() {
		return priority;
	}

	public String getDescription() {
		return description;
	}

	public String getCustomerName() {
		return customerName;
	}
}
