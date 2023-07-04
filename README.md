# command-framework
An lightweight command framework for Discord4J.

## Usage

### Creating a command
```java
public class TestCommand extends AbstractCommand {

    public TestCommand() {
        setOptions(
            ApplicationCommandOptionData.builder()
            .name("test-option")
            .description("An test option.")
            .type(ApplicationCommandOption.Type.STRING.getValue())
            .required(true)
            .build()
        )
    }

    @Command(
        title = "test",
        description = "Test command."
    )
    public Object perform(ChatInputInteractionEvent event) {
        return event.reply("Hello world!");
    } 

}
```

### Registering a command
```java
new CommandHandler(gateway)
    .wrap(new TestCommand())
    .register();
```
