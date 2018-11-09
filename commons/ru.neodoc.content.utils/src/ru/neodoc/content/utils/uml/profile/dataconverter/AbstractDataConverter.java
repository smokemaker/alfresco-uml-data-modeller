package ru.neodoc.content.utils.uml.profile.dataconverter;

public abstract class AbstractDataConverter<SourceClass, TargetClass> implements DataConverter<SourceClass, TargetClass> {

	protected Class<? extends SourceClass> sourceClass;
	protected Class<TargetClass> targetClass;
	
	public AbstractDataConverter(Class<? extends SourceClass> sourceClass, Class<TargetClass> targetClass) {
		super();
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final TargetClass convert(SourceClass source, Object...objects) {
		return convert(source, this.targetClass, objects);
	}
	
	@Override
	public TargetClass convert(SourceClass source, Class<? extends TargetClass> exactTargetClass, Object...objects) {
		if (source==null)
			return getNullValue();
		
		if (sourceClass.equals(targetClass))
			return (TargetClass)source;
		
		if (exactTargetClass.isAssignableFrom(sourceClass))
			return (TargetClass)source;
		
		return exactTargetClass.cast(doConvert(source, exactTargetClass, objects));
	}
	
	protected TargetClass getNullValue() {
		return (TargetClass)null;
	}
	
	protected abstract TargetClass doConvert(SourceClass source, Class<? extends TargetClass> exactTargetClass, Object...objects);
	
	@Override
	public final Class<? extends SourceClass> getSourceClass() {
		return sourceClass;
	}

	@Override
	public final Class<TargetClass> getTargetClass() {
		return targetClass;
	}

}
