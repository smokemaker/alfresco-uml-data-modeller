package ru.neodoc.content.modeller.xml2uml.structure;

import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.ChildAssociation;
import org.alfresco.model.dictionary._1.Association.Source;
import org.alfresco.model.dictionary._1.Association.Target;

public class AssociationInfo {
	
	public String sourceRole = "";
	public int sourceMin = 0;
	public int sourceMax = 1;

	public String targetRole = "";
	public int targetMin = 0;
	public int targetMax = 1;
	public boolean targetForce = false; 
	
	public String childName = null;
	public Boolean isDuplicate = null;
	public Boolean isPropogate = null;
	
	public boolean isChild = false;
	
	public String name = "";
	public String title = "";
	public String description = "";
	
	protected void load(Association pa) {
		
		if (pa.getName()!=null)
			name = pa.getName();
		
		if (pa.getTitle()!=null)
			title = pa.getTitle();
		
		if (pa.getDescription()!=null)
			description = pa.getDescription();
		
		if (pa.getSource()!=null) {
			Source src = pa.getSource();
			if (src.getRole() != null && ! "null".equals(src.getRole()))
				sourceRole = src.getRole();
			
			if (src.isMandatory()!=null) {
				if (src.isMandatory().booleanValue())
					sourceMin = 1;
			}

			if (src.isMany()!=null) {
				if (src.isMany().booleanValue())
					sourceMax = -1;
			}
		}

		if (pa.getTarget()!=null) {
			Target tgt = pa.getTarget();
			if (tgt.getRole() != null && ! "null".equals(tgt.getRole()))
				targetRole = tgt.getRole();
			
			if (tgt.getMandatory()!=null) {
				String content = tgt.getMandatory().getContent();
				if (content!=null && "true".equals(content.toLowerCase()))
					targetMin = 1;
				
				targetForce = (tgt.getMandatory().isEnforced()!=null)
					&& (tgt.getMandatory().isEnforced().booleanValue());
				
			}

			if (tgt.isMany()!=null) {
				if (tgt.isMany().booleanValue())
					targetMax = -1;
			}
		}
		
	}
	
	protected void load(ChildAssociation ca) {
		load((Association)ca);
		isChild = true;
		childName = ca.getChildName();
		isDuplicate = ca.isDuplicate();
		isPropogate = ca.isPropagateTimestamps();
	}
	
	public AssociationInfo(Association pa){
		load(pa);
	}

	public AssociationInfo(ChildAssociation ca){
		this ((Association)ca);
	}

	public AssociationInfo(Object obj){
		Object objectToLoad = obj;
		if (obj instanceof ModelObject<?>)
			objectToLoad = ((ModelObject<?>)obj).source;
		if (objectToLoad instanceof ChildAssociation)
			load((ChildAssociation)objectToLoad);
		else if (objectToLoad instanceof Association)
			load((Association)objectToLoad);
	}
	
	
}