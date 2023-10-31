package ru.gmm.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ERR_CODE_001("ERR.CODE.001", "Что-то пошло не так!", 405),
    ERR_CODE_002("ERR.CODE.002", "Пользователь с id %s не найден", 404);

    private final String code;
    private final String description;
    private final int httpStatus;

    public String formatDescription(final Object... args) {
        return String.format(description, args);
    }
}
