package com.github.lasbonbe.raccoonside

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object MyIconLoader { // Renamed to avoid shadowing
    fun getIcon(path: String): Icon {
        return IconLoader.getIcon(path, MyIconLoader::class.java) // Use MyIconLoader instead of IconLoader
    }
}