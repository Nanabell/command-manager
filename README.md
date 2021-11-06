# Command Manager
This is a little project of mine to easily setup Text & Slash Command Handling for ~~JDA~~ virtually any Discord Framework.  
I've tried making as much as possible Pluggable and Replaceable allowing maximum flexibility.

## Getting Started  
Simply add the Command Manager to your Project and the adapter for your Framework
````shell
dependencies {
  implementation("dev.nanabell.command.manager:command-manager:VERSION")
  implementation("dev.nanabell.command.manager:jda-adapter:VERSION") // JDA as example here
}
````

Constructing Command Manger:
```kotlin
val provider = StaticCommandProvider(listOf(
    MyFirstComamnd(),
    MySecondCommand(),
    MyThirdCommand()
))

val builder = CommandManagerBuilder(prefix = "->")
    .setCommandProvider(provider)
    .useJDA(jda = jdaInstance / jdaBuilder)
...
val manager = builder.build()
```

It is **strongly** recommended you use a framework adapter as the `.useXXX` will set multiple required CommandManager features
like CommandContextBuilder, EventMediator etc...  
If you don't want to use a Framework adapter you can always set these manually and the CommandManager should always complain in warning logs if something might be not be configured correctly.

### Micronaut Framework
If you are using Micronaut you can add the Micronaut Command Provider Dependency, to automatically find and load all Command Classes
```shell
dependencies {
  implementation("dev.nanabell.command.manager:micronaut-provider:VERSION")
}
```
For the MicronautCommandProvider to work properly you need to define all of your commands as `@Singleton`s,  
alternatively add the `@Command` Annotation to the Micronaut Annotation Processing:
```shell
micronaut {
    ...
    processing {
        incremental(true)
        annotations("my.existing.package.*", "dev.nanabell.command.manager.command.Command")
    }
}
```
Additionally, if you are using Kapt add it to kapt's annotation processing as well
```shell
kapt {
    arguments {
        ...
        arg("micronaut.processing.annotations", "my.existing.package.*,dev.nanabell.command.manager.command.Command")
        ...
    }
}
```

and register the Micronaut Provider like this:
```kotlin
val manager = CommandManagerBuilder(prefix = "->")
    .setMicronautProvider(applicationContext)
    ...
    .build()
```
Internally The Micronaut Provider will simply fetch all Beans which implement the `ICommand` Interface
```kotlin
applicationContext.getBeansOfType(ICommand::class.java)
```

### Micrometer Metrics
The CommandManger comes with an extra Module which adds Micrometer Metrics support.
To add Micrometer add the module to gradle
```shell
dependencies {
  implementation("dev.nanabell.command.manager:micrometer-metrics:VERSION")
}
```
And register the Metrics Collector:
```kotlin
val manager = CommandManagerBuilder(prefix = "->")
    .setCommandMetrics(MicrometerCommandMetrics(registry = Metrics.globalRegistry))
    ...
    .build()
```

## Creating Commands
These examples will be using the jda-adapter package. The process is similar for other Frameworks,  
If no adapter for your Framework exists feel free to create your own. 

A Command is always Bound to a Class and requires a `ICommand<ICommandContext>` Interface Implementation
```kotlin
import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.command.JdaCommand
import dev.nanabell.command.manager.context.JdaCommandContext

@Command(name = "demo", description = "This is a Demo Command")
class DemoCommand : JdaCommand {

    override fun execute(context: JdaCommandContext) {
        context.channel.sendMessage("Demo Reply").queue()
    }

}
```
Now simply pass an Instance of this Class to your CommandProvider
```kotlin
...
val provider = StaticCommandProvider(listOf(DemoCommand()))
CommandManagerBuilder(prefix = "->")
    .setCommandProvider(provider)
    .useJda(jda)
    .build()
```
or in case of Micronaut let the CommandProvider discover and load the class
```kotlin
CommandManagerBuilder(prefix = "->")
    .setMicronautProvider(applicationContext)
    .useJda(jda)
    .build()
```

### Custom Permissions
Default CommandManger comes with a very small set of Permission Checks, namely:
- OwnerOnly: Verify Command Author against initially provided set of OwnerIds
- BotPermission: Verify Bot has a certain Discord Permission
- UserPermission: Same thing but verify the User

To include your own Permission System it is recommended that you add a `IPermissionCheck` with your custom Logic and register as follows:
```kotlin
CommandManagerBuilder(prefix = "->")
    ...
    .setPermissionHandler(DefaultPermissionHandlerBuilder()
        .with(MyCustomPermissionCheck()).build())
    .build()
```

## Concepts
The Command Manager tries to be as pluggable and flexible as possible. If you do not like how the CommandManager handles a certain aspect,
chances are you can replace it with your own implementation.  
This section will go over all the different internal Systems

### ICommandProvider
Responsible for finding and creating Instances of the Command classes.  
```kotlin
class StaticCommandProvider(private val commands: Collection<ICommand<*>>) : ICommandProvider {
    override fun provide(): Collection<ICommand<*>> {
        return commands
    }
}
```

### ICommandCompiler
Responsible for "compiling" the Command. Essentially its just loading Command Metadata and filling it into the data class
`CompiledCommand`
```kotlin
class AnnotationCommandCompiler : ICommandCompiler {

    private val logger = LoggerFactory.getLogger(AnnotationCommandCompiler::class.java)

    override fun compile(command: ICommand<ICommandContext>): CompiledCommand {
        logger.debug("Compiling Command ${command::class.qualifiedName}")
        if (!command::class.java.isAnnotationPresent(Command::class.java)) {
            throw MissingCommandAnnotationException(command::class)
        }
        ...
        return CompiledCommand(...)
```

### ICommandContextBuilder
Since the Command Context information changes depending on which underlying Framework is at work
and it being unfeasible to implement a specific Context for each Framework out there.  
The CommandContextBuilder is responsible for converting the CommandManager specific event into the Context for said Framework.  
The CommandManger event includes the Originally passed event as type `Any` which can safely be cast back for additional information
```kotlin
class BasicCommandContextBuilder : ICommandContextBuilder {

    override fun fromMessage(event: MessageReceivedEvent, owners: Set<Long>, arguments: Array<String>): ICommandContext {
        val original: OriginalEventType = event.raw as OriginalEventType // Original Event is passed through
        return BasicContext(owners, arguments, event.authorId, event.channelId, event.guildId, -1 /*Unknown*/)
    }

    override fun fromCommand(event: SlashCommandEvent, owners: Set<Long>): ICommandContext {
        return BasicContext(owners, emptyArray(), event.authorId, event.channelId, event.guildId, -1 /*Unknown*/)
    }
}
```

### ICommandContext
On the Topic of Context, the ICommandContext Implementation holds all Command relevant Context information
like Channel, Guild, Author etc... etc...  
This is just a Data Class which has to inherit from ICommandContext 
```kotlin
class BasicContext(
    override val ownerIds: Set<Long>,
    override val arguments: Array<String>,
    override val authorId: Long,
    override val channelId: Long,
    override val guildId: Long?,
    override val selfUserId: Long
) : ICommandContext {

    override fun hasPermission(memberId: Long, vararg permission: Permission): Boolean {
        throw UnsupportedOperationException()
    }

    override fun reply(message: String) {
        throw UnsupportedOperationException()
    }
}
```

### IEventMediator
Not much to say here, this Class is responsible for Converting Discord Framework Events (or any other source really) into CommandManager events.
Essentially this is the entrypoint into the CommandManger Processing.
```kotlin
import net.dv8tion.api.events.interaction.SlashCommandEvent as JdaSlashCommandEvent
import net.dv8tion.api.events.message.MessageReceivedEvent as JdaMessageReceivedEvent

class JdaEventMediator : IEventMediator, EventListener {

    private lateinit var commandManager: IEventListener

    override fun registerCommandManager(listener: IEventListener) {
        this.commandManager = listener
    }

    override fun onEvent(event: GenericEvent) {
        if (!this::commandListener.isInitialized) return

        when (event) {
            is JdaMessageReceivedEvent -> commandManager.onMessageReceived(
                MessageReceivedEvent(
                    event.message.contentRaw,
                    event.author.idLong,
                    event.messageIdLong,
                    event.channel.idLong,
                    if (event.isFromGuild) event.guild.idLong else null,
                    event.author.isBot,
                    event.isWebhookMessage,
                    event.author.isSystem,
                    event
                )
            )

            is JdaSlashCommandEvent -> commandManager.onSlashCommand(
                SlashCommandEvent(
                    event.commandPath,
                    event.user.idLong,
                    event.channel.idLong,
                    event.guild?.idLong,
                    event
                )
            )
        }
    }
}
```

### IPermissionHandler
This sounds more interesting but the Handler itself is something very few will actually need to replace.  
The Handler is responsible for Checking Command Permission, Who is trying to execute what and where.  
It is also responsible for responding to the User (or not) if a Permission Check has failed.  
The Default Implementation delegates this to a set of Permission Checks implementing `IPermissionCheck`
```kotlin
class DefaultPermissionHandler(private val checks: Set<IPermissionCheck>, private val rootOwner: Boolean) : IPermissionHandler {

    private val logger = LoggerFactory.getLogger(DefaultPermissionHandler::class.java)

    override fun handle(command: CompiledCommand, context: ICommandContext): Boolean {
        if (!command.requirePermission)
            return true

        // Add Owner override to any and all Permission Checks
        if (rootOwner && context.ownerIds.contains(context.authorId))
            return true

        for (check in checks) {
            val result = check.check(command, context)
            if (result.success)
                continue

            val error = result.error
            if (error != null) {
                logger.debug("Permission Check ${check::class.simpleName} failed for $context with error: $error")

                context.reply("Sorry, $error")
                return false
            }
        }

        return true
    }
}
```

### IPermissionCheck
This is the Actual Permission Check that is being done on a Command. By Default only of the these have to succeed for the Command to be Executed  
If you plan to Implement a Database backed Permission System or anything the likes, you would add a Custom IPermissionCheck at the IPermissionHandler above.
See The Permissions Section in Getting Started
```kotlin
class OwnerOnlyCheck : IPermissionCheck {

    override fun check(command: CompiledCommand, context: ICommandContext): PermissionResult {
        if (command.ownerOnly && !context.ownerIds.contains(context.authorId))
            return PermissionResult.fail("This Command can only be ran by the Bot Owner!")

        return PermissionResult.success()
    }

}
```

### ICommandListener
There is no direct Responsibility of a Listener, this is just a way to globally hook into the CommandManager
and receive updates when a Command is executed, rejected, aborted or failed (on the topic of Command States more down below). As well as unknown Command executions.  
There is no default implementation of this.

### ICommandMetrics
Similarly to the ICommandListener the CommandMetrics also receive the same set of updates but is directly responsible for,  
who could've guessed it: Collecting Metrics
The Default Implementation of this is a very simple set of `AtomicLong`s counting up.
The `micrometer-metrics` module implements a [Micrometer](https://micrometer.io/) version of the Metrics Collector

# Missing Features / TODOS
- The current CommandListener Style seems very cumbersome
- Help Command
## Text Commands
- Allow Mention to be used as Prefix
- Add QuotationMarks to allow Spaces in Arguments
## Slash Commands
- Registering Slash Commands with discord
- Handle Command Options
- Acknowledgment of SlashCommands in Context
