package uk.co.opsb.pmc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.opsb.pmc.IOUtils.*;
import static uk.co.opsb.pmc.IOUtils.textFrom;
import static uk.co.opsb.pmc.IOUtils.utf8From;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilsSpec {

	private static final String FILE_CONTENTS = "some test text";
	private static final String CLASSPATH_LOCATION = "uk/co/opsb/pmc/text_file.txt";
	private static final String FILE_LOCATION = "res:" + CLASSPATH_LOCATION;
	
	@Test
	public void shouldReadTextFromVfsLocation() {
		assertThat( textFrom( FILE_LOCATION ), equalTo(FILE_CONTENTS) );
	}
	
	@Test
	public void shouldReadBytesFromVfsLocation() {
		assertThat( bytesFrom( FILE_LOCATION ), equalTo(FILE_CONTENTS.getBytes()) );
	}
	
	@Test
	public void shouldReadBytesFromInputStream() {
		InputStream in = new ByteArrayInputStream(FILE_CONTENTS.getBytes());
		assertThat( bytesFrom( in ), equalTo( FILE_CONTENTS.getBytes() ));
	}
	
	@Test
	public void shouldReadTextFromInputStream() {
		InputStream in = new ByteArrayInputStream(FILE_CONTENTS.getBytes());
		assertThat( textFrom(in), equalTo(FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromInputStream() {
		InputStream in = new ByteArrayInputStream(FILE_CONTENTS.getBytes());
		assertThat( utf8From(in), equalTo(FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromVfsLocation() {
		assertThat( utf8From(FILE_LOCATION), equalTo(FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadFileFromClasspath() {
		assertTrue( "Expected a file", fileFrom(CLASSPATH_LOCATION) instanceof File);
	}
	
}