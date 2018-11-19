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

public class AnyToDynamicEObjectImpl extends AbstractDataConverter<Object, DynamicEObjectImpl> 
		implements DataConverterRegistryListener {

	public AnyToDynamicEObjectImpl() {
		super(Object.class, DynamicEObjectImpl.class);
	}

	@Override
	protected DynamicEObjectImpl doConvert(Object source, Class<? extends DynamicEObjectImpl> exactTargetClass, Object...objects) {
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
		
		
		DynamicEObjectImpl result = eClass==null 
				?new DynamicEObjectImpl()
				:new DynamicEObjectImpl(eClass);
		
		if (container!=null)
			container.eContents().add(result);
		
		if (eClass!=null) {
			Field[] fields = source.getClass().getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				try {
					String name = field.getName();
					EStructuralFeature feature = eClass.getEStructuralFeature(name);
					if (feature==null)
						continue;
					Object value = field.get(source);
					result.eDynamicSet(feature, value);
				} catch (Exception e) {
					
				}
			}
		}
		
		return result;
	}

	@Override
	public void onClassAdded(Class<?> clazz, final DataConverterRegistry registry) {
		CustomDataTypeClassDescriptor cdtcd = CustomDataTypeClassDescriptor.find(clazz);
		if (cdtcd!=null)
			registry.addConverter(clazz, targetClass, this);
			return;
	}

	@Override
	public void onConverterAdded(DataConverter<?, ?> converter, final DataConverterRegistry registry) {
		// NOOP
	}

}
