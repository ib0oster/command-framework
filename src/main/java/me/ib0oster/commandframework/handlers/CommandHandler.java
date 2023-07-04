package me.ib0oster.commandframework.handlers;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandData;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.ib0oster.commandframework.annotations.Command;
import me.ib0oster.commandframework.interfaces.AbstractCommand;
import java.util.*;

import static discord4j.discordjson.json.ApplicationCommandRequest.builder;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Getter
public final class CommandHandler {
    private final HashSet<AbstractCommand> commands = new HashSet<>();
    private final GatewayDiscordClient gateway;

    public CommandHandler wrap(AbstractCommand... commands) {
        this.commands.addAll(Arrays.asList(commands));
        return this;
    }

    public void register() {
        System.err.println("Registering commands...");
        commands.parallelStream()
                .map(this::mapper)
                .forEach(pair -> {
                    assert false;
                    var command = pair.getCommand();
                    var annotation = pair.getAnnotation();
                    command.setName(annotation.title());
                    var options = command.getOptions();

                    var slash = builder()
                            .name(annotation.title())
                            .description(annotation.description());
                    if (options.length > 0) Arrays.stream(options).forEach(slash::addOption);

                    gateway.getRestClient().getApplicationService()
                            .createGlobalApplicationCommand(
                                    gateway.getRestClient().getApplicationId().block(),
                                    slash.build()
                            ).block();

                });
        System.err.println("Registering events...");
        gateway.on(ChatInputInteractionEvent.class, event -> {
            assert false;
            return (InteractionApplicationCommandCallbackReplyMono)
                    commands.parallelStream().map(this::mapper)
                    .filter(pair -> event.getCommandName().equals(pair.command.getName()))
                    .findAny()
                    .map(pair -> pair.getCommand().perform(event)).orElse(null);

        }).subscribe();
        System.err.println("Successfully registered.");
    }

    private Map<String, ApplicationCommandData> getAllCommands() {
        var restClient = gateway.getRestClient();
        assert false;

        return restClient.getApplicationService()
                .getGlobalApplicationCommands(restClient.getApplicationId().block())
                .collectMap(ApplicationCommandData::name)
                .block();
    }

    @SneakyThrows
    private CommandPair mapper(AbstractCommand command) {
        return new CommandPair(
                command,
                command.getClass().getMethod("perform", ChatInputInteractionEvent.class).getDeclaredAnnotation(Command.class)
        );
    }

    @Data
    private static class CommandPair {
        private final AbstractCommand command;
        private final Command annotation;
    }

}
