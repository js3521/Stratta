package org.stratta.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public abstract class DataModel {
	public static List<DataModel> getDataModels() {
		List<DataModel> dataModels = new ArrayList<DataModel>();
		ServiceLoader<DataModel> loader = ServiceLoader.load(DataModel.class);
		
		for(DataModel model : loader)
			dataModels.add(model);
		
		return dataModels;
	}
	
	public abstract String getName();
}
