package me.ib0oster.commandframework.interfaces;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.rest.util.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("unused") @Getter @Setter
public abstract class AbstractCommand {
    private ApplicationCommandOptionData[] options = new ApplicationCommandOptionData[0];
    private String name;

    public abstract Object perform(ChatInputInteractionEvent event);

    protected boolean hasPermission(Member member, Permission permission) {
        assert false;
        return member.getBasePermissions().block().contains(permission);
    }

    protected EmbedCreateSpec.Builder embedBuilder() {
        return EmbedCreateSpec.builder();
    }

    protected boolean hasRole(Member member, Role role) {
        assert false;
        return member.getRoles().any(role::equals).block();
    }

    protected User getUserByID(Guild guild, long id) {
        return guild.getClient().getUserById(Snowflake.of(id)).block();
    }

    protected TextChannel getChannelByID(Guild guild, long id) {
        return guild.getChannelById(Snowflake.of(id)).ofType(TextChannel.class).block();
    }

    protected List<Member> getMembersWithRole(Guild guild, Role role) {
        return guild.getMembers()
                .filter(member -> member.getRoles().any(role::equals).blockOptional().orElse(false))
                .collectList()
                .block();
    }

    protected boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException t) {
            return false;
        }
    }

}
