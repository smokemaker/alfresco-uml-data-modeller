package ru.neodoc.content.modeller.xml2uml.helper;

import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;

public abstract class AbstractSubHelper<ContainerType, ElementType, E extends Element> 
	extends AbstractHelper<ElementType, E> implements SubHelper<ContainerType, ElementType> {

	protected ContainerType container;
	protected ModelObject<ContainerType> containerModelObject;
	protected Class<? extends ContainerType> containerClass;
	
	@Override
	public <ContainerTypeLocal> void populateFromContainer(ModelObject<ContainerTypeLocal> container) {
		populateFromContainer(container, new ExecutionCallback() {
			
			@Override
			public void worked(int amount) {
				
			}
			
			@Override
			public void done() {
				
			}
		});
	}
	
	@Override
	public <ContainerTypeLocal> void populateFromContainer(ModelObject<ContainerTypeLocal> container, ExecutionCallback callback) {
		this.containerModelObject = (ModelObject<ContainerType>)container;
		setContainer(this.containerModelObject.source);
		for (ElementType element: getElementsFromContainer(this.containerModelObject.source)) {
			populate(element, callback);
		}
	}

	public ContainerType getContainer() {
		return container;
	}

	@SuppressWarnings("unchecked")
	public void setContainer(ContainerType container) {
		this.container = container;
		this.containerClass = (Class<? extends ContainerType>)container.getClass();
	}
	
	protected abstract List<ElementType> getElementsFromContainer(ContainerType container);

	protected List<ModelObject<? extends ElementType>> getModelObjectsFromContainer(ModelObject<? extends ContainerType> container){
		return Collections.emptyList();
	};
	
	@Override
	public <ContainerTypeLocal> void deployFromContainer(ModelObject<ContainerTypeLocal> container) {
		deployFromContainer(container, ExecutionCallback.EMPTY_CALLBACK);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <ContainerTypeLocal> void deployFromContainer(ModelObject<ContainerTypeLocal> container, ExecutionCallback callback) {
		this.containerModelObject = (ModelObject<ContainerType>)container;
		setContainer(this.containerModelObject.source);
		for (ModelObject<? extends ElementType> mo: getModelObjectsFromContainer(this.containerModelObject))
			deployToUML((ModelObject<ElementType>) mo);
	}
}
