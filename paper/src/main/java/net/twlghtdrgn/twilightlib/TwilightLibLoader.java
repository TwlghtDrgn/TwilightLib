package net.twlghtdrgn.twilightlib;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

/**
 * @see PluginLoader
 */
@SuppressWarnings("UnstableApiUsage")
public class TwilightLibLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver mvn = new MavenLibraryResolver();

        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.CONFIGURATE.getGradleCoords()), null));
        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.JEDIS.getGradleCoords()), null));

        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Provider.H2.getArtifactId()), null));
        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Provider.POSTGRESQL.getArtifactId()), null));
        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Provider.MARIADB.getArtifactId()), null));

        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Helper.HIKARICP.getGradleCoords()), null));
        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Helper.ORMLITE.getGradleCoords()), null));

        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Helper.Flyway.CORE.getGradleCoords()), null));
        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Helper.Flyway.POSTGRESQL.getGradleCoords()), null));
        mvn.addDependency(new Dependency(new DefaultArtifact(MavenDependency.Database.Helper.Flyway.MYSQL.getGradleCoords()), null));

        mvn.addRepository((new RemoteRepository.Builder("twlghtdrgn-repo", "default", "https://maven.twlghtdrgn.net/repository/public/")).build());
        mvn.addRepository(new RemoteRepository.Builder("maven central","default","https://repo1.maven.org/maven2/").build());

        classpathBuilder.addLibrary(mvn);
    }
}
