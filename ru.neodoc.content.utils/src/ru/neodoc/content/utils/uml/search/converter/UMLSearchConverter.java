package ru.neodoc.content.utils.uml.search.converter;

public interface UMLSearchConverter<SourceClass, TargetClass> {
	
	public <SC extends SourceClass> boolean isConvertible(SC origin);
	public <TC extends TargetClass, SC extends SourceClass> TC convert(SC origin);
	
	public UMLSearchConverter<SourceClass, TargetClass>  addFilter(UMLSearchConverterFilter<? super TargetClass> filter);
	
}
