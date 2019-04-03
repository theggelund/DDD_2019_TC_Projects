import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.2"

project {

    buildType(CompositeBuild)

    subProject(Project1)
    subProject(Project2)
}

object CompositeBuild : BuildType({
    name = "Composite build"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        showDependenciesChanges = true
    }

    dependencies {
        dependency(AbsoluteId("DddNetBuild_Build")) {
            snapshot {
            }

            artifacts {
                artifactRules = "+:*"
            }
        }
        dependency(Project1_Build) {
            snapshot {
            }

            artifacts {
                artifactRules = "+:*"
            }
        }
        dependency(Project2_Build) {
            snapshot {
            }

            artifacts {
                artifactRules = "+:*"
            }
        }
    }
})


object Project1 : Project({
    name = "Project 1"

    buildType(Project1_Build)
})

object Project1_Build : BuildType({
    name = "Build"

    steps {
        script {
            scriptContent = """
                echo "Running build for project 1" > project1_output.txt
                
                echo "##teamcity[publishArtifacts 'project1_output.txt']"
            """.trimIndent()
        }
    }
})


object Project2 : Project({
    name = "Project 2"

    buildType(Project2_Build)
})

object Project2_Build : BuildType({
    name = "Build"

    steps {
        script {
            scriptContent = """
                echo "Running build for project 2" > project2_output.txt
                
                echo "##teamcity[publishArtifacts 'project2_output.txt']"
            """.trimIndent()
        }
    }
})
