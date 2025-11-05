public class DarkThemeFactory implements ThemeFactory {

	@Override
	public Button createButton() {
		System.out.println("[DarkThemeFactory] Creating Dark Button...");
		return new DarkButton();
	}

	@Override
	public Icon createIcon() {
		System.out.println("[DarkThemeFactory] Creating Dark Icon...");
		return new DarkIcon();
	}

	@Override
	public Panel createPanel() {
		System.out.println("[DarkThemeFactory] Creating Dark Panel...");
		return new DarkPanel();
	}
}
