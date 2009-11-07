Butler IO
============

Some of those IO tasks can simply be the height of tedium. Why not ask the butler to take care of it? He's rather clever don't you know, he even understands a thing or two about that [apache VFS](http://commons.apache.org/vfs/ "apache VFS").

Butler can fetch

* byte []
* String
* String in utf8
* InputStream
* File

from

* VFS locations - allows loading from any of; local files, http, https, ftp, sftp, temporary files, zip, jar, tar, gzip, bzip2, res, ram, mime. Take a look at [some examples](http://commons.apache.org/vfs/filesystems.html "VFS examples").
* InputStreams
* Files
* File in same package as a Class

Installation
------------

#### Maven repo

    <repository>
    	<id>opsbreleases</id>
    	<name>opsb-releases</name>
    	<url>http://opsb.co.uk/nexus/content/repositories/releases/</url>
    </repository>

#### Dependency

    <dependency>
      <groupId>uk.co.opsb</groupId>
      <artifactId>butler-io</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>

Usage
-----

First let's call for the butler:

    import static uk.co.opsb.butler.ButlerIO.*;

Now let's put him to task

### Fetching text

    String fromClasspath          = textFrom( "res:articles/steve_jobs.txt" );
    String fromUtf8File           = utf8From( "file:///path/to/steve_jobs.txt" );
    String fromUtf8FileOnWindows  = utf8From( "file:///c:/path/to/steve_jobs.txt" );
    String fromInputStream        = textFrom( inputStream );
    String overHttpsUsingVfs      = textFrom( "https://username:password@domain_name.com/article.txt" );
    String fromFtpZipUsingVfs     = textFrom( "zip:ftp://username:password@domain_name.com/file.txt.zip" );
    String fromFileNextToClass    = textFrom( "name_of_file_in_same_package_as", YourClass.class );

### Fetching bytes

    byte [] fromClasspath          = bytesFrom( "res:articles/steve_jobs.txt" );
    byte [] fromUtf8File           = bytesFrom( "file:///path/to/steve_jobs.txt" );
    byte [] fromUtf8FileOnWindows  = bytesFrom( "file:///c:/path/to/steve_jobs.txt" );
    byte [] fromInputStream        = bytesFrom( inputStream );
    byte [] overHttpsUsingVfs      = bytesFrom( "https://username:password@domain_name.com/article.txt" );
    byte [] fromSftpGzipUsingVfs   = bytesFrom( "gz:sftp://username:password@domain_name.com/file.txt.gz" );   
    byte [] fromFileNextToClass    = bytesFrom( "name_of_file_in_same_package_as", YourClass.class );     
    
### Fetching properties

    Properties fromClasspath          = propertiesFrom( "res:articles/steve_jobs.txt" );
    Properties fromUtf8File           = propertiesFrom( "file:///path/to/steve_jobs.txt" );
    Properties fromUtf8FileOnWindows  = propertiesFrom( "file:///c:/path/to/steve_jobs.txt" );
    Properties fromInputStream        = propertiesFrom( inputStream );
    Properties overHttpsUsingVfs      = propertiesFrom( "https://username:password@domain_name.com/article.txt" );
    Properties fromJarUsingVfs        = propertiesFrom( "jar://username:password@domain_name.com/outer.jar!inner/file.txt" );   
    Properties fromFileNextToClass    = propertiesFrom( "name_of_file_in_same_package_as", YourClass.class );    

### Opening an InputStream

    InputStream fromClasspath          = inputStreamFrom( "res:articles/steve_jobs.txt" );
    InputStream fromUtf8File           = inputStreamFrom( "file:///path/to/steve_jobs.txt" );
    InputStream fromUtf8FileOnWindows  = inputStreamFrom( "file:///c:/path/to/steve_jobs.txt" );
    InputStream overHttpsUsingVfs      = inputStreamFrom( "https://username:password@domain_name.com/article.txt" );
    InputStream fromFtpZipUsingVfs     = inputStreamFrom( "zip:ftp://username:password@domain_name.com/file.txt.zip" );   
    InputStream fromFileNextToClass    = inputStreamFrom( "name_of_file_in_same_package_as", YourClass.class );    

### Getting a reference to a File

    File fromClasspath          = fileFrom( "res:path/to/file" );
    File fromFileNextToClass    = fileFrom( "file_name", YourClass.class );

## VFS Aliases

Perhaps you've gotten used to using classpath:path/to/file with spring? Butler's here to server, simply alias the res: protocol to classpath:

    ButlerIO.alias( "res", "classpath" );
    
Now you can simply do

    String article = textFrom( "classpath:articles/steve_jobs.txt" );
    
In fact the classpath: alias comes preregistered so you don't even need to worry about adding this one.