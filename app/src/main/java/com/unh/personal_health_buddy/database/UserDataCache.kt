package com.unh.personal_health_buddy.database



import android.graphics.Bitmap
import com.unh.personal_health_buddy.database.EmergencyContact
import com.unh.personal_health_buddy.database.HealthInformation
import com.unh.personal_health_buddy.database.User

object UserDataCache {
    var user: User? = null
    var emergencyContacts: List<EmergencyContact> = emptyList()
    var healthInfo: HealthInformation? = null
    var profileBitmap: Bitmap? = null
    var isDataLoaded: Boolean = false

    fun clear() {
        user = null
        emergencyContacts = emptyList()
        healthInfo = null
        profileBitmap = null
        isDataLoaded = false
    }
}