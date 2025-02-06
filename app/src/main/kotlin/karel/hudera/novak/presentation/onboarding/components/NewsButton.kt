package karel.hudera.novak.presentation.onboarding.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NewsButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick

    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun NewsTextButton(
    text: String,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick) {
        Text(
            text = text
        )
    }
}