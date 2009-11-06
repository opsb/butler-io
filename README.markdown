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

Let's put him through his paces.

#### Fetch me the text from the steve_jobs.txt file in the classpath

    ...
    import uk.co.opsb.butler.ButlerIO;

    class ArticleSpec {
  
      @Test
      public void shouldHaveSummary() {
        Article article = ArticleMocks.steveJobsCeoOfTheDecade();
        assertThat( article.getBody(), equalTo( textFrom( "classpath:articles/steve_jobs.txt" ) ) );
      }
  
    }