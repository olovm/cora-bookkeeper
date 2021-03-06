/*
 * Copyright 2015, 2019 Uppsala University Library
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

package se.uu.ub.cora.bookkeeper.metadata.converter;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.DataAtomicSpy;
import se.uu.ub.cora.bookkeeper.DataGroupSpy;
import se.uu.ub.cora.bookkeeper.metadata.TextVariable;
import se.uu.ub.cora.data.DataGroup;

public class DataGroupToTextVariableConverterTest {
	@BeforeMethod
	public void setUp() {

	}

	@Test
	public void testToMetadata() {
		DataGroup dataGroup = createDataGroup();

		DataGroupToTextVariableConverter converter = DataGroupToTextVariableConverter
				.fromDataGroup(dataGroup);
		TextVariable textVariable = converter.toMetadata();

		assertBasicTextVariableValuesAreCorrect(textVariable);
		assertEquals(textVariable.getRefParentId(), null);
		assertEquals(textVariable.getFinalValue(), null);
	}

	private void assertBasicTextVariableValuesAreCorrect(TextVariable textVariable) {
		assertEquals(textVariable.getId(), "otherId");
		assertEquals(textVariable.getNameInData(), "other");
		assertEquals(textVariable.getTextId(), "otherTextId");
		assertEquals(textVariable.getDefTextId(), "otherDefTextId");
		assertEquals(textVariable.getRegularExpression(),
				"((^(([0-1][0-9])|([2][0-3])):[0-5][0-9]$|^$){1}");
	}

	private DataGroup createDataGroup() {
		DataGroup dataGroup = new DataGroupSpy("metadata");
		dataGroup.addAttributeByIdWithValue("type", "textVar");
		dataGroup.addChild(new DataAtomicSpy("nameInData", "other"));

		DataGroup recordInfo = new DataGroupSpy("recordInfo");
		recordInfo.addChild(new DataAtomicSpy("id", "otherId"));
		dataGroup.addChild(recordInfo);

		dataGroup.addChild(new DataAtomicSpy("nameInData", "other"));

		addTextByNameInDataAndId(dataGroup, "textId", "otherTextId");
		addTextByNameInDataAndId(dataGroup, "defTextId", "otherDefTextId");

		dataGroup.addChild(
				new DataAtomicSpy("regEx", "((^(([0-1][0-9])|([2][0-3])):[0-5][0-9]$|^$){1}"));
		return dataGroup;
	}

	private void addTextByNameInDataAndId(DataGroup dataGroup, String nameInData, String textId) {
		DataGroup text = new DataGroupSpy(nameInData);
		text.addChild(new DataAtomicSpy("linkedRecordType", "textSystemOne"));
		text.addChild(new DataAtomicSpy("linkedRecordId", textId));
		dataGroup.addChild(text);
	}

	@Test
	public void testToMetadataWithRefParentId() {
		DataGroup dataGroup = createDataGroup();
		DataGroup refParentId = new DataGroupSpy("refParentId");
		refParentId.addChild(new DataAtomicSpy("linkedRecordType", "metadataTextVariable"));
		refParentId.addChild(new DataAtomicSpy("linkedRecordId", "refParentId"));
		dataGroup.addChild(refParentId);

		DataGroupToTextVariableConverter converter = DataGroupToTextVariableConverter
				.fromDataGroup(dataGroup);
		TextVariable textVariable = converter.toMetadata();

		assertBasicTextVariableValuesAreCorrect(textVariable);
		assertEquals(textVariable.getRefParentId(), "refParentId");
	}

	@Test
	public void testToMetadataWithFinalValue() {
		DataGroup dataGroup = createDataGroup();
		dataGroup.addChild(new DataAtomicSpy("finalValue", "finalValue"));

		DataGroupToTextVariableConverter converter = DataGroupToTextVariableConverter
				.fromDataGroup(dataGroup);
		TextVariable textVariable = converter.toMetadata();

		assertBasicTextVariableValuesAreCorrect(textVariable);
		assertEquals(textVariable.getFinalValue(), "finalValue");
	}
}
