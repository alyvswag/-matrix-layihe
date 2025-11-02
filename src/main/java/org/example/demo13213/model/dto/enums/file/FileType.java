package org.example.demo13213.model.dto.enums.file;

public enum FileType {
    EXE(".exe"),
    DLL(".dll"),
    PS1(".ps1"),
    JS(".js"),
    DOCX(".docx"),
    XLS(".xls"),
    PPT(".ppt"),       // makrolu fayllar da bura daxildir
    ZIP(".zip"),
    RAR(".rar"),
    _7Z(".7z");
    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static FileType fromExtension(String ext) {
        for (FileType type : values()) {
            if (type.extension.equalsIgnoreCase(ext.startsWith(".") ? ext : "." + ext)) {
                return type;
            }
        }
        return null;
    }
}
