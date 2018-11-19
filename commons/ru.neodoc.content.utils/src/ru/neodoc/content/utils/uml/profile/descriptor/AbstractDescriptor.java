package ru.neodoc.content.utils.uml.profile.descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;

public abstract class AbstractDescriptor<JavaElementClass, UMLElementClass extends Element> {

	public static class InnerStorage<T extends AbstractDescriptor<J, ?>, J>{
		
		public static <DescriptorClass extends AbstractDescriptor<ElementClass, ?>, ElementClass> 
				InnerStorage<DescriptorClass, ElementClass> create(){
			return new InnerStorage<DescriptorClass, ElementClass>();
		}
		
		protected Map<String, T> mapByName = new HashMap<>();
		protected Map<J, T> mapByElement = new HashMap<>();
		
		public void add(T descriptor) {
			mapByName.put(descriptor.getName(), descriptor);
			for (J element: descriptor.getKeys())
				if (element!=null)
					mapByElement.put(element, descriptor);
		}
		
		public T getByName(String name) {
			return mapByName.get(name);
		}
		
		public T getByElement(J element) {
			return mapByElement.get(element);
		}
		
		public List<T> getAll(){
			return new ArrayList<>(mapByName.values());
		}
	}
	
	protected JavaElementClass originElement;
	
	protected UMLElementClass umlElement;
	
	protected boolean isInited = false;
	
	protected final Map<Class<? extends AbstractDescriptor<?, ?>>, InnerStorage<?, ?>> innerStorages = new HashMap<>(); 
	
	public AbstractDescriptor(JavaElementClass origin) {
		super();
		
		setOriginElement(origin);
	}
	
	public void init() {
		if (isInited)
			return;
		initInnerStorages();
		getUMLElement();
		ProfileRegistry.INSTANCE.registerDescriptor(this);
		scan();
		isInited = true;
	}
	
	public List<JavaElementClass> getKeys(){
		List<JavaElementClass> result = new ArrayList<>();
		result.add(originElement);
		return result;
	}
	
	public void reinit() {
		isInited = false;
		init();
	}
	
	protected <DescriptorClass extends AbstractDescriptor<ElementClass, ?>, ElementClass> void innerStorage(Class<? extends DescriptorClass> descriptorClass){
		innerStorages.put(descriptorClass, InnerStorage.<DescriptorClass, ElementClass>create());
	}
	
	protected void initInnerStorages() {
		
	}
	
	protected void scan() {
		doScan(this.originElement);
	}
	
	protected void doScan(JavaElementClass javaElement) {
		
	}
	
	public abstract boolean isValid();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addOwned(AbstractDescriptor<?, ?> ownedDescriptor) {
		InnerStorage is = innerStorages.get(ownedDescriptor.getClass());
		if (is==null)
			return;
		is.add(ownedDescriptor);
	}
	
	public <T extends AbstractDescriptor<?, ?>> T getByName(String name, Class<T> clazz){
		return getByFullName(PrefixedName.name(name, "::"), clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractDescriptor<?, ?>> T getByFullName(String name, Class<T> clazz){
		InnerStorage<T, ?> storage = (InnerStorage<T, ?>)innerStorages.get(clazz);
		if (storage==null)
			return null;
		return (T)storage.getByName(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractDescriptor<J, ?>, J> T getByElement(J element, Class<T> clazz){
		InnerStorage<T, J> storage = (InnerStorage<T, J>)innerStorages.get(clazz);
		if (storage==null)
			return null;
		return (T)storage.getByElement(element);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractDescriptor<?, ?>> List<T> getAll(Class<T> clazz){
		InnerStorage<T, ?> storage = (InnerStorage<T, ?>)innerStorages.get(clazz);
		if (storage==null)
			return Collections.emptyList();
		return new ArrayList<T>(storage.getAll());
		
	}
	
	public UMLElementClass getUMLElement() {
		if (umlElement==null) {
			umlElement = loadUMLElement(ProfileRegistry.innerResourceSet);
		}
		return umlElement;
	}

	public UMLElementClass getUMLElement(ResourceSet resourceSet) {
		return loadUMLElement(resourceSet);
	}
	
	protected abstract UMLElementClass loadUMLElement(ResourceSet resourceSet);
	
	// setters & getters
	
	public JavaElementClass getOriginElement() {
		return originElement;
	}

	protected void setOriginElement(JavaElementClass originElement) {
		this.originElement = originElement;
	}

	public abstract String getName(); 
}
