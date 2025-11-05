public interface ThemeFactory {

	// Factory methods for creating product family
	// Each method returns an abstract product type

	Button createButton();
	Icon createIcon();
	Panel createPanel();
}
