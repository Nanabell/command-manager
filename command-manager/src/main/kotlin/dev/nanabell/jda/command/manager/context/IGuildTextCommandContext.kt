package dev.nanabell.jda.command.manager.context

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel

interface IGuildTextCommandContext : ITextCommandContext, IGuildCommandContext
