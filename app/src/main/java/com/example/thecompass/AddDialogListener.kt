package com.example.thecompass

import com.example.thecompass.model.Coordinates

interface AddDialogListener {
    fun onAddButtonClicked(coordinates: Coordinates)
}