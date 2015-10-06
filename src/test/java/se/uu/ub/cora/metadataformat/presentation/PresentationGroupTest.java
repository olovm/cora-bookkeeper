package se.uu.ub.cora.metadataformat.presentation;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metadataformat.presentation.PresentationChildReference;
import se.uu.ub.cora.metadataformat.presentation.PresentationElement;
import se.uu.ub.cora.metadataformat.presentation.PresentationGroup;
import se.uu.ub.cora.metadataformat.presentation.PresentationTextReference;

public class PresentationGroupTest {
	private PresentationGroup presentationGroup;

	@BeforeMethod
	public void beforeMethod() {
		String id = "presentationGroupId";
		String refGroupId = "presentationRefGroupId";
		presentationGroup = new PresentationGroup(id, refGroupId);
	}

	@Test
	public void testInit() {
		assertEquals(presentationGroup.getId(), "presentationGroupId");
		assertEquals(presentationGroup.getRefGroupId(), "presentationRefGroupId");
	}

	@Test
	public void testAddChild() {
		PresentationChildReference textRef = new PresentationTextReference("textRef");
		presentationGroup.addChild(textRef);
		List<PresentationChildReference> childReferences = presentationGroup.getChildReferences();
		PresentationChildReference childReference = childReferences.iterator().next();
		assertEquals(childReference.getReferenceId(), "textRef");
	}

	@Test
	public void testPresentationElement() {
		PresentationElement presentationElement = presentationGroup;
		assertEquals(presentationElement.getId(), "presentationGroupId");
	}
}