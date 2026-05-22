package com.donai.app.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donai.app.theme.DonAIRed

// ─── Labeled Form Field ───────────────────────────────────────────────────────

/**
 * Reusable wrapper: ALL-CAPS label + any [content] slot beneath it.
 * Used by both [CreateRequestScreen] and [CompleteProfileScreen].
 */
@Composable
fun LabeledFormField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            letterSpacing = 1.sp,
        )
        content()
    }
}

// ─── Outlined Text Field ──────────────────────────────────────────────────────

/**
 * App-wide styled [OutlinedTextField]. Consistent border radius, icon tint,
 * error display, and brand focus color across all forms.
 */
@Composable
fun DonAIOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    leadingIconDescription: String,
    error: String?,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingIconDescription,
                tint = if (error != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier.size(20.dp),
            )
        },
        isError = error != null,
        supportingText = error?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DonAIRed,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

// ─── Generic Dropdown ─────────────────────────────────────────────────────────

/**
 * Generic [ExposedDropdownMenuBox] that works with any list of items
 * rendered via [itemLabel]. Avoids copy-pasting the dropdown pattern
 * for BloodType, LastDonation, and future pickers.
 *
 * @param T          The type of selectable item.
 * @param items      Full list of options.
 * @param selected   Currently selected item, or null if nothing is chosen.
 * @param onSelected Called with the chosen item when the user taps a row.
 * @param itemLabel  Converts [T] to the display string shown in the row.
 * @param placeholder Text shown when [selected] is null.
 * @param leadingIcon Icon displayed on the left side of the anchor.
 * @param error       Optional validation error shown below the field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DonAIDropdown(
    items: List<T>,
    selected: T?,
    onSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    placeholder: String,
    leadingIcon: ImageVector,
    error: String?,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = if (error != null) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.outline

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth(),
        ) {
            // ── Anchor ────────────────────────────────────────────────────────
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (error != null) MaterialTheme.colorScheme.error else DonAIRed,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = selected?.let(itemLabel) ?: placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selected != null) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.weight(1f),
                    )
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }

            // ── Menu ──────────────────────────────────────────────────────────
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { item ->
                    val isSelected = item == selected
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = itemLabel(item),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DonAIRed
                                else MaterialTheme.colorScheme.onSurface,
                            )
                        },
                        onClick = {
                            onSelected(item)
                            expanded = false
                        },
                        trailingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = DonAIRed,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        } else null,
                    )
                }
            }
        }

        // ── Error text ────────────────────────────────────────────────────────
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            )
        }
    }
}