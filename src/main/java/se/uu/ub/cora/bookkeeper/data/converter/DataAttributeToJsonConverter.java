/*
 * Copyright 2015 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.bookkeeper.data.converter;

import se.uu.ub.cora.bookkeeper.data.DataAttribute;
import se.uu.ub.cora.bookkeeper.data.DataPart;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.JsonObjectBuilder;

public final class DataAttributeToJsonConverter extends DataToJsonConverter {
	private JsonBuilderFactory factory;

	public static DataToJsonConverter usingJsonFactory(JsonBuilderFactory factory) {
		return new DataAttributeToJsonConverter(factory);
	}

	private DataAttributeToJsonConverter(JsonBuilderFactory factory) {
		this.factory = factory;
	}

	@Override
	JsonObjectBuilder toJsonObjectBuilder(DataPart dataAttributeIn) {
		DataAttribute dataAttribute = (DataAttribute) dataAttributeIn;
		JsonObjectBuilder jsonObjectBuilder = factory.createObjectBuilder();

		jsonObjectBuilder.addKeyString(dataAttribute.getNameInData(), dataAttribute.getValue());
		return jsonObjectBuilder;
	}

}
