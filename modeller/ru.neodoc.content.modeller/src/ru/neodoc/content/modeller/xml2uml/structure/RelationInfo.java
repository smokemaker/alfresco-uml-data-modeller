package ru.neodoc.content.modeller.xml2uml.structure;

import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.ChildAssociation;

public class RelationInfo{
	
	@Deprecated
	public enum DependencyType {
		/* proxied by ru.neodoc.content.modeller.xml2uml.helper.proxy.ParentProxy */PARENT("parent"), 
		/* proxied by ru.neodoc.content.modeller.xml2uml.helper.proxy.MandatoryAspectProxy */MANDATORY_ASPECT("mandatory-aspect"), 
		/* has origin object */CHILD("child"), 
		/* has origin object */PEER("peer"), 
		/* has origin object */IMPORT("import"),
		CONSTRAINT("constraint"),
		CONSTRAINT_INLINE("constraint-inline"),
		DEPENDENCY("dependency"),
		UNKNOWN("unknown");
		
		private String name;
		
		private DependencyType(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
		
	}
	
	public ModelObject<? extends Object> source = null;
	public ModelObject<? extends Object> target = null;
	public DependencyType dependencyType;
	public ModelObject<?> relationObject;
	
	public RelationInfo(ModelObject<? extends Object> src, 
			ModelObject<? extends Object> tgt,
			DependencyType type){
		this.source = src;
		this.target = tgt;
		this.dependencyType = type;
	}
	
	public RelationInfo(ModelObject<? extends Object> src, 
			ModelObject<? extends Object> tgt,
			DependencyType type, ModelObject<?> depObj){
		this(src, tgt, type);
		this.relationObject = depObj;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RelationInfo))
			return false;
		RelationInfo di = (RelationInfo)obj;
		
		boolean result = true;
		
		result = result && 
				((this.source==null && di.source == null)
						|| (this.source.getName()==null && di.source.getName() == null)
						|| (this.source.getName().equals(di.source.getName())));

		result = result && 
				((this.target==null && di.target == null)
						|| (this.target.getName()==null && di.target.getName() == null)
						|| (this.target.getName().equals(di.target.getName())));
		
		result = result &&
				(this.dependencyType.equals(di.dependencyType));
		
		return result;
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public String toString() {
		return this.dependencyType.getName()
				+ ":" + source.getName() + "->" + target.getName();
	}
	
	public boolean isValid(){
		return (source != null && target != null && !source.isVirtual() && !target.isVirtual());
	}
	
	public boolean isAssociation(){
		return (relationObject!=null && (
				(relationObject.source instanceof ChildAssociation) || (relationObject.source instanceof Association)
				));
	}
	
	public AssociationInfo getAssociationInfo(){
		if (isAssociation())
			return new AssociationInfo(relationObject);
		return null;
	}
	
	public RelationInfo relationObject(ModelObject<?> object) {
		this.relationObject = object;
		return this;
	}
}
