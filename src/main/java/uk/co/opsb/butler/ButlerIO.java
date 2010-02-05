package uk.co.opsb.butler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

public class ButlerIO {

	private static Log logger = LogFactory.getLog(ButlerIO.class);
	
	public static final int BUFFER_SIZE = 4096;
	
	private static FileSystemManager fileSystemManager;

	private static Map<String, String> aliases = new HashMap<String, String>();
	
	static {
		loadAliases();
	}
	
	private static void loadAliases() {
		Properties properties = new Properties() ;
		
		try {
			URL url =  resolveFile("res:butler_aliases.properties", false).getURL();
			properties.load(new FileInputStream(new File(url.getFile())));
			for(Map.Entry<Object, Object> entry : properties.entrySet()) {
				alias(entry.getKey().toString(), entry.getValue().toString());
			}
		} catch (Exception e) {
			logger.debug("No butler properties provided");
		}
	}
	
	private static FileSystemManager getFsManager() {
		try {
			if (fileSystemManager == null) {
				fileSystemManager = VFS.getManager();
			}
		} catch (FileSystemException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
		return fileSystemManager;
	}
	
	private static FileObject resolveFile(String vfsLocation) {
		return resolveFile(vfsLocation, true);
	}
	
	private static FileObject resolveFile(String vfsLocation, boolean resolveAliases) {
		
		try {
			return getFsManager().resolveFile(resolveAliases? withResolvedAlias(vfsLocation) : vfsLocation);
		} catch (FileSystemException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	private static String withResolvedAlias(String vfsLocation) {

		if (notStandardProtocol(vfsLocation)) {
			for (String alias : aliases.keySet()) {
				Matcher matcher = Pattern.compile(alias).matcher(vfsLocation);
				if (matcher.find()) {
		
					Object [] groups = new String[matcher.groupCount()];
					for(int i = 0; i < matcher.groupCount(); i++) {
						groups[i] = matcher.group(i + 1);
					}
					
					String replacement = String.format(aliases.get(alias), groups);
					String withAliasResolved = vfsLocation.replaceFirst(alias, replacement);
					
					return withAliasResolved;
				
				}
			
			}
			
		}
		
		return vfsLocation;
	}
	
	private static boolean notStandardProtocol(String vfsLocation) {
		
		for(String scheme : getFsManager().getSchemes()) {
			if (vfsLocation.startsWith(scheme + ":")) {
				return false;
			}
		}
		
		return true;
		
	}

	private static FileContent getFileContent(String vfsLocation) {
		try {
			return resolveFile(vfsLocation, true).getContent();
		} catch (FileSystemException e) {
			throw new uk.co.opsb.butler.IOException(e);
		} 
	}
	
	/**
	 * 
	 * @param inputStream The InputStream containing the text
	 * @return A String containing the text from the InputStream
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String textFrom(InputStream inputStream) {
		return new String(bytesFrom(inputStream));
	}
	
	/**
	 * 
	 * @param vfsLocation The VFS location containing the text
	 * @return A String containing the text
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String textFrom(String vfsLocation) {
		return new String(bytesFrom(vfsLocation));
	}
	
	/**
	 * 
	 * @param file The file containing the text
	 * @return A String containing the text
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String textFrom(File file) {
		return new String(bytesFrom(file));
	}
	
	/**
	 * 
	 * @param name Name of file containing text
	 * @param classInSamePackage Class in same package as file containing text
	 * @return A String containing the text
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String textFrom(String name, Class<? extends Object> classInSamePackage) {
		return textFrom(classInSamePackage.getResourceAsStream(name));
	}
	
	/**
	 * 
	 * @param file File containing the text
	 * @return A utf8 encoded String
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String utf8From(File file) {
		try {
			return new String(bytesFrom(file), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param inputStream The InputStream containing the text
	 * @return A utf8 encoded String
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String utf8From(InputStream inputStream) {
		try {
			return new String(bytesFrom(inputStream), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param vfsLocation The VFS location containing the text
	 * @return A utf8 encoded String
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String utf8From(String vfsLocation) {
		try {
			return new String(bytesFrom(vfsLocation), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param name Name of file containing text
	 * @param classInSamePackage Class in same package as file containing text
	 * @return A utf8 encoded String
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static String utf8From(String name, Class<? extends Object> classInSamePackage) {
		return utf8From(classInSamePackage.getResourceAsStream(name));
	}
	
	/**
	 * 
	 * @param inputStream InputStream to read from
	 * @return byte array containing contents of InputStream
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static byte [] bytesFrom(InputStream inputStream) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		try {
			copy(inputStream, out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param vfsLocation The VFS location containing the text
	 * @return byte array containing contents of InputStream
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static byte [] bytesFrom(String vfsLocation) {
		return bytesFrom( inputStreamFrom( vfsLocation ) );
	}
	
	/**
	 * 
	 * @param file File containing the bytes
	 * @return byte array containing contents of InputStream
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static byte [] bytesFrom(File file) {
		try {
			return bytesFrom( new FileInputStream( file ) );
		} catch (FileNotFoundException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param name Name of file containing text
	 * @param classInSamePackage Class in same package as file containing text
	 * @return byte array containing contents of InputStream
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static byte [] bytesFrom(String name, Class<? extends Object> classInSamePackage) {
		return bytesFrom(classInSamePackage.getResourceAsStream(name));
	}
	
	/**
	 * 
	 * @param vfsLocation VFS location containing the properties
	 * @return Properties from vfsLocation
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static Properties propertiesFrom(String vfsLocation) {
		return propertiesFrom(inputStreamFrom(vfsLocation));
	}
	
	/**
	 * 
	 * @param file File containing properties
	 * @return Properties from File
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static Properties propertiesFrom(File file) {
		return propertiesFrom(inputStreamFrom(file));
	}
	
	/**
	 * 
	 * @param inputStream InputStream to read from
	 * @return Properties from InputStream
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static Properties propertiesFrom(InputStream inputStream) {
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
		return properties;
	}
	
	/**
	 * 
	 * @param vfsLocation VFS location to read from
	 * @return InputStream for VFS location
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static InputStream inputStreamFrom(String vfsLocation) {
		try {
			return getFileContent(vfsLocation).getInputStream();
		} catch (FileSystemException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param file File to read from
	 * @return InputStream for File
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static InputStream inputStreamFrom(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param classpathLocation Location in classpath
	 * @return File
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static File fileAt(String classpathLocation) {
		try {
			return new File(resolveFile("res:" + classpathLocation, true).getURL().getFile());
		} catch (FileSystemException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param name Name of file to read from
	 * @param classInSamePackage Class in same package as file to read from
	 * @return File
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static File fileAt(String name, Class<? extends Object> classInSamePackage) {
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
			catch (IOException e) {
				throw new uk.co.opsb.butler.IOException(e);
			}
			try {
				out.close();
			}
			catch (IOException e) {
				throw new uk.co.opsb.butler.IOException(e);
			}
		}
	}

	private static void alias(String original, String replacement) {
		aliases.put(original, replacement);
	}	

	static void clearAliases() {
		aliases.clear();
	}
	
	/**
	 * 
	 * @param bytes byte array to write
	 * @param vfsLocation VFS location to write to
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static void write(byte [] bytes, String vfsLocation) {
		try {
			write(bytes, resolveFile(vfsLocation).getContent().getOutputStream());
		} catch (FileSystemException e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
	/**
	 * 
	 * @param text Text to write
	 * @param vfsLocation VFS location to write to
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static void write(String text, String vfsLocation) {
		write(text.getBytes(), vfsLocation);
	}
	
	/**
	 * 
	 * @param text Text to write
	 * @param out OutputStream to write to
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static void write(String text, OutputStream out) {
		write(text.getBytes(), out);
	}
	
	/**
	 * 
	 * @param bytes byte array to write
	 * @param out OutputStream to write to
	 * @throws uk.co.opsb.butler.IOException - If an I/O error occurs
	 */
	public static void write(byte [] bytes, OutputStream out) {
		try {
			out.write(bytes);
			out.close();
		} catch (Exception e) {
			throw new uk.co.opsb.butler.IOException(e);
		}
	}
	
}
