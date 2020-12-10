package blue.mild.breviary.backend.enums

/**
 * ActionCodesEnum.
 *
 * @property code
 * @property action
 */
@Suppress("MagicNumber")
enum class ActionCodesEnum(val code: Int, val action: String) {
    USER_LOGIN(1, "User login."),
    USER_LOGOUT(2, "User logout."),
    PASSWORD_CHANGE(3, "Password change."),
    API_CALL(4, "Api call."),
    SAP_IMPORT(5, "SAP import."),
    SAP_SIMULATOR(6, "SAP simulator."),
    TASP(7, "TASP communication."),
    MASTER_DATA(8, "Master data import/export.")
}
