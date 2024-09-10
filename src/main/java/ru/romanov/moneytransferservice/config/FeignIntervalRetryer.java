package ru.romanov.moneytransferservice.config;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Реализация интерфейса {@link Retryer} для Feign клиента с настраиваемыми интервалами повторных попыток.
 * Интервалы задаются в зависимости от статуса HTTP ошибки.
 */
@Slf4j
public class FeignIntervalRetryer implements Retryer {

    private final long initialInterval;   // Начальный интервал для повторных попыток
    private final long extendedInterval;  // Расширенный интервал для повторных попыток
    private final int maxAttempts;        // Максимальное количество попыток
    private int attempt;                  // Текущее количество попыток

    /**
     * Конструктор по умолчанию инициализирует интервалы и количество попыток.
     */
    public FeignIntervalRetryer() {
        this.initialInterval = TimeUnit.SECONDS.toMillis(1);    // Интервал секунд для ошибки 429
        this.extendedInterval = TimeUnit.MINUTES.toMillis(5);   // Интервал минут для 5xx ошибок
        this.maxAttempts = 20;                                          // Количество попыток
        this.attempt = 1;
    }

    /**
     * Метод для продолжения повторных попыток или проброса исключения {@link RetryableException}.
     * Определяет интервал ожидания между попытками в зависимости от статуса HTTP ошибки.
     *
     * @param e {@link RetryableException}, возникшее во время предыдущей попытки выполнения запроса.
     */
    @Override
    public void continueOrPropagate(RetryableException e) {
        if (e.status() == 429 || e.status() >= 500) {
            long interval = (e.status() == 429) ? initialInterval : extendedInterval;

            if (attempt++ >= maxAttempts) {
                log.warn("Max attempts reached. Propagating exception.");
                throw e;
            }

            log.info("Attempt {} failed with status {}. Retrying in {} ms", attempt, e.status(), interval);

            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Метод для клонирования текущего экземпляра {@link Retryer}.
     *
     * @return Новый экземпляр {@link FeignIntervalRetryer}.
     */
    @Override
    public Retryer clone() {
        return new FeignIntervalRetryer();
    }
}
