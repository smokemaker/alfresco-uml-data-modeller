package ru.neodoc.content.utils.uml.profile.dataconverter;

public class DoubleToString extends AbstractFromStringConverter<Double> {

	public DoubleToString() {
		super(Double.class);
	}

	@Override
	protected Double getNullValue() {
		return (double) 0;
	}
	
	@Override
	protected Double doConvert(String source, Class<? extends Double> exactTargetClass, Object...objects) {
		try {
			return Double.parseDouble(source);
		} catch (Exception e) {
			return getNullValue();
		}
	}

}
