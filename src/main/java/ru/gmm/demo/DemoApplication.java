package ru.gmm.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// todo: СДЕЛАНО -> Доработка метода createTransaction в зависимости от статуса тразакции
// todo: СДЕЛАНО -> Если нет юзера то нельзя создавать счет
// todo: СДЕЛАНО -> При создании транзакции СНИМАТЬ и НАЧИСЛЯТЬ деньги на счета, проверять достаточность средств
// todo: СДЕЛАНО -> возвращать статус аккаунт (Сделать в виде перечисления enum)
// todo: Сделать end point который будет возвращать пользователя и список его аккаунтов (id + номер + статус + сумма)
// todo: При удалении пользователя удалять все связи каскадно (Сделать мягкое удаление soft deleted)
// todo: переписать мапперы с использованием map struct
@SpringBootApplication
public class DemoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
