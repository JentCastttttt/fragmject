package com.example.fragment.project.ui.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.fragment.project.WanTheme
import com.example.fragment.project.components.StandardDialog
import kotlinx.coroutines.launch

@Composable
fun PermissionScreen() {
    val context = LocalContext.current
    val storagePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
        )
    else
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    var showDialog by remember { mutableStateOf(false) }
    val requestPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { ps ->
            var isGranted = true
            ps.entries.forEach {
                if (it.key in storagePermissions && !it.value)
                    isGranted = false
            }
            showDialog = !isGranted
        }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState, Modifier)
    StandardDialog(
        show = showDialog,
        title = "申请存储空间权限",
        text = "玩Android需要使用存储空间，我们想要将文章内容缓存到本地，从而加快打开速度和减少用户流量使用",
        onConfirm = { requestPermissions.launch(storagePermissions) },
        onDismiss = { showDialog = false }
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AssistChip(
            onClick = {
                showDialog = !storagePermissions.all {
                    ContextCompat.checkSelfPermission(
                        context, it
                    ) == PackageManager.PERMISSION_GRANTED
                }
                if (!showDialog) {
                    snackScope.launch {
                        snackState.showSnackbar(
                            "存储权限已经获取"
                        )
                    }
                }
            },
            label = { Text("存储权限") },
            leadingIcon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = null,
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = MaterialTheme.colorScheme.primaryContainer,
                leadingIconContentColor = MaterialTheme.colorScheme.primaryContainer
            ),
            border = AssistChipDefaults.assistChipBorder(
                true,
                borderColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun PermissionScreenPreview() {
    WanTheme { PermissionScreen() }
}