package uk.co.opsb.butler;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.opsb.butler.ButlerIO.bytesFrom;
import static uk.co.opsb.butler.ButlerIO.fileAt;
import static uk.co.opsb.butler.ButlerIO.propertiesFrom;
import static uk.co.opsb.butler.ButlerIO.textFrom;
import static uk.co.opsb.butler.ButlerIO.utf8From;
import static uk.co.opsb.butler.ButlerIO.write;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.junit.Test;

public class ButlerIOSpec {

	private static final String EXPECTED_FILE_CONTENTS = "some test text";
	private static final String FILE_NAME = "text_file.txt";
	private static final String CLASSPATH_LOCATION = "uk/co/opsb/butler/" + FILE_NAME;
	private static final String VFS_LOCATION = "res:" + CLASSPATH_LOCATION;
	private static final String PROPERTIES_LOCATION = "res:uk/co/opsb/butler/some.properties";
	private static final File FILE = fileAt(CLASSPATH_LOCATION);

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
		assertTrue( "Expected a file", fileAt(CLASSPATH_LOCATION).isFile());
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
		assertThat(bytesFrom(FILE_NAME, ButlerIOSpec.class), equalTo(EXPECTED_FILE_CONTENTS.getBytes()));
	}
	
	@Test
	public void shouldReadTextFromFileInCurrentPackage() {
		assertThat(textFrom(FILE_NAME, ButlerIOSpec.class), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadUtf8FromFileInCurrentPackage() {
		assertThat(utf8From(FILE_NAME, ButlerIOSpec.class), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldReadFileFromCurrentPackage() {
		assertTrue("Expected a file", fileAt(FILE_NAME, ButlerIOSpec.class) instanceof File);
	}
	
	@Test
	public void shouldResolveRuleBasedAliasForLocation() {
		assertThat(utf8From("butler:" + FILE_NAME), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldResolveAliasesFromButlerProperties() {
		assertThat(utf8From("reports:" + FILE_NAME), equalTo(EXPECTED_FILE_CONTENTS));
	}
	
	@Test
	public void shouldWriteTextToVfsLocation() {
		
		String text = "bring me my slippers";
		String vfsLocation = "tmp://message.txt";
		
		write(text, vfsLocation);
		
		assertThat(textFrom(vfsLocation), equalTo(text));
	}
	
	@Test
	public void shouldWriteBytesToVfsLocation() {
		
		byte [] bytes = "bring me my slippers".getBytes();
		String vfsLocation = "tmp://message.txt";
		
		write(bytes, vfsLocation);
		
		assertThat(bytesFrom(vfsLocation), equalTo(bytes));
	}
	
	@Test
	public void shouldWriteTextToOutputStream() throws FileNotFoundException {
		
		String text = "bring me my slippers";
		OutputStream out = new FileOutputStream("/tmp/text_out_test");
		
		write(text, out);
		
		assertThat(textFrom("file:///tmp/text_out_test"), equalTo(text));
		
	}
	
}