package com.pagingnewsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId LIKE :repoId")
    suspend fun remoteKeysRepoId(repoId: String): RemoteKeys?

    @Query("SELECT * FROM remote_keys ORDER BY repoId DESC")
    suspend fun getRemoteKeys(): List<RemoteKeys>

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: RemoteKeys)
}
