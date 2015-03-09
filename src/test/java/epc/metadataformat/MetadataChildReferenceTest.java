package epc.metadataformat;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class MetadataChildReferenceTest {
	@Test
	public void testInit() {
		MetadataChildReference metadataChildReference = MetadataChildReference
				.withReferenceIdAndRepeatMinAndRepeatMax("childReference", 1,
						MetadataChildReference.UNLIMITED);
		assertEquals(metadataChildReference.getReferenceId(), "childReference",
				"ChildReference should be the value set in the constructor");

		assertEquals(metadataChildReference.getRepeatMin(), 1,
				"RepeatMin should be the value set in the constructor");

		assertEquals(metadataChildReference.getRepeatMax(), Integer.MAX_VALUE,
				"RepeatMax should be the value set in the constructor");

	}
}
