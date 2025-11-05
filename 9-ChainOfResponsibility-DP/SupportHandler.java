public abstract class SupportHandler {

	protected SupportHandler successor;
	protected String handlerName;

	public SupportHandler(String handlerName) {
		this.handlerName = handlerName;
	}

	public void setSuccessor(SupportHandler successor) {
		this.successor = successor;
	}

	public abstract void handleTicket(SupportTicket ticket);

	public String getHandlerName() {
		return handlerName;
	}
}
