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

package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

/* Вот быстрый прогон кода тестирования, потому что это еще один фрагмент кода, который вы можете повторно использовать:
- SleepDabaseTest это тестовый класс.
- @RunWithАннотация идентифицирует тестовый бегун, который является программой, которая устанавливает и выполняет тесты.
- Во время настройки выполняется функция с аннотацией @Before, и она создает в памяти SleepDatabase с SleepDatabaseDao.
 "В памяти" означает, что эта база данных не сохраняется в файловой системе и будет удалена после запуска тестов.
- Также при создании базы данных в памяти код вызывает другой метод, специфичный для теста, allowMainThreadQueries.
 По умолчанию вы получаете ошибку, если пытаетесь выполнить запросы в основном потоке.
 Этот метод позволяет запускать тесты в основном потоке, что вы должны делать только во время тестирования.
- В тестовом методе с аннотацией @Test вы создаете, вставляете и извлекаете a SleepNight и утверждаете, что они одинаковы.
Если что-то пойдет не так, создайте исключение. В реальном тесте у вас будет несколько @Test методов.
- Когда тестирование завершено, функция с аннотацией @After выполняется для закрытия базы данных.
- Щелкните правой кнопкой мыши на тестовом файле в панели проекта и выберите запустить "SleepDatabaseTest".
- После запуска тестов убедитесь в панели SleepDatabaseTest, что все тесты прошли.*/

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        //val getId = sleepDao.get(0L)
        assertEquals(tonight?.sleepQuality, -1)
    }

    @Test
    @Throws(Exception::class)
    suspend fun clearNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        sleepDao.clear()
        val tonight = sleepDao.getTonight()
        assertEquals(tonight?.sleepQuality, null)
    }

//    @Test
//    @Throws(Exception::class)
//    fun updateNight() {
//        val night = SleepNight()
//        sleepDao.insert(night)
//        night.sleepQuality = 2
//        sleepDao.update(night)
//        val getId = sleepDao.get(0L)
//        //val tonight = sleepDao.getTonight()
//        assertEquals(getId?.sleepQuality, 2)
//    }
}