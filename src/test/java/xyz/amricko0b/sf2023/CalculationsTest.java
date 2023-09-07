package xyz.amricko0b.sf2023;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculationsTest extends Assertions {

  private Calculations calculations = new Calculations();

  /** Тест на данных из задания */
  @Test
  void test_greenCase() {

    var startTimes =
        new LocalTime[] {
          LocalTime.of(10, 0),
          LocalTime.of(11, 0),
          LocalTime.of(15, 0),
          LocalTime.of(15, 30),
          LocalTime.of(16, 50)
        };

    var durations = new int[] {60, 30, 10, 10, 40};

    var beginWorkingTime = LocalTime.of(8, 0);
    var endWorkingTime = LocalTime.of(18, 0);

    var consultationTime = 30;

    var expected =
        new String[] {
          "08:00-08:30",
          "08:30-09:00",
          "09:00-09:30",
          "09:30-10:00",
          "11:30-12:00",
          "12:00-12:30",
          "12:30-13:00",
          "13:00-13:30",
          "13:30-14:00",
          "14:00-14:30",
          "14:30-15:00",
          "15:40-16:10",
          "16:10-16:40",
          "17:30-18:00"
        };

    var available =
        calculations.availablePeriods(
            startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime);

    assertArrayEquals(expected, available);
  }

  /** Тест-кейс "Весь день занят одним слотом - времени нет" */
  @Test
  void test_oneHugeSlot_noTimeAvailable() {

    var startTimes = new LocalTime[] {LocalTime.of(10, 0)};

    var durations = new int[] {540};

    var beginWorkingTime = LocalTime.of(10, 0);
    var endWorkingTime = LocalTime.of(19, 0);

    var consultationTime = 10;

    var expected = new String[0];

    var available =
        calculations.availablePeriods(
            startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime);

    assertArrayEquals(expected, available);
  }

  /** Тест-кейс "Куча маленьких дел - времени нет" */
  @Test
  void test_manySmallSlots_noTimeAvailable() {

    var startTimes =
        new LocalTime[] {
          LocalTime.of(8, 0),
          LocalTime.of(10, 0),
          LocalTime.of(11, 0),
          LocalTime.of(12, 0),
          LocalTime.of(13, 30),
          LocalTime.of(15, 0),
          LocalTime.of(15, 30),
          LocalTime.of(16, 0)
        };

    var durations = new int[] {120, 60, 60, 90, 90, 30, 30, 60};

    var beginWorkingTime = LocalTime.of(8, 0);
    var endWorkingTime = LocalTime.of(17, 0);

    var consultationTime = 60;

    var expected = new String[0];

    var available =
        calculations.availablePeriods(
            startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime);

    assertArrayEquals(expected, available);
  }

  /** Тест-кейс "Час обеда по СанПинам" */
  @Test
  void test_imFuckingHungry() {

    var startTimes = new LocalTime[] {LocalTime.of(8, 0), LocalTime.of(13, 0)};

    var durations = new int[] {240, 300};

    var beginWorkingTime = LocalTime.of(8, 0);
    var endWorkingTime = LocalTime.of(18, 0);

    var consultationTime = 60;

    var expected = new String[] {"12:00-13:00"};

    var available =
        calculations.availablePeriods(
            startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime);

    assertArrayEquals(expected, available);
  }
}
