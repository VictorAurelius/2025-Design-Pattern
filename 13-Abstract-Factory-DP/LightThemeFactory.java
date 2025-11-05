public class LightThemeFactory implements ThemeFactory {

	@Override
	public Button createButton() {
		System.out.println("[LightThemeFactory] Creating Light Button...");
		return new LightButton();
	}

	@Override
	public Icon createIcon() {
		System.out.println("[LightThemeFactory] Creating Light Icon...");
		return new LightIcon();
	}

	@Override
	public Panel createPanel() {
		System.out.println("[LightThemeFactory] Creating Light Panel...");
		return new LightPanel();
	}
}
