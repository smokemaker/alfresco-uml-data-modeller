package ru.neodoc.content.modeller.palette.helper.advice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.papyrus.infra.types.core.impl.ConfiguredHintedSpecializationElementType;

import ru.neodoc.content.utils.CommonUtils;

public abstract class AlfrescoRelationEditHelperAdvice extends AbstractEditHelperAdvice {

	@Override
	protected ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		if (request.getEditCommandRequest() instanceof CreateRelationshipRequest) {
			CreateRelationshipRequest createRelationshipRequest = (CreateRelationshipRequest)request.getEditCommandRequest();
			createRelationshipRequest.setParameter(CommandParameters.IS_ALFRESCO_RELATION, Boolean.TRUE);
			IElementType[] elementTypes = createRelationshipRequest.getElementType().getAllSuperTypes();
			for (int i = 0; i < elementTypes.length; i++) {
				IElementType iElementType = elementTypes[i];
				if (iElementType instanceof ConfiguredHintedSpecializationElementType) {
					ConfiguredHintedSpecializationElementType configurationImpl = (ConfiguredHintedSpecializationElementType)iElementType;
					if (CommonUtils.isValueable(configurationImpl.getConfiguration().getDescription())){
						String description = configurationImpl.getConfiguration().getDescription();
						Pattern p = Pattern.compile("^\\#\\{(.+)\\}$");
						Matcher m = p.matcher(description);
						if (m.find()) {
							String value = m.group();
							value = value.substring(2, value.length()-1);
							String[] strings = value.split(";");
							for (int j = 0; j < strings.length; j++) {
								String string = strings[j];
								String[] data = string.split("=");
								request.getEditCommandRequest().setParameter(CommandParameters.PREFIX + data[0].trim(), data[1].trim());
							}
						}
					}
				}
			}
		}
		return super.getBeforeEditContextCommand(request);
	}
	
	
	@Override
	public boolean approveRequest(IEditCommandRequest request) {
		if (request instanceof GetEditContextRequest) {
			IEditCommandRequest editCommandRequest = ((GetEditContextRequest)request).getEditCommandRequest();
			if (editCommandRequest instanceof CreateRelationshipRequest) {
				CreateRelationshipRequest createRelationshipRequest = (CreateRelationshipRequest)editCommandRequest;
				if (RelationshipHelper.isAlfrescoRelation(createRelationshipRequest)) {
					return approveCreateAlfrescoRelationRequest(request, createRelationshipRequest);
				}
			}
		}
		return super.approveRequest(request);
	}
	
	protected abstract boolean approveCreateAlfrescoRelationRequest(IEditCommandRequest request, CreateRelationshipRequest createRelationshipRequest);
	
}
