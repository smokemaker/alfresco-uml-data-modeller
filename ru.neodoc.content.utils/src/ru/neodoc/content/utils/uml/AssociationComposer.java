package ru.neodoc.content.utils.uml;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.utils.CommonUtils;

public class AssociationComposer {

	
	public static class AssociationEnd {
		
		protected AssociationComposer builder;
		protected Property memberEnd;
		
		public AssociationEnd(AssociationComposer builder, Property memberEnd) {
			this.builder = builder;
			this.memberEnd = memberEnd;
		}
	
		public AssociationComposer builder() {
			return this.builder;
		}
		
		public AssociationEnd aggregation(AggregationKind ak) {
			this.memberEnd.setAggregation(ak==null?AggregationKind.NONE_LITERAL:ak);
			return this;
		}
		
		public AggregationKind getAggregation() {
			return this.memberEnd.getAggregation();
		}
		
		public AssociationEnd roleName(String name) {
			if (CommonUtils.isValueable(name))
				this.memberEnd.setName(name);
			else
				this.memberEnd.unsetName();
			return this;
		}
		
		public String getRoleName() {
			return this.memberEnd.getName();
		}
		
		public AssociationEnd navigable(boolean value) {
			this.memberEnd.setIsNavigable(value);
			return this;
		}
		
		public boolean isNavigable() {
			return this.memberEnd.isNavigable();
		}
		
		public AssociationEnd lower(int lower) {
			this.memberEnd.setLower(lower);
			return this;
		}
		
		public int getLower() {
			return this.memberEnd.getLower();
		}
		
		public AssociationEnd upper(int upper) {
			this.memberEnd.setUpper(upper);
			return this;
		}
		
		public int getUpper() {
			return this.memberEnd.getUpper();
		}
		
		public boolean isMandatory() {
			return getLower()>0;
		}
		
		public boolean isMultiple() {
			return getUpper()>1 || getUpper()<0;
		}
		
		public Property getElement() {
			return this.memberEnd;
		}
		
		public Type getType() {
			return this.memberEnd.getType();
		}
	}
	
	public static class TargetAssociationEnd extends AssociationEnd {

		public TargetAssociationEnd(AssociationComposer builder, Property memberEnd) {
			super(builder, memberEnd);
			// TODO Auto-generated constructor stub
		}
		
		public TargetAssociationEnd type(Type type) {
			this.memberEnd.setType(type);
			return this;
		}
		
		public Type getType() {
			return this.memberEnd.getType();
		}
	}
	
	public static class AssociationInitializer {
		
		protected AssociationComposer builder;
		
		public AssociationInitializer(AssociationComposer builder) {
			this.builder = builder;
		}
		
		public AssociationComposer to(Class target) {
			builder.initialize(target);
			builder.defaultName();
			return builder;
		}
	}
	
	protected Class sourceClass;
	protected Class targetClass;
	
	protected Association association;
	
	protected AssociationEnd sourceEnd;
	protected TargetAssociationEnd targetEnd;
	
	public static AssociationInitializer create(Class source) {
		AssociationComposer builder = new AssociationComposer();
		builder.sourceClass = source;
		return new AssociationInitializer(builder);
	}

	public static AssociationComposer create(Association association) {
		AssociationComposer builder = new AssociationComposer();
		builder.association = association;
		if (!association.isBinary())
			return null;
		Type t = association.getMemberEnds().get(0).getType();
		if (t instanceof Class)
			builder.targetClass = (Class)t;
		
		t = association.getMemberEnds().get(1).getType();
		if (t instanceof Class)
			builder.sourceClass = (Class)t;
		builder.initialize();
		return builder;
	}
	
	protected AssociationComposer() {
		
	}
	
	protected void initialize(Class target) {
		this.association = sourceClass.createAssociation(
					// target
				/*navigable*/	false, 
				/*aggregation*/	AggregationKind.NONE_LITERAL, 
				/*name*/		"", 
				/*lower*/		0, 
				/*upper*/		1, 
				/*type*/		target, 
					// source
				/*navigable*/	false, 
				/*aggregation*/	AggregationKind.NONE_LITERAL, 
				/*name*/		"", 
				/*lower*/		0, 
				/*upper*/		1
				);
		this.targetClass = target;
		initialize();
	}
	
	protected void initialize() {
		this.sourceEnd = new AssociationEnd(this, this.association.getMemberEnds().get(1));
		this.targetEnd = new TargetAssociationEnd(this, this.association.getMemberEnds().get(0));
	}
	
	public AssociationComposer name(String name) {
		if (CommonUtils.isValueable(name))
			this.association.setName(name);
		else
			this.association.unsetName();
		return this;
	}
	
	public AssociationComposer prefix(String s) {
		if (CommonUtils.isValueable(s)) {
			String current = this.association.getName();
			String result = s;
			if (CommonUtils.isValueable(current))
				result += current;
			this.association.setName(result);
		}
		return this;
	}

	public AssociationComposer defaultName() {
		this.association.setName(this.sourceClass.getName() + "_" + this.targetClass.getName());
		return this;
	}
	
	public AssociationEnd source() {
		return this.sourceEnd;
	}
	
	public TargetAssociationEnd target() {
		return this.targetEnd;
	}
	
	public Association getAssociation() {
		return this.association;
	}
	
	public boolean isValid(){
		return (this.sourceEnd!=null);
	}
	
	public boolean isCyclic(){
		return (isValid() && (this.targetEnd==null || this.sourceEnd.getElement()==this.targetEnd.getElement()));
	}
	
	public boolean isFull(){
		return (isValid() && !isCyclic());
	}
	
	public boolean agregates(){
		return isFull() && (
				target().getAggregation().equals(AggregationKind.COMPOSITE_LITERAL)
				|| target().getAggregation().equals(AggregationKind.SHARED_LITERAL));
	}
	
	@Deprecated
	public boolean isDefinedFromSource(){
		return ((this.association!=null)
				&& (this.source()!=null)
				&& (this.association.getMemberEnds().size()>1)
				&& (this.source().getElement() == this.association.getMemberEnds().get(1))
				);
	}
	
}
