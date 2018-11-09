package ru.neodoc.content.utils.uml.profile.dataconverter;

import org.jgrapht.graph.DefaultEdge;

public class DataConverterEdge extends DefaultEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7917773815148140763L;

	protected DataConverter<?, ?> dataConverter;
	
	public DataConverterEdge(DataConverter<?, ?> dataConverter) {
		super();
		setDataConverter(dataConverter);
	}

	public DataConverter<?, ?> getDataConverter() {
		return dataConverter;
	}

	private void setDataConverter(DataConverter<?, ?> dataConverter) {
		this.dataConverter = dataConverter;
	}
	
}
