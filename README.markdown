Installation
------------

### Maven repo

    <repository>
    	<id>opsbreleases</id>
    	<name>opsb-releases</name>
    	<url>http://opsb.co.uk/nexus/content/repositories/releases/</url>
    </repository>

### Dependency

    <dependency>
      <groupId>uk.co.opsb</groupId>
      <artifactId>butler-io</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>

### Introduction

Some of those IO tasks can simply be the height of tedium. Why not ask the butler to take care of it? He's rather clever don't you know, he even understands a thing or two about that apache VFS.

Let's put him through his paces. First let's call for the butler:

    import static uk.co.opsb.butler.ButlerIO.*;

### Fetching text
    
#### From the classpath    
    
    String article = textFrom("res:articles/steve_jobs.txt");
    
#### UTF8 from an absolute file path
    
    String article_utf8 = utf8From("file:///path/to/steve_jobs.txt");
    
or on windows

    String article_utf8 = utf8From("file:///c:/path/to/steve_jobs.txt");
    
#### Over the wire    

    String article = textFrom("https://username:password@domain_name.com/article.txt");
    
#### From a zip

    String article = textFrom("zip:https://username:password@domain_name.com/article.txt.zip");
  
#### From an InputStream

    String article textFrom(inputStream);
  
The full list of VFS protocols includes local files, http, https, ftp, sftp, temporary files, zip, jar, tar, gzip, bzip2, res, ram, mime. Take a look at [some examples](http://commons.apache.org/vfs/filesystems.html "VFS examples").

### Fetching bytes

    byte [] articleBytes = bytesFrom("res:articles/steve_jobs.txt");

### Fetching properties

    Properties config = propertiesFrom("res:config.props");

### Aliases

Perhaps you've gotten used to using classpath:path/to/file with spring? Why not add an alias the res: protocol that VFS uses to classpath:

    ButlerIO.alias("res", "classpath");
    
Now you can simply do

    String article = textFrom("classpath:articles/steve_jobs.txt");
    
In fact the classpath: alias comes preregistered so you don't even need to worry about adding this one.