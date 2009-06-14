package uk.co.opsb.pmc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

public class IOUtils {

	public static final int BUFFER_SIZE = 4096;
	
	private static FileSystemManager getFsManager() {
		try {
			return VFS.getManager();
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static FileObject resolveFile(String location) {
		try {
			return getFsManager().resolveFile(location);
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static FileContent getFileContent(String location) {
		try {
			return resolveFile(location).getContent();
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static String textFrom(String location) {
		return new String(bytesFrom(location));
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
	
	public static byte [] bytesFrom(String location) {
		return bytesFrom( inputStreamFrom( location ) );
	}
	
	public static InputStream inputStreamFrom(String location) {
		try {
			return getFileContent(location).getInputStream();
		} catch (FileSystemException e) {
			throw new RuntimeException(e);
		}
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
	
}
