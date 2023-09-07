package xyz.amricko0b.sf2023;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Calculations {

  private static final DateTimeFormatter HOURS_SHORT_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  private static String buildPeriodString(LocalTime start, LocalTime end) {
    return String.format("%s-%s", start.format(HOURS_SHORT_FORMAT), end.format(HOURS_SHORT_FORMAT));
  }

  public String[] availablePeriods(
      LocalTime[] startTimes,
      int[] durations,
      LocalTime beginWorkingTime,
      LocalTime endWorkingTime,
      int consultationTime) {

    var idx = 0;
    var available = new LinkedList<>();

    // Рассматриваем расписание "окнами" величиной в consultationTime
    // Сначала берём начало рабочего дня и consultationTime после него
    var probeStart = beginWorkingTime;
    var probeEnd = probeStart.plusMinutes(consultationTime);

    // Сначала перебираем занятые слоты, пока они есть
    while (idx != startTimes.length) {

      // Если "окно" происходит ДО начала занятого слота - это свободный период
      if (probeStart.isBefore(startTimes[idx]) && !probeEnd.isAfter(startTimes[idx])) {
        available.add(buildPeriodString(probeStart, probeEnd));
        probeStart = probeEnd;
      } else {
        // Если нет - перемещаемся в конец слота и рассматриваем "окно" от него
        probeStart = startTimes[idx].plusMinutes(durations[idx]);
        idx++;
      }

      probeEnd = probeStart.plusMinutes(consultationTime);
    }

    // Как только занятые слоты закончились - просто дробим всё оставшееся свободное время на "окна" и запоминаем
    while (!probeEnd.isAfter(endWorkingTime)) {
      available.add(buildPeriodString(probeStart, probeEnd));
      probeStart = probeEnd;
      probeEnd = probeStart.plusMinutes(consultationTime);
    }

    // Костыль, т.к. нужен массив в качестве возвращаемого типа, а размер неизвестен заранее.
    // Лучше так, чем динамически увеличивать размер массива на каждой итерации
    //noinspection SuspiciousToArrayCall
    return available.toArray(String[]::new);
  }
}
