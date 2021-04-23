package win95.constants;

public enum LogicConstants {
    CD_BACK,
    CD_NEXT,
    PREVIEW_MODE;

    public enum SortingType {
        BY_NAME_DESC,
        BY_NAME_INC,
        BY_SIZE_DESC,
        BY_SIZE_INC,
        BY_ACCESS_DESC,
        BY_ACCESS_INC,
        BY_DEFAULT
    }

    public enum OS {
        LINUX,
        MAC,
        WINDOW,
        UNKNOWN
    }
}
