package dev.ell.pillora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MedicineGreen = Color(0xFF269B72)

private data class MedicineItemData(
    val id: Int,
    val name: String,
    val dosage: String,
    val type: String,
    val time: String,
    val stock: Int,
    val instructions: String,
)

@Composable
fun MedicinesScreen() {

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    var showDialog by remember { mutableStateOf(value = false) }
    var editingMedicine by remember { mutableStateOf<MedicineItemData?>(null) }
    var deleteMedicineTarget by remember { mutableStateOf<MedicineItemData?>(null) }

    var medicines by remember {
        mutableStateOf(
            listOf(
                MedicineItemData(
                    1,
                    "Paracetamol",
                    "500 mg",
                    "Tablet",
                    "08:00 AM",
                    12,
                    "After Food",
                ),
                MedicineItemData(
                    2,
                    "Amoxicillin",
                    "250 mg",
                    "Capsule",
                    "02:00 PM",
                    4,
                    "Before Food",
                ),
                MedicineItemData(
                    3,
                    "Cough Syrup",
                    "10 ml",
                    "Syrup",
                    "08:00 PM",
                    2,
                    "With Water",
                ),
            ),
        )
    }

    val categories = listOf("All", "Tablet", "Capsule", "Syrup", "Drops")

    val filteredMedicines = medicines.filter {
        val matchesSearch = it.name.contains(searchQuery, ignoreCase = true) ||
            it.dosage.contains(searchQuery, ignoreCase = true)

        val matchesCategory = if (selectedCategory == "All") {
            true
        } else {
            it.type.equals(selectedCategory, ignoreCase = true)
        }

        matchesSearch && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .padding(16.dp),
    ) {

        Text(
            text = "My Medicines",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
        )

        Text(
            text = "Manage your active medicine inventory and schedule",
            fontSize = 13.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                editingMedicine = null
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicineGreen),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Medicine", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search medicine...") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                )
            },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category

                Button(
                    onClick = { selectedCategory = category },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MedicineGreen else Color.White,
                        contentColor = if (isSelected) Color.White else Color.DarkGray,
                    ),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                ) {
                    Text(category, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredMedicines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💊", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No medicine found",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                    )
                    Text(
                        "Try searching or adding a new medicine.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(filteredMedicines) { medicine ->
                    MedicineCardItem(
                        medicine = medicine,
                        onEdit = {
                            editingMedicine = medicine
                            showDialog = true
                        },
                        onDelete = {
                            deleteMedicineTarget = medicine
                        },
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddEditMedicineDialog(
            medicine = editingMedicine,
            onDismiss = { showDialog = false },
            onSave = { updatedMedicine ->

                if (editingMedicine == null) {

                    medicines = medicines + updatedMedicine.copy(
                        id = (medicines.maxOfOrNull { it.id } ?: 0) + 1,
                    )

                } else {

                    medicines = medicines.map {
                        if (it.id == updatedMedicine.id) updatedMedicine else it
                    }

                }

                showDialog = false
            },
        )
    }

    deleteMedicineTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteMedicineTarget = null },
            title = { Text("Delete Medicine") },
            text = { Text("Are you sure you want to delete ${target.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        medicines = medicines.filter { it.id != target.id }
                        deleteMedicineTarget = null
                    },
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteMedicineTarget = null }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun MedicineCardItem(
    medicine: MedicineItemData,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Box(
                modifier = Modifier
                    .background(Color(0xFFE5F5EC), RoundedCornerShape(12.dp))
                    .padding(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Medication,
                    contentDescription = null,
                    tint = MedicineGreen,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicine.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )

                Text(
                    text = "${medicine.dosage} · ${medicine.type}",
                    color = Color.Gray,
                    fontSize = 13.sp,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Time: ${medicine.time}",
                    color = MedicineGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )

                if (medicine.instructions.isNotBlank()) {
                    Text(
                        text = medicine.instructions,
                        color = Color.Gray,
                        fontSize = 11.sp,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (medicine.stock <= 3) "Low Stock: ${medicine.stock} left" else "Stock: ${medicine.stock}",
                    color = if (medicine.stock <= 3) Color.Red else Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = if (medicine.stock <= 3) FontWeight.Bold else FontWeight.Normal,
                )
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray,
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE53935),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditMedicineDialog(
    medicine: MedicineItemData?,
    onDismiss: () -> Unit,
    onSave: (MedicineItemData) -> Unit,
) {

    var name by remember { mutableStateOf(medicine?.name ?: "") }
    var dosage by remember { mutableStateOf(medicine?.dosage ?: "") }
    var selectedType by remember { mutableStateOf(medicine?.type ?: "Tablet") }
    var time by remember { mutableStateOf(medicine?.time ?: "08:00 AM") }
    var stock by remember { mutableStateOf(medicine?.stock?.toString() ?: "10") }
    var instructions by remember { mutableStateOf(medicine?.instructions ?: "After Food") }

    var expandedTypeDropdown by remember { mutableStateOf(value = false) }
    var expandedFoodDropdown by remember { mutableStateOf(value = false) }

    var error by remember { mutableStateOf(value = false) }

    val types = listOf("Tablet", "Capsule", "Syrup", "Drops")
    val foodOptions = listOf("Before Food", "After Food", "With Food")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (medicine == null) "Add Medicine" else "Edit Medicine",
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.heightIn(max = 520.dp),
            ) {

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            error = false
                        },
                        label = { Text("Medicine Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    OutlinedTextField(
                        value = dosage,
                        onValueChange = {
                            dosage = it
                            error = false
                        },
                        label = { Text("Dosage (e.g., 500 mg, 10 ml)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedTypeDropdown,
                        onExpandedChange = { expandedTypeDropdown = !expandedTypeDropdown },
                    ) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Medicine Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTypeDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        )

                        ExposedDropdownMenu(
                            expanded = expandedTypeDropdown,
                            onDismissRequest = { expandedTypeDropdown = false },
                        ) {
                            types.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        selectedType = item
                                        expandedTypeDropdown = false
                                    },
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Reminder Time (e.g. 08:00 AM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    OutlinedTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = { Text("Current Stock") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedFoodDropdown,
                        onExpandedChange = { expandedFoodDropdown = !expandedFoodDropdown },
                    ) {
                        OutlinedTextField(
                            value = instructions,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Food Instruction") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFoodDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFoodDropdown,
                            onDismissRequest = { expandedFoodDropdown = false },
                        ) {
                            foodOptions.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        instructions = item
                                        expandedFoodDropdown = false
                                    },
                                )
                            }
                        }
                    }

                    if (error) {
                        Text(
                            text = "Please fill in all required fields correctly.",
                            color = Color.Red,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val stockNumber = stock.toIntOrNull()

                    if (
                        name.isBlank() ||
                        stockNumber == null ||
                        stockNumber < 0 ||
                        time.isBlank() ||
                        dosage.isBlank()
                    ) {
                        error = true
                    } else {
                        onSave(
                            MedicineItemData(
                                id = medicine?.id ?: 0,
                                name = name.trim(),
                                dosage = dosage.trim(),
                                type = selectedType,
                                time = time.trim(),
                                stock = stockNumber,
                                instructions = instructions,
                            ),
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MedicineGreen),
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
