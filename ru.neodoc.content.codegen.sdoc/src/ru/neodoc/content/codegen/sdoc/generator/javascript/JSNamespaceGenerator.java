package ru.neodoc.content.codegen.sdoc.generator.javascript;

import java.util.List;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.generator.java.NamespaceGenerator;
import ru.neodoc.content.codegen.sdoc.generator.javascript.writer.JSResourceWriter;
import ru.neodoc.content.codegen.sdoc.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.AssociationWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.MandatoryAspectWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.TypeWrapper;

public class JSNamespaceGenerator extends NamespaceGenerator {

	public JSNamespaceGenerator(CodegenManager codegenManager, NamespaceWrapper wrapper) {
		super(codegenManager, wrapper);
	}

	@Override
	protected void initTargets() {
		String targetLocation = namespaceWrapper.getTargetJavaScriptLocation();
/*		String workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		
		targetLocation = workspaceRoot + "/" + targetLocation;
		targetLocation = targetLocation.replace("//", "/").replace("//", "/").replace("/", "\\");
*/		
		targetFilePath = targetLocation;
		targetFileName = namespaceWrapper.getTargetJavaScriptName() + ".js";	
	}
	
	@Override
	protected void collectImports() {
		// NOOP
	}
	
	@Override
	protected void initWriter(){
		writer = new JSResourceWriter(namespaceWrapper, reporter, null);
		writer.setLookupProvider(new JavaLookupProvider(codegenManager));
		writer.setCommentProvider(new JSCommentProvider(codegenManager));
		writer.updateChildrenProviders();
		// writer.setImports(imports);
	}
	
	public static class JSCommentProvider extends JavaCommentProvider {

		public JSCommentProvider(CodegenManager codegenManager) {
			super(codegenManager);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected String getTypeComment(TypeWrapper w) {
			String title = w.getTitle();
			String parent = null;
			if (w.getParent()!=null)
				parent = w.getParent().getPrefixedName();
			String mandatoryAspects = null;
			List<MandatoryAspectWrapper> lma = w.getChildren(MandatoryAspectWrapper.class);
			if (!lma.isEmpty()){
				boolean isFirst = true;
				mandatoryAspects = "";
				for (MandatoryAspectWrapper maw: lma){
					if (!isFirst)
						mandatoryAspects += ", ";
					mandatoryAspects += maw.getTargetType().getPrefixedName();
					isFirst = false;
				}
			}
			StringBuffer sb = new StringBuffer();
			
			sb.append("/** ");
			sb.append(title!=null?title:"");
			sb.append("\n");
			
			if (title!=null)
				sb.append("@title ")
					.append(title)
					.append("\n");
			
			if ((parent!=null) && (parent.trim().length()>0))
				sb.append("@parent ")
					.append(parent)
					.append("\n");
			
			if ((mandatoryAspects!=null) && (mandatoryAspects.trim().length()>0))
				sb.append("@mandatoryAspects ")
					.append(mandatoryAspects)
					.append("\n");
			
			sb.append("*/");
			
			return sb.toString();
		}
		
		@Override
		protected String getPropertyComment(PropertyWrapper w) {
			StringBuffer sb = new StringBuffer();
			sb.append("/** ");
			String title = w.getTitle();
			if (title==null)
				title = "";
			sb.append("@title ")
				.append(title)
				.append("\n");
			String typeName = "";
			if (w.getDataTypeWrapper()!=null){
				typeName = w.getDataTypeWrapper().getPrefixedName();
				if (typeName == null)
					typeName = "";
			}
			sb.append("@type ")
				.append(typeName)
				.append(" */");
			return sb.toString();
		}
	
		@Override
		protected String getAspectComment(AspectWrapper w) {
			String title = w.getTitle();
			String mandatoryAspects = null;
			List<MandatoryAspectWrapper> lma = w.getChildren(MandatoryAspectWrapper.class);
			if (!lma.isEmpty()){
				boolean isFirst = true;
				mandatoryAspects = "";
				for (MandatoryAspectWrapper maw: lma){
					if (!isFirst)
						mandatoryAspects += ", ";
					mandatoryAspects += maw.getTargetType().getPrefixedName();
					isFirst = false;
				}
			}
			StringBuffer sb = new StringBuffer();
			
			sb.append("/** ");
			sb.append(title!=null?title:"");
			sb.append("\n");
			
			if (title!=null)
				sb.append("@title ")
					.append(title)
					.append("\n");
			
			if ((mandatoryAspects!=null) && (mandatoryAspects.trim().length()>0))
				sb.append("@mandatoryAspects ")
					.append(mandatoryAspects)
					.append("\n");
			
			sb.append("*/");
			
			return sb.toString();
		}
		
		@Override
		protected String getAssociationComment(AssociationWrapper w) {
			StringBuffer sb = new StringBuffer();
			sb.append("/** @target ");
			String targetName = "";
			if (w.getTargetType() != null) {
				targetName = w.getTargetType().getPrefixedName();
				if (targetName == null)
					targetName = "";
			}
			sb.append(targetName)
				.append(" */");
			return sb.toString();
		}
	}
	
}
