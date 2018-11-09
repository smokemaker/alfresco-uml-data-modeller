package ru.neodoc.content.utils.uml.search.helper;

import java.util.Map;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.search.converter.UMLSearchConvertibleList;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilter;

public interface UMLSearchHelper<ContainerClass extends Element, TargetClass extends Element> {

	/*
	 * search itself
	 */
	public abstract UMLSearchConvertibleList<? extends TargetClass> search();

	public abstract Map<String, ? extends TargetClass> searchToMap();
	
	public abstract TargetClass first();

	public abstract UMLSearchHelper<ContainerClass, TargetClass> includeStartingPoint(
			boolean isp);
	
	public abstract UMLSearchHelper<ContainerClass, TargetClass> recoursive(
			boolean rec);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> stopOnStereotype(
			String stereotype);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> notStopOnStereotype(
			String stereotype);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> target(
			String stereotype);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> target(
			Class<?> clazz);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> startWith(
			ContainerClass startingPoint);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> startWithStereotype(
			String startingPointStereotype);

	public abstract UMLSearchHelper<ContainerClass, TargetClass> filter(
			UMLSearchFilter<? super TargetClass, ? super ContainerClass, ? extends Object> f);

}