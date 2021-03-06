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

package se.uu.ub.cora.bookkeeper.linkcollector;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.DataAtomicSpy;
import se.uu.ub.cora.bookkeeper.DataGroupSpy;
import se.uu.ub.cora.bookkeeper.validator.MetadataStorageStub;
import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataElement;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.data.DataRecordLinkProvider;
import se.uu.ub.cora.storage.MetadataStorage;

public class DataRecordLinkCollectorTest {
	private DataRecordLinkCollector linkCollector;
	private MetadataStorage metadataStorage;
	private DataGroupFactorySpy dataGroupFactory;
	private DataAtomicFactorySpy dataAtomicFactory;
	private DataRecordLinkFactorySpy dataRecordLinkFactory;

	@BeforeMethod
	public void setUp() {
		metadataStorage = new MetadataStorageStub();
		dataGroupFactory = new DataGroupFactorySpy();
		DataGroupProvider.setDataGroupFactory(dataGroupFactory);
		dataRecordLinkFactory = new DataRecordLinkFactorySpy();
		DataRecordLinkProvider.setDataRecordLinkFactory(dataRecordLinkFactory);
		dataAtomicFactory = new DataAtomicFactorySpy();
		DataAtomicProvider.setDataAtomicFactory(dataAtomicFactory);
		linkCollector = new DataRecordLinkCollectorImp(metadataStorage);
	}

	@Test
	public void testGetMetadataStorage() {
		DataRecordLinkCollectorImp collectorImp = (DataRecordLinkCollectorImp) linkCollector;
		assertSame(collectorImp.getMetadataStorage(), metadataStorage);
	}

	@Test
	public void testCollectLinksGroupWithoutLink() {
		DataGroup dataGroup = new DataGroupSpy("bush");
		DataGroup collectedLinks = linkCollector.collectLinks("bush", dataGroup, "recordType",
				"recordId");
		assertEquals(collectedLinks.getNameInData(), "collectedDataLinks");
		assertTrue(collectedLinks.getChildren().isEmpty());

		List<String> namesOfGroupsFactored = dataGroupFactory.usedNameInDatas;

		assertEquals(namesOfGroupsFactored.size(), 1);
		assertEquals(namesOfGroupsFactored.get(0), "collectedDataLinks");
	}

	@Test
	public void testCollectLinksGroupWithOneLink() {
		DataGroup dataGroup = new DataGroupSpy("bush");
		DataGroup dataTestLink = new DataGroupSpy("testLink");

		DataAtomic linkedRecordType = new DataAtomicSpy("linkedRecordType", "bush");
		dataTestLink.addChild(linkedRecordType);

		DataAtomic linkedRecordId = new DataAtomicSpy("linkedRecordId", "bush1");
		dataTestLink.addChild(linkedRecordId);
		dataGroup.addChild(dataTestLink);

		DataGroup collectedLinks = linkCollector.collectLinks("bush", dataGroup, "fromRecordType",
				"fromRecordId");
		List<DataElement> linkList = collectedLinks.getChildren();
		assertEquals(linkList.size(), 1);

		assertCorrectFactoredGroupsAndAtomics();

		assertEquals(collectedLinks.getNameInData(), "collectedDataLinks");

	}

	private void assertCorrectFactoredGroupsAndAtomics() {
		List<String> namesOfGroupsFactored = dataGroupFactory.usedNameInDatas;
		assertEquals(namesOfGroupsFactored.size(), 3);

		assertEquals(namesOfGroupsFactored.get(0), "linkedPath");
		assertEquals(namesOfGroupsFactored.get(1), "recordToRecordLink");
		assertEquals(namesOfGroupsFactored.get(2), "collectedDataLinks");

		List<String> namesOfRecordLinksFactored = dataRecordLinkFactory.usedNameInDatas;
		assertEquals(namesOfRecordLinksFactored.size(), 2);
		assertEquals(namesOfRecordLinksFactored.get(0), "from");
		assertEquals(namesOfRecordLinksFactored.get(1), "to");

		assertEquals(dataAtomicFactory.usedNameInDatas.size(), 5);
		assertEquals(dataAtomicFactory.usedValues.size(), 5);

		assertCorrectAtomicDataUsingIndexNameInDataAndValue(0, "nameInData", "testLink");
		assertCorrectAtomicDataUsingIndexNameInDataAndValue(1, "linkedRecordType",
				"fromRecordType");
		assertCorrectAtomicDataUsingIndexNameInDataAndValue(2, "linkedRecordId", "fromRecordId");
		assertCorrectAtomicDataUsingIndexNameInDataAndValue(3, "linkedRecordType", "bush");
		assertCorrectAtomicDataUsingIndexNameInDataAndValue(4, "linkedRecordId", "bush1");
	}

	private void assertCorrectAtomicDataUsingIndexNameInDataAndValue(int index, String nameInData,
			String value) {
		List<String> namesOfAtomicDataFactored = dataAtomicFactory.usedNameInDatas;
		List<String> valuesOfAtomicDataFactored = dataAtomicFactory.usedValues;
		assertEquals(namesOfAtomicDataFactored.get(index), nameInData);
		assertEquals(valuesOfAtomicDataFactored.get(index), value);

	}
}
