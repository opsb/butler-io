package uk.co.opsb.pmc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static uk.co.opsb.pmc.IOUtils.bytesFrom;
import static uk.co.opsb.pmc.IOUtils.textFrom;

import org.junit.Test;

public class IOUtilsSpec {

	private static final String FILE_CONTENTS = "some test text";
	private static final String FILE_LOCATION = "res:uk/co/opsb/pmc/text_file.txt";
	
	@Test
	public void shouldReadTextFromVfsLocation() {
		assertThat( textFrom( FILE_LOCATION ), equalTo(FILE_CONTENTS) );
	}
	
	@Test
	public void shouldReadBytesFromVfsLocation() {
		assertThat( bytesFrom( FILE_LOCATION ), equalTo(FILE_CONTENTS.getBytes()) );
	}
	
}