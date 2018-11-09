package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.provider.extended;

import org.eclipse.emf.common.notify.Adapter;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.provider.DictionariesItemProviderAdapterFactory;

public class DictionariesItemProviderAdapterFactoryEx extends
		DictionariesItemProviderAdapterFactory {

	protected DictionaryItemProviderEx dictionaryItemProviderEx;
	
	@Override
	public Adapter createDictionaryAdapter() {
		if (dictionaryItemProviderEx == null) {
			dictionaryItemProviderEx = new DictionaryItemProviderEx(this);
		}
		return dictionaryItemProviderEx;
	}

	protected DictionariesItemProviderEx dictionariesItemProviderEx;
	
	@Override
	public Adapter createDictionariesAdapter() {
		if (dictionariesItemProviderEx == null)
			dictionariesItemProviderEx = new DictionariesItemProviderEx(this);
		return dictionariesItemProviderEx;
	}
	
	protected ImportItemProviderEx importItemProviderEx;
	
	@Override
	public Adapter createImportAdapter() {
		if (importItemProviderEx == null)
			importItemProviderEx = new ImportItemProviderEx(this);
		return importItemProviderEx;
	}
	
}
