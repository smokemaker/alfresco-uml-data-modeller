package ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.wizard.provider.AbstractWrapperLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;

public class JavaClassLabelProvider extends AbstractWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		JavaClassLabelProvider lp = new JavaClassLabelProvider();
		return new DelegatingStyledCellLabelProvider(lp);
	}
	
	@Override
	protected StyledString getWrapperStyledText(AbstractWrapper wrapper) {
		WrapperJavaExtension ext = WrapperJavaExtension.get(wrapper);
		if (wrapper instanceof NamespaceWrapper) {
			StringBuffer sb = new StringBuffer();
			sb.append(ext.getFinalJavaPackage())
				.append(".")
				.append(ext.getFullTargetJavaName());
			return new StyledString(sb.toString());
		}
		return new StyledString(ext.getFullTargetJavaName());
	}

}
