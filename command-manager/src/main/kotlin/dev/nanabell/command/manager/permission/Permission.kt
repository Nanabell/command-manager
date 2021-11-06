package dev.nanabell.command.manager.permission

import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
enum class Permission(val offset: Int) {
    CREATE_INSTANT_INVITE(0),
    KICK_MEMBERS(1),
    BAN_MEMBERS(2),
    ADMINISTRATOR(3),
    MANAGE_CHANNEL(4),
    MANAGE_SERVER(5),
    MESSAGE_ADD_REACTION(6),
    VIEW_AUDIT_LOGS(7),
    PRIORITY_SPEAKER(8),
    VIEW_GUILD_INSIGHTS(19),
    VIEW_CHANNEL(10),
    MESSAGE_READ(10),
    MESSAGE_WRITE(11),
    MESSAGE_TTS(12),
    MESSAGE_MANAGE(13),
    MESSAGE_EMBED_LINKS(14),
    MESSAGE_ATTACH_FILES(15),
    MESSAGE_HISTORY(16),
    MESSAGE_MENTION_EVERYONE(17),
    MESSAGE_EXT_EMOJI(18),
    USE_SLASH_COMMANDS(31),
    VOICE_STREAM(9),
    VOICE_CONNECT(20),
    VOICE_SPEAK(21),
    VOICE_MUTE_OTHERS(22),
    VOICE_DEAF_OTHERS(23),
    VOICE_MOVE_OTHERS(24),
    VOICE_USE_VAD(25),
    NICKNAME_CHANGE(26),
    NICKNAME_MANAGE(27),
    MANAGE_ROLES(28),
    MANAGE_PERMISSIONS(28),
    MANAGE_WEBHOOKS(29),
    MANAGE_EMOTES(30),

    UNKNOWN(-1);

    val rawValue: Long = 1L shl offset

    companion object {

        fun getFromOffset(offset: Int): Permission {
            for (perm in values()) {
                if (perm.offset == offset) return perm
            }
            return UNKNOWN
        }

        fun getPermissions(permissions: Long): EnumSet<Permission> {
            if (permissions == 0L) return EnumSet.noneOf(Permission::class.java)
            val perms = EnumSet.noneOf(Permission::class.java)
            for (perm in values()) {
                if (perm != UNKNOWN && permissions and perm.rawValue == perm.rawValue) perms.add(perm)
            }
            return perms
        }

        fun getRaw(vararg permissions: Permission): Long {
            var raw: Long = 0
            for (perm in permissions) {
                if (perm != UNKNOWN) raw = raw or perm.rawValue
            }
            return raw
        }

        fun getRaw(permissions: Collection<Permission>): Long {
            return getRaw(*permissions.toTypedArray())
        }
    }
}
