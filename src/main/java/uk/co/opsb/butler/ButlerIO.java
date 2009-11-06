package uk.co.opsb.butler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

public class ButlerIO {

	private static final String PROTOCOL_REGEX = "^(.*):";

	private static final Pattern PROTOCOL_MATCHER = Pattern.compile(PROTOCOL_REGEX);

	public static final int BUFFER_SIZE = 4096;
	
	private static FileSystemManager fileSystemManager;

	private static Map<String, String> aliases = new HashMap<String, String>();
	static {
		alias("res", "classpath");
	}
	
	private static FileSystemManager getFsManager() {
		try {
			if (fileSystemManager == null) {
				fileSystemManager = VFS.getManager();
			}
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
		return fileSystemManager;
	}
	
	private static FileObject resolveFile(String vfsLocation) {
		
		try {
			return getFsManager().resolveFile(withResolvedAlias(vfsLocation));
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String withResolvedAlias(String vfsLocation) {
		String protocol = protocolUsedIn(vfsLocation);
		
		if (aliases.containsKey(protocol)) {
			String original = aliases.get(protocol);
			vfsLocation = vfsLocation.replaceFirst(PROTOCOL_REGEX, original + ":");
		}
		 
		return vfsLocation;
	}

	private static String protocolUsedIn(String vfsLocation) {
		Matcher matcher = PROTOCOL_MATCHER.matcher(vfsLocation);
		if (!matcher.find()) throw new IllegalArgumentException("No protocol found");
		String protocol = matcher.group(1);
		return protocol;
	}

	private static FileContent getFileContent(String vfsLocation) {
		try {
			return resolveFile(vfsLocation).getContent();
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static String textFrom(InputStream inputStream) {
		return new String(bytesFrom(inputStream));
	}
	
	public static String textFrom(String vfsLocation) {
		return new String(bytesFrom(vfsLocation));
	}
	
	public static String textFrom(File file) {
		return new String(bytesFrom(file));
	}
	
	public static String textFrom(String name, Class<? extends Object> classInSamePackage) {
		return textFrom(classInSamePackage.getResourceAsStream(name));
	}
	
	public static String utf8From(File file) {
		try {
			return new String(bytesFrom(file), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String utf8From(InputStream inputStream) {
		try {
			return new String(bytesFrom(inputStream), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String utf8From(String vfsLocation) {
		try {
			return new String(bytesFrom(vfsLocation), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String utf8From(String name, Class<? extends Object> classInSamePackage) {
		return utf8From(classInSamePackage.getResourceAsStream(name));
	}
	
	public static byte [] bytesFrom(InputStream inputStream) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		try {
			copy(inputStream, out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte [] bytesFrom(String vfsLocation) {
		return bytesFrom( inputStreamFrom( vfsLocation ) );
	}
	
	public static byte [] bytesFrom(File file) {
		try {
			return bytesFrom( new FileInputStream( file ) );
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte [] bytesFrom(String name, Class<? extends Object> classInSamePackage) {
		return bytesFrom(classInSamePackage.getResourceAsStream(name));
	}
	
	public static Properties propertiesFrom(String vfsLocation) {
		Properties properties = new Properties();
		try {
			properties.load(inputStreamFrom(vfsLocation));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return properties;
	}
	
	public static InputStream inputStreamFrom(String vfsLocation) {
		try {
			return getFileContent(vfsLocation).getInputStream();
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File fileFrom(String classpathLocation) {
		try {
			return new File(resolveFile("res:" + classpathLocation).getURL().getFile());
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File fileFrom(String name, Class<? extends Object> classInSamePackage) {
		return new File(classInSamePackage.getResource(name).getFile());
	}
	
	private static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		}
		finally {
			try {
				in.close();
			}
			catch (IOException ex) {
			}
			try {
				out.close();
			}
			catch (IOException ex) {
			}
		}
	}

	public static void alias(String original, String alias) {
		aliases.put(alias, original);
	}	
	
}
