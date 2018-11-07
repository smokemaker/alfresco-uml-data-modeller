package ru.neodoc.content.codegen.sdoc2.wrap.convert;

public class NamespaceSdocNameConverter implements NameConverter {

	@Override
	public String convert(String source) {
		StringBuffer sb = new StringBuffer();
		sb.append(source.substring(0, 1).toUpperCase())
			.append(source.substring(1))
			.append("Model");
		return sb.toString();
	}

}
