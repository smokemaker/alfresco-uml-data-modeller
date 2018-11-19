package ru.neodoc.content.utils.uml.profile.dataconverter;

import java.lang.reflect.Field;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.dataconverter.DataConverterRegistry.DataConverterRegistryListener;
import ru.neodoc.content.utils.uml.profile.descriptor.CustomDataTypeClassDescriptor;

public class DynamicEObjectImplToAny extends AbstractDataConverter<DynamicEObjectImpl, Object> 
		implements DataConverterRegistryListener {

	public DynamicEObjectImplToAny() {
		super(DynamicEObjectImpl.class, Object.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClassAdded(Class<?> clazz, final DataConverterRegistry registry) {
		CustomDataTypeClassDescriptor cdtcd = CustomDataTypeClassDescriptor.find(clazz);
		if (cdtcd!=null)
			registry.addConverter(sourceClass, clazz, this);
			return;
	}

	@Override
	public void onConverterAdded(DataConverter<?, ?> converter, final DataConverterRegistry registry) {
		// NOOP
	}
	
	@Override
	protected Object doConvert(DynamicEObjectImpl source, Class<? extends Object> exactTargetClass, Object... objects) {
//		CustomDataTypeClassDescriptor cdtcd = CustomDataTypeClassDescriptor.find(source.getClass());
		Map<Class<?>, Object> parameters = CommonUtils.toTypedMap(objects, true);
		EClass eClass = (EClass)parameters.get(EClass.class);
		if (eClass==null) {
			for (Class<?> clazz: parameters.keySet())
				if (EClass.class.isAssignableFrom(clazz)) {
					eClass = (EClass)parameters.get(clazz);
					parameters.remove(clazz);
					break;
				}
		}
		
		EObject container = (EObject)parameters.get(EObject.class);
		if (container==null) {
			for (Class<?> clazz: parameters.keySet())
				if (EObject.class.isAssignableFrom(clazz)) {
					container = (EObject)parameters.get(clazz);
					parameters.remove(clazz);
					break;
				}
		} 
		
		
		Object result = null;
		try {
			result = exactTargetClass.newInstance();
		} catch (Exception e) {
			return null;
		}
		
		if (eClass!=null) {
			Field[] fields = exactTargetClass.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				try {
					String name = field.getName();
					EStructuralFeature feature = eClass.getEStructuralFeature(name);
					if (feature==null)
						continue;
					Object value = source.eDynamicGet(feature, true); 
					field.set(result, value);
				} catch (Exception e) {
					
				}
				
			}
		}
		
		return result;
	}

}
