package com.alcolook.data.repository

import com.alcolook.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UserProfileRepositoryImpl : UserProfileRepository {
    
    private val profile = MutableStateFlow(UserProfile())
    
    override fun getUserProfile(): Flow<UserProfile?> = profile
    
    companion object {
        @Volatile
        private var INSTANCE: UserProfileRepositoryImpl? = null
        
        fun getInstance(): UserProfileRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserProfileRepositoryImpl().also { INSTANCE = it }
            }
        }
    }
}
