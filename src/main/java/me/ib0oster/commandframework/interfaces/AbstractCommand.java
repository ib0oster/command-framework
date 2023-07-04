package me.ib0oster.commandframework.interfaces;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused") @Getter @Setter
public abstract class AbstractCommand {
    private ApplicationCommandOptionData[] options = new ApplicationCommandOptionData[0];
    private String name;

    public abstract Object perform(ChatInputInteractionEvent event);

}
