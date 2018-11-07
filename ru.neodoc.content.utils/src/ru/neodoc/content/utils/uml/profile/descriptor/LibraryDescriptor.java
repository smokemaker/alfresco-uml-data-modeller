package ru.neodoc.content.utils.uml.profile.descriptor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.annotation.ACustomDataTypeClass;
import ru.neodoc.content.utils.uml.profile.annotation.ALibrary;
import ru.neodoc.content.utils.uml.profile.dataconverter.AnyToDynamicEObjectImpl;
import ru.neodoc.content.utils.uml.profile.dataconverter.DataConverterRegistry;
import ru.neodoc.content.utils.uml.profile.descriptor.scanner.ClassInClassScanner;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public class LibraryDescriptor extends AbstractOwnedDescriptor<Class, org.eclipse.uml2.uml.Package, ProfileDescriptor> {

	protected Map<String, Class<?>> customDataTypeClasses = new HashMap<>();

	protected ALibrary annotation;
	
	public LibraryDescriptor(Class libraryClass, ProfileDescriptor profileDescriptor) {
		super(libraryClass, profileDescriptor);
		this.annotation = ((Class<?>)this.originElement).getAnnotation(ALibrary.class);
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
/*	protected void scan(Class<?> libraryClass) {
		Class<?>[] innerClasses = libraryClass.getDeclaredClasses(); 
		for (int i=0; i<innerClasses.length; i++) {
			Class<?> innerClass = innerClasses[i];
			if (innerClass.getAnnotation(ACustomDataTypeClass.class)!=null) {
				customDataTypeClasses.put(innerClass.getSimpleName(), innerClass);
				DataConverterRegistry.INSTANCE.addConverter(innerClass, DynamicEObjectImpl.class, new AnyToDynamicEObjectImpl(innerClass));
			}
		}
	}*/

	@Override
	protected void initInnerStorages() {
		super.initInnerStorages();
		innerStorages.put(CustomDataTypeClassDescriptor.class, new InnerStorage<CustomDataTypeClassDescriptor, Class>());
	}
	
	@Override
	protected void doScan(Class javaElement) {
		ClassInClassScanner scanner = new ClassInClassScanner();
		scanner.add(ACustomDataTypeClass.class, CustomDataTypeClassDescriptor.class);
		scanner.stopOn(ALibrary.class);
		scanner.scan(this, javaElement);
	}
	
	public ProfileDescriptor getProfileDescriptor() {
		return this.ownerDescriptor;
	}

	@Override
	public String getName() {
		if (CommonUtils.isValueable(annotation.name()))
			return annotation.name();
		return originElement.getSimpleName();
	}

	@Override
	protected Package getFromParent(Element parentElement) {
		if (!(parentElement instanceof Profile))
			return null;
		Profile profile = (Profile)parentElement;
		return UMLSearchUtils.packageByName(profile, getName());
	}

}
