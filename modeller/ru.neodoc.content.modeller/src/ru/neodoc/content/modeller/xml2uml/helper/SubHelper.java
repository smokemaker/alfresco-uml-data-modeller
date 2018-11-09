package ru.neodoc.content.modeller.xml2uml.helper;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;

public interface SubHelper<ContainerType, ElementType> {
	public <ContainerTypeLocal> void populateFromContainer(ModelObject<ContainerTypeLocal> container);
	public <ContainerTypeLocal> void populateFromContainer(ModelObject<ContainerTypeLocal> container, ExecutionCallback callback);
	
	public <ContainerTypeLocal> void deployFromContainer(ModelObject<ContainerTypeLocal> container);
	public <ContainerTypeLocal> void deployFromContainer(ModelObject<ContainerTypeLocal> container, ExecutionCallback callback);
	
}
