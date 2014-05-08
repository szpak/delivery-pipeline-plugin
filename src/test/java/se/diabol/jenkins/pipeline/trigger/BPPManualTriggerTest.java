/*
This file is part of Delivery Pipeline Plugin.

Delivery Pipeline Plugin is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Delivery Pipeline Plugin is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Delivery Pipeline Plugin.
If not, see <http://www.gnu.org/licenses/>.
*/
package se.diabol.jenkins.pipeline.trigger;

import au.com.centrumsystems.hudson.plugin.buildpipeline.trigger.BuildPipelineTrigger;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.MockFolder;

import static org.junit.Assert.assertNotNull;

public class BPPManualTriggerTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void triggerManualWithFolders() throws Exception {
        BPPManualTrigger trigger = new BPPManualTrigger();
        MockFolder folder = jenkins.createFolder("folder");
        FreeStyleProject a = folder.createProject(FreeStyleProject.class, "a");
        FreeStyleProject b = folder.createProject(FreeStyleProject.class, "b");

        a.getPublishersList().add(new BuildPipelineTrigger("b", null));
        jenkins.getInstance().rebuildDependencyGraph();
        jenkins.setQuietPeriod(0);

        jenkins.buildAndAssertSuccess(a);
        assertNotNull(a.getLastBuild());

        trigger.triggerManual(b, a, "1", folder);

        jenkins.waitUntilNoActivity();
        assertNotNull(b.getLastBuild());

    }

}
