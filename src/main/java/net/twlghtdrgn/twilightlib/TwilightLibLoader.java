package net.twlghtdrgn.twilightlib;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class TwilightLibLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver mvn = new MavenLibraryResolver();

        mvn.addDependency(new Dependency(new DefaultArtifact("org.xerial:sqlite-jdbc:3.41.2.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.0.1"), null));
        mvn.addDependency(new Dependency(new DefaultArtifact("org.mariadb.jdbc:mariadb-java-client:3.1.2"), null));
        mvn.addRepository(new RemoteRepository.Builder("maven central","default","https://repo1.maven.org/maven2/").build());

        classpathBuilder.addLibrary(mvn);
    }
}
