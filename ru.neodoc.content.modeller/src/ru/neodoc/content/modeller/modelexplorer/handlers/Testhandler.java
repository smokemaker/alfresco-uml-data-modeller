package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSDiagram;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ru.neodoc.content.modeller.model.AlfrescoModelUtils;

public class Testhandler extends AbstractAlfrescoHandler {
	
	protected class TestCommand extends AbstractAlfrescoCommand {
		public TestCommand(IEvaluationContext context){
			super(context,
					new Runnable() {
				
						public void run() {
							String message = "";
							Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
							EObject businessObject = getSelectedElement();
							EObject eObject = getExactSelectedElement();
							if (eObject instanceof CSSDiagram){
								CSSDiagram diagram = (CSSDiagram)eObject;
								/*EObjectListValueStyle styleList = null;
								for (Object style: diagram.getStyles()){
									if (!(style instanceof NamedStyle))
										continue;
									if (CSSStyles.CSS_DIAGRAM_STYLESHEETS_KEY.equals(((NamedStyle)style).getName())
											&& (style instanceof EObjectListValueStyle)){
										styleList = (EObjectListValueStyle)style;
										break;
									}
								}
								if (styleList==null) {
									message += "NULL::";
									styleList = (EObjectListValueStyle)diagram.createStyle(
											NotationPackage.eINSTANCE.getEObjectListValueStyle()
										);
									
								}
								StyleSheetReference ref = null;
								String defaultCss = AlfrescoModelUtils.getAlfrescoRoot(businessObject).getDefaultCss();
								for (Object obj: styleList.getEObjectListValue()){
									if (obj instanceof StyleSheetReference)
										if (((StyleSheetReference)obj).getPath().equals(defaultCss)){
											ref = (StyleSheetReference)obj;
											break;
										}
								}
								
								if (ref == null) {
									ref = StylesheetsFactory.eINSTANCE.createStyleSheetReference();
									ref.setPath(defaultCss);
									styleList.getEObjectListValue().add(ref);
								}
*/								
								AlfrescoModelUtils.applyCSSToDiagram(
									diagram, 
									AlfrescoModelUtils.getAlfrescoRoot(businessObject).getDefaultCss());
							}
							MessageBox mb = new MessageBox(shell);
							mb.setMessage(message + eObject.getClass().getName());
							mb.open();
						}
					}, 
					"LABEL", "DESC");
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new TestCommand(context);
	}
	
}
