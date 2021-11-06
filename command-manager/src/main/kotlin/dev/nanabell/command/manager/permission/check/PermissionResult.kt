package dev.nanabell.command.manager.permission.check

data class PermissionResult(
    val success: Boolean,
    val error: String?
) {
    companion object {
        fun success() = PermissionResult(true, null)
        fun fail(error: String) = PermissionResult(false, error)
    }
}
