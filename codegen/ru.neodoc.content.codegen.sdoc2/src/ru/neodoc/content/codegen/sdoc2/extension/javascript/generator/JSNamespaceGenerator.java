package ru.neodoc.content.codegen.sdoc2.extension.javascript.generator;

import java.util.List;

import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.NamespaceGenerator;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.generator.writer.JSResourceWriter;
import ru.neodoc.content.codegen.sdoc2.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AssociationSolidWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.MandatoryAspectWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.TypeWrapper;

public class JSNamespaceGenerator extends NamespaceGenerator {

	protected WrapperJSExtension wrapperJSExtension = null;
	
	public JSNamespaceGenerator(Configuration configuration, NamespaceWrapper wrapper) {
		super(configuration, wrapper);
		wrapperJSExtension = WrapperJSExtension.get(namespaceWrapper);
	}

	@Override
	protected void initTargets() {
		String targetLocation = wrapperJSExtension.getTargetJSLocation();
/*		String workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		
		targetLocation = workspaceRoot + "/" + targetLocation;
		targetLocation = targetLocation.replace("//", "/").replace("//", "/").replace("/", "\\");
*/		
		targetFilePath = targetLocation;
		targetFileName = wrapperJSExtension.getTargetJSName() + ".js";	
	}
	
	@Override
	protected void collectImports() {
		// NOOP
	}
	
	@Override
	protected void initWriter(){
		writer = new JSResourceWriter(namespaceWrapper, reporter, null);
		writer.setLookupProvider(new JavaLookupProvider(getConfiguration()));
		writer.setCommentProvider(new JSCommentProvider(getConfiguration()));
		writer.updateChildrenProviders();
		// writer.setImports(imports);
	}
	
	public static class JSCommentProvider extends JavaCommentProvider {

		public JSCommentProvider(Configuration configuration) {
			super(configuration);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected String getTypeComment(TypeWrapper w) {
			String title = w.getTitle();
			String parent = null;
			if (w.getOwner()!=null)
				parent = w.getOwner().getPrefixedName();
			String mandatoryAspects = null;
			List<MandatoryAspectWrapper> lma = w.getChildren(MandatoryAspectWrapper.class);
			if (!lma.isEmpty()){
				boolean isFirst = true;
				mandatoryAspects = "";
				for (MandatoryAspectWrapper maw: lma){
					if (!isFirst)
						mandatoryAspects += ", ";
					mandatoryAspects += maw.getTargetAspectWrapper().getPrefixedName();
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
					mandatoryAspects += maw.getTargetAspectWrapper().getPrefixedName();
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
		protected String getAssociationComment(AssociationSolidWrapper<?> w) {
			StringBuffer sb = new StringBuffer();
			sb.append("/** @target ");
			String targetName = "";
			if (w.getTargetClassWrapper() != null) {
				targetName = w.getTargetClassWrapper().getPrefixedName();
				if (targetName == null)
					targetName = "";
			}
			sb.append(targetName)
				.append(" */");
			return sb.toString();
		}
	}
	
}
