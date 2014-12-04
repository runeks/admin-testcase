package no.evote.presentation.util;

import java.util.ArrayList;
import java.util.List;

import no.evote.presentation.EnumUserMenuIcons;

public class Menu {
	private boolean header;
	private String text;
	private String url;
	private String icon;
	private boolean dialog;
	private List<Menu> children = new ArrayList<>();

	public Menu(String text) {
		this.text = text;
	}

	public Menu(String text, boolean header) {
		this.text = text;
		this.header = header;
	}

	public Menu(String text, String url) {
		this.text = text;
		this.url = url;
	}

	public Menu(String text, boolean header,  EnumUserMenuIcons icon) {
		this.text = text;
		this.header = header;
		this.icon = icon.getValue();
	}

	public void addChild(Menu item) {
		children.add(item);
	}

	public List<Menu> getChildren() {
		return children;
	}

	public boolean hasHeader() {
		return header;
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		return url;
	}

	public String getIcon() {
		return icon;
	}

	public boolean isDialog() {
		return dialog;
	}

	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}

	public boolean isCentralConfirmDialog() {
		return "centralConfirmDialog".equals(url);
	}

	public boolean isNoOperatorPartyWidget() {
		return "noOperatorPartyWidget".equals(url);
	}

	public boolean isReportLink() {
		return url.startsWith("reportLink:");
	}
	
	public String getReportLinkName() {
		return url.replace("reportLink:", "");
	}
	
    @Override
    public String toString() {
        return "Menu{"
                + "header=" + header
                + ", text='" + text + '\''
                + ", url='" + url + '\''
                + ", icon='" + icon + '\''
                + ", dialog=" + dialog
                + ", children=" + children
                + '}';
    }
}
