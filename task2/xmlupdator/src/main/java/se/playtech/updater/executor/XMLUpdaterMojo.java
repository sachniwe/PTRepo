package se.playtech.updater.executor;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.AbstractMojo;
import se.playtech.updater.impl.XMLUpdaterImpl;
import se.playtech.updater.util.XMLUpdater;

import java.io.IOException;

/**
 * Obtain the artifact defined by the groupId, artifactId, and version
 * from the remote repository.
 *
 * @goal xml-updater
 */

public class XMLUpdaterMojo extends AbstractMojo{

    /**
     * The target pom's type
     *
     * @parameter expression="${path}"
     * @required
     */
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void execute() throws MojoExecutionException
    {
        XMLUpdater xmlUpdater = new XMLUpdaterImpl(this.getPath(), getLog());
        try {
            xmlUpdater.UpdateXML();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
