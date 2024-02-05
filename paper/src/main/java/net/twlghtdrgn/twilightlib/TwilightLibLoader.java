package net.twlghtdrgn.twilightlib;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

/**
 * Loader class that downloads all libraries required for internal use
 */
@SuppressWarnings("UnstableApiUsage")
public class TwilightLibLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver mvn = new MavenLibraryResolver();

        mvn.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.0.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("org.mariadb.jdbc:mariadb-java-client:3.1.2"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("com.h2database:h2:2.2.220"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("redis.clients:jedis:5.0.2"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("com.j256.ormlite:ormlite-jdbc:6.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("javax.persistence:javax.persistence-api:2.2"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("org.flywaydb:flyway-core:10.4.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("org.flywaydb:flyway-mysql:10.4.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("org.flywaydb:flyway-database-postgresql:10.4.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("org.postgresql:postgresql:42.7.1"), null));
        mvn.addRepository(new RemoteRepository.Builder("maven central","default","https://repo1.maven.org/maven2/").build());

        classpathBuilder.addLibrary(mvn);
    }
}
