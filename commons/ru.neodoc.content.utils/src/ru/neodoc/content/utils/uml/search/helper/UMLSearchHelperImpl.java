package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConvertibleList;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConvertibleListImpl;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilter;

public abstract class UMLSearchHelperImpl<ContainerClass extends Element, TargetClass extends Element> 
	implements UMLSearchHelper<ContainerClass, TargetClass> {

	protected boolean recoursiveSearch = true;
	protected boolean includeStartingPointInSearch = false;
	
	protected Set<String> stopOnStereotype = new HashSet<>();
	
	protected String targetStereotype = null;
	protected Class<?> targetClass = null;
	
	protected ContainerClass startingPoint = null;
	protected String startingPointStereotype = null;
	
	protected List<UMLSearchFilter<? super TargetClass, ? super ContainerClass,  ? extends Object>> filters = 
			new ArrayList<>();
	
	/*
	 * search itself
	 */
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#search()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UMLSearchConvertibleList<? extends TargetClass> search(){
		UMLSearchConvertibleList<TargetClass> result = new UMLSearchConvertibleListImpl<>();
		
		if (isAllowedStartingPoint()) {
			/*if (includeStartingPointInSearch) {
				try {
					if (fits((TargetClass)this.startingPoint, (ContainerClass)this.startingPoint))
						result.add((TargetClass)this.startingPoint);
				} catch (Exception e) {
					
				}
			}*/
			result.addAll(searchInContainer(this.startingPoint));
		}
		return result;
	}
	
	@Override
	public Map<String, ? extends TargetClass> searchToMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#first()
	 */
	@Override
	public TargetClass first(){
		List<? extends TargetClass> list = search();
		if (list.size()>0)
			return list.get(0);
		return null;
	}
	
	protected List<? extends TargetClass> searchInContainer(ContainerClass container){
		List<TargetClass> result = new ArrayList<>();
		
		List<? extends TargetClass> elements = getElementsFromContainer(container);
		for (TargetClass element: elements)
			if (fits(element, container))
				result.add(element);
		
		for (ContainerClass cont: getContainersFromContainer(container))
			if (isAllowedContainer(cont))
				result.addAll(searchInContainer(cont));
		
		return result;
	}
	
	protected abstract List<? extends TargetClass> getElementsFromContainer(ContainerClass container);
	protected abstract List<? extends ContainerClass> getContainersFromContainer(ContainerClass container);
	
	protected boolean fits(TargetClass element, ContainerClass container) {
		boolean result = true;
		if (this.targetClass != null)
			result = this.targetClass.isInstance(element);
		if (this.targetStereotype != null) {
			result = element.isStereotypeApplied(
					UMLUtils.getStereotype(element, this.targetStereotype)
				);
		}
		for (UMLSearchFilter<? super TargetClass, ? super ContainerClass, ? extends Object> f: this.filters)
			result = result && f.matches(element, container);
		return result;
	} 
	
	protected boolean isAllowedContainer(ContainerClass container){
		boolean result = true;
		for (String stereotype: this.stopOnStereotype)
			result &= !UMLUtils.hasStereotype(container, stereotype);
		return result;
	}

	protected boolean isAllowedStartingPoint(){
		if (this.startingPointStereotype==null)
			return true;
		return UMLUtils.hasStereotype(this.startingPoint, this.startingPointStereotype);
	}
	
	
	
	/*
	 * BUILDERS
	 */
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#recoursive(boolean)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> recoursive(boolean rec) {
		setRecoursiveSearch(rec);
		return this;
	} 
	
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> includeStartingPoint(
			boolean isp) {
		setIncludeStartingPointInSearch(isp);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#stopOnStereotype(java.lang.String)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> stopOnStereotype(String stereotype) {
		getStopOnStereotype().add(stereotype);
		return this;
	} 
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#notStopOnStereotype(java.lang.String)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> notStopOnStereotype(String stereotype) {
		getStopOnStereotype().remove(stereotype);
		return this;
	} 
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#target(java.lang.String)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> target(String stereotype){
		setTargetStereotype(stereotype);
		return this;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#target(java.lang.Class)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> target(Class<?> clazz){
		setTargetClass(clazz);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#startWith(ContainerClass)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> startWith(@SuppressWarnings("hiding") ContainerClass startingPoint){
		setStartingPoint(startingPoint);
		return this;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#startWithStereotype(java.lang.String)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> startWithStereotype(@SuppressWarnings("hiding") String startingPointStereotype){
		setStartingPointStereotype(startingPointStereotype);
		return this;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchHelper#filter(ru.neodoc.content.modeller.utils.uml.UMLSearchFilterImpl)
	 */
	@Override
	public UMLSearchHelper<ContainerClass, TargetClass> filter(
			UMLSearchFilter<? super TargetClass, ? super ContainerClass, ? extends Object> f){
		this.filters.add(f);
		return this;
	}
	
	/*
	 * ACCESSORS
	 */
	public boolean isRecoursiveSearch() {
		return this.recoursiveSearch;
	}

	public void setRecoursiveSearch(boolean recoursiveSearch) {
		this.recoursiveSearch = recoursiveSearch;
	}

	public boolean isIncludeStartingPointInSearch() {
		return this.includeStartingPointInSearch;
	}

	public void setIncludeStartingPointInSearch(boolean includeStartingPointInSearch) {
		this.includeStartingPointInSearch = includeStartingPointInSearch;
	}

	public Set<String> getStopOnStereotype() {
		return this.stopOnStereotype;
	}

	public void setStopOnStereotype(Set<String> stopOnStereotype) {
		this.stopOnStereotype = stopOnStereotype;
	}

	public String getTargetStereotype() {
		return this.targetStereotype;
	}

	public void setTargetStereotype(String targetStereotype) {
		this.targetStereotype = targetStereotype;
	}

	public Class<?> getTargetClass() {
		return this.targetClass;
	}

	public ContainerClass getStartingPoint() {
		return this.startingPoint;
	}

	public void setStartingPoint(ContainerClass startingPoint) {
		this.startingPoint = startingPoint;
	}

	public String getStartingPointStereotype() {
		return this.startingPointStereotype;
	}

	public void setStartingPointStereotype(String startingPointStereotype) {
		this.startingPointStereotype = startingPointStereotype;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public List<UMLSearchFilter<? super TargetClass, ? super ContainerClass, ? extends Object>> getFilters() {
		return this.filters;
	}

	public void setFilters(
			List<UMLSearchFilter<? super TargetClass, ? super ContainerClass, ? extends Object>> filters) {
		this.filters = filters;
	}
	
}
