package ru.gmm.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ERR_CODE_001("ERR.CODE.001", "Что-то пошло не так!", 405),
    ERR_CODE_002("ERR.CODE.002", "Пользователь с id %s не найден", 400),
    ERR_CODE_003("ERR.CODE.003", "Счет с id %s не найден", 400),
    ERR_CODE_004("ERR.CODE.004", "Счет с id %s имеет недостаточно средств", 400),
    ERR_CODE_005("ERR.CODE.005", "Счет с id %s ЗАКРЫТ", 400),
    ERR_CODE_006("ERR.CODE.006", "Счет с id %s уже существует", 400),
    ERR_CODE_007("ERR.CODE.007", "Счет с id %s не найден", 400),
    ERR_CODE_008("ERR.CODE.008", "Счет счета для перевода должны отличаться", 400);

    private final String code;
    private final String description;
    private final int httpStatus;

    public String formatDescription(final Object... args) {
        return String.format(description, args);
    }
}
