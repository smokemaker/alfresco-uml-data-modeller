package ru.neodoc.content.utils.uml.profile.dataconverter;

public class StringToInteger extends AbstractFromStringConverter<Integer> {

	public StringToInteger() {
		super(Integer.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Integer getNullValue() {
		return 0;
	}
	
	@Override
	protected Integer doConvert(String source, Class<? extends Integer> exactTargetClass, Object...objects) {
		try {
			return Integer.parseInt(source);
		} catch (Exception e) {
			return getNullValue();
		}
	}

}
