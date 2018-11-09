package ru.neodoc.content.utils.uml.search.converter;

public interface UMLSearchConverterFilter<T> {

	public <S extends T> boolean matches(S element);
	
}
