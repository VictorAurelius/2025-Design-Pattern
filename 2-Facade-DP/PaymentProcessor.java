public class PaymentProcessor {

	public boolean validatePaymentInfo(String cardNumber, String cvv, double amount) {
		System.out.println("  [Payment] Validating payment info for amount: $" + amount);
		return cardNumber != null && !cardNumber.isEmpty();
	}

	public String chargePayment(String cardNumber, double amount) {
		System.out.println("  [Payment] Charging $" + amount + " to card: " + cardNumber);
		return "TXN" + System.currentTimeMillis();
	}

	public boolean refundPayment(String transactionId) {
		System.out.println("  [Payment] Refunding transaction: " + transactionId);
		return true;
	}
}
