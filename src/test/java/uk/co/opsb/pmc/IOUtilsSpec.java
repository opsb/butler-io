package uk.co.opsb.pmc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.opsb.pmc.IOUtils.bytesFrom;
import static uk.co.opsb.pmc.IOUtils.fileFrom;
import static uk.co.opsb.pmc.IOUtils.propertiesFrom;
import static uk.co.opsb.pmc.IOUtils.textFrom;
import static uk.co.opsb.pmc.IOUtils.utf8From;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class IOUtilsSpec {

	private static final String FILE_CONTENTS = "some test text";
	private static final String FILE_NAME = "text_file.txt";
	private static final String CLASSPATH_LOCATION = "uk/co/opsb/pmc/" + FILE_NAME;
	private static final String FILE_LOCATION = "res:" + CLASSPATH_LOCATION;
	private static final String PROPERTIES_LOCATION = "res:uk/co/opsb/pmc/some.properties";
	private static final File FILE = fileFrom(CLASSPATH_LOCATION);
	private String text = textFrom(getClass().getResource("text_file.txt").getFile());
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
	
	@Test
	public void shouldReadBytesFromFile() {
		assertThat(bytesFrom(FILE), equalTo(FILE_CONTENTS.getBytes()));
	}
	
	@Test
	public void shouldReadTextFromFile() {
		assertThat(textFrom(FILE), equalTo(FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromFile() {
		assertThat(utf8From(FILE), equalTo(FILE_CONTENTS));
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
		assertThat(bytesFrom(FILE_NAME, IOUtilsSpec.class), equalTo(FILE_CONTENTS.getBytes()));
	}
	
	
	
}