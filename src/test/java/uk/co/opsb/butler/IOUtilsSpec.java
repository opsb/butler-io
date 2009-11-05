package uk.co.opsb.butler;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.opsb.butler.IOUtils.bytesFrom;
import static uk.co.opsb.butler.IOUtils.fileFrom;
import static uk.co.opsb.butler.IOUtils.propertiesFrom;
import static uk.co.opsb.butler.IOUtils.textFrom;
import static uk.co.opsb.butler.IOUtils.utf8From;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class IOUtilsSpec {

	private static final String EXPECTED_FILE_CONTENTS = "some test text";
	private static final String FILE_NAME = "text_file.txt";
	private static final String CLASSPATH_LOCATION = "uk/co/opsb/butler/" + FILE_NAME;
	private static final String VFS_LOCATION = "res:" + CLASSPATH_LOCATION;
	private static final String PROPERTIES_LOCATION = "res:uk/co/opsb/butler/some.properties";
	private static final File FILE = fileFrom(CLASSPATH_LOCATION);

	@Test
	public void shouldReadTextFromVfsLocation() {
		assertThat( textFrom( VFS_LOCATION ), equalTo(EXPECTED_FILE_CONTENTS) );
	}
	
	@Test
	public void shouldReadBytesFromVfsLocation() {
		assertThat( bytesFrom( VFS_LOCATION ), equalTo(EXPECTED_FILE_CONTENTS.getBytes()) );
	}
	
	@Test
	public void shouldReadBytesFromInputStream() {
		InputStream in = new ByteArrayInputStream(EXPECTED_FILE_CONTENTS.getBytes());
		assertThat( bytesFrom( in ), equalTo( EXPECTED_FILE_CONTENTS.getBytes() ));
	}
	
	@Test
	public void shouldReadTextFromInputStream() {
		InputStream in = new ByteArrayInputStream(EXPECTED_FILE_CONTENTS.getBytes());
		assertThat( textFrom(in), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromInputStream() {
		InputStream in = new ByteArrayInputStream(EXPECTED_FILE_CONTENTS.getBytes());
		assertThat( utf8From(in), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromVfsLocation() {
		assertThat( utf8From(VFS_LOCATION), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadFileFromClasspath() {
		assertTrue( "Expected a file", fileFrom(CLASSPATH_LOCATION).isFile());
	}
	
	@Test
	public void shouldReadBytesFromFile() {
		assertThat(bytesFrom(FILE), equalTo(EXPECTED_FILE_CONTENTS.getBytes()));
	}
	
	@Test
	public void shouldReadTextFromFile() {
		assertThat(textFrom(FILE), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromFile() {
		assertThat(utf8From(FILE), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadPropertiesFromVfsLocation() {
		Properties properties = new Properties();
		properties.put("name", "jim");
		properties.put("age", "23");
		properties.put("height", "153cm");
		assertThat(propertiesFrom(PROPERTIES_LOCATION), equalTo(properties));
	}
	
	@Test
	public void shouldReadBytesFromFileInCurrentPackage() {
		assertThat(bytesFrom(FILE_NAME, IOUtilsSpec.class), equalTo(EXPECTED_FILE_CONTENTS.getBytes()));
	}
	
	@Test
	public void shouldReadTextFromFileInCurrentPackage() {
		assertThat(textFrom(FILE_NAME, IOUtilsSpec.class), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromFileInCurrentPackage() {
		assertThat(utf8From(FILE_NAME, IOUtilsSpec.class), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadFileFromCurrentPackage() {
		assertTrue("Expected a file", fileFrom(FILE_NAME, IOUtilsSpec.class) instanceof File);
	}
	
}