package ru.neodoc.content.utils.uml.profile.dataconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preloader {

	@SuppressWarnings("unchecked")
	public static void preload() {
		ClassLoader cl = Preloader.class.getClassLoader();
		
		List<Class<?>> classes = new ArrayList<>(Arrays.asList(
					new Class<?>[] {
						DataConverterRegistry.class
					}
				)); 
		
		for (Class<?> clazz: classes)
			try {
				Class.forName(clazz.getName(), true, cl);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		DataConverterRegistry.INSTANCE.register(
					BooleanToString.class,
					DoubleToString.class,
					DynamicEObjectImplToAny.class,
					EnumerationLiteralToString.class,
					ObjectToString.class,
					StringToBoolean.class,
					StringToEnum.class,
					StringToInteger.class,
					StringToObject.class,
					AnyToDynamicEObjectImpl.class
				);
	}
		
}
