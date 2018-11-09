package ru.neodoc.content.utils.uml;

import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Type;

@Deprecated
public class DecomposedAssociation {
	public org.eclipse.uml2.uml.Property source;
	public org.eclipse.uml2.uml.Property target;
	public Association umlAssociation;
	public Type umlType;
	
	public DecomposedAssociation(Association umlAssociation, Type umlType){
		this.umlAssociation = umlAssociation;
		this.umlType = umlType;
		List<org.eclipse.uml2.uml.Property> memberEnds = this.umlAssociation.getMemberEnds(); 
		for (org.eclipse.uml2.uml.Property property: memberEnds){
			if (property.getType() == this.umlType)
				this.source = property;
			else
				this.target = property;
		}
		if (memberEnds.size()==2 && target==null)
			target = source;
	}
	
	public boolean isValid(){
		return (source!=null);
	}
	
	public boolean isCyclic(){
		return (isValid() && (target==null || source==target));
	}
	
	public boolean isFull(){
		return (isValid() && !isCyclic());
	}
	
	public boolean agregates(){
		return isFull() && (
				target.getAggregation().equals(AggregationKind.COMPOSITE_LITERAL)
				|| target.getAggregation().equals(AggregationKind.SHARED_LITERAL));
	}
	
	public Type getSourceType(){
		return source==null?null:source.getType();
	}

	public Type getTargetType(){
		return target==null?null:target.getType();
	}
	
	public boolean isTargetNavigable(){
		return (target!=null && target.isNavigable());
	}
	
	public boolean isDefinedFromSource(){
		return ((this.umlAssociation!=null)
				&& (this.source!=null)
				&& (this.umlAssociation.getMemberEnds().size()>1)
				&& (this.source == this.umlAssociation.getMemberEnds().get(1))
				);
	}
}
