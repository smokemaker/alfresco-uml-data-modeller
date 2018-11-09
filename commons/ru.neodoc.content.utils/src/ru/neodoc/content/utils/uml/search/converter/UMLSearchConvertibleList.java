package ru.neodoc.content.utils.uml.search.converter;

import java.util.List;

public interface UMLSearchConvertibleList<T> extends List<T> {
	public <S> UMLSearchConvertibleList<S> convert(UMLSearchConverter<? super T, S> converter);
	public T first();
}
