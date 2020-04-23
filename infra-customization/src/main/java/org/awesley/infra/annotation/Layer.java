package org.awesley.infra.annotation;

import java.util.HashMap;
import java.util.Map;

public enum Layer {
	UNDEFINED("Undefined"),
	CORE("Core"),
	CUSTOMIZATION("Customization");
	
	private final String label;
	
	private Layer(String label) { this.label = label; }
	
	private static Map<String, Layer> BY_LABEL = new HashMap<String, Layer>();
	
	static {
		for (Layer l : values()) {
			BY_LABEL.put(l.label, l);
		}
	}
	
	public static Layer valueOfLabel(String label) {
		if (BY_LABEL.containsKey(label)) {
			return BY_LABEL.get(label);
		}
		return UNDEFINED;
	}
}
