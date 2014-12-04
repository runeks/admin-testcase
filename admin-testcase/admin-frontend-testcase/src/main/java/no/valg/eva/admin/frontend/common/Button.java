package no.valg.eva.admin.frontend.common;

public class Button {
	private final ButtonState state;

	public Button(ButtonState state) {
		this.state = state;
	}

	public static Button rendered(boolean enabled) {
		if (enabled) {
            return new Button(ButtonState.ENABLED);
        }
		return new Button(ButtonState.DISABLED);
	}

	public static Button renderedAndEnabled() {
		return rendered(true);
	}

	public static Button notRendered() {
		return new Button(ButtonState.NOT_RENDERED);
	}

	public boolean isDisabled() {
		return state.isDisabled();
	}

	public boolean isRendered() {
		return state.isRendered();
	}
}
