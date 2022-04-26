/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/* Когда вы используете Room базу данных, вы запрашиваете базу данных, определяя и вызывая функции Kotlin в своем коде.
Эти функции Kotlin сопоставляются с запросами SQL.
Вы определяете эти сопоставления в DAO с помощью аннотаций и Room создаете необходимый код.
Для базы данных sleep-tracker о ночах сна вам нужно сделать следующее:
Вставьте новые ночи.
Обновите существующую ночь, чтобы обновить время окончания и рейтинг качества.
Получите конкретную ночь на основе ее ключа.
Получите все ночи, чтобы вы могли их отображать.
Получите самую последнюю ночь.
Удалите все записи в базе данных.*/

@Dao
interface SleepDatabaseDao {
    @Insert
    suspend fun insert(night: SleepNight)

    @Update
    suspend fun update(night: SleepNight)

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Long): LiveData<SleepNight>

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key") // daily_sleep_quality_table
    suspend fun get(key: Long): SleepNight?


    /* @Delete Аннотация удаляет один элемент, и вы можете использовать @Delete и предоставить список ночей для удаления.
     Недостатком является то, что вам нужно получить или узнать, что находится в таблице.
      @Delete Аннотация отлично подходит для удаления определенных записей,
      но не эффективна для очистки всех записей из таблицы.*/

    @Query("DELETE from daily_sleep_quality_table")
    suspend fun clear()

    /* Добавьте @Query getTonight() функцию. Сделайте SleepNight возвращаемое getTonight() значение nullable,
     чтобы функция могла обрабатывать случай, когда таблица пуста. (Таблица пуста в начале и после очистки данных.)
     Чтобы получить "сегодня вечером" из базы данных, напишите запрос SQLite, который возвращает первый элемент списка
      результатов, упорядоченных nightId в порядке убывания. Используйте LIMIT 1 для возврата только одного элемента.*/

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    /* Пусть запрос SQLite возвращает все столбцы из daily_sleep_quality_table, упорядоченные в порядке убывания.
     getAllNights() возвращает список SleepNight как LiveData. Room обновляет это LiveData для вас, что означает,
      что вам нужно только явно получить данные один раз. */

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>
}
