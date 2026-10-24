**Nobody Lib** is a simple library mod made by/for Nobodyasidentity for developing Fabric mods.  
> <font color="orange">⚠ Nobody Lib is still under development and stuff might change</font>

Please use a borderless fullscreen mod like <a href="https://modrinth.com/mod/cubes-without-borders" target="_blank"><b><img src="https://cdn.modrinth.com/data/ETlrkaYF/images/b359ea368791493eb62786ac91a59cee3a5bd7f7.webp" align="center" width=30 alt="CWB icon"> Cubes Without Borders</b></a> or <a href="https://modrinth.com/mod/borderless-mining-revived" target="_blank"><b><img src="https://cdn.modrinth.com/data/kio1kLH4/c0a7bd8ccf8ef9929ccca33224f011778c53f556.gif" align="center" width=30 alt="CWB icon"> Borderless Mining Revived</b></a> to use the overlay feature in fullscreen mode
# Usage
## Including in Gradle project
`build.gradle`:
```gradle
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
	implementation "maven.modrinth:nobodylib:${project.nobodylib_version}"
}
```
`gradle.properties`:
```properties
# Change to whatever is the latest version for your Minecraft version
nobodylib_version=0.0.6+26.2
```
# Dependencies
* <a href="https://modrinth.com/mod/fabric-api" target="_blank">**FabricAPI**</a>