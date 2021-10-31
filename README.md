# Command Manager 4 JDA
This is a little project of mine to easily setup Text & Slash Command Handling for JDA.

### Getting Started  
Simply add the Command Manager to your Project
````shell
dependencies {
  implementation("dev.nanabell.jda.command.manager:command-manager:VERSION")
}
````

Constructing Command Manger:
```kotlin
val commands = listOf(
    MyFirstComamnd(),
    MySecondCommand(),
    MyThirdCommand()
)

val provider = StaticCommandProvider(commands)

val builder = CommandManagerBuilder()
builder.setPrefix("->") // Or whatever Prefix you want
builder.setCommandProvider(provider)
...
val manager = builder.build()

jda.addEventListeners(manager)
```

If you are using Micronaut you can add the Micronaut Command Provider Dependency
```shell
dependencies {
  implementation("dev.nanabell.jda.command.manager:micronaut-provider:VERSION")
}
```
For the MicronautCommandProvider to work properly you need to define all of your commands as `@Singleton`s,  
alternatively add the `@Command` Annotation to the Micronaut Annotation Processing
```shell
micronaut {
    ...
    processing {
        incremental(true)
        annotations("my.existing.package.*", "dev.nanabell.jda.command.manager.command.Command")
    }
}
```
Additionally, if you are using Kapt add it to kapt's annotation processing as well
```shell
kapt {
    arguments {
        ...
        arg("micronaut.processing.annotations", "my.existing.package.*,dev.nanabell.jda.command.manager.command.Command")
        ...
    }
}
```

and register the Micronaut Provider like this:
```kotlin
val manager = CommandManagerBuilder()
    .setMicronautProvider()
    ...
    .build()
```
This will automatically 

## Missing Features
- Predicates (Like Required Permissions etc.)
- Bunch of stuff in the Command Contexts
### Text Commands
- Allow Mention to be used as Prefix
- Add QuotationMarks to allow Spaces in Arguments
### Slash Commands
- Registering Slash Commands with discord
- Handle Command Options
- Acknowledgment of SlashCommands in Context
