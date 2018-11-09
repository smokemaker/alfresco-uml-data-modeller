package ru.neodoc.content.modeller.xml2uml.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;

public class RelationRegistry{
	
	Set<RelationInfo> dependencies = Collections.newSetFromMap(new ConcurrentHashMap<RelationInfo, Boolean>());
	List<RelationInfo> unsatisfiedDependencies = new ArrayList<>();
	
	Map<Object, RelationInfo> objectToRelationMap = new HashMap<>();
	
	private ObjectRegistry objRegistry; 
	
	public RelationRegistry(ObjectRegistry registry){
		this.objRegistry = registry;
	}
	
	public RelationInfo add(RelationInfo info){
		if (this.dependencies.contains(info))
			return info;
		this.dependencies.add(info);
		
		if (info.relationObject!=null)
			objectToRelationMap.put(info.relationObject, info);
		
		// auto create package imports
		if (!info.dependencyType.equals(DependencyType.IMPORT)
				&& !info.dependencyType.equals(DependencyType.DEPENDENCY)				
				){
			String srcPackage = info.source.pack;
			String tgtPackage = info.target.pack;
			if (srcPackage!=null && tgtPackage != null 
					&& !srcPackage.equals("") && !tgtPackage.equals("")
					&& !srcPackage.equals(tgtPackage)){
				ModelObject<?> moSrc = this.objRegistry.get(srcPackage);
				ModelObject<?> moTgt = this.objRegistry.get(tgtPackage);
				if (!moSrc.imports.contains(moTgt)){
					moSrc.imports.add(moTgt);
				}
				add(moSrc, moTgt, DependencyType.DEPENDENCY);
			}
		}
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object, S extends Object> RelationInfo add(ModelObject<T> src, ModelObject<S> tgt, DependencyType type){
		if (src==null || tgt==null)
			return null;
		ModelObject<?> s = this.objRegistry.get(src.getName());
		ModelObject<?> t = this.objRegistry.get(tgt.getName());
		return add(new RelationInfo(
				(ModelObject<Object>)s!=null?s:src, 
				(ModelObject<Object>)t!=null?t:tgt, 
				type));
	}
	
	public <T extends Object, S extends Object> RelationInfo relationInfo(ModelObject<T> src, ModelObject<S> tgt, 
			DependencyType type, ModelObject<?> dependencyObject){
		ModelObject<?> s = this.objRegistry.get(src.getName());
		ModelObject<?> t = this.objRegistry.get(tgt.getName());
		return add(new RelationInfo(s!=null?s:src, t!=null?t:tgt, type, dependencyObject));
	}
	
	public List<RelationInfo> getUnsatisfiedDependencies(){
		List<RelationInfo> result = new ArrayList<>();
		for (RelationInfo di: this.dependencies)
			if (!di.isValid())
				result.add(di);
		return result;
	}
	
	public List<RelationInfo> getAllDependencies() {
		return new ArrayList<>(this.dependencies);
	}
	
	public void remove(RelationInfo dependencyInfo) {
		if (objectToRelationMap.containsValue(dependencyInfo) && (dependencyInfo.relationObject!=null))
			objectToRelationMap.remove(dependencyInfo.relationObject);
			
		this.dependencies.remove(dependencyInfo);
	}
	
	public RelationInfo getRelationByObject(Object object) {
		if (objectToRelationMap.containsKey(object))
			return objectToRelationMap.get(object);
		return null;
	}
}

