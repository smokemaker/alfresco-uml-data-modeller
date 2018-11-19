package ru.neodoc.content.utils.uml.profile.dataconverter;

public interface DataConverter<SourceClass, TargetClass> {
	
	public DataConverter<Object, Object> DEFAULT_CONVERTER = new DataConverter<Object, Object>() {

		@Override
		public Object convert(Object source, Object...objects) {
			return source;
		}

		@Override
		public Class<Object> getSourceClass() {
			return Object.class;
		}

		@Override
		public Class<Object> getTargetClass() {
			return Object.class;
		}

		@Override
		public Object convert(Object source, Class<? extends Object> exactTargetClass, Object...objects) {
			return source;
		}
	};
	
	public TargetClass convert(SourceClass source, Object...objects);
/*	public List<TargetClass> convert(List<SourceClass> source);
	public TargetClass[] convert(SourceClass[] source);
*/	
	public TargetClass convert(SourceClass source, Class<? extends TargetClass> targetClass, Object...objects);
	
	public Class<? extends SourceClass> getSourceClass();
	public Class<TargetClass> getTargetClass();
	
}
