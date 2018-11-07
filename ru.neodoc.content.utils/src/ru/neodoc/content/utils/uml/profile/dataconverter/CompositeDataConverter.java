package ru.neodoc.content.utils.uml.profile.dataconverter;

import java.util.ArrayList;
import java.util.List;

public class CompositeDataConverter<SourceType, TargetType> implements DataConverter<SourceType, TargetType> {

	protected Class<SourceType> sourceClass;
	protected Class<TargetType> targetClass;
	
	protected final List<DataConverter<?, ?>> chain = new ArrayList<>();
	
	public CompositeDataConverter(Class<SourceType> sourceClass, Class<TargetType> targetClass) {
		super();
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}
	
	public void add(DataConverter<?, ?> dataConverter) {
		this.chain.add(dataConverter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TargetType convert(SourceType source, Object...objects) {
		return convert(source, this.targetClass, objects);
	}

	@Override
	public TargetType convert(SourceType source, Class<? extends TargetType> exactTargetClass, Object...objects) {
		Object value = source;
		for (DataConverter<?, ?> converter: this.chain)
			value = ((DataConverter)converter).convert(value, exactTargetClass, objects);
		return exactTargetClass.cast((TargetType)value);
	}
	
	@Override
	public Class<SourceType> getSourceClass() {
		return sourceClass;
	}

	@Override
	public Class<TargetType> getTargetClass() {
		return targetClass;
	}

}
