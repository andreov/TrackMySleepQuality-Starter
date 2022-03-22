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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*Вам нужно создать абстрактный класс держателя базы данных,
аннотированный @Database. Этот класс имеет один метод,
который либо создает экземпляр базы данных, если база данных не существует,
либо возвращает ссылку на существующую базу данных. */

/* - Создайте public abstract класс, который extends RoomDatabase. Этот класс должен выступать
в качестве держателя базы данных. Класс является абстрактным, потому Room что создает реализацию для вас.
- Аннотируйте класс @Database. В аргументах объявите сущности для базы данных и задайте номер версии.
- Внутри companion объекта определите абстрактный метод или свойство, которое возвращает SleepDatabaseDao.
Room будет генерировать тело для вас.
- Вам нужен только один экземпляр Room базы данных для всего приложения, поэтому сделайте Room Database синглтон.
- Используйте Room конструктор баз данных для создания базы данных, только если база данных не существует.
 В противном случае верните существующую базу данных.
 Код будет практически одинаковым для любой Room базы данных, поэтому вы можете использовать этот код в качестве шаблона.*/

@Database (entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase: RoomDatabase(){

    /* База данных должна знать о DAO. Внутри тела класса объявите абстрактное значение, которое возвращает SleepDatabaseDao.
     У вас может быть несколько DAO.*/
    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: SleepDatabase? = null
        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            SleepDatabase::class.java,
                            "sleep_history_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}