package com.miiiin15.myloan.base.presentation.nav

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

class NavManager {
    private var navEventListener: ((navDirections: NavDirections?, navOptions: NavOptions?, popBackStack: Boolean) -> Unit)? = null
    var currentDestinationId: Int? = null

    fun navigate(navDirections: NavDirections, navOptions: NavOptions? = null) {
        navEventListener?.invoke(navDirections, navOptions, false)
    }

    fun replace(navDirections: NavDirections) {
        currentDestinationId?.let { id ->
            val navOptions = NavOptions.Builder()
                .setPopUpTo(id, true)
                .build()
            navigate(navDirections, navOptions)
        } ?: run {
            navigate(navDirections)
        }
    }

    fun popBackStack() {
        navEventListener?.invoke(null, null, true)
    }

    fun setOnNavEvent(navEventListener: (navDirections: NavDirections?, navOptions: NavOptions?, popBackStack: Boolean) -> Unit) {
        this.navEventListener = navEventListener
    }
}