package com.core.data.repos

import com.core.data.base.BaseRepo
import com.core.data.constant.SharedPrefKeys
import com.core.network.NetworkFactoryInterface
import com.core.prefrence.SharedPreference

class ChangeThemeRepo(
    sharedPreferences: SharedPreference,
    networkFactory: NetworkFactoryInterface
) : BaseRepo(sharedPreferences, networkFactory) {
    fun saveThemeTypeToSharedPref(type: String) {
        sharedPreference.saveString(SharedPrefKeys.THEME, type)
    }

    fun getAppThemeFromSharedPref(): String? {
        return sharedPreference.getString(SharedPrefKeys.THEME)
    }
}